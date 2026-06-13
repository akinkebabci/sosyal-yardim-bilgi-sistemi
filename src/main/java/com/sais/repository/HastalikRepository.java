package com.sais.repository;

import com.sais.entity.Hastalik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface HastalikRepository extends JpaRepository<Hastalik, Long> {

    Optional<Hastalik> findByKod(String kod);

    @Query("SELECT h FROM Hastalik h WHERE h.aktif = true ORDER BY h.adi")
    List<Hastalik> findAllActive();

    @Query("SELECT h FROM Hastalik h WHERE h.kronik = true AND h.aktif = true ORDER BY h.adi")
    List<Hastalik> findKronikHastaliklar();
}


