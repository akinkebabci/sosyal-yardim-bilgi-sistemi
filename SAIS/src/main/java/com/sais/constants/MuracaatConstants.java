package com.sais.constants;


public final class MuracaatConstants {

    private MuracaatConstants() {
        throw new IllegalStateException("Utility class");
    }


    public static final int TAB_MURACAAT_BILGILERI = 0;
    public static final int TAB_AILE_FERTLERI = 1;
    public static final int TAB_MADDI_DURUM = 2;
    public static final int TAB_TUTANAK = 3;
    public static final int TAB_KOMISYONLU_KARAR = 4;
    public static final int TAB_KOMISYONSUZ_KARAR = 5;
    public static final int MAX_TAB_INDEX = 5;


    public static final int MIN_BASVURU_TEXT_LENGTH = 10;
    public static final int MAX_RECENT_MURACAAT_COUNT = 10;
    public static final int MAX_ONCEKI_MURACAAT_COUNT = 3;


    
}

