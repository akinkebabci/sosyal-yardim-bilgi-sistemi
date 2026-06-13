package com.sais.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "muracaat_dokuman", indexes = {
    @Index(name = "idx_dokuman_muracaat", columnList = "muracaat_id"),
    @Index(name = "idx_dokuman_tarih", columnList = "yuklenme_tarihi")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuracaatDokuman extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "muracaat_id", nullable = false)
    private Muracaat muracaat;

    @Column(name = "dosya_adi", nullable = false)
    private String dosyaAdi;

    @Column(name = "orijinal_dosya_adi", nullable = false)
    private String orijinalDosyaAdi;

    @Column(name = "dosya_yolu", nullable = false, length = 500)
    private String dosyaYolu;

    @Column(name = "dosya_tipi", nullable = false, length = 100)
    private String dosyaTipi;

    @Column(name = "dosya_boyutu", nullable = false)
    private Long dosyaBoyutu;

    @Column(name = "aciklama", length = 500)
    private String aciklama;

    @Column(name = "yuklenme_tarihi", nullable = false)
    private LocalDateTime yuklenmeTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yukleyen_personel_id")
    private Personel yukleyenPersonel;

    @PrePersist
    protected void onCreate() {
        if (yuklenmeTarihi == null) {
            yuklenmeTarihi = LocalDateTime.now();
        }
    }
}

