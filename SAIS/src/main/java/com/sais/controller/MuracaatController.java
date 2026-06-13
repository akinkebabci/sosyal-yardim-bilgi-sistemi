package com.sais.controller;

import com.sais.entity.*;
import com.sais.enums.MuracaatDurum;
import com.sais.exception.BusinessException;
import com.sais.service.*;
import com.sais.util.MessageUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component("muracaatController")
@ViewScoped
@Getter
@Setter
@Slf4j
public class MuracaatController implements Serializable {

    private final MuracaatService muracaatService;
    private final KisiService kisiService;
    private final YardimService yardimService;
    private final MernisService mernisService;
    private MuracaatDokumanController muracaatDokumanController;

    // Müracaat
    private Muracaat selectedMuracaat;
    private String tcKimlikNo;
    private String adinaBasvurulanTcNo;
    private Boolean komisyonKararli = false;  
    private Boolean kendisiBasvurdu = true;
    private String basvuruMetni;
    private String personelGorusNotu;
    private String dokumanListesi;

    // Tab Kontrolü
    private int activeTabIndex = 0;
    private boolean muracaatTamamlandi = false;
    private boolean aileFertGirildi = false;
    private boolean maddiDurumGirildi = false;
    private boolean tutanakGirildi = false;

    // Listeler
    private List<Muracaat> muracaatList;
    private List<Muracaat> tumMuracaatlar;
    private List<YardimAltTipi> yardimTipleri;
    private List<Long> selectedYardimTipleri = new ArrayList<>();
    
    // Dialog kontrolü
    private boolean tumMuracaatlarDialogAcik = false;
    
    
    public List<Long> getTalepEdilenYardimIdler() {
        return selectedYardimTipleri;
    }

    public MuracaatController(MuracaatService muracaatService, KisiService kisiService, 
                       YardimService yardimService, MernisService mernisService) {
        this.muracaatService = muracaatService;
        this.kisiService = kisiService;
        this.yardimService = yardimService;
        this.mernisService = mernisService;
    }

    @PostConstruct
    public void init() {
        loadYardimTipleri();
        newMuracaat();
    }

    public void newMuracaat() {
        try {
            selectedMuracaat = new Muracaat();
            
            Long sonMuracaatNo = muracaatService.getSonMuracaatNo();
            Long yeniMuracaatNo = sonMuracaatNo + 1;
            selectedMuracaat.setMuracaatNo(yeniMuracaatNo);
            
            selectedMuracaat.setMuracaatTarihi(LocalDate.now());
            selectedMuracaat.setKomisyonKararli(false);
            selectedMuracaat.setKendisiBasvurdu(true);
            selectedMuracaat.setDurum(MuracaatDurum.BEKLEMEDE);
            
            tcKimlikNo = null;
            adinaBasvurulanTcNo = null;
            komisyonKararli = false;
            kendisiBasvurdu = true;
            basvuruMetni = null;
            personelGorusNotu = null;
            dokumanListesi = null;
            
            activeTabIndex = 0;
            muracaatTamamlandi = false;
            aileFertGirildi = false;
            maddiDurumGirildi = false;
            tutanakGirildi = false;
            
            selectedYardimTipleri.clear();
            
            // Geçici dosyaları temizle
            if (muracaatDokumanController != null) {
                muracaatDokumanController.temizleGeciciDosyalar();
            }
            
            MessageUtil.showInfoMessage("Başarılı", "Yeni müracaat başlatıldı. Müracaat No: " + yeniMuracaatNo);
                
        } catch (Exception e) {
            log.error("Yeni müracaat başlatılırken hata", e);
            MessageUtil.showErrorMessage("Hata", "Yeni müracaat başlatılırken hata: " + e.getMessage());
        }
    }

