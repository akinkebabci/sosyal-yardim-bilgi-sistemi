package com.sais.service;

import com.sais.config.FileStorageProperties;
import com.sais.exception.FileStorageException;
import com.sais.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final FileStorageProperties fileStorageProperties;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @Override
    public String storeMuracaatDokuman(MultipartFile file, Long muracaatNo, 
                                       String basvuruSahibiAd, String basvuruSahibiSoyad) {
        validateFile(file);

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFileName);

        if (!fileStorageProperties.isAllowedFileType(fileExtension)) {
            throw new FileStorageException(
                "Dosya türü izin verilmemektedir: " + fileExtension + 
                ". İzin verilen türler: " + fileStorageProperties.getAllowedTypes()
            );
        }

        if (file.getSize() > fileStorageProperties.getMaxFileSize()) {
            throw new FileStorageException(
                "Dosya boyutu çok büyük. Maksimum: " + 
                (fileStorageProperties.getMaxFileSize() / 1024 / 1024) + "MB"
            );
        }

        try {
            String newFileName = generateUniqueFileName(originalFileName);
            Path targetLocation = createMuracaatDirectory(muracaatNo, basvuruSahibiAd, basvuruSahibiSoyad)
                .resolve(newFileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("Dosya başarıyla kaydedildi: {}", targetLocation);

            return newFileName;

        } catch (IOException ex) {
            log.error("Dosya kaydedilirken hata oluştu: {}", originalFileName, ex);
            throw new FileStorageException("Dosya kaydedilemedi: " + originalFileName, ex);
        }
    }

    @Override
    public Path getMuracaatDokumanPath(Long muracaatNo, String basvuruSahibiAd, 
                                       String basvuruSahibiSoyad, String fileName) {
        String folderName = createFolderName(basvuruSahibiAd, basvuruSahibiSoyad);
        return fileStorageProperties.getMuracaatUploadPath()
            .resolve(folderName)
            .resolve(String.valueOf(muracaatNo))
            .resolve(fileName)
            .normalize();
    }

    @Override
    public Resource loadFileAsResource(Path filePath) {
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("Dosya bulunamadı: " + filePath);
            }
        } catch (MalformedURLException ex) {
            log.error("Dosya yüklenirken hata: {}", filePath, ex);
            throw new ResourceNotFoundException("Dosya bulunamadı: " + filePath, ex);
        }
    }

    @Override
    public void deleteFile(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
            log.info("Dosya silindi: {}", filePath);
        } catch (IOException ex) {
            log.error("Dosya silinirken hata: {}", filePath, ex);
            throw new FileStorageException("Dosya silinemedi: " + filePath, ex);
        }
    }

    @Override
    public String sanitizeFileName(String fileName) {
        
        String sanitized = StringUtils.cleanPath(fileName);
        
        
        sanitized = sanitized.replaceAll("[^a-zA-Z0-9._-]", "_");
        
        
        if (sanitized.startsWith(".")) {
            sanitized = "_" + sanitized;
        }
        
        return sanitized;
    }

   
    private Path createMuracaatDirectory(Long muracaatNo, String ad, String soyad) {
        try {
            String folderName = createFolderName(ad, soyad);
            Path directory = fileStorageProperties.getMuracaatUploadPath()
                .resolve(folderName)
                .resolve(String.valueOf(muracaatNo));

            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
                log.info("Dizin oluşturuldu: {}", directory);
            }

            return directory;
        } catch (IOException ex) {
            log.error("Dizin oluşturulamadı", ex);
            throw new FileStorageException("Dizin oluşturulamadı", ex);
        }
    }

    
    private String createFolderName(String ad, String soyad) {
        String folderName = (ad + "_" + soyad)
            .replaceAll("[^a-zA-Z0-9]", "_")
            .replaceAll("_+", "_")
            .toUpperCase();
        
        return folderName;
    }

   
    private String generateUniqueFileName(String originalFileName) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String baseName = getFileBaseName(originalFileName);
        String extension = getFileExtension(originalFileName);
        
        String sanitizedBaseName = sanitizeFileName(baseName);
        
        return String.format("%s_%s_%s.%s", timestamp, uuid, sanitizedBaseName, extension);
    }

    
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1) : "";
    }

    
    private String getFileBaseName(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(0, lastDotIndex) : fileName;
    }

  
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("Dosya boş olamaz");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        
        if (fileName.contains("..")) {
            throw new FileStorageException("Dosya adı geçersiz karakterler içeriyor: " + fileName);
        }
    }

    @Override
    public String storeTahkikatGorsel(MultipartFile file, Long muracaatNo,
                                      String basvuruSahibiAd, String basvuruSahibiSoyad) {
        validateFile(file);

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFileName);

        
        if (!isImageFile(fileExtension)) {
            throw new FileStorageException(
                "Sadece resim dosyaları yüklenebilir (JPG, JPEG, PNG). " +
                "Yüklenen dosya türü: " + fileExtension
            );
        }

        if (file.getSize() > fileStorageProperties.getMaxFileSize()) {
            throw new FileStorageException(
                "Dosya boyutu çok büyük. Maksimum: " +
                (fileStorageProperties.getMaxFileSize() / 1024 / 1024) + "MB"
            );
        }

        try {
            String newFileName = generateUniqueFileName(originalFileName);
            Path targetLocation = createTahkikatDirectory(muracaatNo, basvuruSahibiAd, basvuruSahibiSoyad)
                .resolve(newFileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("Tahkikat görseli başarıyla kaydedildi: {}", targetLocation);

            return newFileName;

        } catch (IOException ex) {
            log.error("Tahkikat görseli kaydedilirken hata oluştu: {}", originalFileName, ex);
            throw new FileStorageException("Tahkikat görseli kaydedilemedi: " + originalFileName, ex);
        }
    }

    @Override
    public Path getTahkikatGorselPath(Long muracaatNo, String basvuruSahibiAd,
                                      String basvuruSahibiSoyad, String fileName) {
        String folderName = createFolderName(basvuruSahibiAd, basvuruSahibiSoyad);
        return fileStorageProperties.getMuracaatUploadPath()
            .resolve(folderName)
            .resolve(String.valueOf(muracaatNo))
            .resolve("tahkikat")
            .resolve(fileName)
            .normalize();
    }

    
    private Path createTahkikatDirectory(Long muracaatNo, String ad, String soyad) {
        try {
            String folderName = createFolderName(ad, soyad);
            Path directory = fileStorageProperties.getMuracaatUploadPath()
                .resolve(folderName)
                .resolve(String.valueOf(muracaatNo))
                .resolve("tahkikat");

            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
                log.info("Tahkikat dizini oluşturuldu: {}", directory);
            }

            return directory;
        } catch (IOException ex) {
            log.error("Tahkikat dizini oluşturulamadı", ex);
            throw new FileStorageException("Tahkikat dizini oluşturulamadı", ex);
        }
    }

   
    private boolean isImageFile(String fileExtension) {
        if (fileExtension == null || fileExtension.isEmpty()) {
            return false;
        }
        String extension = fileExtension.toLowerCase();
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png");
    }
}

