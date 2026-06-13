package com.sais.repository;

import com.sais.entity.AileMaddiDurum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AileMaddiDurumRepository extends JpaRepository<AileMaddiDurum, Long> {

  
    @Query("SELECT amd FROM AileMaddiDurum amd " +
           "LEFT JOIN FETCH amd.gayrimenkulBilgisi " +
           "WHERE amd.muracaat.id = :muracaatId")
    Optional<AileMaddiDurum> findByMuracaatId(@Param("muracaatId") Long muracaatId);

  
    boolean existsByMuracaatId(Long muracaatId);
    
    void deleteByMuracaatId(Long muracaatId);
}


