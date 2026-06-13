package com.sais.controller;

import com.sais.entity.*;
import com.sais.enums.*;
import com.sais.exception.BusinessException;
import com.sais.service.AileMaddiDurumService;
import com.sais.service.LookupService;
import com.sais.util.MessageUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Component("maddiDurumController")
@ViewScoped
@Getter
@Setter
@Slf4j
public class MaddiDurumController implements Serializable {

    private final AileMaddiDurumService maddiDurumService;
    private final LookupService lookupService;
    private final com.sais.service.AileFertService aileFertService;
    
    @Getter
    @Setter
    private MuracaatController muracaatController;

    private Long muracaatId;
    private AileMaddiDurum maddiDurum;

    private AileFert selectedGelirKisi;
    private GelirTuru selectedGelirTuru;
    private Double gelirTutari;
    private List<GelirBilgisi> gelirBilgileri;
    private List<GelirTuru> gelirTurleri;
    private List<AileFert> aileFertleri;

    private BorcTuru selectedBorcTuru;
    private Double borcTutari;
    private List<BorcBilgisi> borcBilgileri;
    private List<BorcTuru> borcTurleri;

    private GayrimenkulBilgisi gayrimenkulBilgisi;

    public MaddiDurumController(AileMaddiDurumService maddiDurumService, 
                               LookupService lookupService,
                               com.sais.service.AileFertService aileFertService) {
        this.maddiDurumService = maddiDurumService;
        this.lookupService = lookupService;
        this.aileFertService = aileFertService;
    }

    @PostConstruct
    public void init() {
        loadLookupData();
        prepareNewForms();
    }

    private void loadLookupData() {
        gelirTurleri = lookupService.getAllGelirTurleri();
        borcTurleri = lookupService.getAllBorcTurleri();
    }

    private void prepareNewForms() {
        selectedGelirKisi = null;
        selectedGelirTuru = null;
        gelirTutari = null;
        selectedBorcTuru = null;
        borcTutari = null;
        
       
        gayrimenkulBilgisi = new GayrimenkulBilgisi();
        gayrimenkulBilgisi.setEviVar(false);
        gayrimenkulBilgisi.setKiradaEviVar(false);
        gayrimenkulBilgisi.setArabaVar(false);
        gayrimenkulBilgisi.setGayrimenkulVar(false);
    }

    
    public void loadMaddiDurum(Long muracaatId) {
        try {
            this.muracaatId = muracaatId;

            if (gelirTurleri == null || gelirTurleri.isEmpty() || 
                borcTurleri == null || borcTurleri.isEmpty()) {
                loadLookupData();
            }

            aileFertleri = aileFertService.findByMuracaatId(muracaatId);

            if (maddiDurumService.existsByMuracaatId(muracaatId)) {
                maddiDurum = maddiDurumService.findByMuracaatId(muracaatId);
                gayrimenkulBilgisi = maddiDurum.getGayrimenkulBilgisi();
                if (gayrimenkulBilgisi == null) {
                    gayrimenkulBilgisi = new GayrimenkulBilgisi();
                    gayrimenkulBilgisi.setEviVar(false);
                    gayrimenkulBilgisi.setKiradaEviVar(false);
                    gayrimenkulBilgisi.setArabaVar(false);
                    gayrimenkulBilgisi.setGayrimenkulVar(false);
                    gayrimenkulBilgisi.setAileMaddiDurum(maddiDurum);
                    maddiDurum.setGayrimenkulBilgisi(gayrimenkulBilgisi);
                }
                
                gelirBilgileri = maddiDurum.getGelirBilgileri();
                borcBilgileri = maddiDurum.getBorcBilgileri();
    
            } else {
                maddiDurum = new AileMaddiDurum();
                Muracaat muracaat = new Muracaat();
                muracaat.setId(muracaatId);
                maddiDurum.setMuracaat(muracaat);
                
             
                maddiDurum.setToplamGelir(0.0);
                maddiDurum.setToplamBorc(0.0);
                
                
                gelirBilgileri = new java.util.ArrayList<>();
                borcBilgileri = new java.util.ArrayList<>();
                
                
                gayrimenkulBilgisi = new GayrimenkulBilgisi();
                gayrimenkulBilgisi.setEviVar(false);
                gayrimenkulBilgisi.setKiradaEviVar(false);
                gayrimenkulBilgisi.setArabaVar(false);
                gayrimenkulBilgisi.setGayrimenkulVar(false);
                gayrimenkulBilgisi.setAileMaddiDurum(maddiDurum);
                maddiDurum.setGayrimenkulBilgisi(gayrimenkulBilgisi);
            }

        } catch (Exception e) {
            log.error("Maddi durum yükleme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Maddi durum bilgileri yüklenemedi");
        }
    }
    
