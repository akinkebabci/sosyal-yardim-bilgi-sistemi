package com.sais.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "borc_bilgisi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorcBilgisi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aile_maddi_durum_id", nullable = false)
    private AileMaddiDurum aileMaddiDurum;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "borc_turu_id", nullable = false)
    private BorcTuru borcTuru;

    @Column(name = "borc_tutari", nullable = false)
    private Double borcTutari;

    @Column(name = "aciklama", length = 255)
    private String aciklama;
}


