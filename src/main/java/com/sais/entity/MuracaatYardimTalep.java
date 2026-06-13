package com.sais.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "muracaat_yardim_talep")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuracaatYardimTalep extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "muracaat_id", nullable = false)
    private Muracaat muracaat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "yardim_alt_tipi_id", nullable = false)
    private YardimAltTipi yardimAltTipi;

    @Column(name = "talep_metni", columnDefinition = "TEXT")
    private String talepMetni;

    @Column(name = "aciklama", length = 500)
    private String aciklama;

    @Column(name = "sira_no")
    private Integer siraNo;
}


