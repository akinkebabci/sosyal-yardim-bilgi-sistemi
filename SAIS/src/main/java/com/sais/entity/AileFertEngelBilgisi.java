package com.sais.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "aile_fert_engel_bilgisi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AileFertEngelBilgisi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aile_fert_id", nullable = false)
    private AileFert aileFert;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "engelli_tipi_id", nullable = false)
    private EngelliTipi engelliTipi;

    @Column(name = "engel_orani", nullable = false)
    private Integer engelOrani;

    @Column(name = "rapor_tarihi")
    private java.time.LocalDate raporTarihi;

    @Column(name = "rapor_no", length = 100)
    private String raporNo;

    @Column(name = "aciklama", length = 500)
    private String aciklama;
}


