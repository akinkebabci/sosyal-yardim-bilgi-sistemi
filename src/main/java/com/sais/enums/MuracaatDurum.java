package com.sais.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MuracaatDurum {
    
    TALEP_IPTAL_EDILDI("Talep İptal Edildi", "Başvuru iptal edildi"),
    BASVURU_SAHIBINE_ULASILMADI("Başvuru Sahibine Ulaşılamadı", "Başvuru sahibi ile irtibat kurulamadı"),
    BEKLEMEDE("Beklemede", "Başvuru beklemede"),
    TAHKIKATA_SEVK("Tahkikata Sevk", "Tahkikat için sevk edildi"),
    DEGERLENDIRME_KOMISYONU("Değerlendirme Komisyonu", "Komisyon değerlendirmesinde"),
    SONUCLANDI("Sonuçlanan", "Başvuru sonuçlandı"),
    BASVURU_SAHIBI_VEFAT_ETTI("Başvuru Sahibi Vefat Etti", "Başvuru sahibi vefat etti");
    
    private final String label;
    private final String description;
}


