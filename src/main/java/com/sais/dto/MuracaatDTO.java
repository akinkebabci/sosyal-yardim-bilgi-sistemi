package com.sais.dto;

import com.sais.enums.MuracaatDurum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuracaatDTO {

    private Long id;
    private Long muracaatNo;
    private String basvuruSahibiAdSoyad;
    private String basvuruSahibiTcNo;
    private Boolean komisyonKararli;
    private LocalDate muracaatTarihi;
    private LocalDate incelemeTarihi;
    private MuracaatDurum durum;
    private String basvuruMetnu;
    private String personelGorusNotu;
    private Long kararNo;
    private LocalDate kararTarihi;
}


