package com.sais.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "gelir_turu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GelirTuru extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kod", unique = true, length = 20)
    private String kod;

    @Column(name = "adi", nullable = false, length = 100)
    private String adi;

    @Column(name = "aciklama", length = 255)
    private String aciklama;

    @Column(name = "sira_no")
    private Integer siraNo;
}