    public void mernistenKisiSorgula() {
        try {
            if (tcKimlikNo == null || tcKimlikNo.isEmpty()) {
                MessageUtil.showWarningMessage("Uyarı", "TC Kimlik No giriniz");
                return;
            }

            Kisi kisi = kisiService.getOrCreateFromMernis(tcKimlikNo);
            selectedMuracaat.setBasvuruSahibi(kisi);

            if (!kisi.getGebzeIkameti()) {
                MessageUtil.showWarningMessage("Uyarı", 
                    "Başvuru sahibi Gebze dışında ikamet ediyor. Müracaat kabul edilmeyebilir.");
            }

            if (muracaatService.findByBasvuruSahibi(kisi.getId()).stream()
                    .anyMatch(m -> m.getDurum() != MuracaatDurum.SONUCLANDI)) {
                MessageUtil.showErrorMessage("Hata", 
                    "Bu kişinin sonuçlanmamış bir müracaatı bulunmaktadır!");
            }

            MessageUtil.showInfoMessage("Başarılı", "Kişi bilgileri getirildi");
            
        } catch (Exception e) {
            log.error("MERNİS sorgu hatası", e);
            MessageUtil.showErrorMessage("Hata", "MERNİS sorgusu sırasında hata oluştu: " + e.getMessage());
        }
    }

