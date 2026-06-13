package com.sais.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tutanak_bilgisi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TutanakBilgisi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "muracaat_id", nullable = false)
    private Muracaat muracaat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tahkikat_personel_id")
    private Personel tahkikatPersonel;

    @Column(name = "tahkikat_tarihi")
    private LocalDate tahkikatTarihi;

    @Column(name = "tahkikat_metni", columnDefinition = "TEXT")
    private String tahkikatMetni;

    @Column(name = "ev_gorselleri", columnDefinition = "TEXT")
    @Deprecated // Artık TutanakGorsel entity'si kullanılıyor (OneToMany)
    private String evGorselleri;

    @OneToMany(mappedBy = "tutanakBilgisi", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TutanakGorsel> gorseller = new ArrayList<>();

    @Column(name = "tamamlandi")
    private Boolean tamamlandi = false;

    // Helper metodlar
    public void addGorsel(TutanakGorsel gorsel) {
        gorseller.add(gorsel);
        gorsel.setTutanakBilgisi(this);
    }

    public void removeGorsel(TutanakGorsel gorsel) {
        gorseller.remove(gorsel);
        gorsel.setTutanakBilgisi(null);
    }
}


