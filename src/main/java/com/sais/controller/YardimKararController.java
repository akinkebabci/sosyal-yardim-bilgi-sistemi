package com.sais.controller;

import com.sais.entity.*;
import com.sais.enums.YardimDurum;
import com.sais.enums.YardimTipi;
import com.sais.exception.BusinessException;
import com.sais.service.LookupService;
import com.sais.service.YardimService;
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


@Component("yardimKararController")
@ViewScoped
@Getter
@Setter
@Slf4j
public class YardimKararController implements Serializable {

    private final YardimService yardimService;
    private final LookupService lookupService;
    private final com.sais.service.MuracaatService muracaatService;
    
    private MuracaatController muracaatController;

    // Form Fields
    private Long muracaatId;
    private boolean komisyonKararli;
    private LocalDate toplantiTarihi;

    // Yardım Karar
    private YardimAltTipi selectedYardimAltTipi;
    private Long selectedYardimAltTipiId;  // ID-based selection (ajax=false için)
    private YardimDurum selectedYardimDurum;
    private Double verilenTutar;
    private Integer adetSayi;
    private String aciklama;
    private YardimKarar selectedYardimKarar;
    
    // Listeler
    private List<YardimKarar> yardimKararlari = new ArrayList<>();
    private List<YardimKarar> komisyonsuzKararlar = new ArrayList<>();
    private List<YardimAltTipi> yardimTipleri = new ArrayList<>();  // KOMİSYONLU TAB için
    private List<YardimAltTipi> komisyonsuzYardimlar = new ArrayList<>();  // KOMİSYONSUZ TAB için
    
    // Lookup Lists
    private YardimDilimi selectedYardimDilimi;
    private YardimDonemi selectedYardimDonemi;
    private YardimRedSebebi selectedRedSebebi;
    private HesapBilgisi selectedHesapBilgisi;
    private Long selectedHesapBilgisiId;  // ID-based selection
    private List<YardimDilimi> yardimDilimleri;
    private List<YardimDonemi> yardimDonemleri;
    private List<YardimRedSebebi> redSebepleri;
    private List<HesapBilgisi> hesapBilgileri = new ArrayList<>();

    // Talep edilen yardımlar
    private List<Long> talepEdilenYardimIdler = new ArrayList<>();
    
    // Dinamik görünüm kontrolleri
    private boolean talepDisindaSecim = false;

    public YardimKararController(YardimService yardimService, 
                                 LookupService lookupService,
                                 com.sais.service.MuracaatService muracaatService) {
        this.yardimService = yardimService;
        this.lookupService = lookupService;
        this.muracaatService = muracaatService;
    }

    @PostConstruct
    public void init() {
        loadLookupData();
        prepareNewYardimKarar();
        try {
            yardimTipleri = yardimService.findAllYardimAltTipleri();
        } catch (Exception e) {
            log.error("Init'te yardım tipleri yüklenemedi", e);
        }
    }

    /**
     * Lookup verilerini yükler
     */
    private void loadLookupData() {
        yardimDilimleri = lookupService.getAllYardimDilimleri();
        yardimDonemleri = lookupService.getAllYardimDonemleri();
        redSebepleri = lookupService.getAllYardimRedSebepleri();
    }

