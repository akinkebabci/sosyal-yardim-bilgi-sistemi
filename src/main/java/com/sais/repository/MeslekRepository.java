package com.sais.repository;

import com.sais.entity.Meslek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MeslekRepository extends JpaRepository<Meslek, Long> {

    Optional<Meslek> findByKod(String kod);

    @Query("SELECT m FROM Meslek m WHERE m.aktif = true ORDER BY m.adi")
    List<Meslek> findAllActive();
}


