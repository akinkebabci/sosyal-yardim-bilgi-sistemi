package com.sais.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @CreatedDate
    @Column(name = "olusturma_tarihi", nullable = false, updatable = false)
    private LocalDateTime olusturmaTarihi;

    @LastModifiedDate
    @Column(name = "guncelleme_tarihi")
    private LocalDateTime guncellemeTarihi;

    @CreatedBy
    @Column(name = "olusturan", length = 100, updatable = false)
    private String olusturan;

    @LastModifiedBy
    @Column(name = "guncelleyen", length = 100)
    private String guncelleyen;

    @Column(name = "aktif")
    private Boolean aktif = true;
}


