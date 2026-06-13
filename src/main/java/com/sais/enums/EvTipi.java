package com.sais.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum EvTipi {
    
    APARTMAN_DAIRESI("Apartman Dairesi", "Apartman dairesi"),
    MUSTAKIL_EV("Müstakil Ev", "Müstakil ev"),
    GECEKONDU("Gecekondu", "Gecekondu"),
    VILLA("Villa", "Villa"),
    DIGER("Diğer", "Diğer");
    
    private final String label;
    private final String description;
}


