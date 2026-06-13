package com.sais.repository;

import com.sais.entity.BorcBilgisi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BorcBilgisiRepository extends JpaRepository<BorcBilgisi, Long> {

   
    @Query("SELECT b FROM BorcBilgisi b WHERE b.aileMaddiDurum.id = :maddiDurumId")
    List<BorcBilgisi> findByAileMaddiDurumId(@Param("maddiDurumId") Long maddiDurumId);
}

