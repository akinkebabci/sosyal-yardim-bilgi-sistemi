package com.sais.repository;

import com.sais.entity.AileFert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AileFertRepository extends JpaRepository<AileFert, Long> {

    
    @Query("SELECT DISTINCT af FROM AileFert af " +
           "LEFT JOIN FETCH af.kisi " +
           "LEFT JOIN FETCH af.yakinlikKodu " +
           "LEFT JOIN FETCH af.meslek " +
           "LEFT JOIN FETCH af.ozelStatu " +
           "WHERE af.muracaat.id = :muracaatId")
    List<AileFert> findByMuracaatId(@Param("muracaatId") Long muracaatId);

    
    @Query("SELECT af FROM AileFert af WHERE af.kisi.id = :kisiId")
    List<AileFert> findByKisiId(@Param("kisiId") Long kisiId);

    
    long countByMuracaatId(Long muracaatId);

    
    @Query("SELECT COUNT(af) > 0 FROM AileFert af WHERE af.muracaat.id = :muracaatId AND af.kisi.id = :kisiId")
    boolean existsByMuracaatIdAndKisiId(@Param("muracaatId") Long muracaatId, @Param("kisiId") Long kisiId);
    
    /**
     * Detay gösterimi için tüm ilişkileri eager fetch ile getirir
     */
    @Query("SELECT DISTINCT af FROM AileFert af " +
           "LEFT JOIN FETCH af.kisi k " +
           "LEFT JOIN FETCH af.yakinlikKodu " +
           "LEFT JOIN FETCH af.meslek " +
           "LEFT JOIN FETCH af.ozelStatu " +
           "LEFT JOIN FETCH af.engelBilgisi eb " +
           "LEFT JOIN FETCH eb.engelliTipi et " +
           "LEFT JOIN FETCH et.ustTip " +
           "LEFT JOIN FETCH af.hastalikBilgisi hb " +
           "WHERE af.id = :id")
    AileFert findByIdWithAllDetails(@Param("id") Long id);
    
    void deleteByMuracaatId(Long muracaatId);
}


