package com.sais.service;

import com.sais.dto.MuracaatDokumanDTO;
import com.sais.entity.Muracaat;
import com.sais.entity.MuracaatDokuman;
import com.sais.entity.Personel;
import com.sais.exception.ResourceNotFoundException;
import com.sais.mapper.MuracaatDokumanMapper;
import com.sais.repository.MuracaatDokumanRepository;
import com.sais.repository.MuracaatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MuracaatDokumanService {

    private final MuracaatDokumanRepository dokumanRepository;
    private final MuracaatRepository muracaatRepository;
    private final FileStorageService fileStorageService;
    private final MuracaatDokumanMapper dokumanMapper;

 
    public MuracaatDokumanDTO uploadDokuman(MultipartFile file, Long muracaatId, 
                                            Personel yukleyenPersonel) {
        log.info("Doküman yükleniyor - Müracaat ID: {}, Dosya: {}", muracaatId, file.getOriginalFilename());

        Muracaat muracaat = muracaatRepository.findById(muracaatId)
            .orElseThrow(() -> new ResourceNotFoundException("Müracaat bulunamadı: " + muracaatId));

        
        String fileName = fileStorageService.storeMuracaatDokuman(
            file,
            muracaat.getMuracaatNo(),
            muracaat.getBasvuruSahibi().getAd(),
            muracaat.getBasvuruSahibi().getSoyad()
        );

        
        Path filePath = fileStorageService.getMuracaatDokumanPath(
            muracaat.getMuracaatNo(),
            muracaat.getBasvuruSahibi().getAd(),
            muracaat.getBasvuruSahibi().getSoyad(),
            fileName
        );

       
        MuracaatDokuman dokuman = MuracaatDokuman.builder()
            .muracaat(muracaat)
            .dosyaAdi(fileName)
            .orijinalDosyaAdi(file.getOriginalFilename())
            .dosyaYolu(filePath.toString())
            .dosyaTipi(file.getContentType())
            .dosyaBoyutu(file.getSize())
            .yukleyenPersonel(yukleyenPersonel)
            .build();

        MuracaatDokuman savedDokuman = dokumanRepository.save(dokuman);
        log.info("Doküman başarıyla kaydedildi - ID: {}", savedDokuman.getId());

        return dokumanMapper.toDto(savedDokuman);
    }

    
    @Transactional(readOnly = true)
    public List<MuracaatDokumanDTO> getDokumanlarByMuracaatId(Long muracaatId) {
        log.debug("Müracaat dokümanları getiriliyor - Müracaat ID: {}", muracaatId);
        
        List<MuracaatDokuman> dokumanlar = dokumanRepository.findByMuracaatId(muracaatId);
        return dokumanlar.stream()
            .map(dokumanMapper::toDto)
            .collect(Collectors.toList());
    }

    
    @Transactional(readOnly = true)
    public MuracaatDokumanDTO getDokumanById(Long id) {
        log.debug("Doküman detayı getiriliyor - ID: {}", id);
        
        MuracaatDokuman dokuman = dokumanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Doküman bulunamadı: " + id));
        
        return dokumanMapper.toDto(dokuman);
    }

   
    @Transactional(readOnly = true)
    public Resource downloadDokuman(Long id) {
        log.debug("Doküman indiriliyor - ID: {}", id);
        
        MuracaatDokuman dokuman = dokumanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Doküman bulunamadı: " + id));
        
        Path filePath = Path.of(dokuman.getDosyaYolu());
        return fileStorageService.loadFileAsResource(filePath);
    }

   
    public void deleteDokuman(Long id) {
        log.info("Doküman siliniyor - ID: {}", id);
        
        MuracaatDokuman dokuman = dokumanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Doküman bulunamadı: " + id));
        
        
        Path filePath = Path.of(dokuman.getDosyaYolu());
        fileStorageService.deleteFile(filePath);
        
       
        Long muracaatId = dokuman.getMuracaat().getId();
        dokumanRepository.delete(dokuman);
        
        log.info("Doküman başarıyla silindi - ID: {}", id);
    }

   
    @Transactional(readOnly = true)
    public long getDokumanCount(Long muracaatId) {
        return dokumanRepository.countByMuracaatId(muracaatId);
    }
    
   
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateMuracaatDokumanListesi(Long muracaatId) {
        try {
            log.info("📄 Müracaat dokuman_listesi güncelleniyor - Müracaat ID: {}", muracaatId);
            
            List<MuracaatDokuman> dokumanlar = dokumanRepository.findByMuracaatId(muracaatId);
            log.info("   📊 Bulunan doküman sayısı: {}", dokumanlar.size());
            
            Muracaat muracaat = muracaatRepository.findById(muracaatId)
                .orElseThrow(() -> new ResourceNotFoundException("Müracaat bulunamadı: " + muracaatId));
            
            if (dokumanlar.isEmpty()) {
                
                muracaat.setDokumanListesi(null);
                muracaatRepository.save(muracaat);
                log.info("   ✅ dokuman_listesi = NULL (doküman yok)");
                return;
            }
            
            
            StringBuilder dokumanListesi = new StringBuilder();
            for (int i = 0; i < dokumanlar.size(); i++) {
                MuracaatDokuman dokuman = dokumanlar.get(i);
                dokumanListesi.append(i + 1)
                             .append(". ")
                             .append(dokuman.getOrijinalDosyaAdi());
                
                if (i < dokumanlar.size() - 1) {
                    dokumanListesi.append("\n");
                }
            }
            
            String listeString = dokumanListesi.toString();
            muracaat.setDokumanListesi(listeString);
            muracaatRepository.save(muracaat);
            
            log.info("   ✅ dokuman_listesi güncellendi:\n{}", listeString);
            
        } catch (Exception e) {
            log.error("❌ Dokuman listesi güncellenirken hata - Müracaat ID: {}", muracaatId, e);
            
        }
    }

}

