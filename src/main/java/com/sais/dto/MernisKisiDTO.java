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
public class MernisKisiDTO {

    private String tcKimlikNo;
    private String ad;
    private String soyad;
    private String babaAdi;
    private String anneAdi;
    private LocalDate dogumTarihi;
    private String dogumYeri;
    private String cinsiyet;
    private String medeniDurum;
    private String adres;
    private String il;
    private String ilce;
    private String mahalle;
    private String telefon;
}