    public void saveMuracaat() {
        try {
            selectedMuracaat.setBasvuruMetnu(basvuruMetni);
            selectedMuracaat.setPersonelGorusNotu(personelGorusNotu);
            selectedMuracaat.setDokumanListesi(dokumanListesi);
            selectedMuracaat.setKomisyonKararli(komisyonKararli);
            selectedMuracaat.setKendisiBasvurdu(kendisiBasvurdu);
            
            if (!kendisiBasvurdu && adinaBasvurulanTcNo != null && !adinaBasvurulanTcNo.isEmpty()) {
                Kisi adinaBasvurulan = kisiService.getOrCreateFromMernis(adinaBasvurulanTcNo);
                selectedMuracaat.setAdinaBasvurulanKisi(adinaBasvurulan);
            }
            
            Muracaat saved;
            boolean isUpdate = selectedMuracaat.getId() != null;
            
            if (isUpdate) {
                // GÜNCELLEME MODU
                log.info("Müracaat güncelleniyor - ID: {}", selectedMuracaat.getId());
                
                // Doküman listesini controller'dan oluştur ve set et
                if (muracaatDokumanController != null && muracaatDokumanController.getDokumanList() != null) {
                    StringBuilder dokumanListesiStr = new StringBuilder();
                    for (int i = 0; i < muracaatDokumanController.getDokumanList().size(); i++) {
                        dokumanListesiStr.append(i + 1)
                                        .append(". ")
                                        .append(muracaatDokumanController.getDokumanList().get(i).getOrijinalDosyaAdi());
                        if (i < muracaatDokumanController.getDokumanList().size() - 1) {
                            dokumanListesiStr.append("\n");
                        }
                    }
                    selectedMuracaat.setDokumanListesi(dokumanListesiStr.toString());
                    log.info("📄 Doküman listesi set edildi: {} dosya", muracaatDokumanController.getDokumanList().size());
                }
                
                saved = muracaatService.update(selectedMuracaat);
                // Lazy loading hatalarını önlemek için tekrar yükle (eager fetch ile)
                saved = muracaatService.findByIdWithBasvuruSahibi(saved.getId());
                MessageUtil.showInfoMessage("Başarılı", "Müracaat güncellendi. Müracaat No: " + saved.getMuracaatNo());
            } else {
                // YENİ KAYIT MODU
                log.info("Yeni müracaat oluşturuluyor");
                saved = muracaatService.create(selectedMuracaat);
                
                // Yardım taleplerini ekle (sadece yeni kayıtta)
                for (Long yardimTipiId : selectedYardimTipleri) {
                    YardimAltTipi yardimTipi = yardimService.findYardimAltTipiById(yardimTipiId);
                    MuracaatYardimTalep talep = MuracaatYardimTalep.builder()
                            .muracaat(saved)
                            .yardimAltTipi(yardimTipi)
                            .build();
                    saved.addYardimTalep(talep);
                }
                
                // Geçici dosyaları yükle (varsa)
                if (muracaatDokumanController != null) {
                    muracaatDokumanController.yukleGeciciDosyalar(saved.getId());
                    // Dosyalar yüklendikten sonra listeyi oluştur ve set et
                    if (muracaatDokumanController.getDokumanList() != null && !muracaatDokumanController.getDokumanList().isEmpty()) {
                        StringBuilder dokumanListesiStr = new StringBuilder();
                        for (int i = 0; i < muracaatDokumanController.getDokumanList().size(); i++) {
                            dokumanListesiStr.append(i + 1)
                                            .append(". ")
                                            .append(muracaatDokumanController.getDokumanList().get(i).getOrijinalDosyaAdi());
                            if (i < muracaatDokumanController.getDokumanList().size() - 1) {
                                dokumanListesiStr.append("\n");
                            }
                        }
                        saved.setDokumanListesi(dokumanListesiStr.toString());
                        saved = muracaatService.update(saved);
                        log.info("📄 Yeni kayıt: Doküman listesi set edildi: {} dosya", muracaatDokumanController.getDokumanList().size());
                    }
                }
                
               
                saved = muracaatService.findByIdWithBasvuruSahibi(saved.getId());
                MessageUtil.showInfoMessage("Başarılı", "Müracaat kaydedildi. Müracaat No: " + saved.getMuracaatNo());
                
               
                if (!komisyonKararli) {
                    activeTabIndex = com.sais.constants.MuracaatConstants.TAB_KOMISYONSUZ_KARAR;
                } else {
                    activeTabIndex = com.sais.constants.MuracaatConstants.TAB_AILE_FERTLERI;
                }
            }

            selectedMuracaat = saved;
            muracaatTamamlandi = true;

        } catch (BusinessException e) {
            log.error("İş kuralı hatası: {}", e.getMessage());
            MessageUtil.showErrorMessage("Hata", e.getMessage());
        } catch (Exception e) {
            log.error("Müracaat kaydetme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Beklenmeyen bir hata oluştu: " + e.getMessage());
        }
    }

   
    private void loadYardimTipleri() {
        if (komisyonKararli) {
            yardimTipleri = yardimService.findAllYardimAltTipleri();
        } else {
            yardimTipleri = yardimService.findKomisyonsuzYardimlar();
        }
    }

 
    public void onKomisyonKararliChange() {
        loadYardimTipleri();
        selectedYardimTipleri.clear();
    }

  
    public void onTabChange(org.primefaces.event.TabChangeEvent<?> event) {
        try {
            int newIndex = event.getComponent().getChildren().indexOf(event.getTab());
            
            // Null kontrolü
            if (selectedMuracaat == null) {
                log.warn("selectedMuracaat null, tab değişimi iptal edildi");
                activeTabIndex = com.sais.constants.MuracaatConstants.TAB_MURACAAT_BILGILERI;
                return;
            }
            
            // Komisyonsuz müracaatlar için sadece 0 ve 5. tab'lar aktif olmalı
            if (!komisyonKararli) {
                if (newIndex != com.sais.constants.MuracaatConstants.TAB_MURACAAT_BILGILERI && 
                    newIndex != com.sais.constants.MuracaatConstants.TAB_KOMISYONSUZ_KARAR) {
                    log.warn("Komisyonsuz müracaat - sadece tab 0 ve 5 erişilebilir");
                    return;
                }
            }
            
            // Komisyonlu müracaatlar için sıralı kontroller
            if (komisyonKararli) {
                if (newIndex == com.sais.constants.MuracaatConstants.TAB_AILE_FERTLERI && !muracaatTamamlandi) {
                    MessageUtil.showWarningMessage("Uyarı", "Önce müracaat bilgilerini kaydedin");
                    activeTabIndex = com.sais.constants.MuracaatConstants.TAB_MURACAAT_BILGILERI;
                    return;
                }

                if (newIndex == com.sais.constants.MuracaatConstants.TAB_MADDI_DURUM && !aileFertGirildi) {
                    MessageUtil.showWarningMessage("Uyarı", "Önce aile fert bilgilerini girin");
                    activeTabIndex = com.sais.constants.MuracaatConstants.TAB_AILE_FERTLERI;
                    return;
                }

                if (newIndex == com.sais.constants.MuracaatConstants.TAB_TUTANAK && (!aileFertGirildi || !maddiDurumGirildi)) {
                    MessageUtil.showWarningMessage("Uyarı", "Önce aile fert ve maddi durum bilgilerini girin");
                    return;
                }

                if (newIndex == com.sais.constants.MuracaatConstants.TAB_TUTANAK && 
                    selectedMuracaat.getDurum() != MuracaatDurum.TAHKIKATA_SEVK) {
                    MessageUtil.showWarningMessage("Uyarı", "Tutanak için müracaat tahkikata sevk edilmelidir");
                    return;
                }

                if (newIndex == com.sais.constants.MuracaatConstants.TAB_KOMISYONLU_KARAR) {
                    if (selectedMuracaat.getDurum() != MuracaatDurum.DEGERLENDIRME_KOMISYONU) {
                        MessageUtil.showWarningMessage("Uyarı", "Karar için müracaat değerlendirme komisyonuna gönderilmelidir");
                        return;
                    }
                }
            }

            activeTabIndex = newIndex;
            log.info("Tab değişti: {} -> komisyonKararli={}", newIndex, komisyonKararli);
            
        } catch (Exception e) {
            log.error("Tab değiştirme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Tab değiştirilirken hata oluştu");
        }
    }

