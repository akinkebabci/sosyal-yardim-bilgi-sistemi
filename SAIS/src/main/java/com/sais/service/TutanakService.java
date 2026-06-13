package com.sais.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sais.dto.TutanakBilgisiDTO;
import com.sais.entity.Muracaat;
import com.sais.entity.Personel;
import com.sais.entity.TutanakBilgisi;
import com.sais.enums.MuracaatDurum;
import com.sais.exception.BusinessException;
import com.sais.exception.ResourceNotFoundException;
import com.sais.mapper.TutanakBilgisiMapper;
import com.sais.repository.MuracaatRepository;
import com.sais.repository.PersonelRepository;
import com.sais.repository.TutanakBilgisiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class TutanakService {

    private final TutanakBilgisiRepository tutanakBilgisiRepository;
    private final com.sais.repository.TutanakGorselRepository tutanakGorselRepository;
    private final MuracaatRepository muracaatRepository;
    private final PersonelRepository personelRepository;
    private final TutanakBilgisiMapper tutanakBilgisiMapper;
    private final FileStorageService fileStorageService;
    
   
    @org.springframework.beans.factory.annotation.Autowired
    @org.springframework.context.annotation.Lazy
    private TutanakService self;
    
    public TutanakService(TutanakBilgisiRepository tutanakBilgisiRepository,
                         com.sais.repository.TutanakGorselRepository tutanakGorselRepository,
                         MuracaatRepository muracaatRepository,
                         PersonelRepository personelRepository,
                         TutanakBilgisiMapper tutanakBilgisiMapper,
                         FileStorageService fileStorageService) {
        this.tutanakBilgisiRepository = tutanakBilgisiRepository;
        this.tutanakGorselRepository = tutanakGorselRepository;
        this.muracaatRepository = muracaatRepository;
        this.personelRepository = personelRepository;
        this.tutanakBilgisiMapper = tutanakBilgisiMapper;
        this.fileStorageService = fileStorageService;
    }

    public TutanakBilgisi createOrUpdate(TutanakBilgisi tutanakBilgisi) {
        validateTutanak(tutanakBilgisi);

        Muracaat muracaat = muracaatRepository.findById(tutanakBilgisi.getMuracaat().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Müracaat", "id", tutanakBilgisi.getMuracaat().getId()));

        if (muracaat.getDurum() != MuracaatDurum.TAHKIKATA_SEVK) {
            throw new BusinessException(
                    "Tutanak için müracaat durumu 'Tahkikata Sevk' olmalıdır");
        }

        if (tutanakBilgisiRepository.existsByMuracaatId(muracaat.getId())) {
            TutanakBilgisi existing = findByMuracaatId(muracaat.getId());
            tutanakBilgisi.setId(existing.getId());
        }

        if (tutanakBilgisi.getTahkikatTarihi() == null) {
            tutanakBilgisi.setTahkikatTarihi(LocalDate.now());
        }

        return tutanakBilgisiRepository.save(tutanakBilgisi);
    }

    @Transactional(readOnly = true)
    public TutanakBilgisi findByMuracaatId(Long muracaatId) {
        return tutanakBilgisiRepository.findByMuracaatId(muracaatId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tutanak Bilgisi", "muracaatId", muracaatId));
    }

 
    @Transactional(readOnly = true)
    public boolean existsByMuracaatId(Long muracaatId) {
        return tutanakBilgisiRepository.existsByMuracaatId(muracaatId);
    }

   
    public TutanakBilgisi ataTahkikatPersoneli(Long muracaatId, Long personelId) {
        log.info("Tahkikat personeli atanıyor: Müracaat {} - Personel {}", 
                muracaatId, personelId);

        
        if (personelId == null) {
            throw new BusinessException("Personel seçilmelidir");
        }

        self.ensureTutanakExists(muracaatId);
        TutanakBilgisi tutanak = findByMuracaatId(muracaatId);

        Personel personel = personelRepository.findById(personelId)
                .orElseThrow(() -> new ResourceNotFoundException("Personel", "id", personelId));

        if (!personel.getTahkikatYetkili()) {
            throw new BusinessException("Seçilen personel tahkikat yetkisine sahip değil");
        }

        tutanak.setTahkikatPersonel(personel);

        return tutanakBilgisiRepository.save(tutanak);
    }

  
    private void validateTutanak(TutanakBilgisi tutanakBilgisi) {
        if (tutanakBilgisi.getTahkikatPersonel() == null) {
            throw new BusinessException("Tahkikat personeli seçilmelidir");
        }
        
        if (tutanakBilgisi.getTahkikatMetni() == null || tutanakBilgisi.getTahkikatMetni().trim().isEmpty()) {
            throw new BusinessException("Tahkikat metni girilmelidir");
        }
        
        if (tutanakBilgisi.getTahkikatTarihi() == null) {
            throw new BusinessException("Tahkikat tarihi girilmelidir");
        }
    }

   
    @Deprecated
    public TutanakBilgisi addEvGorselleri(Long muracaatId, String gorselPaths) {
        log.warn("DEPRECATED: addEvGorselleri kullanıldı, uploadTahkikatGorsel kullanın");

        self.ensureTutanakExists(muracaatId);
        TutanakBilgisi tutanak = findByMuracaatId(muracaatId);
        tutanak.setEvGorselleri(gorselPaths);

        return tutanakBilgisiRepository.save(tutanak);
    }

    
    public com.sais.entity.TutanakGorsel uploadTahkikatGorsel(Long muracaatId, MultipartFile file) {
        
        return self.uploadTahkikatGorselInternal(muracaatId, file);
    }
    
  
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public com.sais.entity.TutanakGorsel uploadTahkikatGorselInternal(Long muracaatId, MultipartFile file) {
        log.info("Tahkikat görseli yükleniyor: Müracaat ID {} - Dosya: {}", 
                 muracaatId, file.getOriginalFilename());

        
        if (file == null || file.isEmpty()) {
            throw new BusinessException("Resim dosyası seçilmelidir");
        }

       
        self.ensureTutanakExists(muracaatId);
       
        TutanakBilgisi tutanak = tutanakBilgisiRepository.findByMuracaatId(muracaatId)
                .orElseThrow(() -> new ResourceNotFoundException("Tutanak Bilgisi", "muracaatId", muracaatId));
        
        Muracaat muracaat = tutanak.getMuracaat();

       
        long mevcutGorselSayisi = tutanakGorselRepository.countByTutanakBilgisiId(tutanak.getId());
        if (mevcutGorselSayisi >= 10) {
            throw new BusinessException("Maksimum 10 adet resim yüklenebilir");
        }

        String fileName = fileStorageService.storeTahkikatGorsel(
            file,
            muracaatId,
            muracaat.getBasvuruSahibi().getAd(),
            muracaat.getBasvuruSahibi().getSoyad()
        );

       
        com.sais.entity.TutanakGorsel gorsel = com.sais.entity.TutanakGorsel.builder()
                .tutanakBilgisi(tutanak)
                .dosyaAdi(fileName)
                .dosyaYolu("tahkikat/" + fileName)
                .dosyaBoyutu(file.getSize())
                .contentType(file.getContentType())
                .siraNo((int) mevcutGorselSayisi + 1)
                .build();

        com.sais.entity.TutanakGorsel savedGorsel = tutanakGorselRepository.save(gorsel);
        log.info("✅ Tahkikat görseli kaydedildi - ID: {}, Dosya: {}", savedGorsel.getId(), fileName);

        return savedGorsel;
    }
    
  
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void ensureTutanakExists(Long muracaatId) {
       
        if (existsByMuracaatId(muracaatId)) {
            log.debug("Tutanak zaten var: Müracaat ID {}", muracaatId);
            return;
        }

        Muracaat muracaat = muracaatRepository.findById(muracaatId)
                .orElseThrow(() -> new ResourceNotFoundException("Müracaat", "id", muracaatId));

        if (muracaat.getDurum() != MuracaatDurum.TAHKIKATA_SEVK) {
            throw new BusinessException(
                    "Tutanak için müracaat durumu 'Tahkikata Sevk' olmalıdır");
        }

        try {
            TutanakBilgisi tutanak = TutanakBilgisi.builder()
                    .muracaat(muracaat)
                    .tahkikatTarihi(LocalDate.now())
                    .tamamlandi(false)
                    .build();

            tutanakBilgisiRepository.save(tutanak);
            tutanakBilgisiRepository.flush();
            log.info("✅ Yeni tutanak kaydı oluşturuldu: Müracaat ID {}", muracaatId);
            
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            
            log.debug("ℹ️ Tutanak başka thread tarafından oluşturuldu: Müracaat ID {}", muracaatId);
            
        } catch (Exception e) {
            log.error("❌ Beklenmeyen hata - ensureTutanakExists: {}", e.getMessage());
            
        }
    }

    
    public void deleteTahkikatGorsel(Long gorselId) {
        log.info("Tahkikat görseli siliniyor - ID: {}", gorselId);

        com.sais.entity.TutanakGorsel gorsel = tutanakGorselRepository.findById(gorselId)
                .orElseThrow(() -> new ResourceNotFoundException("Tahkikat Görseli", "id", gorselId));

        TutanakBilgisi tutanak = gorsel.getTutanakBilgisi();
        Muracaat muracaat = tutanak.getMuracaat();

       
        try {
            fileStorageService.deleteFile(
                fileStorageService.getTahkikatGorselPath(
                    muracaat.getId(),
                    muracaat.getBasvuruSahibi().getAd(),
                    muracaat.getBasvuruSahibi().getSoyad(),
                    gorsel.getDosyaAdi()
                )
            );
        } catch (Exception e) {
            log.warn("Fiziksel dosya silinemedi (devam ediliyor): {}", gorsel.getDosyaAdi(), e);
        }

       
        tutanakGorselRepository.delete(gorsel);
        log.info("Tahkikat görseli silindi - Dosya: {}", gorsel.getDosyaAdi());
    }

  
    public List<com.sais.entity.TutanakGorsel> getTahkikatGorselleri(Long muracaatId) {
        if (!existsByMuracaatId(muracaatId)) {
            return new ArrayList<>();
        }
        
        TutanakBilgisi tutanak = findByMuracaatId(muracaatId);
        return tutanakGorselRepository.findByTutanakBilgisiId(tutanak.getId());
    }

    
    public TutanakBilgisi updateTahkikatMetni(Long muracaatId, String tahkikatMetni) {
        log.info("Tahkikat metni güncelleniyor: Müracaat ID {}", muracaatId);

        self.ensureTutanakExists(muracaatId);
        TutanakBilgisi tutanak = findByMuracaatId(muracaatId);
        tutanak.setTahkikatMetni(tahkikatMetni);

        return tutanakBilgisiRepository.save(tutanak);
    }

   
    public TutanakBilgisi completeTutanak(Long muracaatId) {
        log.info("Tutanak tamamlanıyor: Müracaat ID {}", muracaatId);

        TutanakBilgisi tutanak = findByMuracaatId(muracaatId);

      
        if (tutanak.getTahkikatPersonel() == null) {
            throw new BusinessException("Tahkikat personeli seçilmelidir");
        }

        if (tutanak.getTahkikatMetni() == null || tutanak.getTahkikatMetni().isEmpty()) {
            throw new BusinessException("Tahkikat metni girilmelidir");
        }

        tutanak.setTamamlandi(true);

        return tutanakBilgisiRepository.save(tutanak);
    }


   
    @Transactional(readOnly = true)
    public java.util.List<Personel> getTahkikatYetkililer() {
        return personelRepository.findTahkikatYetkililer();
    }

    @Transactional(readOnly = true)
    public TutanakBilgisiDTO findByMuracaatIdDTO(Long muracaatId) {
        TutanakBilgisi entity = findByMuracaatId(muracaatId);
        return tutanakBilgisiMapper.toDto(entity);
    }
}


