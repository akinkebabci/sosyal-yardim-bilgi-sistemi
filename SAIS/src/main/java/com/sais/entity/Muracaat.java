package com.sais.entity;

import com.sais.enums.MuracaatDurum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "muracaat", indexes = {
    @Index(name = "idx_muracaat_no", columnList = "muracaat_no"),
    @Index(name = "idx_muracaat_durum", columnList = "durum"),
    @Index(name = "idx_muracaat_tarih", columnList = "muracaat_tarihi")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Muracaat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "muracaat_no", nullable = false, unique = true)
    private Long muracaatNo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "basvuru_sahibi_id", nullable = false)
    private Kisi basvuruSahibi;

    @Column(name = "komisyon_kararli", nullable = false)
    private Boolean komisyonKararli = true;

    @Column(name = "kendisi_basvurdu", nullable = false)
    private Boolean kendisiBasvurdu = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "adina_basvurulan_kisi_id")
    private Kisi adinaBasvurulanKisi;

    @Column(name = "muracaat_tarihi", nullable = false)
    private LocalDate muracaatTarihi;

    @Column(name = "inceleme_tarihi")
    private LocalDate incelemeTarihi;

    @Column(name = "basvuru_metni", columnDefinition = "TEXT")
    private String basvuruMetnu;

    @Column(name = "personel_gorus_notu", columnDefinition = "TEXT")
    private String personelGorusNotu;

    @Column(name = "dokuman_listesi", columnDefinition = "TEXT")
    private String dokumanListesi;

    @Enumerated(EnumType.STRING)
    @Column(name = "durum", nullable = false)
    private MuracaatDurum durum = MuracaatDurum.BEKLEMEDE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kaydeden_personel_id")
    private Personel kaydedenPersonel;

    @Column(name = "karar_no")
    private Long kararNo;

    @Column(name = "karar_tarihi")
    private LocalDate kararTarihi;

    @Column(name = "sonuclanma_tarihi")
    private LocalDate sonuclenmaTarihi;

    @OneToMany(mappedBy = "muracaat", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MuracaatYardimTalep> yardimTalepleri = new ArrayList<>();

    @OneToMany(mappedBy = "muracaat", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AileFert> aileFertleri = new ArrayList<>();

    @OneToOne(mappedBy = "muracaat", cascade = CascadeType.ALL, orphanRemoval = true)
    private AileMaddiDurum aileMaddiDurum;

    @OneToOne(mappedBy = "muracaat", cascade = CascadeType.ALL, orphanRemoval = true)
    private TutanakBilgisi tutanakBilgisi;

    @OneToMany(mappedBy = "muracaat", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<YardimKarar> yardimKararlari = new ArrayList<>();

    @OneToMany(mappedBy = "muracaat", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MuracaatDokuman> dokumanlar = new ArrayList<>();

    public void addYardimTalep(MuracaatYardimTalep talep) {
        yardimTalepleri.add(talep);
        talep.setMuracaat(this);
    }

    public void removeYardimTalep(MuracaatYardimTalep talep) {
        yardimTalepleri.remove(talep);
        talep.setMuracaat(null);
    }

    public void addAileFert(AileFert fert) {
        aileFertleri.add(fert);
        fert.setMuracaat(this);
    }

    public void removeAileFert(AileFert fert) {
        aileFertleri.remove(fert);
        fert.setMuracaat(null);
    }

    public void addYardimKarar(YardimKarar karar) {
        yardimKararlari.add(karar);
        karar.setMuracaat(this);
    }

    public void removeYardimKarar(YardimKarar karar) {
        yardimKararlari.remove(karar);
        karar.setMuracaat(null);
    }

    public void addDokuman(MuracaatDokuman dokuman) {
        dokumanlar.add(dokuman);
        dokuman.setMuracaat(this);
    }

    public void removeDokuman(MuracaatDokuman dokuman) {
        dokumanlar.remove(dokuman);
        dokuman.setMuracaat(null);
    }
}


