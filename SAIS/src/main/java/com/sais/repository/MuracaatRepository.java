package com.sais.repository;

import com.sais.entity.Muracaat;
import com.sais.enums.MuracaatDurum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MuracaatRepository extends JpaRepository<Muracaat, Long> {

    
    Optional<Muracaat> findByMuracaatNo(Long muracaatNo);

    
    @Query("SELECT MAX(m.muracaatNo) FROM Muracaat m")
    Optional<Long> findMaxMuracaatNo();

    
    @Query("SELECT m FROM Muracaat m WHERE m.basvuruSahibi.id = :kisiId ORDER BY m.muracaatTarihi DESC")
    List<Muracaat> findByBasvuruSahibiId(@Param("kisiId") Long kisiId);

    
    @Query("SELECT m FROM Muracaat m WHERE m.basvuruSahibi.id = :kisiId AND m.durum = 'SONUCLANDI' ORDER BY m.muracaatTarihi DESC")
    List<Muracaat> findSonuclananMuracaatlarByKisi(@Param("kisiId") Long kisiId);

    
    @Query("SELECT COUNT(m) > 0 FROM Muracaat m WHERE m.basvuruSahibi.id = :kisiId AND m.durum != 'SONUCLANDI'")
    boolean existsSonuclanmamisMuracaat(@Param("kisiId") Long kisiId);

    
    List<Muracaat> findByDurum(MuracaatDurum durum);

    
    long countByDurum(MuracaatDurum durum);

    
    @Query("SELECT MAX(m.kararNo) FROM Muracaat m WHERE m.kararNo IS NOT NULL")
    Optional<Long> findMaxKararNo();

    
    Optional<Muracaat> findByKararNo(Long kararNo);

   
    @Query("SELECT m FROM Muracaat m LEFT JOIN FETCH m.basvuruSahibi WHERE m.id = :id")
    Optional<Muracaat> findByIdWithBasvuruSahibi(@Param("id") Long id);
    
    
    @Query("SELECT DISTINCT m FROM Muracaat m LEFT JOIN FETCH m.basvuruSahibi ORDER BY m.muracaatNo DESC")
    List<Muracaat> findAllWithBasvuruSahibi();
    
   
    @Query("SELECT DISTINCT m FROM Muracaat m LEFT JOIN FETCH m.basvuruSahibi WHERE m.durum = 'BEKLEMEDE' ORDER BY m.muracaatTarihi DESC")
    List<Muracaat> findYarimKalanMuracaatlar();
    
    
    @Query("SELECT DISTINCT m FROM Muracaat m LEFT JOIN FETCH m.basvuruSahibi WHERE m.durum = 'SONUCLANDI' ORDER BY m.muracaatTarihi DESC")
    List<Muracaat> findTamamlananMuracaatlar();
    
   
    @Query("SELECT DISTINCT m FROM Muracaat m LEFT JOIN FETCH m.basvuruSahibi WHERE m.durum IN ('SONUCLANDI', 'BEKLEMEDE') ORDER BY m.muracaatTarihi DESC")
    List<Muracaat> findTamamlananVeBekleyenMuracaatlar();
    
   
    @Query("SELECT DISTINCT m FROM Muracaat m " +
           "LEFT JOIN FETCH m.basvuruSahibi " +
           "LEFT JOIN FETCH m.yardimTalepleri " +
           "WHERE m.id = :id")
    Optional<Muracaat> findByIdWithAllDetails(@Param("id") Long id);
}


