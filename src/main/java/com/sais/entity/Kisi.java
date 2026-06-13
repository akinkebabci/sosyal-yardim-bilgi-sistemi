package com.sais.entity;

import com.sais.enums.OgrenimDurum;
import com.sais.enums.SGKDurum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "kisi", indexes = {
    @Index(name = "idx_kisi_tc", columnList = "tc_kimlik_no"),
    @Index(name = "idx_kisi_son_mernis", columnList = "son_mernis_sorgu_tarihi")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Kisi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tc_kimlik_no", nullable = false, unique = true, length = 11)
    private String tcKimlikNo;

    @Column(name = "ad", nullable = false, length = 100)
    private String ad;

    @Column(name = "soyad", nullable = false, length = 100)
    private String soyad;

    @Column(name = "baba_adi", length = 100)
    private String babaAdi;

    @Column(name = "anne_adi", length = 100)
    private String anneAdi;

    @Column(name = "dogum_tarihi")
    private LocalDate dogumTarihi;

    @Column(name = "dogum_yeri", length = 100)
    private String dogumYeri;

    @Column(name = "cinsiyet", length = 1)
    private String cinsiyet; // E: Erkek, K: Kadın

    @Column(name = "medeni_durum", length = 20)
    private String medeniDurum;

    @Column(name = "adres", columnDefinition = "TEXT")
    private String adres;

    @Column(name = "il", length = 50)
    private String il;

    @Column(name = "ilce", length = 50)
    private String ilce;

    @Column(name = "mahalle", length = 100)
    private String mahalle;

    @Column(name = "telefon", length = 20)
    private String telefon;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "gebze_ikameti")
    private Boolean gebzeIkameti = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "sgk_durum")
    private SGKDurum sgkDurum;

    @Enumerated(EnumType.STRING)
    @Column(name = "ogrenim_durum")
    private OgrenimDurum ogrenimDurum;

    @Column(name = "son_mernis_sorgu_tarihi")
    private LocalDate sonMernisSorguTarihi;

    @Column(name = "mernis_guncelleme_sayisi")
    private Integer mernisGuncellemeSayisi = 0;

    @Transient
    public String getAdSoyad() {
        return ad + " " + soyad;
    }

    @Transient
    public Integer getYas() {
        if (dogumTarihi != null) {
            return LocalDate.now().getYear() - dogumTarihi.getYear();
        }
        return null;
    }
}


