# Sosyal Yardım Bilgi Sistemi - Veritabanı Şeması



```
┌─────────────────┐
│   PERSONEL      │
├─────────────────┤
│ id (PK)         │
│ tc_kimlik_no    │
│ ad              │
│ soyad           │
│ email           │
│ telefon         │
│ unvan           │
│ departman       │
│ tahkikat_yetkili│
│ komisyon_uyesi  │
└─────────────────┘
         │
         │ kaydeden
         ▼
┌─────────────────┐       ┌──────────────────┐       ┌────────────────────┐
│   MURACAAT      │◄─────►│ MURACAAT_YARDIM  │◄─────►│  YARDIM_ALT_TIPI  │
├─────────────────┤       │      _TALEP      │       ├────────────────────┤
│ id (PK)         │       ├──────────────────┤       │ id (PK)            │
│ muracaat_no     │       │ id (PK)          │       │ kod                │
│ basvuru_sahibi  │───┐   │ muracaat_id (FK) │       │ adi                │
│ komisyon_kararli│   │   │ yardim_alt_tipi  │       │ yardim_tipi (ENUM) │
│ muracaat_tarihi │   │   │        _id (FK)  │       │ komisyon_kararli   │
│ durum (ENUM)    │   │   └──────────────────┘       │ birim              │
│ karar_no        │   │                               │ varsayilan_tutar   │
│ karar_tarihi    │   │                               └────────────────────┘
└─────────────────┘   │
         │            │
         │            │   ┌─────────────────┐
         │            └──►│      KISI       │
         │                ├─────────────────┤
         │                │ id (PK)         │
         │                │ tc_kimlik_no    │
         │                │ ad              │
         │                │ soyad           │
         │                │ dogum_tarihi    │
         │                │ adres           │
         │                │ il              │
         │                │ ilce            │
         │                │ gebze_ikameti   │
         │                │ sgk_durum       │
         │                │ ogrenim_durum   │
         │                └─────────────────┘
         │                         ▲
         │                         │
         ▼                         │
┌──────────────────┐               │
│   AILE_FERT      │───────────────┘
├──────────────────┤
│ id (PK)          │
│ muracaat_id (FK) │
│ kisi_id (FK)     │
│ yakinlik_kodu_id │───────┐
│ ozel_statu_id    │       │
│ meslek_id        │       │
│ yaptigi_is       │       │
└──────────────────┘       │
         │                 │
         ├─────────────────┼────────────────┐
         ▼                 ▼                ▼
┌─────────────────┐  ┌─────────────┐  ┌────────────┐
│AILE_FERT_ENGEL  │  │YAKINLIK_KODU│  │  MESLEK    │
│     _BILGISI    │  ├─────────────┤  ├────────────┤
├─────────────────┤  │ id (PK)     │  │ id (PK)    │
│ id (PK)         │  │ kod         │  │ kod        │
│ aile_fert_id    │  │ adi         │  │ adi        │
│ engelli_tipi_id │  └─────────────┘  └────────────┘
│ engel_orani     │
└─────────────────┘

┌──────────────────────┐
│ AILE_MADDI_DURUM     │
├──────────────────────┤
│ id (PK)              │
│ muracaat_id (FK)     │
│ toplam_gelir         │
│ toplam_borc          │
└──────────────────────┘
         │
         ├───────────────────┬──────────────────┐
         ▼                   ▼                  ▼
┌─────────────────┐  ┌─────────────────┐  ┌──────────────────┐
│  GELIR_BILGISI  │  │  BORC_BILGISI   │  │  GAYRIMENKUL     │
├─────────────────┤  ├─────────────────┤  │     _BILGISI     │
│ id (PK)         │  │ id (PK)         │  ├──────────────────┤
│ aile_maddi_     │  │ aile_maddi_     │  │ id (PK)          │
│   durum_id (FK) │  │   durum_id (FK) │  │ aile_maddi_      │
│ kisi_id (FK)    │  │ borc_turu_id    │  │   durum_id (FK)  │
│ gelir_turu_id   │  │ borc_tutari     │  │ evi_var          │
│ gelir_tutari    │  └─────────────────┘  │ ev_tipi (ENUM)   │
└─────────────────┘                       │ araba_var        │
                                          │ gayrimenkul_var  │
┌──────────────────────┐                  └──────────────────┘
│  TUTANAK_BILGISI     │
├──────────────────────┤
│ id (PK)              │
│ muracaat_id (FK)     │
│ tahkikat_personel_id │
│ tahkikat_tarihi      │
│ tahkikat_metni       │
│ ev_gorselleri        │
└──────────────────────┘

┌──────────────────────┐
│    YARDIM_KARAR      │
├──────────────────────┤
│ id (PK)              │
│ muracaat_id (FK)     │
│ yardim_alt_tipi_id   │
│ yardim_durum (ENUM)  │
│ komisyon_kararli     │
│ toplanti_tarihi      │
│ verilen_tutar        │
│ adet_sayi            │
│ yardim_dilimi_id     │
│ yardim_donemi_id     │
│ hesap_bilgisi_id     │
│ red_sebebi_id        │
│ kesinlesti           │
└──────────────────────┘
```

