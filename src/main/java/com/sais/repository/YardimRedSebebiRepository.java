package com.sais.repository;

import com.sais.entity.YardimRedSebebi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface YardimRedSebebiRepository extends JpaRepository<YardimRedSebebi, Long> {

    Optional<YardimRedSebebi> findByKod(String kod);

    @Query("SELECT y FROM YardimRedSebebi y WHERE y.aktif = true ORDER BY y.siraNo")
    List<YardimRedSebebi> findAllActive();
}