    public void loadYardimKararlari(Long muracaatId, boolean komisyonKararli, 
                                    List<Long> talepEdilenYardimIdler) {
        try {
            if (muracaatId == null) {
                return;
            }
            
        this.muracaatId = muracaatId;
        this.komisyonKararli = komisyonKararli;
            this.talepEdilenYardimIdler = talepEdilenYardimIdler != null ? talepEdilenYardimIdler : new ArrayList<>();

            if (yardimDilimleri == null || yardimDilimleri.isEmpty()) {
                loadLookupData();
            }

        if (komisyonKararli) {
            List<YardimAltTipi> tumYardimlar = yardimService.findAllYardimAltTipleri();
            yardimTipleri = tumYardimlar.stream()
                .sorted((y1, y2) -> {
                    boolean y1Talep = this.talepEdilenYardimIdler.contains(y1.getId());
                    boolean y2Talep = this.talepEdilenYardimIdler.contains(y2.getId());
                    
                    if (y1Talep && !y2Talep) return -1;
                    if (!y1Talep && y2Talep) return 1;
                    
                    int tipiKarsilastir = y1.getYardimTipi().compareTo(y2.getYardimTipi());
                    if (tipiKarsilastir != 0) return tipiKarsilastir;
                    
                    return Integer.compare(y1.getSiraNo() != null ? y1.getSiraNo() : 0, 
                                          y2.getSiraNo() != null ? y2.getSiraNo() : 0);
                })
                .collect(Collectors.toList());
        } else {
            komisyonsuzYardimlar = yardimService.findKomisyonsuzYardimlar();
        }

            List<YardimKarar> tumKararlar = yardimService.findYardimKararlariByMuracaat(muracaatId);
            
            if (komisyonKararli) {
                yardimKararlari = tumKararlar.stream()
                    .filter(k -> k.getKomisyonKararli())
                    .collect(Collectors.toList());
            } else {
                komisyonsuzKararlar = tumKararlar.stream()
                    .filter(k -> !k.getKomisyonKararli())
                    .collect(Collectors.toList());
            }
            
            loadHesapBilgileri(muracaatId);

        } catch (Exception e) {
            log.error("Yardım kararları yükleme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Yardım kararları yüklenemedi: " + e.getMessage());
        }
    }
    
    private void loadHesapBilgileri(Long muracaatId) {
        try {
            if (muracaatId == null) {
                hesapBilgileri = new ArrayList<>();
                return;
            }
            
            hesapBilgileri = yardimService.getHesapBilgileriByMuracaatId(muracaatId);
            
        } catch (Exception e) {
            log.error("Hesap bilgileri yükleme hatası", e);
            hesapBilgileri = new ArrayList<>();
        }
    }

    /**
     * UI için helper metodlar
     */
    public long getNakdiYardimSayisi() {
        if (yardimTipleri == null) return 0;
        return yardimTipleri.stream()
            .filter(y -> y.getYardimTipi() == YardimTipi.NAKDI)
            .count();
    }
    
    public long getAyniYardimSayisi() {
        if (yardimTipleri == null) return 0;
        return yardimTipleri.stream()
            .filter(y -> y.getYardimTipi() == YardimTipi.AYNI)
            .count();
    }

    /**
     * Yeni yardım kararı hazırlar
     */
    public void prepareNewYardimKarar() {
        selectedYardimKarar = YardimKarar.builder()
                .komisyonKararli(komisyonKararli)
                .yardimDurum(YardimDurum.KABUL_EDILDI)
                .kesinlesti(false)
                .build();
    }

    public void onYardimTipiChangeById() {
        if (selectedYardimAltTipiId == null) {
            selectedYardimAltTipi = null;
            return;
        }

        selectedYardimAltTipi = yardimTipleri.stream()
            .filter(y -> y.getId().equals(selectedYardimAltTipiId))
            .findFirst()
            .orElse(null);
        
        if (selectedYardimAltTipi != null) {
            talepDisindaSecim = !talepEdilenYardimIdler.contains(selectedYardimAltTipi.getId());
            
            
            if (talepDisindaSecim) {
                selectedYardimDurum = null; 
            } else {
                selectedYardimDurum = null; 
            }
        }
    }
    
    public void onYardimTipiChange() {
        if (selectedYardimAltTipi == null) {
            selectedYardimAltTipiId = null;
            return;
        }

        selectedYardimAltTipiId = selectedYardimAltTipi.getId();
        talepDisindaSecim = !talepEdilenYardimIdler.contains(selectedYardimAltTipi.getId());
        
       
        selectedYardimDurum = null;
    }
    
    public boolean isShowNakdiDetaylar() {
        return selectedYardimAltTipi != null 
            && selectedYardimAltTipi.getYardimTipi() == YardimTipi.NAKDI
            && selectedYardimDurum == YardimDurum.KABUL_EDILDI;
    }
    
