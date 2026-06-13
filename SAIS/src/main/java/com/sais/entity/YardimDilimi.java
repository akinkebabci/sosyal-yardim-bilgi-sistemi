package com.sais.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "yardim_dilimi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YardimDilimi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kod", unique = true, length = 20)
    private String kod;

    @Column(name = "adi", nullable = false, length = 50)
    private String adi;

    @Column(name = "aciklama", length = 255)
    private String aciklama;

    @Column(name = "sira_no")
    private Integer siraNo;
}


