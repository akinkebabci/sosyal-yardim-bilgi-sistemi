package com.sais.service;

import com.sais.dto.AileFertDTO;
import com.sais.dto.MernisYakinDTO;
import com.sais.entity.*;
import com.sais.exception.BusinessException;
import com.sais.exception.ResourceNotFoundException;
import com.sais.mapper.AileFertMapper;
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
public class AileFertService {

    private final AileFertRepository aileFertRepository;
    private final KisiService kisiService;
    private final MernisService mernisService;
    private final MuracaatRepository muracaatRepository;
    private final AileFertMapper aileFertMapper;

    public AileFert create(AileFert aileFert) {
        validateAileFert(aileFert);

        if (aileFertRepository.existsByMuracaatIdAndKisiId(
                aileFert.getMuracaat().getId(), 
                aileFert.getKisi().getId())) {
            throw new BusinessException("Bu kişi zaten aile ferdi olarak eklenmiş");
        }

        return aileFertRepository.save(aileFert);
    }

    private void validateAileFert(AileFert aileFert) {
        if (aileFert.getKisi() == null) {
            throw new BusinessException("Kişi bilgisi seçilmelidir");
        }

        if (aileFert.getYakinlikKodu() == null) {
            throw new BusinessException("Yakınlık kodu seçilmelidir");
        }

        if (aileFert.getMuracaat() == null) {
            throw new BusinessException("Müracaat bilgisi eksik");
        }
    }

    public AileFert update(AileFert aileFert) {
        if (aileFert.getId() == null) {
            throw new BusinessException("Güncellenecek aile ferdi ID'si boş olamaz");
        }

        findById(aileFert.getId());

        return aileFertRepository.save(aileFert);
    }

    @Transactional(readOnly = true)
    public AileFert findById(Long id) {
        return aileFertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aile Ferdi", "id", id));
    }

   
    @Transactional(readOnly = true)
    public List<AileFert> findByMuracaatId(Long muracaatId) {
        return aileFertRepository.findByMuracaatId(muracaatId);
    }

    
    public void delete(Long id) {
        log.info("Aile ferdi siliniyor: {}", id);
        AileFert aileFert = findById(id);
        aileFertRepository.delete(aileFert);
    }

   
    public List<MernisYakinDTO> getYakinlarFromMernis(String tcKimlikNo) {
        log.info("Yakınlık servisi sorgusu: {}", tcKimlikNo);
        return mernisService.yakinlariSorgula(tcKimlikNo);
    }

    
    public AileFert createFromYakinlik(Long muracaatId, MernisYakinDTO yakin, 
                                       Long yakinlikKoduId) {
        log.info("Yakınlık bilgisi ile aile ferdi oluşturuluyor: {}", yakin.getTcKimlikNo());

        Muracaat muracaat = muracaatRepository.findById(muracaatId)
                .orElseThrow(() -> new ResourceNotFoundException("Müracaat", "id", muracaatId));

      
        Kisi kisi = kisiService.getOrCreateFromMernis(yakin.getTcKimlikNo());

        AileFert aileFert = AileFert.builder()
                .muracaat(muracaat)
                .kisi(kisi)
                .build();

        
        if (yakinlikKoduId != null) {
            YakinlikKodu yakinlikKodu = new YakinlikKodu();
            yakinlikKodu.setId(yakinlikKoduId);
            aileFert.setYakinlikKodu(yakinlikKodu);
        }

        return create(aileFert);
    }

    
    @Transactional(readOnly = true)
    public List<AileFert> getOncekiAileFertleri(Long kisiId) {
        log.info("Önceki aile fertleri getiriliyor: {}", kisiId);
        return aileFertRepository.findByKisiId(kisiId);
    }

    
    @Transactional(readOnly = true)
    public long countByMuracaatId(Long muracaatId) {
        return aileFertRepository.countByMuracaatId(muracaatId);
    }

   
    public AileFert updateEngelBilgisi(Long aileFertId, AileFertEngelBilgisi engelBilgisi) {
        log.info("Engel bilgisi güncelleniyor: {}", aileFertId);

        AileFert aileFert = findById(aileFertId);
        engelBilgisi.setAileFert(aileFert);
        aileFert.setEngelBilgisi(engelBilgisi);

        return aileFertRepository.save(aileFert);
    }

    
    public AileFert updateHastalikBilgisi(Long aileFertId, AileFertHastalikBilgisi hastalikBilgisi) {
        log.info("Hastalık bilgisi güncelleniyor: {}", aileFertId);

        AileFert aileFert = findById(aileFertId);
        hastalikBilgisi.setAileFert(aileFert);
        aileFert.setHastalikBilgisi(hastalikBilgisi);

        return aileFertRepository.save(aileFert);
    }

    @Transactional(readOnly = true)
    public List<AileFertDTO> findByMuracaatIdDTO(Long muracaatId) {
        List<AileFert> entities = findByMuracaatId(muracaatId);
        return aileFertMapper.toDtoList(entities);
    }

    @Transactional(readOnly = true)
    public AileFertDTO findByIdDTO(Long id) {
        AileFert entity = findById(id);
        return aileFertMapper.toDto(entity);
    }
    
    
    @Transactional(readOnly = true)
    public AileFert findByIdWithAllDetails(Long id) {
        AileFert aileFert = aileFertRepository.findByIdWithAllDetails(id);
        if (aileFert == null) {
            throw new ResourceNotFoundException("Aile Ferdi", "id", id);
        }
        return aileFert;
    }
}


