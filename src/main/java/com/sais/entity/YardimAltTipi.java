package com.sais.entity;

import com.sais.enums.YardimTipi;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "yardim_alt_tipi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YardimAltTipi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kod", unique = true, length = 20)
    private String kod;

    @Column(name = "adi", nullable = false, length = 150)
    private String adi;

    @Enumerated(EnumType.STRING)
    @Column(name = "yardim_tipi", nullable = false)
    private YardimTipi yardimTipi;

    @Column(name = "komisyon_kararli", nullable = false)
    private Boolean komisyonKararli = true;

    @Column(name = "birim", length = 50)
    private String birim; // Adet, Paket, TL vb.

    @Column(name = "varsayilan_tutar")
    private Double varsayilanTutar;

    @Column(name = "aciklama", length = 500)
    private String aciklama;

    @Column(name = "sira_no")
    private Integer siraNo;
}


