package com.sais.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MuracaatDokumanDTO {

    private Long id;
    
    private Long muracaatId;
    
    private String dosyaAdi;
    
    private String orijinalDosyaAdi;
    
    private String dosyaYolu;
    
    private String dosyaTipi;
    
    private Long dosyaBoyutu;
    
    private String aciklama;
    
    private LocalDateTime yuklenmeTarihi;
    
    private Long yukleyenPersonelId;
    
    private String yukleyenPersonelAd;
    
    private String yukleyenPersonelSoyad;
    
    /**
     * Dosya boyutunu okunabilir formatta döndürür
     */
    public String getFormattedFileSize() {
        if (dosyaBoyutu == null) {
            return "0 B";
        }
        
        if (dosyaBoyutu < 1024) {
            return dosyaBoyutu + " B";
        } else if (dosyaBoyutu < 1024 * 1024) {
            return String.format("%.2f KB", dosyaBoyutu / 1024.0);
        } else if (dosyaBoyutu < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", dosyaBoyutu / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", dosyaBoyutu / (1024.0 * 1024.0 * 1024.0));
        }
    }
}

