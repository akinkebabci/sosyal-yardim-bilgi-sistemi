-- =========================================================================
-- SOSYAL YARDIM BİLGİ SİSTEMİ (SAIS)
-- MASTER REFERANS VERİ YÜKLEME SCRIPT'İ
-- =========================================================================
-- Bu dosya tüm lookup tablolarını, örnek kişileri ve hesap bilgilerini içerir.
-- DataInitializer.java yerine bu SQL çalıştırılabilir.
-- MySQL uyumlu versiyon
-- =========================================================================

-- 1. YAKINLIK KODLARI (MERNİS Standartları)
-- =========================================================================
INSERT IGNORE INTO yakinlik_kodu (kod, adi, aciklama, sira_no, aktif, olusturma_tarihi, olusturan) VALUES
('01', 'Kendisi', 'Kendisi', 1, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('02', 'Eşi', 'Eşi', 2, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('03', 'Annesi', 'Annesi', 3, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('04', 'Babası', 'Babası', 4, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('05', 'Oğlu', 'Oğlu', 5, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('06', 'Kızı', 'Kızı', 6, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('07', 'Kardeşi', 'Kardeşi', 7, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('08', 'Büyükanne', 'Büyükanne', 8, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('09', 'Büyükbaba', 'Büyükbaba', 9, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('10', 'Torun', 'Torun', 10, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('11', 'Gelin', 'Gelin', 11, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('12', 'Damat', 'Damat', 12, true, CURRENT_TIMESTAMP, 'SYSTEM');

-- 2. MESLEK TANIMLARI
-- =========================================================================
INSERT IGNORE INTO meslek (kod, adi, aktif, olusturma_tarihi, olusturan) VALUES
('M001', 'İşsiz', true, CURRENT_TIMESTAMP, 'SYSTEM'),
('M002', 'İşçi', true, CURRENT_TIMESTAMP, 'SYSTEM'),
('M003', 'Memur', true, CURRENT_TIMESTAMP, 'SYSTEM'),
('M004', 'Öğretmen', true, CURRENT_TIMESTAMP, 'SYSTEM'),
('M005', 'Doktor', true, CURRENT_TIMESTAMP, 'SYSTEM'),
('M006', 'Hemşire', true, CURRENT_TIMESTAMP, 'SYSTEM'),
('M007', 'Mühendis', true, CURRENT_TIMESTAMP, 'SYSTEM'),
('M008', 'Serbest Meslek', true, CURRENT_TIMESTAMP, 'SYSTEM'),
('M009', 'Esnaf', true, CURRENT_TIMESTAMP, 'SYSTEM'),
('M010', 'Emekli', true, CURRENT_TIMESTAMP, 'SYSTEM'),
('M011', 'Ev Hanımı', true, CURRENT_TIMESTAMP, 'SYSTEM'),
('M012', 'Öğrenci', true, CURRENT_TIMESTAMP, 'SYSTEM'),
('M013', 'Çiftçi', true, CURRENT_TIMESTAMP, 'SYSTEM'),
('M014', 'Şoför', true, CURRENT_TIMESTAMP, 'SYSTEM'),
('M015', 'Güvenlik Görevlisi', true, CURRENT_TIMESTAMP, 'SYSTEM');

-- 3. ÖZEL STATÜ TANIMLARI
-- =========================================================================
INSERT IGNORE INTO ozel_statu (kod, adi, aciklama, oncelik_puani, aktif, olusturma_tarihi, olusturan) VALUES
('OS001', 'Şehit Yakını', 'Şehit ailesi', 100, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('OS002', 'Gazi Yakını', 'Gazi ailesi', 90, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('OS003', 'Yetim', 'Anne veya baba kaybı', 80, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('OS004', 'Öksüz', 'Anne ve baba kaybı', 85, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('OS005', 'Dul', 'Eş kaybı', 70, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('OS006', 'Koruma Altında', 'ASPB koruma altında', 60, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('OS007', 'Mülteci', 'Geçici koruma statüsü', 50, true, CURRENT_TIMESTAMP, 'SYSTEM');

-- 4. ENGELLİ TİPİ TANIMLARI (Ana Tipler + Alt Tipler)
-- =========================================================================
-- Ana Tipler
INSERT IGNORE INTO engelli_tipi (kod, adi, ust_tip_id, sira_no, aktif, olusturma_tarihi, olusturan) VALUES
('ET001', 'Bedensel Engelli', NULL, 1, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('ET002', 'Görme Engelli', NULL, 2, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('ET003', 'İşitme Engelli', NULL, 3, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('ET004', 'Zihinsel Engelli', NULL, 4, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('ET005', 'Ruhsal/Psikolojik Engelli', NULL, 5, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('ET006', 'Süreğen Hastalık', NULL, 6, true, CURRENT_TIMESTAMP, 'SYSTEM');

-- Alt Tipler (ust_tip_id daha sonra güncellenecek)
INSERT IGNORE INTO engelli_tipi (kod, adi, sira_no, aktif, olusturma_tarihi, olusturan) VALUES
('ET001-01', 'Ortopedik Engelli', 7, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('ET001-02', 'Omurilik Felci', 8, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('ET002-01', 'Az Gören', 9, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('ET002-02', 'Tamamen Görme Engelli', 10, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('ET003-01', 'Az İşiten', 11, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('ET003-02', 'Tamamen İşitme Engelli', 12, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('ET004-01', 'Hafif Zihinsel Engelli', 13, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('ET004-02', 'Orta Zihinsel Engelli', 14, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('ET004-03', 'Ağır Zihinsel Engelli', 15, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('ET005-01', 'Otizm', 16, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('ET005-02', 'Down Sendromu', 17, true, CURRENT_TIMESTAMP, 'SYSTEM');

-- Alt tiplerin üst tiplerini bağlama (MySQL uyumlu - derived table kullanımı)
UPDATE engelli_tipi SET ust_tip_id = (SELECT id FROM (SELECT id FROM engelli_tipi WHERE kod = 'ET001') AS temp) WHERE kod LIKE 'ET001-%';
UPDATE engelli_tipi SET ust_tip_id = (SELECT id FROM (SELECT id FROM engelli_tipi WHERE kod = 'ET002') AS temp) WHERE kod LIKE 'ET002-%';
UPDATE engelli_tipi SET ust_tip_id = (SELECT id FROM (SELECT id FROM engelli_tipi WHERE kod = 'ET003') AS temp) WHERE kod LIKE 'ET003-%';
UPDATE engelli_tipi SET ust_tip_id = (SELECT id FROM (SELECT id FROM engelli_tipi WHERE kod = 'ET004') AS temp) WHERE kod LIKE 'ET004-%';
UPDATE engelli_tipi SET ust_tip_id = (SELECT id FROM (SELECT id FROM engelli_tipi WHERE kod = 'ET005') AS temp) WHERE kod LIKE 'ET005-%';

-- 5. HASTALIK TANIMLARI
-- =========================================================================
INSERT IGNORE INTO hastalik (kod, adi, kronik, aktif, olusturma_tarihi, olusturan) VALUES
('H001', 'Şeker Hastalığı (Diyabet)', true, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('H002', 'Tansiyon (Hipertansiyon)', true, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('H003', 'Kalp Hastalığı', true, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('H004', 'Astım', true, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('H005', 'KOAH', true, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('H006', 'Böbrek Yetmezliği', true, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('H007', 'Kanser', true, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('H008', 'Epilepsi', true, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('H009', 'Multiple Skleroz (MS)', true, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('H010', 'Parkinson', true, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('H011', 'Alzheimer', true, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('H012', 'Talasemi', true, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('H013', 'Hemofili', true, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('H014', 'Sedef Hastalığı', true, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('H015', 'Romatizma', true, true, CURRENT_TIMESTAMP, 'SYSTEM');

-- 6. GELİR TÜRÜ TANIMLARI
-- =========================================================================
INSERT IGNORE INTO gelir_turu (kod, adi, aciklama, sira_no, aktif, olusturma_tarihi, olusturan) VALUES
('GT001', 'Maaş', 'Aylık maaş geliri', 1, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('GT002', 'Emekli Maaşı', 'Emeklilik maaşı', 2, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('GT003', 'Kira Geliri', 'Gayrimenkulden kira geliri', 3, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('GT004', 'Ticari Kazanç', 'Ticari faaliyetten gelir', 4, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('GT005', 'Sosyal Yardım', 'Devlet/kuruluşlardan sosyal yardım', 5, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('GT006', 'Nafaka', 'Nafaka geliri', 6, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('GT007', 'Engelli Maaşı', 'Engelliler için aylık', 7, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('GT008', 'Yaşlılık Aylığı', '65 yaş üstü aylık', 8, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('GT009', 'Diğer', 'Diğer gelir kaynakları', 9, true, CURRENT_TIMESTAMP, 'SYSTEM');

-- 7. BORÇ TÜRÜ TANIMLARI
-- =========================================================================
INSERT IGNORE INTO borc_turu (kod, adi, aciklama, sira_no, aktif, olusturma_tarihi, olusturan) VALUES
('BT001', 'Elektrik', 'Elektrik faturası', 1, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('BT002', 'Su', 'Su faturası', 2, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('BT003', 'Doğalgaz', 'Doğalgaz faturası', 3, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('BT004', 'Kira', 'Aylık kira borcu', 4, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('BT005', 'Kredi Kartı', 'Kredi kartı borcu', 5, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('BT006', 'Banka Kredisi', 'Tüketici kredisi', 6, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('BT007', 'İnternet', 'İnternet faturası', 7, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('BT008', 'Telefon', 'Telefon faturası', 8, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('BT009', 'Eğitim Masrafı', 'Okul, kurs vb.', 9, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('BT010', 'Sağlık Masrafı', 'İlaç, tedavi vb.', 10, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('BT011', 'Diğer', 'Diğer borç/giderler', 11, true, CURRENT_TIMESTAMP, 'SYSTEM');

-- 8. YARDIM DİLİMİ TANIMLARI
-- =========================================================================
INSERT IGNORE INTO yardim_dilimi (kod, adi, sira_no, aktif, olusturma_tarihi, olusturan) VALUES
('D01', 'Birinci Dilim', 1, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('D02', 'İkinci Dilim', 2, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('D03', 'Üçüncü Dilim', 3, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('D04', 'Dördüncü Dilim', 4, true, CURRENT_TIMESTAMP, 'SYSTEM');

-- 9. YARDIM DÖNEMİ TANIMLARI (1-12 ay)
-- =========================================================================
INSERT IGNORE INTO yardim_donemi (adi, ay_sayisi, aktif, olusturma_tarihi, olusturan) VALUES
('1 Aylık', 1, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('2 Aylık', 2, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('3 Aylık', 3, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('4 Aylık', 4, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('5 Aylık', 5, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('6 Aylık', 6, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('7 Aylık', 7, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('8 Aylık', 8, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('9 Aylık', 9, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('10 Aylık', 10, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('11 Aylık', 11, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('12 Aylık', 12, true, CURRENT_TIMESTAMP, 'SYSTEM');

-- 10. YARDIM RED SEBEBİ TANIMLARI
-- =========================================================================
INSERT IGNORE INTO yardim_red_sebebi (kod, adi, aciklama, sira_no, aktif, olusturma_tarihi, olusturan) VALUES
('RS001', 'Gelir Düzeyi Yüksek', 'Gelir seviyesi yardım almak için uygun değil', 1, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('RS002', 'Evrak Eksikliği', 'Gerekli belgeler eksik', 2, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('RS003', 'Gebze Dışı İkamet', 'Başvuru sahibi Gebze dışında ikamet ediyor', 3, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('RS004', 'Sahte Beyan', 'Yanlış veya eksik bilgi verme', 4, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('RS005', 'Başka Kuruluştan Alıyor', 'Başka bir yerden benzer yardım alıyor', 5, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('RS006', 'Kriterlere Uymuyor', 'Yardım kriterlere uygun değil', 6, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('RS007', 'Bütçe Yetersizliği', 'Yardım bütçesi tükendi', 7, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('RS008', 'Diğer', 'Diğer sebepler', 8, true, CURRENT_TIMESTAMP, 'SYSTEM');

-- 11. YARDIM ALT TİPİ TANIMLARI
-- =========================================================================
-- Komisyonlu Nakdi Yardımlar
INSERT IGNORE INTO yardim_alt_tipi (kod, adi, komisyon_kararli, yardim_tipi, aciklama, birim, sira_no, aktif, olusturma_tarihi, olusturan) VALUES
('NAKDI-001', 'Genel Ekonomik Destek', true, 'NAKDI', 'Komisyonlu nakdi yardım - Genel ihtiyaçlar', 'TL', 1, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('NAKDI-002', 'Kira Yardımı', true, 'NAKDI', 'Komisyonlu nakdi yardım - Kira ödemesi', 'TL', 2, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('NAKDI-003', 'Eğitim Yardımı', true, 'NAKDI', 'Komisyonlu nakdi yardım - Eğitim masrafları', 'TL', 3, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('NAKDI-004', 'Sağlık Yardımı', true, 'NAKDI', 'Komisyonlu nakdi yardım - Tedavi masrafları', 'TL', 4, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('NAKDI-005', 'Engelli Bakım Desteği', true, 'NAKDI', 'Komisyonlu nakdi yardım - Engelli bakım', 'TL', 5, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('NAKDI-006', 'Yaşlı Bakım Desteği', true, 'NAKDI', 'Komisyonlu nakdi yardım - Yaşlı bakım', 'TL', 6, true, CURRENT_TIMESTAMP, 'SYSTEM');

-- Komisyonlu Ayni Yardımlar
INSERT IGNORE INTO yardim_alt_tipi (kod, adi, komisyon_kararli, yardim_tipi, aciklama, birim, sira_no, aktif, olusturma_tarihi, olusturan) VALUES
('AYNI-001', 'Gıda Kolisi', true, 'AYNI', 'Komisyonlu ayni yardım - Gıda ürünleri', 'Adet', 7, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('AYNI-002', 'Kömür Yardımı', true, 'AYNI', 'Komisyonlu ayni yardım - Kış yakıtı', 'Adet', 8, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('AYNI-003', 'Beyaz Eşya', true, 'AYNI', 'Komisyonlu ayni yardım - Buzdolabı, çamaşır makinesi', 'Adet', 9, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('AYNI-004', 'Mobilya', true, 'AYNI', 'Komisyonlu ayni yardım - Yatak, dolap, masa', 'Adet', 10, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('AYNI-005', 'Kırtasiye Yardımı', true, 'AYNI', 'Komisyonlu ayni yardım - Okul malzemeleri', 'Adet', 11, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('AYNI-006', 'Giyim Yardımı', true, 'AYNI', 'Komisyonlu ayni yardım - Giyecek', 'Adet', 12, true, CURRENT_TIMESTAMP, 'SYSTEM');

-- Komisyonsuz Ayni Yardımlar (Nakdi komisyonsuz VERİLMEZ - senaryo kuralı)
INSERT IGNORE INTO yardim_alt_tipi (kod, adi, komisyon_kararli, yardim_tipi, aciklama, birim, sira_no, aktif, olusturma_tarihi, olusturan) VALUES
('AYNI-K001', 'Acil Gıda Paketi', false, 'AYNI', 'Komisyonsuz ayni yardım - Acil durum paketi', 'Adet', 13, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('AYNI-K002', 'Hijyen Paketi', false, 'AYNI', 'Komisyonsuz ayni yardım - Temizlik malzemeleri', 'Adet', 14, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('AYNI-K003', 'Bebek Paketi', false, 'AYNI', 'Komisyonsuz ayni yardım - Bebek bezi, mama', 'Adet', 15, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('AYNI-K004', 'İlaç Kuponu', false, 'AYNI', 'Komisyonsuz ayni yardım - Eczane kuponu', 'Adet', 16, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('AYNI-K005', 'Ekmek Kuponu', false, 'AYNI', 'Komisyonsuz ayni yardım - Günlük ekmek', 'Adet', 17, true, CURRENT_TIMESTAMP, 'SYSTEM');

-- 12. PERSONEL KAYITLARI
-- =========================================================================
INSERT IGNORE INTO personel (tc_kimlik_no, ad, soyad, telefon, email, unvan, departman, tahkikat_yetkili, komisyon_uyesi, aktif, olusturma_tarihi, olusturan) VALUES
('11111111110', 'Ahmet', 'YILMAZ', '5551234567', 'ahmet.yilmaz@gebze.bel.tr', 'Sosyal Yardım Uzmanı', 'Sosyal Hizmetler', true, false, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('22222222220', 'Ayşe', 'KAYA', '5559876543', 'ayse.kaya@gebze.bel.tr', 'Sosyal Çalışmacı', 'Sosyal Hizmetler', true, false, true, CURRENT_TIMESTAMP, 'SYSTEM'),
('33333333330', 'Mehmet', 'DEMİR', '5551112233', 'mehmet.demir@gebze.bel.tr', 'Müdür', 'Sosyal Hizmetler', false, true, true, CURRENT_TIMESTAMP, 'SYSTEM');

-- 13. KİŞİ KAYITLARI (Gerçekçi test verileri)
-- =========================================================================
-- GEBZE İÇİ KİŞİLER (TC son hanesi 0-5: Gebze içi simülasyon kuralı)
INSERT IGNORE INTO kisi (tc_kimlik_no, ad, soyad, baba_adi, anne_adi, dogum_tarihi, dogum_yeri, cinsiyet, adres, il, ilce, telefon, gebze_ikameti, sgk_durum, ogrenim_durum, son_mernis_sorgu_tarihi, mernis_guncelleme_sayisi, olusturma_tarihi, olusturan) VALUES
('24151827460', 'Fatma', 'YILMAZ', 'Mehmet', 'Ayşe', '1985-03-15', 'İstanbul', 'K', 'Güzeller Mahallesi No:45 Gebze', 'Kocaeli', 'Gebze', '5551112233', true, 'SSK', 'LISE', CURRENT_DATE, 1, CURRENT_TIMESTAMP, 'SYSTEM'),
('35678912340', 'Ali', 'KAYA', 'Hasan', 'Zeynep', '1978-07-22', 'Ankara', 'E', 'Aydıntepe Mahallesi No:12 Gebze', 'Kocaeli', 'Gebze', '5552223344', true, 'EMEKLI_SANDIGI', 'LISANS', CURRENT_DATE, 1, CURRENT_TIMESTAMP, 'SYSTEM'),
('18956234520', 'Zeynep', 'ÇELİK', 'Ahmet', 'Hatice', '1990-11-08', 'İzmir', 'K', 'Cumhuriyet Mahallesi No:78 Gebze', 'Kocaeli', 'Gebze', '5553334455', true, 'BAG_KUR', 'ORTAOKUL', CURRENT_DATE, 1, CURRENT_TIMESTAMP, 'SYSTEM'),
('42789654310', 'Mustafa', 'ŞAHİN', 'İbrahim', 'Emine', '1965-05-30', 'Kocaeli', 'E', 'Eskihisar Mahallesi No:23 Gebze', 'Kocaeli', 'Gebze', '5554445566', true, 'EMEKLI_SANDIGI', 'ILKOKUL', CURRENT_DATE, 1, CURRENT_TIMESTAMP, 'SYSTEM'),
('56234789120', 'Emine', 'YILDIZ', 'Ömer', 'Fatma', '1995-09-12', 'Bursa', 'K', 'Kirazpınar Mahallesi No:56 Gebze', 'Kocaeli', 'Gebze', '5555556677', true, 'YOK', 'LISE', CURRENT_DATE, 1, CURRENT_TIMESTAMP, 'SYSTEM'),
('67123456780', 'Hasan', 'ÖZTÜRK', 'Ali', 'Zeynep', '1982-01-25', 'Kocaeli', 'E', 'Adnan Menderes Mahallesi Gebze', 'Kocaeli', 'Gebze', '5556667788', true, 'SSK', 'ON_LISANS', CURRENT_DATE, 1, CURRENT_TIMESTAMP, 'SYSTEM'),
('78945612300', 'Hatice', 'AYDIN', 'Mustafa', 'Ayşe', '1988-12-03', 'Sakarya', 'K', 'Karadeniz Mahallesi No:34 Gebze', 'Kocaeli', 'Gebze', '5557778899', true, 'YESIL_KART', 'OKUR_YAZAR', CURRENT_DATE, 1, CURRENT_TIMESTAMP, 'SYSTEM'),
('89456723410', 'İbrahim', 'ARSLAN', 'Mehmet', 'Emine', '1970-04-18', 'Ankara', 'E', 'Çınar Mahallesi No:89 Gebze', 'Kocaeli', 'Gebze', '5558889900', true, 'BAG_KUR', 'ILKOKUL', CURRENT_DATE, 1, CURRENT_TIMESTAMP, 'SYSTEM'),
('12345678920', 'Elif', 'DOĞAN', 'Hüseyin', 'Fatma', '1998-06-27', 'İstanbul', 'K', 'Pelitli Mahallesi No:67 Gebze', 'Kocaeli', 'Gebze', '5559990011', true, 'YOK', 'LISANS', CURRENT_DATE, 1, CURRENT_TIMESTAMP, 'SYSTEM'),
('23456789030', 'Hüseyin', 'KURT', 'Ali', 'Zeynep', '1975-08-14', 'Kocaeli', 'E', 'Hürriyet Mahallesi No:45 Gebze', 'Kocaeli', 'Gebze', '5550011122', true, 'SSK', 'ORTAOKUL', CURRENT_DATE, 1, CURRENT_TIMESTAMP, 'SYSTEM');

-- GEBZE DIŞI KİŞİLER (TC son hanesi 6-9: Gebze dışı - MERNİS red kontrolü için)
INSERT IGNORE INTO kisi (tc_kimlik_no, ad, soyad, baba_adi, anne_adi, dogum_tarihi, dogum_yeri, cinsiyet, adres, il, ilce, telefon, gebze_ikameti, sgk_durum, ogrenim_durum, son_mernis_sorgu_tarihi, mernis_guncelleme_sayisi, olusturma_tarihi, olusturan) VALUES
('98765432106', 'Emre', 'YILDIRIM', 'Mustafa', 'Ayşe', '1992-02-10', 'İstanbul', 'E', 'Kadıköy Mahallesi No:123', 'İstanbul', 'Kadıköy', '5551234321', false, 'SSK', 'LISANS', CURRENT_DATE, 1, CURRENT_TIMESTAMP, 'SYSTEM'),
('87654321097', 'Derya', 'ÖZKAN', 'Ahmet', 'Fatma', '1987-05-20', 'Ankara', 'K', 'Çankaya Mahallesi No:45', 'Ankara', 'Çankaya', '5552345432', false, 'BAG_KUR', 'YUKSEK_LISANS', CURRENT_DATE, 1, CURRENT_TIMESTAMP, 'SYSTEM'),
('76543210988', 'Burak', 'AKTAŞ', 'Hasan', 'Zeynep', '1980-09-08', 'İzmir', 'E', 'Karşıyaka Mahallesi No:67', 'İzmir', 'Karşıyaka', '5553456543', false, 'EMEKLI_SANDIGI', 'LISE', CURRENT_DATE, 1, CURRENT_TIMESTAMP, 'SYSTEM'),
('65432109879', 'Selin', 'POLAT', 'Mehmet', 'Emine', '1993-11-15', 'Bursa', 'K', 'Nilüfer Mahallesi No:89', 'Bursa', 'Nilüfer', '5554567654', false, 'YOK', 'ORTAOKUL', CURRENT_DATE, 1, CURRENT_TIMESTAMP, 'SYSTEM'),
('54321098766', 'Serkan', 'KAPLAN', 'İbrahim', 'Hatice', '1968-03-28', 'Antalya', 'E', 'Muratpaşa Mahallesi No:12', 'Antalya', 'Muratpaşa', '5555678765', false, 'EMEKLI_SANDIGI', 'ILKOKUL', CURRENT_DATE, 1, CURRENT_TIMESTAMP, 'SYSTEM');

-- 14. HESAP BİLGİSİ (IBAN) KAYITLARI
-- =========================================================================
-- Gebze içi kişiler için IBAN bilgileri (sadece SQL'de tanımlı TC No'lar için)
INSERT IGNORE INTO hesap_bilgisi (kisi_id, banka_adi, iban, hesap_sahibi_adi, varsayilan, aktif, olusturma_tarihi, olusturan)
SELECT k.id, 'Ziraat Bankası', 'TR330006100519786457841326', 'Fatma YILMAZ', true, true, CURRENT_TIMESTAMP, 'SYSTEM'
FROM kisi k WHERE k.tc_kimlik_no = '24151827460' AND NOT EXISTS (SELECT 1 FROM hesap_bilgisi WHERE kisi_id = k.id)
UNION ALL
SELECT k.id, 'Halkbank', 'TR120001200934800019850016', 'Ali KAYA', true, true, CURRENT_TIMESTAMP, 'SYSTEM'
FROM kisi k WHERE k.tc_kimlik_no = '35678912340' AND NOT EXISTS (SELECT 1 FROM hesap_bilgisi WHERE kisi_id = k.id)
UNION ALL
SELECT k.id, 'Vakıfbank', 'TR150001500158007301234567', 'Zeynep ÇELİK', true, true, CURRENT_TIMESTAMP, 'SYSTEM'
FROM kisi k WHERE k.tc_kimlik_no = '18956234520' AND NOT EXISTS (SELECT 1 FROM hesap_bilgisi WHERE kisi_id = k.id)
UNION ALL
SELECT k.id, 'İş Bankası', 'TR640006400000112340056789', 'Mustafa ŞAHİN', true, true, CURRENT_TIMESTAMP, 'SYSTEM'
FROM kisi k WHERE k.tc_kimlik_no = '42789654310' AND NOT EXISTS (SELECT 1 FROM hesap_bilgisi WHERE kisi_id = k.id)
UNION ALL
SELECT k.id, 'Akbank', 'TR460004600232888001298765', 'Emine YILDIZ', true, true, CURRENT_TIMESTAMP, 'SYSTEM'
FROM kisi k WHERE k.tc_kimlik_no = '56234789120' AND NOT EXISTS (SELECT 1 FROM hesap_bilgisi WHERE kisi_id = k.id)
UNION ALL
SELECT k.id, 'Garanti BBVA', 'TR370006200019200006789123', 'Hasan ÖZTÜRK', true, true, CURRENT_TIMESTAMP, 'SYSTEM'
FROM kisi k WHERE k.tc_kimlik_no = '67123456780' AND NOT EXISTS (SELECT 1 FROM hesap_bilgisi WHERE kisi_id = k.id)
UNION ALL
SELECT k.id, 'Yapı Kredi', 'TR670006700100000022334455', 'Hatice AYDIN', true, true, CURRENT_TIMESTAMP, 'SYSTEM'
FROM kisi k WHERE k.tc_kimlik_no = '78945612300' AND NOT EXISTS (SELECT 1 FROM hesap_bilgisi WHERE kisi_id = k.id)
UNION ALL
SELECT k.id, 'QNB Finansbank', 'TR110001100000000111122233', 'İbrahim ARSLAN', true, true, CURRENT_TIMESTAMP, 'SYSTEM'
FROM kisi k WHERE k.tc_kimlik_no = '89456723410' AND NOT EXISTS (SELECT 1 FROM hesap_bilgisi WHERE kisi_id = k.id)
UNION ALL
SELECT k.id, 'Denizbank', 'TR340001340002550099887766', 'Elif DOĞAN', true, true, CURRENT_TIMESTAMP, 'SYSTEM'
FROM kisi k WHERE k.tc_kimlik_no = '12345678920' AND NOT EXISTS (SELECT 1 FROM hesap_bilgisi WHERE kisi_id = k.id)
UNION ALL
SELECT k.id, 'TEB', 'TR320003200015510000554433', 'Hüseyin KURT', true, true, CURRENT_TIMESTAMP, 'SYSTEM'
FROM kisi k WHERE k.tc_kimlik_no = '23456789030' AND NOT EXISTS (SELECT 1 FROM hesap_bilgisi WHERE kisi_id = k.id);

-- =========================================================================
-- VERİ YÜKLEME TAMAMLANDI
-- =========================================================================
-- Özet:
-- ✅ 12 Yakınlık Kodu
-- ✅ 15 Meslek
-- ✅ 7 Özel Statü
-- ✅ 17 Engelli Tipi (6 ana + 11 alt)
-- ✅ 15 Hastalık
-- ✅ 9 Gelir Türü
-- ✅ 11 Borç Türü
-- ✅ 4 Yardım Dilimi
-- ✅ 12 Yardım Dönemi
-- ✅ 8 Yardım Red Sebebi
-- ✅ 17 Yardım Alt Tipi (6 Nakdi + 6 Ayni Komisyonlu, 5 Ayni Komisyonsuz)
-- ✅ 3 Personel
-- ✅ 15 Kişi (10 Gebze içi, 5 Gebze dışı)
-- ✅ 10 Hesap Bilgisi (IBAN - Gebze içi kişiler için)
-- =========================================================================
