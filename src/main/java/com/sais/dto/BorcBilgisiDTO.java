package com.sais.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorcBilgisiDTO {

    private Long id;
    private Long borcTuruId;
    private String borcTuruAdi;
    private Double borcTutari;
    private String aciklama;
}


