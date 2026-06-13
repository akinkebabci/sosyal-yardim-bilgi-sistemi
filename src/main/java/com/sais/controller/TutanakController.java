package com.sais.controller;

import com.sais.entity.Muracaat;
import com.sais.entity.Personel;
import com.sais.entity.TutanakBilgisi;
import com.sais.exception.BusinessException;
import com.sais.service.TutanakService;
import com.sais.util.MessageUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component("tutanakController")
@ViewScoped
@Getter
@Setter
@Slf4j
public class TutanakController implements Serializable {

    private final TutanakService tutanakService;
    
    private MuracaatController muracaatController;

    private Long muracaatId;
    private TutanakBilgisi tutanakBilgisi;
    private Personel selectedTahkikatPersoneli;
    private List<Personel> tahkikatYetkililer;
    private List<Personel> tahkikatPersonelleri;

    public TutanakController(TutanakService tutanakService) {
        this.tutanakService = tutanakService;
    }

    @PostConstruct
    public void init() {
        loadTahkikatYetkililer();
    }

    private void loadTahkikatYetkililer() {
        tahkikatYetkililer = tutanakService.getTahkikatYetkililer();
        tahkikatPersonelleri = tahkikatYetkililer;
    }
    
    public java.time.LocalDate getBugun() {
        return java.time.LocalDate.now();
    }

    public void loadTutanak(Long muracaatId) {
        try {
            this.muracaatId = muracaatId;

            if (tutanakService.existsByMuracaatId(muracaatId)) {
                tutanakBilgisi = tutanakService.findByMuracaatId(muracaatId);
                
                if (tutanakBilgisi.getTahkikatPersonel() != null) {
                    selectedTahkikatPersoneli = tutanakBilgisi.getTahkikatPersonel();
                }
            } else {
                tutanakBilgisi = new TutanakBilgisi();
                Muracaat muracaat = new Muracaat();
                muracaat.setId(muracaatId);
                tutanakBilgisi.setMuracaat(muracaat);
                selectedTahkikatPersoneli = null;
            }

        } catch (Exception e) {
            log.error("Tutanak yükleme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Tutanak bilgileri yüklenemedi");
        }
    }

    
    public void ataTahkikatPersoneli() {
        try {
            log.info("Tahkikat personeli atanıyor");

           
            tutanakBilgisi = tutanakService.ataTahkikatPersoneli(
                    muracaatId, 
                    tutanakBilgisi.getTahkikatPersonel().getId()
            );

            MessageUtil.showInfoMessage("Başarılı", "Tahkikat personeli atandı");

        } catch (BusinessException e) {
            log.error("İş kuralı hatası: {}", e.getMessage());
            MessageUtil.showErrorMessage("Hata", e.getMessage());
        } catch (Exception e) {
            log.error("Personel atama hatası", e);
            MessageUtil.showErrorMessage("Hata", "Beklenmeyen bir hata oluştu");
        }
    }

   
    public void handleTahkikatGorselUpload(FileUploadEvent event) {
        try {
            UploadedFile uploadedFile = event.getFile();
            log.info("Tahkikat görseli yükleniyor: {} ({} bytes)", 
                    uploadedFile.getFileName(), uploadedFile.getSize());

           
            MultipartFile multipartFile = new PrimeFacesMultipartFile(uploadedFile);
            
            
            tutanakService.uploadTahkikatGorsel(muracaatId, multipartFile);

           
            MessageUtil.showInfoMessage("Başarılı", uploadedFile.getFileName() + " yüklendi");
            
            log.info("Tahkikat görseli kaydedildi: {}", uploadedFile.getFileName());

        } catch (BusinessException e) {
            log.error("İş kuralı hatası: {}", e.getMessage());
            MessageUtil.showErrorMessage("Hata", e.getMessage());
        } catch (org.springframework.transaction.UnexpectedRollbackException e) {
           
            log.debug("Transaction rollback (race condition): {} - Bu normaldir, dosya yüklendi", 
                     event.getFile().getFileName());
            
        } catch (Exception e) {
            log.error("Dosya yükleme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Dosya yüklenirken hata oluştu");
        }
    }

   
    public void deleteTahkikatGorsel(Long gorselId) {
        try {
            log.info("Tahkikat görseli siliniyor - ID: {}", gorselId);

            tutanakService.deleteTahkikatGorsel(gorselId);

            MessageUtil.showInfoMessage("Başarılı", "Görsel silindi");

        } catch (BusinessException e) {
            log.error("İş kuralı hatası: {}", e.getMessage());
            MessageUtil.showErrorMessage("Hata", e.getMessage());
        } catch (Exception e) {
            log.error("Görsel silme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Görsel silinemedi");
        }
    }

   
    public List<com.sais.entity.TutanakGorsel> getYuklenenGorseller() {
        try {
            return tutanakService.getTahkikatGorselleri(muracaatId);
        } catch (Exception e) {
            log.error("Görseller listelenirken hata", e);
            return new ArrayList<>();
        }
    }

   
    private static class PrimeFacesMultipartFile implements MultipartFile {
        private final UploadedFile uploadedFile;

