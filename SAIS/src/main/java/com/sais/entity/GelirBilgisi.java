package com.sais.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "gelir_bilgisi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GelirBilgisi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aile_maddi_durum_id", nullable = false)
    private AileMaddiDurum aileMaddiDurum;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kisi_id", nullable = false)
    private Kisi kisi;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gelir_turu_id", nullable = false)
    private GelirTuru gelirTuru;

    @Column(name = "gelir_tutari", nullable = false)
    private Double gelirTutari;

    @Column(name = "aciklama", length = 255)
    private String aciklama;
}


