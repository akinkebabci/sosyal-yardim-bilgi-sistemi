package com.sais.repository;

import com.sais.entity.BorcTuru;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BorcTuruRepository extends JpaRepository<BorcTuru, Long> {

    Optional<BorcTuru> findByKod(String kod);

    @Query("SELECT b FROM BorcTuru b WHERE b.aktif = true ORDER BY b.siraNo")
    List<BorcTuru> findAllActive();
}


