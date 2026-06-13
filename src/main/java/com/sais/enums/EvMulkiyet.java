package com.sais.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EvMulkiyet {
    
    KIRA("Kira", "Kirada oturuyor"),
    LOJMAN("Lojman", "Lojmanda oturuyor"),
    AKRABA_EVI("Akraba Evi", "Akraba evinde oturuyor"),
    DIGER("Diğer", "Diğer");
    
    private final String label;
    private final String description;
}


