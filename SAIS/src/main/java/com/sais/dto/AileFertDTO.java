package com.sais.dto;

import com.sais.enums.OgrenimDurum;
import com.sais.enums.SGKDurum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AileFertDTO {

    private Long id;
    private Long muracaatId;
    
    // Kişi Bilgileri
    private Long kisiId;
    private String tcKimlikNo;
    private String adSoyad;
    private LocalDate dogumTarihi;
    private Integer yas;
    private String cinsiyet;
    
    // İlişki Bilgileri
    private Long yakinlikKoduId;
    private String yakinlikAdi;
    
    // Diğer Bilgiler
    private Long ozelStatuId;
    private String ozelStatuAdi;
    private Long meslekId;
    private String meslekAdi;
    private String yaptigiIs;
    private SGKDurum sgkDurum;
    private OgrenimDurum ogrenimDurum;
    private String aciklama;
    
    // Engel Bilgisi
    private boolean engelliMi;
    private Long engelliTipiId;
    private String engelliTipiAdi;
    private Integer engelOrani;
    
    // Hastalık Bilgisi
    private boolean hastalikVarMi;
    private Long hastalikId;
    private String hastalikAdi;
    private boolean kronik;
}


