package com.sais.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MernisYakinDTO {

    private String tcKimlikNo;
    private String ad;
    private String soyad;
    private LocalDate dogumTarihi;
    private String yakinlikKodu;
    private String yakinlikAdi;
}


