package com.sais.repository;

import com.sais.entity.OzelStatu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface OzelStatuRepository extends JpaRepository<OzelStatu, Long> {

    Optional<OzelStatu> findByKod(String kod);

    @Query("SELECT os FROM OzelStatu os WHERE os.aktif = true ORDER BY os.oncelikPuani DESC, os.adi")
    List<OzelStatu> findAllActive();
}


