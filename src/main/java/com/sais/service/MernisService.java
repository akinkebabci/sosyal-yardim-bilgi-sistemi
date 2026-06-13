package com.sais.service;

import com.sais.dto.MernisKisiDTO;
import com.sais.dto.MernisYakinDTO;
import com.sais.exception.MernisServiceException;
import com.sais.util.MessageUtil;
import com.sais.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class MernisService {

    @Value("${sais.mernis.simulate:true}")
    private boolean simulate;

    @Value("${sais.gebze.check-address:true}")
    private boolean checkGebzeAddress;
    
    private final java.util.Map<String, java.time.LocalDateTime> mernisCache = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Map<String, MernisKisiDTO> mernisDataCache = new java.util.concurrent.ConcurrentHashMap<>();

    public MernisKisiDTO sorgula(String tcKimlikNo) {
        if (!simulate && !ValidationUtil.isValidTcKimlikNo(tcKimlikNo)) {
            MessageUtil.showErrorMessage("Hata", "Geçersiz TC Kimlik Numarası!");
            throw new MernisServiceException("Geçersiz TC Kimlik Numarası: " + tcKimlikNo);
        }

        java.time.LocalDateTime cachedTime = mernisCache.get(tcKimlikNo);
        if (cachedTime != null && cachedTime.toLocalDate().equals(java.time.LocalDate.now())) {
            MernisKisiDTO cachedData = mernisDataCache.get(tcKimlikNo);
            if (cachedData != null) {
                MessageUtil.showInfoMessage("Cache", "Kişi bilgileri bugün sorgulanmış, kayıtlı veriden getiriliyor.");
                return cachedData;
            }
        }

        if (simulate) {
            MernisKisiDTO result = simulateKisiSorgusu(tcKimlikNo);
            mernisCache.put(tcKimlikNo, java.time.LocalDateTime.now());
            mernisDataCache.put(tcKimlikNo, result);
            return result;
        } else {
            throw new UnsupportedOperationException("Gerçek MERNİS entegrasyonu henüz yapılmadı");
        }
    }

    public List<MernisYakinDTO> yakinlariSorgula(String tcKimlikNo) {

        
        if (!simulate && !ValidationUtil.isValidTcKimlikNo(tcKimlikNo)) {
            MessageUtil.showErrorMessage("Hata", "Geçersiz TC Kimlik Numarası!");
            throw new MernisServiceException("Geçersiz TC Kimlik Numarası: " + tcKimlikNo);
        }

        if (simulate) {
            return simulateYakinlikSorgusu(tcKimlikNo);
        } else {
            
            throw new UnsupportedOperationException("Gerçek Yakınlık Servisi entegrasyonu henüz yapılmadı");
        }
    }

    
    private MernisKisiDTO simulateKisiSorgusu(String tcKimlikNo) {
        MessageUtil.showInfoMessage("MERNİS Simülasyon", 
            "🔄 MERNİS servisine bağlanıldı, kişi bilgileri sorgulanıyor...");
        
        log.info("   ├─ MERNİS servisine bağlanılıyor...");

       
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        
        int sonRakam = Character.getNumericValue(tcKimlikNo.charAt(10));
        boolean gebzeIkameti = sonRakam < 7; // %70 Gebze'de

        
        String[] erkekIsimler = {"Ahmet", "Mehmet", "Mustafa", "Ali", "Hasan", "Hüseyin", "İbrahim", 
                                 "Emre", "Burak", "Serkan", "Can", "Efe", "Yusuf", "Cem", "Murat"};
        String[] kadinIsimler = {"Ayşe", "Fatma", "Emine", "Hatice", "Zeynep", "Elif", "Selin", 
                                "Derya", "Merve", "Büşra", "Eda", "Gül", "Yasemin", "Sevgi", "Nur"};
        String[] soyisimler = {"YILMAZ", "KAYA", "DEMİR", "ŞAHİN", "ÇELİK", "YILDIZ", "ÖZTÜRK", 
                              "AYDIN", "ARSLAN", "DOĞAN", "KURT", "KOÇ", "ASLAN", "POLAT", "KAPLAN"};
        String[] babaIsimleri = {"Mehmet", "Ali", "Hasan", "Ahmet", "Mustafa", "İbrahim", "Ömer"};
        String[] anneIsimleri = {"Ayşe", "Fatma", "Emine", "Zeynep", "Hatice", "Elif", "Meryem"};
        
        
        int seed = Integer.parseInt(tcKimlikNo.substring(0, 4));
        Random tcRandom = new Random(seed);
        
        boolean erkek = tcRandom.nextBoolean();
        String ad = erkek ? erkekIsimler[tcRandom.nextInt(erkekIsimler.length)] 
                         : kadinIsimler[tcRandom.nextInt(kadinIsimler.length)];
        String soyad = soyisimler[tcRandom.nextInt(soyisimler.length)];
        String babaAdi = babaIsimleri[tcRandom.nextInt(babaIsimleri.length)];
        String anneAdi = anneIsimleri[tcRandom.nextInt(anneIsimleri.length)];
        
        String il = gebzeIkameti ? "Kocaeli" : "İstanbul";
        String ilce = gebzeIkameti ? "Gebze" : "Kadıköy";
        String mahalle = gebzeIkameti ? "Cumhuriyet Mahallesi" : "Fenerbahçe Mahallesi";

        MernisKisiDTO kisi = MernisKisiDTO.builder()
                .tcKimlikNo(tcKimlikNo)
                .ad(ad)
                .soyad(soyad)
                .babaAdi(babaAdi)
                .anneAdi(anneAdi)
                .dogumTarihi(LocalDate.of(1960 + tcRandom.nextInt(43), 1 + tcRandom.nextInt(12), 1 + tcRandom.nextInt(28)))
                .dogumYeri("Kocaeli")
                .cinsiyet(erkek ? "E" : "K")
                .medeniDurum(tcRandom.nextBoolean() ? "Evli" : "Bekar")
                .adres(mahalle + " " + (100 + tcRandom.nextInt(500)) + " Sokak No: " + (1 + tcRandom.nextInt(99)))
                .il(il)
                .ilce(ilce)
                .mahalle(mahalle)
                .telefon("05" + String.format("%09d", tcRandom.nextInt(1000000000)))
                .build();

        log.info("MERNİS simülasyon sonucu: {} {} - İl: {}, İlçe: {}", 
                kisi.getAd(), kisi.getSoyad(), kisi.getIl(), kisi.getIlce());

       
        if (checkGebzeAddress && !ValidationUtil.isGebzeAddress(kisi.getIl(), kisi.getIlce())) {
            MessageUtil.showWarningMessage("Uyarı", 
                "Kişi Gebze dışında ikamet ediyor: " + kisi.getIl() + "/" + kisi.getIlce());
        } else {
            MessageUtil.showInfoMessage("Başarılı", 
                "MERNİS'ten kişi bilgileri başarıyla çekildi: " + kisi.getAd() + " " + kisi.getSoyad());
        }

        return kisi;
    }

    
    private List<MernisYakinDTO> simulateYakinlikSorgusu(String tcKimlikNo) {
        MessageUtil.showInfoMessage("MERNİS Yakınlık Servisi Simülasyon", 
            "Yakınlık servisi sorgulanıyor...");

        List<MernisYakinDTO> yakinlar = new ArrayList<>();

       
        String[] erkekIsimler = {"Ahmet", "Mehmet", "Mustafa", "Ali", "Hasan", "Emre", "Burak", "Yusuf"};
        String[] kadinIsimler = {"Ayşe", "Fatma", "Emine", "Zeynep", "Elif", "Selin", "Merve", "Gül"};
        String[] soyisimler = {"YILMAZ", "KAYA", "DEMİR", "ŞAHİN", "ÇELİK", "ÖZTÜRK", "DOĞAN"};
        
        
        int seed = Integer.parseInt(tcKimlikNo.substring(0, 4));
        Random tcRandom = new Random(seed);
        
        String aileSoyadi = soyisimler[tcRandom.nextInt(soyisimler.length)];

       
        Object[][] yakinlikBilgileri = {
            {"02", "Eşi", tcRandom.nextBoolean() ? "E" : "K", 30 + tcRandom.nextInt(20)},  
            {"05", "Oğlu", "E", 5 + tcRandom.nextInt(15)},     
            {"06", "Kızı", "K", 8 + tcRandom.nextInt(12)},     
            {"03", "Annesi", "K", 55 + tcRandom.nextInt(20)}   
        };

        int yakinSayisi = 2 + tcRandom.nextInt(3); 

        for (int i = 0; i < yakinSayisi && i < yakinlikBilgileri.length; i++) {
            Object[] yakinlik = yakinlikBilgileri[i];
            String yakinlikKodu = (String) yakinlik[0];
            String yakinlikAdi = (String) yakinlik[1];
            String cinsiyet = (String) yakinlik[2];
            int yas = (Integer) yakinlik[3];
            
            
            String yakinTc = String.format("%010d%d", tcRandom.nextInt(1000000000), tcRandom.nextInt(6));
            
            String ad = "E".equals(cinsiyet) 
                ? erkekIsimler[tcRandom.nextInt(erkekIsimler.length)]
                : kadinIsimler[tcRandom.nextInt(kadinIsimler.length)];

            yakinlar.add(MernisYakinDTO.builder()
                    .tcKimlikNo(yakinTc)
                    .ad(ad)
                    .soyad(aileSoyadi)
                    .dogumTarihi(LocalDate.now().minusYears(yas))
                    .yakinlikKodu(yakinlikKodu)
                    .yakinlikAdi(yakinlikAdi)
                    .build());
        }

        log.info("MERNİS Yakınlık Servisi simülasyon sonucu: {} yakın bulundu", yakinlar.size());
        MessageUtil.showInfoMessage("Başarılı", 
            "Yakınlık servisi sorgulandı: " + yakinlar.size() + " yakın bulundu");

        return yakinlar;
    }
}


