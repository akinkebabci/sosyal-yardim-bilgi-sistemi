package com.sais.repository;

import com.sais.entity.EngelliTipi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EngelliTipiRepository extends JpaRepository<EngelliTipi, Long> {

    Optional<EngelliTipi> findByKod(String kod);

    @Query("SELECT e FROM EngelliTipi e WHERE e.ustTip IS NULL AND e.aktif = true ORDER BY e.siraNo")
    List<EngelliTipi> findAnaTipler();

    @Query("SELECT e FROM EngelliTipi e WHERE e.ustTip.id = :ustTipId AND e.aktif = true ORDER BY e.siraNo")
    List<EngelliTipi> findAltTiplerByUstTipId(Long ustTipId);

    @Query("SELECT e FROM EngelliTipi e WHERE e.ustTip IS NOT NULL AND e.aktif = true ORDER BY e.siraNo")
    List<EngelliTipi> findAllAltTipler();
}