    public void nextTab() {
        if (activeTabIndex >= com.sais.constants.MuracaatConstants.MAX_TAB_INDEX) {
            MessageUtil.showWarningMessage("Uyarı", "Son sekmedesiniz");
            return;
        }
        
        activeTabIndex++;
        MessageUtil.showInfoMessage("Başarılı", "Sonraki adıma geçildi");
    }

  
    public void tahkikataSevkEt() {
        try {
            muracaatService.tahkikataSevkEt(selectedMuracaat.getId());
            selectedMuracaat = muracaatService.findById(selectedMuracaat.getId());
            MessageUtil.showInfoMessage("Başarılı", "Müracaat tahkikata sevk edildi");
            activeTabIndex = com.sais.constants.MuracaatConstants.TAB_TUTANAK; // Tutanak sekmesine geç
        } catch (Exception e) {
            MessageUtil.showErrorMessage("Hata", e.getMessage());
        }
    }

  
    public void komisyonaGonder() {
        try {
            muracaatService.komisyonaGonder(selectedMuracaat.getId());
            selectedMuracaat = muracaatService.findById(selectedMuracaat.getId());
            MessageUtil.showInfoMessage("Başarılı", "Müracaat değerlendirme komisyonuna gönderildi");
            activeTabIndex = com.sais.constants.MuracaatConstants.TAB_KOMISYONLU_KARAR; // Yardım kararları sekmesine geç
        } catch (Exception e) {
            MessageUtil.showErrorMessage("Hata", e.getMessage());
        }
    }
  
    public void refreshSelectedMuracaat() {
        if (selectedMuracaat != null && selectedMuracaat.getId() != null) {
            selectedMuracaat = muracaatService.findById(selectedMuracaat.getId());
            log.info("Müracaat yeniden yüklendi - Durum: {}", selectedMuracaat.getDurum());
        }
    }
    

    public void degerlendirmeKomisyonunaGonder() {
        try {
            muracaatService.komisyonaGonder(selectedMuracaat.getId());
            selectedMuracaat = muracaatService.findById(selectedMuracaat.getId());
            log.info("Müracaat değerlendirme komisyonuna gönderildi");
        } catch (Exception e) {
            log.error("Komisyona gönderme hatası", e);
        }
    }

    public void kesinlestir() {
        try {
            muracaatService.kesinlestir(selectedMuracaat.getId());
            selectedMuracaat = muracaatService.findById(selectedMuracaat.getId());
            MessageUtil.showInfoMessage("Başarılı", 
                "Müracaat kesinleştirildi. Karar No: " + selectedMuracaat.getKararNo());
        } catch (Exception e) {
            MessageUtil.showErrorMessage("Hata", e.getMessage());
        }
    }

