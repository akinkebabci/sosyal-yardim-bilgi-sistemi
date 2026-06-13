package com.sais.repository;

import com.sais.entity.MuracaatYardimTalep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MuracaatYardimTalepRepository extends JpaRepository<MuracaatYardimTalep, Long> {

    @Query("SELECT m FROM MuracaatYardimTalep m WHERE m.muracaat.id = :muracaatId ORDER BY m.siraNo")
    List<MuracaatYardimTalep> findByMuracaatId(@Param("muracaatId") Long muracaatId);

  
    @Query("SELECT COUNT(m) > 0 FROM MuracaatYardimTalep m WHERE m.muracaat.id = :muracaatId AND m.yardimAltTipi.id = :yardimAltTipiId")
    boolean existsByMuracaatIdAndYardimAltTipiId(@Param("muracaatId") Long muracaatId, @Param("yardimAltTipiId") Long yardimAltTipiId);
    
    void deleteByMuracaatId(Long muracaatId);
}

