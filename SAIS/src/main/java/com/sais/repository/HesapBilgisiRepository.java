package com.sais.repository;

import com.sais.entity.HesapBilgisi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface HesapBilgisiRepository extends JpaRepository<HesapBilgisi, Long> {

  
    @Query("SELECT h FROM HesapBilgisi h WHERE h.kisi.id = :kisiId AND h.aktif = true ORDER BY h.varsayilan DESC")
    List<HesapBilgisi> findByKisiId(@Param("kisiId") Long kisiId);

  
    @Query("SELECT h FROM HesapBilgisi h WHERE h.kisi.id = :kisiId AND h.varsayilan = true AND h.aktif = true")
    Optional<HesapBilgisi> findVarsayilanHesap(@Param("kisiId") Long kisiId);

   
    Optional<HesapBilgisi> findByIban(String iban);
}