    public boolean isShowAyniDetaylar() {
        return selectedYardimAltTipi != null 
            && selectedYardimAltTipi.getYardimTipi() == YardimTipi.AYNI
            && selectedYardimDurum == YardimDurum.KABUL_EDILDI;
    }
    
    public boolean isShowRedDetaylar() {
        return selectedYardimDurum == YardimDurum.RED_EDILDI;
    }
    
   
    public List<YardimDurum> getAvailableYardimDurumlari() {
        List<YardimDurum> durumlar = new ArrayList<>();
        
        
        durumlar.add(YardimDurum.KABUL_EDILDI);
        
       
        if (!talepDisindaSecim) {
            durumlar.add(YardimDurum.RED_EDILDI);
        }
        
        return durumlar;
    }

    public void onYardimDurumChange() {
        if (selectedYardimKarar.getYardimDurum() == YardimDurum.RED_EDILDI) {
            selectedYardimKarar.setVerilenTutar(null);
            selectedYardimKarar.setAdetSayi(null);
            selectedYardimKarar.setYardimDilimi(null);
            selectedYardimKarar.setYardimDonemi(null);
            selectedYardimKarar.setHesapBilgisi(null);
        } else {
            selectedYardimKarar.setRedSebebi(null);
        }
    }

    public void addKomisyonluKarar() {
        this.komisyonKararli = true;
        addKarar();
    }
    
    private void addKarar() {
        try {
            if (selectedYardimAltTipiId != null && selectedYardimAltTipi == null) {
                selectedYardimAltTipi = yardimTipleri.stream()
                    .filter(y -> y.getId().equals(selectedYardimAltTipiId))
                    .findFirst()
                    .orElse(null);
            }
            
            if (selectedHesapBilgisiId != null && selectedHesapBilgisi == null) {
                selectedHesapBilgisi = hesapBilgileri.stream()
                    .filter(h -> h.getId().equals(selectedHesapBilgisiId))
                    .findFirst()
                    .orElse(null);
            }

            YardimKarar karar = YardimKarar.builder()
                .muracaat(Muracaat.builder().id(muracaatId).build())
                .yardimAltTipi(selectedYardimAltTipi)
                .yardimDurum(selectedYardimDurum)
                .toplantiTarihi(toplantiTarihi)
                .komisyonKararli(komisyonKararli)
                .talepEdilmisMi(!talepDisindaSecim)
                .verilenTutar(verilenTutar)
                .adetSayi(adetSayi)
                .yardimDilimi(selectedYardimDilimi)
                .yardimDonemi(selectedYardimDonemi)
                .hesapBilgisi(selectedHesapBilgisi)
                .redSebebi(selectedRedSebebi)
                .aciklama(aciklama)
                .kesinlesti(false)
                .build();
            
            yardimService.createYardimKarar(karar);
            
            List<YardimKarar> tumKararlar = yardimService.findYardimKararlariByMuracaat(muracaatId);
            yardimKararlari = tumKararlar.stream()
                .filter(k -> k.getKomisyonKararli())
                .collect(Collectors.toList());
            
            clearForm();
            MessageUtil.showInfoMessage("Başarılı", "Yardım kararı eklendi");

        } catch (BusinessException e) {
            log.error("İş kuralı hatası: {}", e.getMessage());
            MessageUtil.showErrorMessage("Hata", e.getMessage());
        } catch (Exception e) {
            log.error("Yardım kararı ekleme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Beklenmeyen bir hata oluştu: " + e.getMessage());
        }
    }

