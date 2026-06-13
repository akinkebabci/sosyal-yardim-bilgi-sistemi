package com.sais.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AileMaddiDurumDTO {

    private Long id;
    private Long muracaatId;
    
    // Gelir Bilgileri
    private List<GelirBilgisiDTO> gelirBilgileri;
    private Double toplamGelir;
    
    // Borç Bilgileri
    private List<BorcBilgisiDTO> borcBilgileri;
    private Double toplamBorc;
    
    // Gayrimenkul Bilgileri
    private GayrimenkulBilgisiDTO gayrimenkulBilgisi;
    
    private String aciklama;
}


