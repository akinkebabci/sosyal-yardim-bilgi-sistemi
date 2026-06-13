package com.sais.repository;

import com.sais.entity.Kisi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;


@Repository
public interface KisiRepository extends JpaRepository<Kisi, Long> {

    
    Optional<Kisi> findByTcKimlikNo(String tcKimlikNo);

    
    @Query("SELECT k FROM Kisi k WHERE k.tcKimlikNo = :tcKimlikNo AND k.sonMernisSorguTarihi = :tarih")
    Optional<Kisi> findByTcKimlikNoAndSonMernisSorguTarihi(@Param("tcKimlikNo") String tcKimlikNo, @Param("tarih") LocalDate tarih);

    
    boolean existsByTcKimlikNo(String tcKimlikNo);

   
    @Query("SELECT k FROM Kisi k WHERE k.gebzeIkameti = true")
    java.util.List<Kisi> findGebzeIkametliler();
}


