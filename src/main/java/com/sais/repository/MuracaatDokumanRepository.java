package com.sais.repository;

import com.sais.entity.MuracaatDokuman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MuracaatDokumanRepository extends JpaRepository<MuracaatDokuman, Long> {

    
    List<MuracaatDokuman> findByMuracaatId(Long muracaatId);

    
    long countByMuracaatId(Long muracaatId);

    
    @Query("SELECT d FROM MuracaatDokuman d WHERE d.muracaat.id = :muracaatId AND d.dosyaAdi = :dosyaAdi")
    List<MuracaatDokuman> findByMuracaatIdAndDosyaAdi(
        @Param("muracaatId") Long muracaatId, 
        @Param("dosyaAdi") String dosyaAdi
    );

    
    @Query("SELECT d FROM MuracaatDokuman d WHERE d.muracaat.id = :muracaatId AND d.dosyaTipi = :dosyaTipi")
    List<MuracaatDokuman> findByMuracaatIdAndDosyaTipi(
        @Param("muracaatId") Long muracaatId, 
        @Param("dosyaTipi") String dosyaTipi
    );
    
    void deleteByMuracaatId(Long muracaatId);
}