    public void saveGayrimenkulBilgisi() {
        try {
            log.info("Gayrimenkul bilgisi kaydediliyor");

            maddiDurum = maddiDurumService.updateGayrimenkulBilgisi(muracaatId, gayrimenkulBilgisi);
            MessageUtil.showInfoMessage("Başarılı", "Gayrimenkul bilgisi kaydedildi");

        } catch (Exception e) {
            log.error("Gayrimenkul bilgisi kaydetme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Gayrimenkul bilgisi kaydedilemedi");
        }
    }

    public void saveMaddiDurum() {
        try {
            if (gayrimenkulBilgisi != null && gayrimenkulBilgisi.getId() == null) {
                gayrimenkulBilgisi.setAileMaddiDurum(maddiDurum);
                maddiDurum.setGayrimenkulBilgisi(gayrimenkulBilgisi);
            }

            maddiDurum = maddiDurumService.createOrUpdate(maddiDurum);
            loadMaddiDurum(muracaatId);
            
            if (muracaatController != null) {
                muracaatController.setMaddiDurumGirildi(true);
                
                if (muracaatController.getSelectedMuracaat() != null) {
                    muracaatController.tahkikataSevkEt();
                }
            }
            
            MessageUtil.showInfoMessage("Başarılı", 
                String.format("Maddi durum kaydedildi. Toplam Gelir: %.2f ₺, Toplam Borç: %.2f ₺", 
                    getToplamGelir(), getToplamBorc()));

        } catch (Exception e) {
            log.error("Maddi durum kaydetme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Maddi durum kaydedilemedi: " + e.getMessage());
        }
    }

    public void saveMaddiDurumVeIleri() {
        try {
            if (gayrimenkulBilgisi != null && gayrimenkulBilgisi.getId() == null) {
                gayrimenkulBilgisi.setAileMaddiDurum(maddiDurum);
                maddiDurum.setGayrimenkulBilgisi(gayrimenkulBilgisi);
            }

            maddiDurum = maddiDurumService.createOrUpdate(maddiDurum);
            loadMaddiDurum(muracaatId);
            
            if (muracaatController != null) {
                muracaatController.setMaddiDurumGirildi(true);
                
                if (muracaatController.getSelectedMuracaat() != null) {
                    if (muracaatController.getKomisyonKararli()) {
                        muracaatController.tahkikataSevkEt();
                    } else {
                        muracaatController.setActiveTabIndex(com.sais.constants.MuracaatConstants.TAB_AILE_FERTLERI);
                    }
                }
            }

        } catch (Exception e) {
            log.error("Maddi durum kaydetme ve ilerleme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Maddi durum kaydedilemedi: " + e.getMessage());
        }
    }
    
    public void addGelir() {
        try {
            GelirBilgisi gelirBilgisi = GelirBilgisi.builder()
                .kisi(selectedGelirKisi != null ? selectedGelirKisi.getKisi() : null)
                .gelirTuru(selectedGelirTuru)
                .gelirTutari(gelirTutari)
                .build();
            
            maddiDurumService.addGelirWithValidation(muracaatId, gelirBilgisi);
            loadMaddiDurum(muracaatId);
            
            selectedGelirKisi = null;
            selectedGelirTuru = null;
            gelirTutari = null;
            
            MessageUtil.showInfoMessage("Başarılı", "Gelir bilgisi eklendi");
            
        } catch (BusinessException e) {
            MessageUtil.showErrorMessage("Hata", e.getMessage());
        } catch (Exception e) {
            log.error("Gelir ekleme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Gelir eklenirken hata: " + e.getMessage());
        }
    }
    
    public void addBorc() {
        try {
            BorcBilgisi borcBilgisi = BorcBilgisi.builder()
                .borcTuru(selectedBorcTuru)
                .borcTutari(borcTutari)
                .build();
            
            maddiDurumService.addBorcWithValidation(muracaatId, borcBilgisi);
            loadMaddiDurum(muracaatId);
            
            selectedBorcTuru = null;
            borcTutari = null;
            
            MessageUtil.showInfoMessage("Başarılı", "Borç bilgisi eklendi");
            
        } catch (BusinessException e) {
            MessageUtil.showErrorMessage("Hata", e.getMessage());
        } catch (Exception e) {
            log.error("Borç ekleme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Borç eklenirken hata: " + e.getMessage());
        }
    }
    
