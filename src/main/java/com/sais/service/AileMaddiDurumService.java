package com.sais.service;

import com.sais.dto.AileMaddiDurumDTO;
import com.sais.entity.*;
import com.sais.exception.BusinessException;
import com.sais.exception.ResourceNotFoundException;
import com.sais.mapper.AileMaddiDurumMapper;
import com.sais.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AileMaddiDurumService {

    private final AileMaddiDurumRepository aileMaddiDurumRepository;
    private final MuracaatRepository muracaatRepository;
    private final GelirTuruRepository gelirTuruRepository;
    private final BorcTuruRepository borcTuruRepository;
    private final AileMaddiDurumMapper aileMaddiDurumMapper;

    public AileMaddiDurum createOrUpdate(AileMaddiDurum aileMaddiDurum) {
        if (aileMaddiDurumRepository.existsByMuracaatId(aileMaddiDurum.getMuracaat().getId())) {
            AileMaddiDurum existing = findByMuracaatId(aileMaddiDurum.getMuracaat().getId());
            aileMaddiDurum.setId(existing.getId());
        }

        calculateTotals(aileMaddiDurum);

        return aileMaddiDurumRepository.save(aileMaddiDurum);
    }

    @Transactional(readOnly = true)
    public AileMaddiDurum findByMuracaatId(Long muracaatId) {
        AileMaddiDurum maddiDurum = aileMaddiDurumRepository.findByMuracaatId(muracaatId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aile Maddi Durum", "muracaatId", muracaatId));
        
        if (maddiDurum.getGelirBilgileri() != null) {
            maddiDurum.getGelirBilgileri().size();
            maddiDurum.getGelirBilgileri().forEach(gelir -> {
                if (gelir.getKisi() != null) {
                    gelir.getKisi().getAd();
                }
                if (gelir.getGelirTuru() != null) {
                    gelir.getGelirTuru().getAdi();
                }
            });
        }
        if (maddiDurum.getBorcBilgileri() != null) {
            maddiDurum.getBorcBilgileri().size();
            maddiDurum.getBorcBilgileri().forEach(borc -> {
                if (borc.getBorcTuru() != null) {
                    borc.getBorcTuru().getAdi();
                }
            });
        }
        
        return maddiDurum;
    }

   
    @Transactional(readOnly = true)
    public boolean existsByMuracaatId(Long muracaatId) {
        return aileMaddiDurumRepository.existsByMuracaatId(muracaatId);
    }

    
    public AileMaddiDurum addGelirBilgisi(Long muracaatId, GelirBilgisi gelirBilgisi) {
        log.info("Gelir bilgisi ekleniyor: Müracaat ID {}", muracaatId);

        validateGelirBilgisi(gelirBilgisi);

        AileMaddiDurum maddiDurum = getOrCreateMaddiDurum(muracaatId);
        gelirBilgisi.setAileMaddiDurum(maddiDurum);
        maddiDurum.addGelirBilgisi(gelirBilgisi);

        calculateTotals(maddiDurum);

        return aileMaddiDurumRepository.save(maddiDurum);
    }

    
    private void validateGelirBilgisi(GelirBilgisi gelirBilgisi) {
        if (gelirBilgisi.getKisi() == null) {
            throw new BusinessException("Geliri olan aile ferdi seçilmelidir");
        }

        if (gelirBilgisi.getGelirTuru() == null) {
            throw new BusinessException("Gelir türü seçilmelidir");
        }

        if (gelirBilgisi.getGelirTutari() == null || gelirBilgisi.getGelirTutari() <= 0) {
            throw new BusinessException("Gelir tutarı girilmelidir");
        }
    }

    
    public AileMaddiDurum removeGelirBilgisi(Long muracaatId, Long gelirBilgisiId) {
        log.info("Gelir bilgisi siliniyor: {}", gelirBilgisiId);

        AileMaddiDurum maddiDurum = findByMuracaatId(muracaatId);

        GelirBilgisi toRemove = maddiDurum.getGelirBilgileri().stream()
                .filter(g -> g.getId().equals(gelirBilgisiId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Gelir Bilgisi", "id", gelirBilgisiId));

        maddiDurum.removeGelirBilgisi(toRemove);

        calculateTotals(maddiDurum);

        return aileMaddiDurumRepository.save(maddiDurum);
    }

   
    public AileMaddiDurum addBorcBilgisi(Long muracaatId, BorcBilgisi borcBilgisi) {
        log.info("Borç bilgisi ekleniyor: Müracaat ID {}", muracaatId);

       
        validateBorcBilgisi(borcBilgisi);

        AileMaddiDurum maddiDurum = getOrCreateMaddiDurum(muracaatId);
        borcBilgisi.setAileMaddiDurum(maddiDurum);
        maddiDurum.addBorcBilgisi(borcBilgisi);

        calculateTotals(maddiDurum);

        return aileMaddiDurumRepository.save(maddiDurum);
    }

   
    private void validateBorcBilgisi(BorcBilgisi borcBilgisi) {
        if (borcBilgisi.getBorcTuru() == null) {
            throw new BusinessException("Borç türü seçilmelidir");
        }

        if (borcBilgisi.getBorcTutari() == null || borcBilgisi.getBorcTutari() <= 0) {
            throw new BusinessException("Borç tutarı girilmelidir");
        }
    }

   
    public AileMaddiDurum removeBorcBilgisi(Long muracaatId, Long borcBilgisiId) {
        log.info("Borç bilgisi siliniyor: {}", borcBilgisiId);

        AileMaddiDurum maddiDurum = findByMuracaatId(muracaatId);

        BorcBilgisi toRemove = maddiDurum.getBorcBilgileri().stream()
                .filter(b -> b.getId().equals(borcBilgisiId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Borç Bilgisi", "id", borcBilgisiId));

        maddiDurum.removeBorcBilgisi(toRemove);

        calculateTotals(maddiDurum);

        return aileMaddiDurumRepository.save(maddiDurum);
    }

   
    public AileMaddiDurum updateGayrimenkulBilgisi(Long muracaatId, 
                                                   GayrimenkulBilgisi gayrimenkulBilgisi) {
        log.info("Gayrimenkul bilgisi güncelleniyor: Müracaat ID {}", muracaatId);

        AileMaddiDurum maddiDurum = getOrCreateMaddiDurum(muracaatId);
        gayrimenkulBilgisi.setAileMaddiDurum(maddiDurum);
        maddiDurum.setGayrimenkulBilgisi(gayrimenkulBilgisi);

        return aileMaddiDurumRepository.save(maddiDurum);
    }

   
    private void calculateTotals(AileMaddiDurum maddiDurum) {
        // Toplam gelir
        double toplamGelir = maddiDurum.getGelirBilgileri().stream()
                .mapToDouble(GelirBilgisi::getGelirTutari)
                .sum();
        maddiDurum.setToplamGelir(toplamGelir);

        // Toplam borç
        double toplamBorc = maddiDurum.getBorcBilgileri().stream()
                .mapToDouble(BorcBilgisi::getBorcTutari)
                .sum();
        maddiDurum.setToplamBorc(toplamBorc);

        log.info("Hesaplanan toplam - Gelir: {}, Borç: {}", toplamGelir, toplamBorc);
    }

   
    private AileMaddiDurum getOrCreateMaddiDurum(Long muracaatId) {
        if (existsByMuracaatId(muracaatId)) {
            return findByMuracaatId(muracaatId);
        }

        Muracaat muracaat = muracaatRepository.findById(muracaatId)
                .orElseThrow(() -> new ResourceNotFoundException("Müracaat", "id", muracaatId));

        AileMaddiDurum maddiDurum = AileMaddiDurum.builder()
                .muracaat(muracaat)
                .toplamGelir(0.0)
                .toplamBorc(0.0)
                .build();

        return aileMaddiDurumRepository.save(maddiDurum);
    }

    @Transactional(readOnly = true)
    public List<GelirTuru> getAllGelirTurleri() {
        return gelirTuruRepository.findAllActive();
    }

  
    @Transactional(readOnly = true)
    public List<BorcTuru> getAllBorcTurleri() {
        return borcTuruRepository.findAllActive();
    }

    @Transactional(readOnly = true)
    public AileMaddiDurumDTO findByMuracaatIdDTO(Long muracaatId) {
        AileMaddiDurum entity = findByMuracaatId(muracaatId);
        return aileMaddiDurumMapper.toDto(entity);
    }

    public void addGelirWithValidation(Long muracaatId, com.sais.entity.GelirBilgisi gelirBilgisi) {
        if (gelirBilgisi.getKisi() == null) {
            throw new BusinessException("Geliri olan kişi seçilmelidir!");
        }
        if (gelirBilgisi.getGelirTuru() == null) {
            throw new BusinessException("Gelir türü seçilmelidir!");
        }
        if (gelirBilgisi.getGelirTutari() == null || gelirBilgisi.getGelirTutari() <= 0) {
            throw new BusinessException("Gelir tutarı girilmelidir!");
        }
        addGelirBilgisi(muracaatId, gelirBilgisi);
    }

    public void addBorcWithValidation(Long muracaatId, com.sais.entity.BorcBilgisi borcBilgisi) {
        if (borcBilgisi.getBorcTuru() == null) {
            throw new BusinessException("Borç türü seçilmelidir!");
        }
        if (borcBilgisi.getBorcTutari() == null || borcBilgisi.getBorcTutari() <= 0) {
            throw new BusinessException("Borç tutarı girilmelidir!");
        }
        addBorcBilgisi(muracaatId, borcBilgisi);
    }
}



