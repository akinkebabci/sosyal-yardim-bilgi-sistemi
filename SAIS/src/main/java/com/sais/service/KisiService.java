package com.sais.service;

import com.sais.dto.MernisKisiDTO;
import com.sais.entity.Kisi;
import com.sais.exception.DuplicateResourceException;
import com.sais.exception.ResourceNotFoundException;
import com.sais.repository.KisiRepository;
import com.sais.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KisiService {

    private final KisiRepository kisiRepository;
    private final MernisService mernisService;

    public Kisi getOrCreateFromMernis(String tcKimlikNo) {
      
        Optional<Kisi> existingKisi = kisiRepository.findByTcKimlikNo(tcKimlikNo);

        if (existingKisi.isPresent()) {
            Kisi kisi = existingKisi.get();
            log.info("Kişi database'de bulundu: {} {} (ID: {})", 
                kisi.getAd(), kisi.getSoyad(), kisi.getId());
            
          
            return kisi;
            
          
        }

        
        log.warn("Kişi database'de bulunamadı, MERNİS simülasyonundan oluşturuluyor: {}", tcKimlikNo);
        return createFromMernis(tcKimlikNo);
    }

    private Kisi createFromMernis(String tcKimlikNo) {

        MernisKisiDTO mernisData = mernisService.sorgula(tcKimlikNo);

        Kisi kisi = Kisi.builder()
                .tcKimlikNo(mernisData.getTcKimlikNo())
                .ad(mernisData.getAd())
                .soyad(mernisData.getSoyad())
                .babaAdi(mernisData.getBabaAdi())
                .anneAdi(mernisData.getAnneAdi())
                .dogumTarihi(mernisData.getDogumTarihi())
                .dogumYeri(mernisData.getDogumYeri())
                .cinsiyet(mernisData.getCinsiyet())
                .medeniDurum(mernisData.getMedeniDurum())
                .adres(mernisData.getAdres())
                .il(mernisData.getIl())
                .ilce(mernisData.getIlce())
                .mahalle(mernisData.getMahalle())
                .telefon(mernisData.getTelefon())
                .gebzeIkameti(ValidationUtil.isGebzeAddress(mernisData.getIl(), mernisData.getIlce()))
                .sonMernisSorguTarihi(LocalDate.now())
                .mernisGuncellemeSayisi(1)
                .build();

        return kisiRepository.save(kisi);
    }

    
    private Kisi updateFromMernis(Kisi kisi) {
        log.info("Kişi MERNİS'ten güncelleniyor: {}", kisi.getTcKimlikNo());

        MernisKisiDTO mernisData = mernisService.sorgula(kisi.getTcKimlikNo());

        kisi.setAd(mernisData.getAd());
        kisi.setSoyad(mernisData.getSoyad());
        kisi.setBabaAdi(mernisData.getBabaAdi());
        kisi.setAnneAdi(mernisData.getAnneAdi());
        kisi.setDogumTarihi(mernisData.getDogumTarihi());
        kisi.setDogumYeri(mernisData.getDogumYeri());
        kisi.setCinsiyet(mernisData.getCinsiyet());
        kisi.setMedeniDurum(mernisData.getMedeniDurum());
        kisi.setAdres(mernisData.getAdres());
        kisi.setIl(mernisData.getIl());
        kisi.setIlce(mernisData.getIlce());
        kisi.setMahalle(mernisData.getMahalle());
        kisi.setTelefon(mernisData.getTelefon());
        kisi.setGebzeIkameti(ValidationUtil.isGebzeAddress(mernisData.getIl(), mernisData.getIlce()));
        kisi.setSonMernisSorguTarihi(LocalDate.now());
        kisi.setMernisGuncellemeSayisi(kisi.getMernisGuncellemeSayisi() + 1);

        return kisiRepository.save(kisi);
    }

    
    @Transactional(readOnly = true)
    public Kisi findById(Long id) {
        return kisiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kişi", "id", id));
    }

    
    @Transactional(readOnly = true)
    public Optional<Kisi> findByTcKimlikNo(String tcKimlikNo) {
        return kisiRepository.findByTcKimlikNo(tcKimlikNo);
    }

   
    @Transactional(readOnly = true)
    public List<Kisi> findAll() {
        return kisiRepository.findAll();
    }

    
    public Kisi save(Kisi kisi) {
        log.info("Kişi kaydediliyor: {}", kisi.getTcKimlikNo());
        
       
        if (!ValidationUtil.isValidTcKimlikNo(kisi.getTcKimlikNo())) {
            throw new IllegalArgumentException("Geçersiz TC Kimlik Numarası");
        }

        
        if (kisi.getId() == null) {
            Optional<Kisi> existing = kisiRepository.findByTcKimlikNo(kisi.getTcKimlikNo());
            if (existing.isPresent()) {
                throw new DuplicateResourceException("Kişi", "tcKimlikNo", kisi.getTcKimlikNo());
            }
        }

        return kisiRepository.save(kisi);
    }

   
    public void delete(Long id) {
        log.info("Kişi siliniyor: {}", id);
        Kisi kisi = findById(id);
        kisi.setAktif(false);
        kisiRepository.save(kisi);
    }

   
    @Transactional(readOnly = true)
    public List<Kisi> findGebzeIkametliler() {
        return kisiRepository.findGebzeIkametliler();
    }
}


