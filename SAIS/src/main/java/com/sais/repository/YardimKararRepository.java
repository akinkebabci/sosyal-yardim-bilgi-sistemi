package com.sais.repository;

import com.sais.entity.YardimKarar;
import com.sais.enums.YardimDurum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface YardimKararRepository extends JpaRepository<YardimKarar, Long> {

    
    @Query("SELECT yk FROM YardimKarar yk " +
           "LEFT JOIN FETCH yk.yardimAltTipi " +
           "WHERE yk.muracaat.id = :muracaatId " +
           "ORDER BY yk.olusturmaTarihi")
    List<YardimKarar> findByMuracaatId(@Param("muracaatId") Long muracaatId);

    
    @Query("SELECT yk FROM YardimKarar yk " +
           "LEFT JOIN FETCH yk.yardimAltTipi " +
           "WHERE yk.muracaat.id = :muracaatId AND yk.kesinlesti = true")
    List<YardimKarar> findKesinlesmisKararlar(@Param("muracaatId") Long muracaatId);

  
    @Query("SELECT yk FROM YardimKarar yk " +
           "LEFT JOIN FETCH yk.yardimAltTipi " +
           "WHERE yk.komisyonKararli = true AND yk.kesinlesti = true " +
           "ORDER BY yk.toplantiTarihi DESC")
    List<YardimKarar> findKomisyonKararliYardimlar();

    @Query("SELECT yk FROM YardimKarar yk " +
           "LEFT JOIN FETCH yk.yardimAltTipi " +
           "WHERE yk.komisyonKararli = false AND yk.kesinlesti = true " +
           "ORDER BY yk.kesinlesmeTarihi DESC")
    List<YardimKarar> findKomisyonsuzYardimlar();

    List<YardimKarar> findByYardimDurum(YardimDurum yardimDurum);

    @Query("SELECT MAX(yk.toplantiTarihi) FROM YardimKarar yk WHERE yk.toplantiTarihi IS NOT NULL AND yk.kesinlesti = true")
    Optional<LocalDate> findSonToplantiTarihi();
    
    void deleteByMuracaatId(Long muracaatId);

 
    @Query("SELECT yk FROM YardimKarar yk " +
           "LEFT JOIN FETCH yk.yardimAltTipi " +
           "WHERE yk.toplantiTarihi BETWEEN :baslangic AND :bitis AND yk.kesinlesti = true")
    List<YardimKarar> findByToplantiTarihiBetween(@Param("baslangic") LocalDate baslangic, @Param("bitis") LocalDate bitis);

    @Query("SELECT COUNT(yk) > 0 FROM YardimKarar yk WHERE yk.muracaat.id = :muracaatId AND yk.yardimAltTipi.id = :yardimAltTipiId")
    boolean existsByMuracaatIdAndYardimAltTipiId(@Param("muracaatId") Long muracaatId, @Param("yardimAltTipiId") Long yardimAltTipiId);

    @Query("SELECT MAX(yk.kararSayisi) FROM YardimKarar yk WHERE yk.kesinlesti = true AND yk.kararSayisi IS NOT NULL")
    Optional<Integer> findMaxKararSayisi();
}


