package com.sais.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;


public interface FileStorageService {

   
    String storeMuracaatDokuman(MultipartFile file, Long muracaatNo, 
                                String basvuruSahibiAd, String basvuruSahibiSoyad);

   
    Path getMuracaatDokumanPath(Long muracaatNo, String basvuruSahibiAd, 
                                String basvuruSahibiSoyad, String fileName);

    
    Resource loadFileAsResource(Path filePath);

  
    void deleteFile(Path filePath);

    
    String sanitizeFileName(String fileName);

   
    String storeTahkikatGorsel(MultipartFile file, Long muracaatNo,
                               String basvuruSahibiAd, String basvuruSahibiSoyad);

    
    Path getTahkikatGorselPath(Long muracaatNo, String basvuruSahibiAd,
                               String basvuruSahibiSoyad, String fileName);
}

