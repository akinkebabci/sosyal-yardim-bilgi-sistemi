package com.sais.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "hesap_bilgisi", indexes = {
    @Index(name = "idx_hesap_kisi", columnList = "kisi_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HesapBilgisi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kisi_id", nullable = false)
    private Kisi kisi;

    @Column(name = "banka_adi", nullable = false, length = 100)
    private String bankaAdi;

    @Column(name = "sube_adi", length = 100)
    private String subeAdi;

    @Column(name = "iban", nullable = false, length = 32)
    private String iban;

    @Column(name = "hesap_sahibi_adi", nullable = false, length = 200)
    private String hesapSahibiAdi;

    @Column(name = "varsayilan")
    private Boolean varsayilan = false;

    @Column(name = "aciklama", length = 255)
    private String aciklama;
}


