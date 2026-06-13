package com.sais.service;

import com.sais.dto.MuracaatDTO;
import com.sais.entity.*;
import com.sais.enums.MuracaatDurum;
import com.sais.exception.BusinessException;
import com.sais.exception.ResourceNotFoundException;
import com.sais.mapper.MuracaatMapper;
import com.sais.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MuracaatService {

    private final MuracaatRepository muracaatRepository;
    private final AileFertRepository aileFertRepository;
    private final AileMaddiDurumRepository aileMaddiDurumRepository;
    private final TutanakBilgisiRepository tutanakBilgisiRepository;
    private final YardimKararRepository yardimKararRepository;
    private final MuracaatYardimTalepRepository muracaatYardimTalepRepository;
    private final MuracaatDokumanRepository muracaatDokumanRepository;
    private final MuracaatMapper muracaatMapper;

    public Long getSonMuracaatNo() {
        return muracaatRepository.findMaxMuracaatNo().orElse(0L);
    }

    public Muracaat create(Muracaat muracaat) {
        validateMuracaat(muracaat);

        if (muracaat.getBasvuruSahibi() != null && 
            muracaatRepository.existsSonuclanmamisMuracaat(muracaat.getBasvuruSahibi().getId())) {
            throw new BusinessException("Başvuru sahibinin sonuçlanmamış bir müracaatı bulunmaktadır. " +
                "Önce mevcut müracaatı sonuçlandırınız.");
        }

        if (muracaat.getMuracaatNo() == null) {
            Long sonMuracaatNo = getSonMuracaatNo();
            muracaat.setMuracaatNo(sonMuracaatNo + 1);
        }

        if (muracaat.getMuracaatTarihi() == null) {
            muracaat.setMuracaatTarihi(LocalDate.now());
        }

        if (muracaat.getDurum() == null) {
            muracaat.setDurum(MuracaatDurum.BEKLEMEDE);
        }

        return muracaatRepository.save(muracaat);
    }

    public Muracaat update(Muracaat muracaat) {
        if (muracaat.getId() == null) {
            throw new BusinessException("Güncellenecek müracaat ID'si boş olamaz");
        }

        Muracaat existing = findByIdWithBasvuruSahibi(muracaat.getId());

        if (existing.getDurum() == MuracaatDurum.SONUCLANDI) {
            throw new BusinessException("Sonuçlanmış müracaat güncellenemez");
        }
        
        log.info("Müracaat güncelleniyor - ID: {}, Müracaat No: {}", muracaat.getId(), muracaat.getMuracaatNo());
        return muracaatRepository.save(muracaat);
    }

    @Transactional(readOnly = true)
    public Muracaat findById(Long id) {
        return muracaatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Müracaat", "id", id));
    }

    @Transactional(readOnly = true)
    public Muracaat findByIdWithBasvuruSahibi(Long id) {
        return muracaatRepository.findByIdWithBasvuruSahibi(id)
                .orElseThrow(() -> new ResourceNotFoundException("Müracaat", "id", id));
    }

    @Transactional(readOnly = true)
    public Muracaat findByMuracaatNo(Long muracaatNo) {
        return muracaatRepository.findByMuracaatNo(muracaatNo)
                .orElseThrow(() -> new ResourceNotFoundException("Müracaat", "muracaatNo", muracaatNo));
    }

    @Transactional(readOnly = true)
    public List<Muracaat> findAll() {
        return muracaatRepository.findAllWithBasvuruSahibi();
    }

    @Transactional(readOnly = true)
    public List<Muracaat> findByDurum(MuracaatDurum durum) {
        return muracaatRepository.findByDurum(durum);
    }

    @Transactional(readOnly = true)
    public List<Muracaat> findByBasvuruSahibi(Long kisiId) {
        return muracaatRepository.findByBasvuruSahibiId(kisiId);
    }

    @Transactional(readOnly = true)
    public List<Muracaat> findSon3SonuclananMuracaat(Long kisiId) {
        List<Muracaat> muracaatlar = muracaatRepository.findSonuclananMuracaatlarByKisi(kisiId);
        int limit = com.sais.constants.MuracaatConstants.MAX_ONCEKI_MURACAAT_COUNT;
        return muracaatlar.size() > limit ? muracaatlar.subList(0, limit) : muracaatlar;
    }

    public void updateDurum(Long muracaatId, MuracaatDurum yeniDurum) {
        Muracaat muracaat = findById(muracaatId);
        validateDurumGecisi(muracaat.getDurum(), yeniDurum, muracaat);

        muracaat.setDurum(yeniDurum);

        if (yeniDurum == MuracaatDurum.SONUCLANDI) {
            muracaat.setSonuclenmaTarihi(LocalDate.now());
        }

        muracaatRepository.save(muracaat);
    }

    private void validateMuracaat(Muracaat muracaat) {
        if (muracaat.getBasvuruSahibi() == null) {
            throw new BusinessException("Başvuru sahibi bilgisi girilmelidir!");
        }

        if (!muracaat.getBasvuruSahibi().getGebzeIkameti()) {
            throw new BusinessException("Gebze dışından müracaat kabul edilmez! " +
                "Başvuru sahibi Gebze'de ikamet etmelidir.");
        }

        if (muracaat.getBasvuruMetnu() == null || muracaat.getBasvuruMetnu().trim().isEmpty()) {
            throw new BusinessException("Başvuru metni girilmelidir!");
        }
        if (muracaat.getBasvuruMetnu().trim().length() < com.sais.constants.MuracaatConstants.MIN_BASVURU_TEXT_LENGTH) {
            throw new BusinessException("Başvuru metni en az " + com.sais.constants.MuracaatConstants.MIN_BASVURU_TEXT_LENGTH + " karakter olmalıdır!");
        }

        if (muracaat.getKomisyonKararli() == null) {
            throw new BusinessException("'Komisyon Kararlı mı?' alanı seçilmelidir!");
        }

        if (muracaat.getKendisiBasvurdu() == null) {
            throw new BusinessException("'Kendisi Başvurdu mu?' alanı seçilmelidir!");
        }

        if (Boolean.FALSE.equals(muracaat.getKendisiBasvurdu()) && 
            muracaat.getAdinaBasvurulanKisi() == null) {
            throw new BusinessException("Başvuruyu kimin adına yaptığınızı belirtmelisiniz!");
        }
    }

    private void validateDurumGecisi(MuracaatDurum eskiDurum, MuracaatDurum yeniDurum, Muracaat muracaat) {
        if (eskiDurum == MuracaatDurum.SONUCLANDI) {
            throw new BusinessException("Sonuçlanmış müracaat tekrar açılamaz");
        }

        if (yeniDurum == MuracaatDurum.TAHKIKATA_SEVK) {
            long aileFertSayisi = aileFertRepository.countByMuracaatId(muracaat.getId());
            if (aileFertSayisi == 0) {
                throw new BusinessException("Tahkikata sevk için aile fert bilgileri girilmelidir");
            }
        }

        if (yeniDurum == MuracaatDurum.DEGERLENDIRME_KOMISYONU) {
            if (!tutanakBilgisiRepository.existsByMuracaatId(muracaat.getId())) {
                throw new BusinessException("Değerlendirme komisyonuna göndermek için tutanak bilgisi girilmelidir");
            }
            if (!aileMaddiDurumRepository.existsByMuracaatId(muracaat.getId())) {
                throw new BusinessException("Değerlendirme komisyonuna göndermek için aile maddi durum bilgisi girilmelidir");
            }
        }
    }

    public void tahkikataSevkEt(Long muracaatId) {
        updateDurum(muracaatId, MuracaatDurum.TAHKIKATA_SEVK);
    }

    public void komisyonaGonder(Long muracaatId) {
        updateDurum(muracaatId, MuracaatDurum.DEGERLENDIRME_KOMISYONU);
    }

    public void kesinlestir(Long muracaatId) {
        Muracaat muracaat = findById(muracaatId);

        if (!muracaat.getKomisyonKararli()) {
            throw new BusinessException("Bu müracaat komisyon kararlı değil");
        }

        List<YardimKarar> kararlar = yardimKararRepository.findByMuracaatId(muracaatId);
        if (kararlar.isEmpty()) {
            throw new BusinessException("Kesinleştirmek için en az bir yardım kararı girilmelidir");
        }

        Long sonKararNo = muracaatRepository.findMaxKararNo().orElse(0L);
        muracaat.setKararNo(sonKararNo + 1);
        muracaat.setKararTarihi(LocalDate.now());

        for (YardimKarar karar : kararlar) {
            karar.setKesinlesti(true);
            karar.setKesinlesmeTarihi(LocalDate.now());
            yardimKararRepository.save(karar);
        }

        updateDurum(muracaatId, MuracaatDurum.SONUCLANDI);
    }

    public void komisyonsuzMuracaatiTamamla(Long muracaatId) {
        Muracaat muracaat = findById(muracaatId);

        if (muracaat.getKomisyonKararli()) {
            throw new BusinessException("Bu müracaat komisyon kararlı, kesinleştir butonunu kullanınız");
        }

        List<YardimKarar> kararlar = yardimKararRepository.findByMuracaatId(muracaatId);
        if (kararlar.isEmpty()) {
            throw new BusinessException("Tamamlamak için en az bir yardım kararı girilmelidir");
        }

        muracaat.setKararNo(null);
        muracaat.setKararTarihi(null);

        for (YardimKarar karar : kararlar) {
            karar.setKesinlesti(true);
            karar.setKesinlesmeTarihi(LocalDate.now());
            yardimKararRepository.save(karar);
        }

        updateDurum(muracaatId, MuracaatDurum.SONUCLANDI);
    }

    public void delete(Long id) {
        Muracaat muracaat = findById(id);

        if (muracaat.getDurum() == MuracaatDurum.SONUCLANDI) {
            throw new BusinessException("Sonuçlanmış müracaat silinemez");
        }

        muracaat.setAktif(false);
        muracaatRepository.save(muracaat);
    }

    @Transactional(readOnly = true)
    public boolean isMuracaatEksiksiz(Long muracaatId) {
        Muracaat muracaat = findById(muracaatId);

        if (muracaat.getBasvuruMetnu() == null || muracaat.getBasvuruMetnu().isEmpty()) {
            return false;
        }

        long aileFertSayisi = aileFertRepository.countByMuracaatId(muracaatId);
        if (aileFertSayisi == 0) {
            return false;
        }

        if (muracaat.getKomisyonKararli()) {
            if (!tutanakBilgisiRepository.existsByMuracaatId(muracaatId)) {
                return false;
            }
            if (!aileMaddiDurumRepository.existsByMuracaatId(muracaatId)) {
                return false;
            }
        }

        return true;
    }

    @Transactional(readOnly = true)
    public List<MuracaatDTO> findAllDTO() {
        List<Muracaat> entities = findAll();
        return muracaatMapper.toDtoList(entities);
    }

    @Transactional(readOnly = true)
    public MuracaatDTO findDTOById(Long id) {
        Muracaat entity = findById(id);
        return muracaatMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<MuracaatDTO> findByDurumDTO(MuracaatDurum durum) {
        List<Muracaat> entities = findByDurum(durum);
        return muracaatMapper.toDtoList(entities);
    }

    
    @Transactional(readOnly = true)
    public List<Muracaat> findYarimKalanMuracaatlar() {
        return muracaatRepository.findYarimKalanMuracaatlar();
    }

    
    @Transactional(readOnly = true)
    public List<Muracaat> findTamamlananMuracaatlar() {
        return muracaatRepository.findTamamlananMuracaatlar();
    }

    
    @Transactional(readOnly = true)
    public List<Muracaat> findTamamlananVeBekleyenMuracaatlar() {
        return muracaatRepository.findTamamlananVeBekleyenMuracaatlar();
    }

    
    public void yarimKalanMuracaatiIptalEt(Long muracaatId) {
        Muracaat muracaat = findById(muracaatId);
        
        if (muracaat.getDurum() != MuracaatDurum.BEKLEMEDE) {
            throw new BusinessException("Sadece beklemede olan müracaatlar iptal edilebilir");
        }
        
 
        muracaatYardimTalepRepository.deleteByMuracaatId(muracaatId);
        
       
        yardimKararRepository.deleteByMuracaatId(muracaatId);
        
        
        muracaatDokumanRepository.deleteByMuracaatId(muracaatId);
        
        
        aileFertRepository.deleteByMuracaatId(muracaatId);
        
        
        aileMaddiDurumRepository.deleteByMuracaatId(muracaatId);
        
        
        tutanakBilgisiRepository.deleteByMuracaatId(muracaatId);
        
        
        muracaatRepository.delete(muracaat);
        
        log.info("Müracaat tamamen silindi - ID: {}, Müracaat No: {}", muracaatId, muracaat.getMuracaatNo());
    }

    
    @Transactional(readOnly = true)
    public Muracaat findByIdWithAllDetails(Long id) {
        return muracaatRepository.findByIdWithAllDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Müracaat", "id", id));
    }
}