        public PrimeFacesMultipartFile(UploadedFile uploadedFile) {
            this.uploadedFile = uploadedFile;
        }

        @Override
        public String getName() {
            return uploadedFile.getFileName();
        }

        @Override
        public String getOriginalFilename() {
            return uploadedFile.getFileName();
        }

        @Override
        public String getContentType() {
            return uploadedFile.getContentType();
        }

        @Override
        public boolean isEmpty() {
            return uploadedFile == null || uploadedFile.getSize() == 0;
        }

        @Override
        public long getSize() {
            return uploadedFile.getSize();
        }

        @Override
        public byte[] getBytes() throws IOException {
            return uploadedFile.getContent();
        }

        @Override
        public java.io.InputStream getInputStream() throws IOException {
            return uploadedFile.getInputStream();
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
            java.nio.file.Files.copy(getInputStream(), dest.toPath(), 
                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }

    
    public void saveTutanak() {
        try {
            
            if (selectedTahkikatPersoneli != null) {
                tutanakBilgisi.setTahkikatPersonel(selectedTahkikatPersoneli);
            }
            
           
            if (tutanakBilgisi.getTahkikatTarihi() == null) {
                tutanakBilgisi.setTahkikatTarihi(java.time.LocalDate.now());
            }

            
            tutanakBilgisi = tutanakService.createOrUpdate(tutanakBilgisi);
            
           
            loadTutanak(muracaatId);
            
            log.info("✅ Tutanak kaydedildi - ID: {}, Personel: {}, Metin uzunluk: {}", 
                    tutanakBilgisi != null ? tutanakBilgisi.getId() : "null",
                    tutanakBilgisi != null && tutanakBilgisi.getTahkikatPersonel() != null ? "var" : "yok",
                    tutanakBilgisi != null && tutanakBilgisi.getTahkikatMetni() != null ? tutanakBilgisi.getTahkikatMetni().length() : 0);
            
            MessageUtil.showInfoMessage("Başarılı", "Tahkikat bilgileri kaydedildi");

        } catch (BusinessException e) {
            log.error("İş kuralı hatası: {}", e.getMessage());
            MessageUtil.showErrorMessage("Hata", e.getMessage());
        } catch (Exception e) {
            log.error("Tutanak kaydetme hatası", e);
            MessageUtil.showErrorMessage("Hata", "Beklenmeyen bir hata oluştu: " + e.getMessage());
        }
    }

   
    public void completeTutanak() {
        try {
            log.info("Tutanak tamamlanıyor");

            tutanakBilgisi = tutanakService.completeTutanak(muracaatId);
            MessageUtil.showInfoMessage("Başarılı", "Tutanak tamamlandı");

        } catch (Exception e) {
            log.error("Tutanak tamamlama hatası", e);
            MessageUtil.showErrorMessage("Hata", "Tutanak tamamlanamadı: " + e.getMessage());
        }
    }

   
    public boolean isTutanakTamamlandi() {
        if (tutanakBilgisi == null || tutanakBilgisi.getId() == null) {
            log.debug("Tutanak tamamlanmadı: Tutanak bulunamadı");
            return false;
        }
        
        if (tutanakBilgisi.getTahkikatPersonel() == null) {
            log.debug("Tutanak tamamlanmadı: Personel seçilmemiş");
            return false;
        }
        
        if (tutanakBilgisi.getTahkikatMetni() == null || 
            tutanakBilgisi.getTahkikatMetni().trim().isEmpty()) {
            log.debug("Tutanak tamamlanmadı: Tahkikat metni girilmemiş");
            return false;
        }
        
        if (tutanakBilgisi.getTahkikatTarihi() == null) {
            log.debug("Tutanak tamamlanmadı: Tahkikat tarihi girilmemiş");
            return false;
        }
        
        log.debug("✅ Tutanak tamamlandı - Komisyona gönderilebilir");
        return true;
    }
    
   
    public boolean getTutanakTamamlandi() {
        return isTutanakTamamlandi();
    }
}

