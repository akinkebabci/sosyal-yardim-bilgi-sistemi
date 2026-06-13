package com.sais.entity;

import com.sais.enums.OgrenimDurum;
import com.sais.enums.SGKDurum;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "aile_fert", indexes = {
    @Index(name = "idx_aile_fert_muracaat", columnList = "muracaat_id"),
    @Index(name = "idx_aile_fert_kisi", columnList = "kisi_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AileFert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "muracaat_id", nullable = false)
    private Muracaat muracaat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kisi_id", nullable = false)
    private Kisi kisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yakinlik_kodu_id")
    private YakinlikKodu yakinlikKodu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ozel_statu_id")
    private OzelStatu ozelStatu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meslek_id")
    private Meslek meslek;

    @Column(name = "yaptigi_is", length = 150)
    private String yaptigiIs;

    @Enumerated(EnumType.STRING)
    @Column(name = "sgk_durum", length = 50)
    private SGKDurum sgkDurum;

    @Enumerated(EnumType.STRING)
    @Column(name = "ogrenim_durumu", length = 50)
    private OgrenimDurum ogrenimDurumu;

    @Column(name = "aciklama", columnDefinition = "TEXT")
    private String aciklama;

    @OneToOne(mappedBy = "aileFert", cascade = CascadeType.ALL, orphanRemoval = true)
    private AileFertEngelBilgisi engelBilgisi;

    @OneToOne(mappedBy = "aileFert", cascade = CascadeType.ALL, orphanRemoval = true)
    private AileFertHastalikBilgisi hastalikBilgisi;
}


