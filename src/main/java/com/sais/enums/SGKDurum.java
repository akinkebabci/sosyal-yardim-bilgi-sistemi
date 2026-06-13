package com.sais.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum SGKDurum {
    
    SSK("SSK", "Sosyal Sigortalar Kurumu"),
    BAG_KUR("Bağ-Kur", "Bağ-Kur"),
    EMEKLI_SANDIGI("Emekli Sandığı", "Emekli Sandığı"),
    YESIL_KART("Yeşil Kart", "Yeşil Kart"),
    YOK("Yok", "Sosyal güvencesi yok");
    
    private final String label;
    private final String description;
}


