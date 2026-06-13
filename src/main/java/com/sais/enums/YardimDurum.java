package com.sais.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum YardimDurum {
    
    KABUL_EDILDI("Kabul Edildi", "Yardım talebi kabul edildi"),
    RED_EDILDI("Red Edildi", "Yardım talebi reddedildi");
    
    private final String label;
    private final String description;
}


