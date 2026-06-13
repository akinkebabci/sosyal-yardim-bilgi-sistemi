package com.sais.repository;

import com.sais.entity.GelirBilgisi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GelirBilgisiRepository extends JpaRepository<GelirBilgisi, Long> {


    @Query("SELECT g FROM GelirBilgisi g WHERE g.aileMaddiDurum.id = :maddiDurumId")
    List<GelirBilgisi> findByAileMaddiDurumId(@Param("maddiDurumId") Long maddiDurumId);
}

