package com.sais.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum OgrenimDurum {
    
    OKUR_YAZAR_DEGIL("Okur Yazar Değil", "Okuma yazma bilmiyor"),
    OKUR_YAZAR("Okur Yazar", "Okuma yazma biliyor"),
    ILKOKUL("İlkokul", "İlkokul mezunu"),
    ORTAOKUL("Ortaokul", "Ortaokul mezunu"),
    LISE("Lise", "Lise mezunu"),
    ON_LISANS("Ön Lisans", "Ön lisans mezunu"),
    LISANS("Lisans", "Üniversite mezunu"),
    YUKSEK_LISANS("Yüksek Lisans", "Yüksek lisans mezunu"),
    DOKTORA("Doktora", "Doktora mezunu");
    
    private final String label;
    private final String description;
}


