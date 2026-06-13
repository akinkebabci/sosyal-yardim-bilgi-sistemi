package com.sais.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "aile_fert_hastalik_bilgisi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AileFertHastalikBilgisi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aile_fert_id", nullable = false)
    private AileFert aileFert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hastalik_id")
    private Hastalik hastalik;

    @Column(name = "hastalik_adi", length = 150)
    private String hastalikAdi;

    @Column(name = "teshis_tarihi")
    private java.time.LocalDate teshisTarihi;

    @Column(name = "aciklama", columnDefinition = "TEXT")
    private String aciklama;
}


