package com.sais.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "tutanak_gorsel", indexes = {
    @Index(name = "idx_tutanak_gorsel_tutanak", columnList = "tutanak_bilgisi_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TutanakGorsel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tutanak_bilgisi_id", nullable = false)
    private TutanakBilgisi tutanakBilgisi;

    @Column(name = "dosya_adi", nullable = false, length = 255)
    private String dosyaAdi;

    @Column(name = "dosya_yolu", nullable = false, length = 500)
    private String dosyaYolu;

    @Column(name = "dosya_boyutu")
    private Long dosyaBoyutu;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "aciklama", length = 500)
    private String aciklama;

    @Column(name = "sira_no")
    private Integer siraNo;
}

