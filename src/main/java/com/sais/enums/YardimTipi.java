package com.sais.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum YardimTipi {
    
    NAKDI("Nakdi", "Nakit para yardımı"),
    AYNI("Ayni", "Mal/Eşya yardımı"),
    SAGLIK("Sağlık", "Sağlık yardımı"),
    EGITIM("Eğitim", "Eğitim yardımı"),
    BARIS("Barış", "Barınma yardımı"),
    DIGER("Diğer", "Diğer yardım türleri");
    
    private final String label;
    private final String description;
}


