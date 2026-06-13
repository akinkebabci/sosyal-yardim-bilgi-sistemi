package com.sais.dto;

import com.sais.enums.EvMulkiyet;
import com.sais.enums.EvTipi;
import com.sais.enums.EvYakacakTipi;
import com.sais.enums.GayrimenkulTuru;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GayrimenkulBilgisiDTO {

    private Long id;
    
    // Ev Bilgileri
    private Boolean eviVar;
    private EvTipi evTipi;
    private EvYakacakTipi evYakacakTipi;
    private EvMulkiyet evMulkiyet;
    private Double kiraTutari;
    
    // Kirada Ev
    private Boolean kiradaEviVar;
    private Integer kiradaEvSayisi;
    private Double toplamKiraGeliri;
    
    // Araba
    private Boolean arabaVar;
    private String arabaModeli;
    
    // Gayrimenkul
    private Boolean gayrimenkulVar;
    private GayrimenkulTuru gayrimenkulTuru;
    private String gayrimenkulAciklama;
}


