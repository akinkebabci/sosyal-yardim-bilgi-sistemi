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
public class TutanakBilgisiDTO {

    private Long id;
    private Long muracaatId;
    private Long tahkikatPersonelId;
    private String tahkikatPersonelAdi;
    private LocalDate tahkikatTarihi;
    private String tahkikatMetni;
    private String evGorselleri;
    private boolean tamamlandi;
}


