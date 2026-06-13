package com.sais.repository;

import com.sais.entity.GayrimenkulBilgisi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GayrimenkulBilgisiRepository extends JpaRepository<GayrimenkulBilgisi, Long> {

    
    @Query("SELECT g FROM GayrimenkulBilgisi g WHERE g.aileMaddiDurum.id = :maddiDurumId")
    Optional<GayrimenkulBilgisi> findByAileMaddiDurumId(@Param("maddiDurumId") Long maddiDurumId);
}

