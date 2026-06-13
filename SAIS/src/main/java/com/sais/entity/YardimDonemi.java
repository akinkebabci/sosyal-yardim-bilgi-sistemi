package com.sais.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "yardim_donemi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YardimDonemi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ay_sayisi", nullable = false, unique = true)
    private Integer aySayisi;

    @Column(name = "adi", nullable = false, length = 50)
    private String adi;

    @Column(name = "aciklama", length = 255)
    private String aciklama;
}


