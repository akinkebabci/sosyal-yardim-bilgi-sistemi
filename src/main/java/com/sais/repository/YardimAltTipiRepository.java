package com.sais.repository;

import com.sais.entity.YardimAltTipi;
import com.sais.enums.YardimTipi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface YardimAltTipiRepository extends JpaRepository<YardimAltTipi, Long> {

    Optional<YardimAltTipi> findByKod(String kod);

    List<YardimAltTipi> findByYardimTipiOrderBySiraNo(YardimTipi yardimTipi);

    @Query("SELECT y FROM YardimAltTipi y WHERE y.komisyonKararli = true AND y.aktif = true ORDER BY y.siraNo")
    List<YardimAltTipi> findKomisyonKararliYardimlar();

    @Query("SELECT y FROM YardimAltTipi y WHERE y.komisyonKararli = false AND y.aktif = true ORDER BY y.siraNo")
    List<YardimAltTipi> findKomisyonsuzYardimlar();

    @Query("SELECT y FROM YardimAltTipi y WHERE y.yardimTipi = 'NAKDI' AND y.aktif = true ORDER BY y.siraNo")
    List<YardimAltTipi> findNakdiYardimlar();

    @Query("SELECT y FROM YardimAltTipi y WHERE y.yardimTipi = 'AYNI' AND y.aktif = true ORDER BY y.siraNo")
    List<YardimAltTipi> findAyniYardimlar();

    @Query("SELECT y FROM YardimAltTipi y WHERE y.aktif = true ORDER BY y.yardimTipi, y.siraNo")
    List<YardimAltTipi> findAllActive();

    @Query("SELECT y FROM YardimAltTipi y WHERE y.komisyonKararli = :komisyonKararli AND y.aktif = true ORDER BY y.siraNo")
    List<YardimAltTipi> findByKomisyonKararli(@Param("komisyonKararli") Boolean komisyonKararli);
}


