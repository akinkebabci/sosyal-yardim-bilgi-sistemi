package com.sais.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;


@Configuration
@ConfigurationProperties(prefix = "file.storage")
@Getter
@Setter
public class FileStorageProperties {


    private String uploadDir = "uploads";

 
    private String muracaatDir = "muracaat";

  
    private String allowedTypes = "pdf,doc,docx,jpg,jpeg,png,xls,xlsx";

  
    private Long maxFileSize = 10485760L; // 10MB

   
    public Path getUploadPath() {
        return Paths.get(uploadDir).toAbsolutePath().normalize();
    }

 
    public Path getMuracaatUploadPath() {
        return getUploadPath().resolve(muracaatDir);
    }

  
    public boolean isAllowedFileType(String fileExtension) {
        if (fileExtension == null || fileExtension.isEmpty()) {
            return false;
        }
        String extension = fileExtension.toLowerCase().replaceFirst("^\\.", "");
        return allowedTypes.toLowerCase().contains(extension);
    }
}