    public void addKomisyonsuzKarar() {
        this.komisyonKararli = false;
        
        try {
            YardimKarar karar = YardimKarar.builder()
                .muracaat(Muracaat.builder().id(muracaatId).build())
                .yardimAltTipi(selectedYardimAltTipi)
                .yardimDurum(YardimDurum.KABUL_EDILDI)
                .toplantiTarihi(null)
                .komisyonKararli(false)
                .talepEdilmisMi(talepEdilenYardimIdler.contains(selectedYardimAltTipi.getId()))
                .adetSayi(adetSayi)
                .aciklama(aciklama)
                .kesinlesti(false)
                .build();
            
            yardimService.createYardimKarar(karar);
            
            List<YardimKarar> tumKararlar = yardimService.findYardimKararlariByMuracaat(muracaatId);
            komisyonsuzKararlar = tumKararlar.stream()
                .filter(k -> !k.getKomisyonKararli())
                .collect(Collectors.toList());
            
            clearForm();
            MessageUtil.showInfoMessage("Başarılı", "Yardım kararı eklendi");

        } catch (BusinessException e) {
            log.error("İş kuralı hatası: {}", e.getMessage());
            MessageUtil.showErrorMessage("Hata", e.getMessage());
        } catch (Exception e) {
            log.error("Komisyonsuz yardım kararı ekleme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Beklenmeyen bir hata oluştu: " + e.getMessage());
        }
    }

    public void updateYardimKarar() {
        try {
            yardimService.updateYardimKarar(selectedYardimKarar);
            MessageUtil.showInfoMessage("Başarılı", "Yardım kararı güncellendi");
            yardimKararlari = yardimService.findYardimKararlariByMuracaat(muracaatId);
        } catch (Exception e) {
            log.error("Yardım kararı güncelleme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Yardım kararı güncellenemedi: " + e.getMessage());
        }
    }

    public void deleteKarar(YardimKarar karar) {
        try {
            boolean komisyonluMuydu = karar.getKomisyonKararli();
            yardimService.deleteYardimKarar(karar.getId());
            
            List<YardimKarar> tumKararlar = yardimService.findYardimKararlariByMuracaat(muracaatId);
            
            if (komisyonluMuydu) {
                yardimKararlari = tumKararlar.stream()
                    .filter(k -> k.getKomisyonKararli())
                    .collect(Collectors.toList());
            } else {
                komisyonsuzKararlar = tumKararlar.stream()
                    .filter(k -> !k.getKomisyonKararli())
                    .collect(Collectors.toList());
            }
            
            MessageUtil.showInfoMessage("Başarılı", "Yardım kararı silindi");
        } catch (Exception e) {
            log.error("Yardım kararı silme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Yardım kararı silinemedi: " + e.getMessage());
        }
    }

    public boolean isTalepEdilmis(Long yardimAltTipiId) {
        return talepEdilenYardimIdler.contains(yardimAltTipiId);
    }

    public boolean isNakdiYardim() {
        return selectedYardimKarar.getYardimAltTipi() != null && 
               selectedYardimKarar.getYardimAltTipi().getYardimTipi() == YardimTipi.NAKDI;
    }

    public boolean isAyniYardim() {
        return selectedYardimKarar.getYardimAltTipi() != null && 
               selectedYardimKarar.getYardimAltTipi().getYardimTipi() == YardimTipi.AYNI;
    }

    public boolean isRedDurumu() {
        return selectedYardimKarar.getYardimDurum() == YardimDurum.RED_EDILDI;
    }

    public void clearForm() {
        selectedYardimAltTipi = null;
        selectedYardimAltTipiId = null;
        selectedYardimDurum = null;
        verilenTutar = null;
        adetSayi = null;
        aciklama = null;
        selectedYardimDilimi = null;
        selectedYardimDonemi = null;
        selectedRedSebebi = null;
        selectedHesapBilgisi = null;
        selectedHesapBilgisiId = null;
        talepDisindaSecim = false;
    }
    
    public LocalDate getBugun() {
        return LocalDate.now();
    }
    
    public LocalDate getMinToplantiTarihi() {
        if (yardimKararlari == null || yardimKararlari.isEmpty()) {
            return null;
        }
        
        return yardimKararlari.stream()
            .map(YardimKarar::getToplantiTarihi)
            .filter(t -> t != null)
            .max(LocalDate::compareTo)
            .orElse(null);
    }

