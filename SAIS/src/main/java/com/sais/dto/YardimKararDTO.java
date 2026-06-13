package com.sais.dto;

import com.sais.enums.YardimDurum;
import com.sais.enums.YardimTipi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YardimKararDTO {

    private Long id;
    private Long muracaatId;
    
    // Yardım Bilgisi
    private Long yardimAltTipiId;
    private String yardimAdi;
    private YardimTipi yardimTipi;
    private boolean talepEdilmisMi; // *** işareti için
    
    // Karar Bilgisi
    private YardimDurum yardimDurum;
    private boolean komisyonKararli;
    private LocalDate toplantiTarihi;
    
    // Nakdi Yardım
    private Double verilenTutar;
    private Long yardimDilimiId;
    private String yardimDilimiAdi;
    private Long yardimDonemiId;
    private Integer ayKont;
    private Long hesapBilgisiId;
    private String iban;
    
    // Ayni Yardım
    private Integer adetSayi;
    
    // Red Bilgisi
    private Long redSebebiId;
    private String redSebebiAdi;
    
    private String aciklama;
    private boolean kesinlesti;
    private LocalDate kesinlesmeTarihi;
}


