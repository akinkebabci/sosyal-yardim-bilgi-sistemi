package com.sais.service;

import com.sais.entity.YardimKarar;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class YardimRaporService {

    @PersistenceContext
    private EntityManager entityManager;

    public Double calculateTotalYardimTutari(List<YardimKarar> yardimlar) {
        return yardimlar.stream()
            .filter(y -> y.getVerilenTutar() != null)
            .mapToDouble(YardimKarar::getVerilenTutar)
            .sum();
    }

    /**
     * Yeni rapor formatı için yardım kararlarını getirir
     * TİPİ, Yardım adı, DURUMU, AÇIKLAMA, YARDIM TUTARI, YARDIM SAYISI, YARDIM DİLİMİ kolonları
     */
    public List<YardimKarar> findYardimKararlariForNewReport() {
        List<YardimKarar> yardimlar = entityManager.createQuery(
            "SELECT yk FROM YardimKarar yk " +
            "LEFT JOIN FETCH yk.muracaat m " +
            "LEFT JOIN FETCH m.basvuruSahibi bs " +
            "LEFT JOIN FETCH yk.yardimAltTipi " +
            "LEFT JOIN FETCH yk.yardimDilimi " +
            "WHERE yk.aktif = true " +
            "ORDER BY yk.kararTarihi DESC, yk.olusturmaTarihi DESC",
            YardimKarar.class)
            .getResultList();
        
        // Karar numaralarını otomatik oluştur
        generateKararNumaralari(yardimlar);
        
        return yardimlar;
    }
    
    /**
     * Yardım kararları için karar numaralarını otomatik oluşturur
     */
    private void generateKararNumaralari(List<YardimKarar> yardimlar) {
        int kararSayisi = 1;
        
        for (YardimKarar yardim : yardimlar) {
            if (yardim.getKararTarihi() == null) {
                yardim.setKararTarihi(java.time.LocalDate.now());
            }
            
            if (yardim.getKararSayisi() == null) {
                yardim.setKararSayisi(kararSayisi++);
            }
        }
    }
}
