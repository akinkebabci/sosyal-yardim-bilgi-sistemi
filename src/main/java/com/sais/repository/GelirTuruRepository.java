package com.sais.repository;

import com.sais.entity.GelirTuru;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface GelirTuruRepository extends JpaRepository<GelirTuru, Long> {

    Optional<GelirTuru> findByKod(String kod);

    @Query("SELECT g FROM GelirTuru g WHERE g.aktif = true ORDER BY g.siraNo")
    List<GelirTuru> findAllActive();
}


