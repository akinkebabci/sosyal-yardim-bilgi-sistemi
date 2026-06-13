package com.sais.repository;

import com.sais.entity.TutanakGorsel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TutanakGorselRepository extends JpaRepository<TutanakGorsel, Long> {

   
    @Query("SELECT tg FROM TutanakGorsel tg WHERE tg.tutanakBilgisi.id = :tutanakId ORDER BY tg.siraNo, tg.olusturmaTarihi")
    List<TutanakGorsel> findByTutanakBilgisiId(@Param("tutanakId") Long tutanakId);

    
    long countByTutanakBilgisiId(Long tutanakId);

    
    Optional<TutanakGorsel> findByTutanakBilgisiIdAndDosyaAdi(Long tutanakId, String dosyaAdi);

   
    void deleteByTutanakBilgisiId(Long tutanakId);
}

