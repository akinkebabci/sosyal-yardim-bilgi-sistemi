package com.sais.repository;

import com.sais.entity.TutanakBilgisi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TutanakBilgisiRepository extends JpaRepository<TutanakBilgisi, Long> {

  
    @Query("SELECT tb FROM TutanakBilgisi tb " +
           "LEFT JOIN FETCH tb.tahkikatPersonel " +
           "WHERE tb.muracaat.id = :muracaatId")
    Optional<TutanakBilgisi> findByMuracaatId(@Param("muracaatId") Long muracaatId);

   
    boolean existsByMuracaatId(Long muracaatId);

   
    @Query("SELECT tb FROM TutanakBilgisi tb WHERE tb.tahkikatPersonel.id = :personelId AND tb.tamamlandi = false")
    java.util.List<TutanakBilgisi> findTamamlanmamisTutanaklar(@Param("personelId") Long personelId);
    
    void deleteByMuracaatId(Long muracaatId);
}


