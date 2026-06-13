package com.sais.repository;

import com.sais.entity.AileFertHastalikBilgisi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AileFertHastalikBilgisiRepository extends JpaRepository<AileFertHastalikBilgisi, Long> {

 
    @Query("SELECT h FROM AileFertHastalikBilgisi h WHERE h.aileFert.id = :aileFertId")
    Optional<AileFertHastalikBilgisi> findByAileFertId(@Param("aileFertId") Long aileFertId);
}