    public void komisyonsuzTamamla() {
        try {
            muracaatService.komisyonsuzMuracaatiTamamla(selectedMuracaat.getId());
            selectedMuracaat = muracaatService.findById(selectedMuracaat.getId());
            
            MessageUtil.showInfoMessage("Başarılı", 
                "Müracaat tamamlandı! Durum: " + selectedMuracaat.getDurum());
        } catch (Exception e) {
            log.error("Komisyonsuz müracaat tamamlama hatası", e);
            MessageUtil.showErrorMessage("Hata", e.getMessage());
        }
    }

    
    public void adinaBasvurulanKisiSorgula() {
        try {
            if (adinaBasvurulanTcNo == null || adinaBasvurulanTcNo.isEmpty()) {
                MessageUtil.showWarningMessage("Uyarı", "TC Kimlik No giriniz");
                return;
            }
            
            // Başvuran ile aynı kişi olamaz
            if (selectedMuracaat.getBasvuruSahibi() != null && 
                adinaBasvurulanTcNo.equals(selectedMuracaat.getBasvuruSahibi().getTcKimlikNo())) {
                MessageUtil.showErrorMessage("Hata", 
                    "Adına başvurulan kişi, başvuruyu yapan kişi ile aynı olamaz!");
                return;
            }
            
            Kisi kisi = kisiService.getOrCreateFromMernis(adinaBasvurulanTcNo);
            selectedMuracaat.setAdinaBasvurulanKisi(kisi);
            
            MessageUtil.showInfoMessage("Başarılı", 
                "Adına başvurulan kişi bilgileri getirildi: " + kisi.getAd() + " " + kisi.getSoyad());
            
        } catch (Exception e) {
            log.error("Adına başvurulan kişi sorgu hatası", e);
            MessageUtil.showErrorMessage("Hata", "Kişi sorgusu sırasında hata: " + e.getMessage());
        }
    }
    
   
    public void listMuracaatlar() {
        try {
            muracaatList = muracaatService.findAll();
            MessageUtil.showInfoMessage("Başarılı", muracaatList.size() + " adet müracaat listelendi");
        } catch (Exception e) {
            log.error("Müracaat listeleme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Müracaatlar listelenirken hata oluştu");
        }
    }
    
   
    public void onMuracaatSelect() {
        try {
            if (selectedMuracaat == null) {
                return;
            }
            
            selectedMuracaat = muracaatService.findById(selectedMuracaat.getId());
            
            muracaatTamamlandi = selectedMuracaat.getId() != null;
            komisyonKararli = selectedMuracaat.getKomisyonKararli();
            
            selectedYardimTipleri = selectedMuracaat.getYardimTalepleri().stream()
                .map(yt -> yt.getYardimAltTipi().getId())
                .collect(Collectors.toList());
            
            activeTabIndex = com.sais.constants.MuracaatConstants.TAB_MURACAAT_BILGILERI;
            
            MessageUtil.showInfoMessage("Başarılı", "Müracaat yüklendi. Tab'larda gezinebilirsiniz.");
        } catch (Exception e) {
            log.error("Müracaat seçme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Müracaat yüklenemedi: " + e.getMessage());
        }
    }
    
   
    public boolean isAileFertTabEnabled() {
        return muracaatTamamlandi;
    }

    public boolean isMaddiDurumTabEnabled() {
        return muracaatTamamlandi && aileFertGirildi;
    }

    public boolean isTutanakTabEnabled() {
        return komisyonKararli && muracaatTamamlandi && aileFertGirildi && 
               maddiDurumGirildi && selectedMuracaat.getDurum() == MuracaatDurum.TAHKIKATA_SEVK;
    }

