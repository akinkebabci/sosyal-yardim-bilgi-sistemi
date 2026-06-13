package com.sais.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EvYakacakTipi {
    
    DOGALGAZ("Doğalgaz", "Doğalgaz"),
    KOMUR("Kömür", "Kömür"),
    ODUN("Odun", "Odun"),
    ELEKTRIK("Elektrik", "Elektrik"),
    SOBA("Soba", "Soba"),
    DIGER("Diğer", "Diğer");
    
    private final String label;
    private final String description;
}


