package com.sais.repository;

import com.sais.entity.YakinlikKodu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface YakinlikKoduRepository extends JpaRepository<YakinlikKodu, Long> {

    Optional<YakinlikKodu> findByKod(String kod);

    @Query("SELECT y FROM YakinlikKodu y WHERE y.aktif = true ORDER BY y.siraNo")
    List<YakinlikKodu> findAllActive();
}