    public boolean isYardimKararTabEnabled() {
        if (komisyonKararli) {
            return selectedMuracaat.getDurum() == MuracaatDurum.DEGERLENDIRME_KOMISYONU;
        } else {
            return muracaatTamamlandi;
        }
    }
    
  
    public long getBekleyenMuracaatSayisi() {
        try {
            return muracaatService.findByDurum(MuracaatDurum.BEKLEMEDE).size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    public long getSonuclananMuracaatSayisi() {
        try {
            return muracaatService.findByDurum(MuracaatDurum.SONUCLANDI).size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    public long getTahkikattaMuracaatSayisi() {
        try {
            return muracaatService.findByDurum(MuracaatDurum.TAHKIKATA_SEVK).size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    public long getRedMuracaatSayisi() {
        try {
            return muracaatService.findByDurum(MuracaatDurum.TALEP_IPTAL_EDILDI).size();
        } catch (Exception e) {
            return 0;
        }
    }
    
   
    public void tumMuracaatlariGoster() {
        try {
            tumMuracaatlar = muracaatService.findTamamlananVeBekleyenMuracaatlar();
            MessageUtil.showInfoMessage("Bilgi", tumMuracaatlar.size() + " adet müracaat bulundu");
        } catch (Exception e) {
            log.error("Müracaatlar yüklenirken hata", e);
            MessageUtil.showErrorMessage("Hata", "Müracaatlar yüklenirken hata oluştu: " + e.getMessage());
        }
    }
    
    
    public void muracaataDevamEt(Muracaat muracaat) {
        try {
           
            selectedMuracaat = muracaatService.findByIdWithAllDetails(muracaat.getId());
            
           
            komisyonKararli = selectedMuracaat.getKomisyonKararli();
            kendisiBasvurdu = selectedMuracaat.getKendisiBasvurdu();
            basvuruMetni = selectedMuracaat.getBasvuruMetnu();
            personelGorusNotu = selectedMuracaat.getPersonelGorusNotu();
            dokumanListesi = selectedMuracaat.getDokumanListesi();
            
           
            selectedYardimTipleri = selectedMuracaat.getYardimTalepleri().stream()
                .map(yt -> yt.getYardimAltTipi().getId())
                .collect(Collectors.toList());
            
          
            if (selectedMuracaat.getDurum() == MuracaatDurum.SONUCLANDI) {
                
                activeTabIndex = com.sais.constants.MuracaatConstants.TAB_MURACAAT_BILGILERI;
                MessageUtil.showInfoMessage("Bilgi", "Müracaat detayları görüntüleniyor");
            } else if (selectedMuracaat.getDurum() == MuracaatDurum.BEKLEMEDE) {
               
                if (!komisyonKararli) {
                   
                    activeTabIndex = com.sais.constants.MuracaatConstants.TAB_KOMISYONSUZ_KARAR;
                } else {
                   
                    if (!aileFertGirildi) {
                        activeTabIndex = com.sais.constants.MuracaatConstants.TAB_AILE_FERTLERI;
                    } else if (!maddiDurumGirildi) {
                        activeTabIndex = com.sais.constants.MuracaatConstants.TAB_MADDI_DURUM;
                    } else if (selectedMuracaat.getDurum() == MuracaatDurum.TAHKIKATA_SEVK) {
                        activeTabIndex = com.sais.constants.MuracaatConstants.TAB_TUTANAK;
                    } else if (selectedMuracaat.getDurum() == MuracaatDurum.DEGERLENDIRME_KOMISYONU) {
                        activeTabIndex = com.sais.constants.MuracaatConstants.TAB_KOMISYONLU_KARAR;
                    } else {
                        activeTabIndex = com.sais.constants.MuracaatConstants.TAB_MURACAAT_BILGILERI;
                    }
                }
                MessageUtil.showInfoMessage("Başarılı", "Müracaat yüklendi. Kaldığınız yerden devam edebilirsiniz.");
            }
            
            muracaatTamamlandi = true;
            
        } catch (Exception e) {
            log.error("Müracaata devam edilirken hata", e);
            MessageUtil.showErrorMessage("Hata", "Müracaat yüklenirken hata oluştu: " + e.getMessage());
        }
    }
    
  
    public void muracaatiIptalEt(Muracaat muracaat) {
        try {
            muracaatService.yarimKalanMuracaatiIptalEt(muracaat.getId());
            tumMuracaatlar.remove(muracaat);
            MessageUtil.showInfoMessage("Başarılı", "Müracaat iptal edildi");
        } catch (Exception e) {
            log.error("Müracaat iptal edilirken hata", e);
            MessageUtil.showErrorMessage("Hata", "Müracaat iptal edilirken hata oluştu: " + e.getMessage());
        }
    }
}