## Tablo İlişkileri

### Ana İlişkiler

1. **MURACAAT ↔ KISI**
   - Bir müracaatın bir başvuru sahibi vardır (1:1)
   - Bir kişinin birden fazla müracaatı olabilir (1:N)

2. **MURACAAT ↔ AILE_FERT**
   - Bir müracaatta birden fazla aile ferdi olabilir (1:N)
   - Her aile ferdi bir kişiye bağlıdır

3. **MURACAAT ↔ AILE_MADDI_DURUM**
   - Bir müracaatın bir maddi durum bilgisi vardır (1:1)

4. **MURACAAT ↔ TUTANAK_BILGISI**
   - Bir müracaatın bir tutanak bilgisi vardır (1:1)

5. **MURACAAT ↔ YARDIM_KARAR**
   - Bir müracaatta birden fazla yardım kararı olabilir (1:N)

## Enum Değerleri

### MuracaatDurum
- TALEP_IPTAL_EDILDI
- BASVURU_SAHIBINE_ULASILMADI
- BEKLEMEDE
- TAHKIKATA_SEVK
- DEGERLENDIRME_KOMISYONU
- SONUCLANDI
- BASVURU_SAHIBI_VEFAT_ETTI

### YardimTipi
- NAKDI
- AYNI
- SAGLIK
- EGITIM
- BARIS
- DIGER

### YardimDurum
- KABUL_EDILDI
- RED_EDILDI

### SGKDurum
- SSK
- BAG_KUR
- EMEKLI_SANDIGI
- YESIL_KART
- YOK

### OgrenimDurum
- OKUR_YAZAR_DEGIL
- OKUR_YAZAR
- ILKOKUL
- ORTAOKUL
- LISE
- ON_LISANS
- LISANS
- YUKSEK_LISANS
- DOKTORA

## İndeksler

Performans için oluşturulan indeksler:

- `idx_kisi_tc` - Kişi tablosunda TC Kimlik No
- `idx_muracaat_no` - Müracaat numarasında arama
- `idx_muracaat_durum` - Müracaat durumuna göre filtreleme
- `idx_muracaat_tarih` - Tarih bazlı sorgular
- `idx_aile_fert_muracaat` - Müracaata göre aile fertleri
- `idx_yardim_karar_muracaat` - Müracaata göre kararlar

## Audit Alanları

Her tabloda bulunan ortak audit alanları:

- `olusturma_tarihi` - Kaydın oluşturulma zamanı
- `guncelleme_tarihi` - Kaydın son güncellenme zamanı
- `olusturan` - Kaydı oluşturan kullanıcı
- `guncelleyen` - Kaydı güncelleyen kullanıcı
- `aktif` - Soft delete için aktiflik durumu

## Veritabanı Kısıtlamaları

### Zorunlu Alanlar
- Tüm TC Kimlik Numaraları zorunlu ve unique
- Müracaat numarası unique
- Başvuru sahibi zorunlu
- Yardım alt tipi seçimi zorunlu
- Yardım karar durumu zorunlu

### İş Kuralı Kısıtlamaları
- Engel oranı: 1-99 arası
- Yardım dönemi: 1-12 ay arası
- IBAN: TR ile başlamalı, standart format
- Gebze kontrolü: İl = Kocaeli, İlçe = Gebze

## Veri Bütünlüğü

### Cascade İşlemler
- Müracaat silindiğinde tüm ilişkili kayıtlar (aile fert, maddi durum, tutanak, karar) otomatik silinir
- Soft delete kullanılır, fiziksel silme yapılmaz

### Foreign Key İlişkileri
- Tüm foreign key'ler referential integrity ile korunur
- Orphan kayıt bırakılmaz


