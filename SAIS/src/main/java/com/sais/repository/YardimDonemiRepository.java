package com.sais.repository;

import com.sais.entity.YardimDonemi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface YardimDonemiRepository extends JpaRepository<YardimDonemi, Long> {

    Optional<YardimDonemi> findByAySayisi(Integer aySayisi);

    @Query("SELECT y FROM YardimDonemi y WHERE y.aktif = true ORDER BY y.aySayisi")
    List<YardimDonemi> findAllActive();
}


