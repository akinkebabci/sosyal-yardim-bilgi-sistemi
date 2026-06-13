package com.sais.config;

import com.sais.repository.KisiRepository;
import com.sais.repository.YakinlikKoduRepository;
import com.sais.repository.YardimAltTipiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final YakinlikKoduRepository yakinlikKoduRepository;
    private final YardimAltTipiRepository yardimAltTipiRepository;
    private final KisiRepository kisiRepository;

    @Override
    public void run(String... args) {
        log.info("=".repeat(70));
        log.info("📦 VERİTABANI VERİ KONTROLÜ");
        log.info("=".repeat(70));

        try {
            printDataSummary();

            if (isDataMissing()) {
                log.warn("");
                log.warn("⚠️  EKSİK VERİ TESPİT EDİLDİ!");
                log.warn("⚠️  Lütfen SQL dosyasını çalıştırın:");
                log.warn("");
                log.warn("   psql -U postgres -d saisdb -f src/main/resources/database/master-data.sql");
                log.warn("");
                log.warn("   Detaylı bilgi: SQL_KULLANIM_KILAVUZU.md");
                log.warn("");
            } else {
            log.info("");
                log.info("✅ Tüm referans veriler hazır!");
            }
            
            log.info("=".repeat(70));

        } catch (Exception e) {
            log.error("❌ VERİ KONTROLÜ HATASI: {}", e.getMessage(), e);
        }
    }

    /**
     * Kritik verilerin eksik olup olmadığını kontrol eder
     */
    private boolean isDataMissing() {
        long yakinlikCount = yakinlikKoduRepository.count();
        long yardimCount = yardimAltTipiRepository.count();
        long kisiCount = kisiRepository.count();
        
        return yakinlikCount < 12 || yardimCount < 17 || kisiCount < 10;
    }

    /**
     * Veritabanındaki mevcut veri durumunu raporlar
     */
    private void printDataSummary() {
        long yakinlikCount = yakinlikKoduRepository.count();
        long yardimCount = yardimAltTipiRepository.count();
        long kisiCount = kisiRepository.count();
        
        log.info("");
        log.info("📊 MEVCUT VERİ DURUMU:");
        log.info("   Yakınlık Kodları      : {} / 12", yakinlikCount);
        log.info("   Yardım Alt Tipleri    : {} / 17", yardimCount);
        log.info("   Kişi Kayıtları        : {} / 10+", kisiCount);
        log.info("");
    }
}
