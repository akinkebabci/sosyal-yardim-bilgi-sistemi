package com.sais.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "personel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Personel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tc_kimlik_no", nullable = false, unique = true, length = 11)
    private String tcKimlikNo;

    @Column(name = "ad", nullable = false, length = 100)
    private String ad;

    @Column(name = "soyad", nullable = false, length = 100)
    private String soyad;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "telefon", length = 20)
    private String telefon;

    @Column(name = "unvan", length = 100)
    private String unvan;

    @Column(name = "departman", length = 100)
    private String departman;

    @Column(name = "tahkikat_yetkili")
    private Boolean tahkikatYetkili = false;

    @Column(name = "komisyon_uyesi")
    private Boolean komisyonUyesi = false;

    @Column(name = "aciklama", length = 500)
    private String aciklama;

    @Transient
    public String getAdSoyad() {
        return ad + " " + soyad;
    }
}