    public int getKararSayisi() {
        return yardimKararlari.size() + komisyonsuzKararlar.size();
    }
    
    public boolean isHasKesinlesmemisKarar() {
        if (yardimKararlari == null || yardimKararlari.isEmpty()) {
            return false;
        }
        return yardimKararlari.stream().anyMatch(k -> !k.getKesinlesti());
    }
    
    public void kesinlestir() {
        try {
            if (yardimKararlari.isEmpty()) {
                MessageUtil.showErrorMessage("Hata", "Kesinleştirmek için en az 1 yardım kararı girilmelidir!");
                return;
            }
            
            Integer sonKararSayisi = yardimService.getSonKararSayisi();
            Integer yeniKararSayisi = (sonKararSayisi != null ? sonKararSayisi : 0) + 1;
            LocalDate kararTarihi = LocalDate.now();
            
            yardimService.kesinlestirVeSonuclandir(muracaatId, yardimKararlari);
            
           
            if (muracaatController != null) {
                muracaatController.refreshSelectedMuracaat();
                log.info("✅ MuracaatController güncellendi - Durum: {}", 
                        muracaatController.getSelectedMuracaat().getDurum());
            }
            
            loadYardimKararlari(muracaatId, komisyonKararli, talepEdilenYardimIdler);
            
           
            StringBuilder mesaj = new StringBuilder();
            mesaj.append(String.format("Karar No: %d\n\n", yeniKararSayisi));
            mesaj.append(String.format("Karar Tarihi: %s\n\n", kararTarihi));
            mesaj.append(String.format("Toplam %d adet yardım kararı kesinleştirildi:\n\n", yardimKararlari.size()));
            
           
            List<YardimKarar> nakdiKararlar = yardimKararlari.stream()
                .filter(k -> k.getYardimAltTipi().getYardimTipi() == YardimTipi.NAKDI && k.getYardimDurum() == YardimDurum.KABUL_EDILDI)
                .collect(Collectors.toList());
            
            if (!nakdiKararlar.isEmpty()) {
                mesaj.append("Nakdi Yardımlar:\n");
                double toplamNakdi = 0;
                for (YardimKarar k : nakdiKararlar) {
                    mesaj.append(String.format("   • %s: %.2f ₺\n", k.getYardimAltTipi().getAdi(), k.getVerilenTutar()));
                    toplamNakdi += k.getVerilenTutar();
                }
                mesaj.append(String.format("   Toplam: %.2f ₺\n\n", toplamNakdi));
            }
            
            
            List<YardimKarar> ayniKararlar = yardimKararlari.stream()
                .filter(k -> k.getYardimAltTipi().getYardimTipi() == YardimTipi.AYNI && k.getYardimDurum() == YardimDurum.KABUL_EDILDI)
                .collect(Collectors.toList());
            
            if (!ayniKararlar.isEmpty()) {
                mesaj.append("Ayni Yardımlar:\n");
                for (YardimKarar k : ayniKararlar) {
                    mesaj.append(String.format("   • %s: %d adet\n", k.getYardimAltTipi().getAdi(), k.getAdetSayi()));
                }
                mesaj.append("\n");
            }
            
          
            List<YardimKarar> redKararlar = yardimKararlari.stream()
                .filter(k -> k.getYardimDurum() == YardimDurum.RED_EDILDI)
                .collect(Collectors.toList());
            
            if (!redKararlar.isEmpty()) {
                mesaj.append("Red Edilen Yardımlar:\n");
                for (YardimKarar k : redKararlar) {
                    mesaj.append(String.format("   • %s\n", k.getYardimAltTipi().getAdi()));
                }
                mesaj.append("\n");
            }
            
            mesaj.append("Müracaat durumu: SONUÇLANDI");
            
            MessageUtil.showInfoMessage("Kararlar Kesinleştirildi", mesaj.toString());
            
        } catch (Exception e) {
            log.error("Kesinleştirme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Kararlar kesinleştirilemedi: " + e.getMessage());
        }
    }
}