    /**
     * Gelir sil
     */
    public void deleteGelir(GelirBilgisi gelirBilgisi) {
        try {
            maddiDurumService.removeGelirBilgisi(muracaatId, gelirBilgisi.getId());
            loadMaddiDurum(muracaatId);
            MessageUtil.showInfoMessage("Başarılı", "Gelir bilgisi silindi");
        } catch (Exception e) {
            log.error("Gelir silme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Gelir silinemedi");
        }
    }
    
    
    public void deleteBorc(BorcBilgisi borcBilgisi) {
        try {
            maddiDurumService.removeBorcBilgisi(muracaatId, borcBilgisi.getId());
            loadMaddiDurum(muracaatId);
            MessageUtil.showInfoMessage("Başarılı", "Borç bilgisi silindi");
        } catch (Exception e) {
            log.error("Borç silme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Borç silinemedi");
        }
    }
    
   
    public void saveGayrimenkul() {
        try {
           
            MessageUtil.showInfoMessage("Başarılı", "Gayrimenkul bilgileri kaydedildi");
        } catch (Exception e) {
            log.error("Gayrimenkul kaydetme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Kayıt sırasında hata oluştu");
        }
    }
    
    public Double getToplamGelir() {
        if (maddiDurum == null || maddiDurum.getToplamGelir() == null) {
            return 0.0;
        }
        return maddiDurum.getToplamGelir();
    }

    public Double getToplamBorc() {
        if (maddiDurum == null || maddiDurum.getToplamBorc() == null) {
            return 0.0;
        }
        return maddiDurum.getToplamBorc();
    }
    
    public Double getNetGelir() {
        return getToplamGelir() - getToplamBorc();
    }
    
    public String getYakinlik(Kisi kisi) {
        if (kisi == null || aileFertleri == null) {
            return "-";
        }
        
        return aileFertleri.stream()
            .filter(fert -> fert.getKisi() != null && fert.getKisi().getId().equals(kisi.getId()))
            .findFirst()
            .map(fert -> fert.getYakinlikKodu() != null ? fert.getYakinlikKodu().getAdi() : "-")
            .orElse("-");
    }
    
    public void temizle() {
        try {
            prepareNewForms();
            
            if (maddiDurum != null) {
                gayrimenkulBilgisi = new GayrimenkulBilgisi();
                gayrimenkulBilgisi.setEviVar(false);
                gayrimenkulBilgisi.setKiradaEviVar(false);
                gayrimenkulBilgisi.setArabaVar(false);
                gayrimenkulBilgisi.setGayrimenkulVar(false);
                gayrimenkulBilgisi.setAileMaddiDurum(maddiDurum);
                maddiDurum.setGayrimenkulBilgisi(gayrimenkulBilgisi);
            }
            
            
            MessageUtil.showInfoMessage("Başarılı", "Form alanları temizlendi");
        } catch (Exception e) {
            log.error("Form temizleme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Form temizlenirken hata oluştu");
        }
    }
    
  
    public void onGayrimenkulCheckboxChange() {
        try {
            log.info("Gayrimenkul bilgisi değişti - Ev: {}, Kira: {}, Araba: {}, Gayrimenkul: {}",
                gayrimenkulBilgisi.getEviVar(),
                gayrimenkulBilgisi.getKiradaEviVar(),
                gayrimenkulBilgisi.getArabaVar(),
                gayrimenkulBilgisi.getGayrimenkulVar());
            
            if (maddiDurum.getId() == null) {
                maddiDurum = maddiDurumService.createOrUpdate(maddiDurum);
                log.info("   ├─ Maddi durum kaydedildi, ID: {}", maddiDurum.getId());
            }
            
           
            if (gayrimenkulBilgisi.getId() == null) {
                gayrimenkulBilgisi.setAileMaddiDurum(maddiDurum);
                maddiDurum.setGayrimenkulBilgisi(gayrimenkulBilgisi);
            }
            
            maddiDurum = maddiDurumService.createOrUpdate(maddiDurum);
            
            
            loadMaddiDurum(muracaatId);
            
            
            
        } catch (Exception e) {
            log.error("Gayrimenkul checkbox kaydetme hatası", e);
        }
    }
 
    
    public List<EvMulkiyet> getEvMulkiyetList() {
        return Arrays.asList(EvMulkiyet.values());
    }
    
    public List<EvTipi> getEvTipiList() {
        return Arrays.asList(EvTipi.values());
    }
    
    public List<EvYakacakTipi> getEvYakacakTipiList() {
        return Arrays.asList(EvYakacakTipi.values());
    }
    
    public List<GayrimenkulTuru> getGayrimenkulTuruList() {
        return Arrays.asList(GayrimenkulTuru.values());
    }
}


