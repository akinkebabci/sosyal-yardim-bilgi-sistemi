package com.sais.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GelirBilgisiDTO {

    private Long id;
    private Long kisiId;
    private String tcKimlikNo;
    private String adSoyad;
    private String yakinlik;
    private Long gelirTuruId;
    private String gelirTuruAdi;
    private Double gelirTutari;
    private String aciklama;
}


