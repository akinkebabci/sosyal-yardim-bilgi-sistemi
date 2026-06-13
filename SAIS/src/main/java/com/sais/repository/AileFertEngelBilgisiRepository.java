package com.sais.repository;

import com.sais.entity.AileFertEngelBilgisi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AileFertEngelBilgisiRepository extends JpaRepository<AileFertEngelBilgisi, Long> {


    @Query("SELECT e FROM AileFertEngelBilgisi e WHERE e.aileFert.id = :aileFertId")
    Optional<AileFertEngelBilgisi> findByAileFertId(@Param("aileFertId") Long aileFertId);
}

