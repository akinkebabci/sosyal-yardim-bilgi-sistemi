package com.sais.repository;

import com.sais.entity.YardimDilimi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YardimDilimiRepository extends JpaRepository<YardimDilimi, Long> {

    Optional<YardimDilimi> findByKod(String kod);

    @Query("SELECT y FROM YardimDilimi y WHERE y.aktif = true ORDER BY y.siraNo")
    List<YardimDilimi> findAllActive();
}


