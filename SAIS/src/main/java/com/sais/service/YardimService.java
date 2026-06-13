package com.sais.service;

import com.sais.dto.YardimKararDTO;
import com.sais.entity.YardimAltTipi;
import com.sais.entity.YardimKarar;
import com.sais.enums.YardimTipi;
import com.sais.exception.BusinessException;
import com.sais.exception.ResourceNotFoundException;
import com.sais.mapper.YardimKararMapper;
import com.sais.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class YardimService {

    private final YardimAltTipiRepository yardimAltTipiRepository;
    private final YardimKararRepository yardimKararRepository;
    private final com.sais.repository.HesapBilgisiRepository hesapBilgisiRepository;
    private final com.sais.repository.MuracaatRepository muracaatRepository;
    private final YardimKararMapper yardimKararMapper;
    
    @Transactional(readOnly = true)
    public List<YardimAltTipi> findAllYardimAltTipleri() {
        return yardimAltTipiRepository.findAllActive();
    }

    @Transactional(readOnly = true)
    public List<YardimAltTipi> findKomisyonKararliYardimlar() {
        return yardimAltTipiRepository.findKomisyonKararliYardimlar();
    }

    @Transactional(readOnly = true)
    public List<YardimAltTipi> findKomisyonsuzYardimlar() {
        return yardimAltTipiRepository.findKomisyonsuzYardimlar();
    }

    @Transactional(readOnly = true)
    public List<YardimAltTipi> findByYardimTipi(YardimTipi yardimTipi) {
        return yardimAltTipiRepository.findByYardimTipiOrderBySiraNo(yardimTipi);
    }

    @Transactional(readOnly = true)
    public YardimAltTipi findYardimAltTipiById(Long id) {
        return yardimAltTipiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Yardım Alt Tipi", "id", id));
    }

    public YardimKarar createYardimKarar(YardimKarar yardimKarar) {
        validateYardimKarar(yardimKarar);

        if (yardimKarar.getKomisyonKararli() && yardimKarar.getToplantiTarihi() != null) {
            validateToplantiTarihi(yardimKarar.getToplantiTarihi());
        }

        return yardimKararRepository.save(yardimKarar);
    }

    private void validateYardimKarar(YardimKarar yardimKarar) {
        if (yardimKarar.getYardimAltTipi() == null) {
            throw new BusinessException("Yardım türü seçilmelidir");
        }

        if (yardimKarar.getKomisyonKararli() && yardimKarar.getToplantiTarihi() == null) {
            throw new BusinessException("Toplantı tarihi girilmelidir");
        }

        if (yardimKarar.getYardimAltTipi().getYardimTipi() == YardimTipi.NAKDI) {
            validateNakdiYardim(yardimKarar);
        }

        if (yardimKarar.getYardimAltTipi().getYardimTipi() == YardimTipi.AYNI) {
            validateAyniYardim(yardimKarar);
        }

        if (yardimKarar.getYardimDurum() == com.sais.enums.YardimDurum.RED_EDILDI) {
            validateRedDurumu(yardimKarar);
        }
    }

    private void validateNakdiYardim(YardimKarar yardimKarar) {
        if (yardimKarar.getYardimDurum() == com.sais.enums.YardimDurum.KABUL_EDILDI) {
            if (yardimKarar.getVerilenTutar() == null || yardimKarar.getVerilenTutar() <= 0) {
                throw new BusinessException("Nakdi yardım için tutar girilmelidir");
            }
            if (yardimKarar.getHesapBilgisi() == null) {
                throw new BusinessException("IBAN seçilmelidir");
            }
        }
    }

    private void validateAyniYardim(YardimKarar yardimKarar) {
        if (yardimKarar.getYardimDurum() == com.sais.enums.YardimDurum.KABUL_EDILDI) {
            if (yardimKarar.getAdetSayi() == null || yardimKarar.getAdetSayi() <= 0) {
                throw new BusinessException("Ayni yardım için adet/sayı girilmelidir");
            }
        }
    }

    private void validateRedDurumu(YardimKarar yardimKarar) {
        if (yardimKarar.getRedSebebi() == null) {
            throw new BusinessException("Red sebebi seçilmelidir");
        }
    }

    public YardimKarar updateYardimKarar(YardimKarar yardimKarar) {
        YardimKarar existing = findYardimKararById(yardimKarar.getId());

        if (existing.getKesinlesti()) {
            throw new BusinessException("Kesinleşmiş yardım kararı güncellenemez");
        }

        return yardimKararRepository.save(yardimKarar);
    }

    public void deleteYardimKarar(Long id) {
        YardimKarar karar = findYardimKararById(id);

        if (karar.getKesinlesti()) {
            throw new BusinessException("Kesinleşmiş yardım kararı silinemez");
        }

        yardimKararRepository.delete(karar);
    }

    @Transactional(readOnly = true)
    public YardimKarar findYardimKararById(Long id) {
        return yardimKararRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Yardım Kararı", "id", id));
    }

    @Transactional(readOnly = true)
    public List<YardimKarar> findYardimKararlariByMuracaat(Long muracaatId) {
        return yardimKararRepository.findByMuracaatId(muracaatId);
    }

    private void validateToplantiTarihi(LocalDate toplantiTarihi) {
        if (toplantiTarihi.isAfter(LocalDate.now())) {
            throw new BusinessException("Toplantı tarihi bugünden sonra olamaz");
        }

        LocalDate sonToplantiTarihi = yardimKararRepository.findSonToplantiTarihi().orElse(null);
        if (sonToplantiTarihi != null && toplantiTarihi.isBefore(sonToplantiTarihi)) {
            throw new BusinessException("Toplantı tarihi son toplantı tarihinden (" + sonToplantiTarihi + ") önce olamaz");
        }
    }
    
    @Transactional(readOnly = true)
    public Integer getSonKararSayisi() {
        return yardimKararRepository.findMaxKararSayisi().orElse(0);
    }

    @Transactional(readOnly = true)
    public List<com.sais.entity.HesapBilgisi> getHesapBilgileriByMuracaatId(Long muracaatId) {
        var muracaat = muracaatRepository.findById(muracaatId);
        if (muracaat.isEmpty() || muracaat.get().getBasvuruSahibi() == null) {
            return new ArrayList<>();
        }
        
        Long kisiId = muracaat.get().getBasvuruSahibi().getId();
        return hesapBilgisiRepository.findByKisiId(kisiId);
    }

    public void kesinlestirVeSonuclandir(Long muracaatId, List<com.sais.entity.YardimKarar> kararlar) {
        Integer sonKararSayisi = getSonKararSayisi();
        Integer yeniKararSayisi = (sonKararSayisi != null ? sonKararSayisi : 0) + 1;
        java.time.LocalDate kararTarihi = java.time.LocalDate.now();
        
        // Tüm kararları kesinleştir
        for (com.sais.entity.YardimKarar karar : kararlar) {
            karar.setKesinlesti(true);
            karar.setKararSayisi(yeniKararSayisi);
            karar.setKararTarihi(kararTarihi);
            karar.setKesinlesmeTarihi(kararTarihi);
            updateYardimKarar(karar);
        }
        
       
        var muracaat = muracaatRepository.findById(muracaatId);
        if (muracaat.isPresent()) {
            com.sais.entity.Muracaat m = muracaat.get();
            m.setDurum(com.sais.enums.MuracaatDurum.SONUCLANDI);
            m.setKararNo(Long.valueOf(yeniKararSayisi));  
            m.setKararTarihi(kararTarihi);                 
            m.setSonuclenmaTarihi(kararTarihi);           
            muracaatRepository.save(m);
            
            log.info("Müracaat kesinleştirildi - ID: {}, Karar No: {}, Karar Tarihi: {}", 
                     muracaatId, yeniKararSayisi, kararTarihi);
        }
    }

    @Transactional(readOnly = true)
    public List<YardimKararDTO> findYardimKararlariDTOByMuracaat(Long muracaatId) {
        List<YardimKarar> entities = findYardimKararlariByMuracaat(muracaatId);
        return yardimKararMapper.toDtoList(entities);
    }

    @Transactional(readOnly = true)
    public YardimKararDTO findYardimKararDTOById(Long id) {
        YardimKarar entity = findYardimKararById(id);
        return yardimKararMapper.toDto(entity);
    }
}


