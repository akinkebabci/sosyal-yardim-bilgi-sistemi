package com.sais.repository;

import com.sais.entity.Personel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PersonelRepository extends JpaRepository<Personel, Long> {

    
    Optional<Personel> findByTcKimlikNo(String tcKimlikNo);

    
    @Query("SELECT p FROM Personel p WHERE p.tahkikatYetkili = true AND p.aktif = true ORDER BY p.ad, p.soyad")
    List<Personel> findTahkikatYetkililer();

    
    @Query("SELECT p FROM Personel p WHERE p.komisyonUyesi = true AND p.aktif = true ORDER BY p.ad, p.soyad")
    List<Personel> findKomisyonUyeleri();

    
    @Query("SELECT p FROM Personel p WHERE p.aktif = true ORDER BY p.ad, p.soyad")
    List<Personel> findAktifPersoneller();
}


