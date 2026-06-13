# Sosyal Yardım Bilgi Sistemi (SAIS)

> **Spring Boot + JSF/PrimeFaces tabanlı, belediye sosyal yardım müracaat ve takip otomasyonu.**

---

## Proje Özeti

SAIS, belediye sosyal hizmetler müdürlükleri için geliştirilmiş kapsamlı bir **sosyal yardım müracaat yönetim sistemidir**. Vatandaşların sosyal yardım başvurularını; başvuru alımından tutanak/tahkikat aşamasına, komisyon değerlendirmesinden yardım kararı ve kesinleştirmeye kadar tüm süreçleri dijital ortamda yönetir.

**Bu proje bir simülasyon (demo) projesidir.** Gerçek belediye sistemi yerine, yazılım geliştirme, eğitim ve referans amacıyla herkese açık şekilde paylaşılmaktadır. MERNİS entegrasyonu simülasyon modunda çalışmaktadır — gerçek kimlik servisine bağlantı yapılmamıştır.

Sistem; **MERNİS simülasyonu** ile kişi bilgilerini otomatik çekme, **aile ferdi analizi**, **maddi durum değerlendirmesi** ve **komisyon karar süreçleri** gibi temel işlevleri içerir. İlk çalıştırmada 25+ referans tablosu otomatik olarak doldurulur, böylece uygulama anında kullanıma hazır olur.

---

## Teknoloji Yığını (Tech Stack)

| Katman | Teknoloji |
|--------|-----------|
| **Backend** | Java 17, Spring Boot 3.2.0 |
| **Web Katmanı** | JSF (Jakarta Faces), PrimeFaces 13, JoinFaces 5.3.3 |
| **Veri Erişimi** | Spring Data JPA, Hibernate |
| **Veritabanı** | MySQL 8+ |
| **Harita/Dönüştürme** | MapStruct 1.6.3 |
| **Kod İyileştirme** | Lombok |
| **Raporlama** | JasperReports 6.21.0 (PDF & Excel) |
| **Build** | Maven 3.x |

---

## Temel Özellikler

### 1. Müracaat Yönetimi
- **Yeni müracaat girişi** otomatik numara atama ile
- **MERNİS sorgulama**: TC Kimlik No ile otomatik kişi bilgisi çekme *(simülasyon modu destekli)*
- **Adına başvuru**: Kendisi yerine başka bir kişi adına başvuru desteği
- **Komisyonlu / Komisyonsuz** başvuru ayrımı

### 2. Çok Adımlı İş Akışı (Workflow)
```
Beklemede → Tahkikata Sevk → Değerlendirme Komisyonu → Sonuçlandı
```
- Sekmeli (tab-based) navigasyon ile adım adım veri girişi
- Her adımda veri doğrulama ve zorunlu alan kontrolü
- Durum geçişlerinde iş kuralı validasyonları

### 3. Aile Fert Yönetimi
- MERNİS **yakınlık servisi** ile otomatik aile ferdi çekme
- Manuel aile ferdi ekleme/düzenleme
- Her ferde özel: **engellilik bilgisi**, **hastalık bilgisi**, **meslek**, **yakınlık derecesi**

### 4. Maddi Durum Analizi
- **Gelir bilgisi**: Maaş, emekli, kira, sosyal yardım vb. gelir türleri
- **Borç bilgisi**: Elektrik, su, kira, kredi kartı vb. borç türleri
- **Gayrimenkul bilgisi**: Ev mülkiyeti, araç durumu, ev tipi, yakacak türü

### 5. Tutanak & Tahkikat
- Tahkikat personeli atama
- Tahkikat tarihi ve metni kaydı
- **Ev görselleri**: Çoklu fotoğraf yükleme ve görüntüleme

### 6. Yardım Kararları
- **Komisyon kararlı yardımlar**: Komisyon toplantısı ile karar
  - Kabul/Red kararı, yardım dilimi, dönemi, tutar, adet
- **Komisyonsuz yardımlar**: Hızlı karar (acil yardım paketleri vb.)
- **Kesinleştirme**: Otomatik karar numarası ve tarih atama

### 7. Doküman Yönetimi
- Başvuraya özel dosya yükleme (PDF, DOC, JPG, PNG, XLS vb.)
- Geçici dosya önbelleği ile kayıt öncesi yükleme desteği
- Dosya türü ve boyut validasyonları (max 10MB)

### 8. Raporlama
- **JasperReports** entegrasyonu
- PDF ve Excel formatında rapor üretimi
- Müracaat listeleri, yardım kararları ve istatistiksel raporlar

### 9. Referans Veri Yönetimi
- Yakınlık kodları (MERNİS standartları)
- Meslek, hastalık, engelli tipi tanımları
- Gelir/borç türleri, yardım alt tipleri, red sebepleri
- Otomatik veri yükleme (`master-data.sql`)

---

## Veritabanı Şeması

Sistem 25+ tablodan oluşan kapsamlı bir ilişkisel veritabanı kullanır:

- **Ana tablolar**: `muracaat`, `kisi`, `personel`
- **Başvuru detayları**: `aile_fert`, `aile_maddi_durum`, `gelir_bilgisi`, `borc_bilgisi`, `gayrimenkul_bilgisi`
- **Tutanak**: `tutanak_bilgisi`, `tutanak_gorsel`
- **Kararlar**: `yardim_karar`
- **Referans**: `yakinlik_kodu`, `meslek`, `engelli_tipi`, `hastalik`, `yardim_alt_tipi`, `yardim_dilimi`, `yardim_donemi`, `yardim_red_sebebi`, `gelir_turu`, `borc_turu`

> Detaylı şema için: `database-schema.md`

---

## Proje Yapısı

```
SAIS/
├── src/main/java/com/sais/
│   ├── config/           # Konfigürasyon (JPA, Jasper, Dosya, Audit)
│   ├── constants/          # Sabit değerler
│   ├── controller/         # JSF Managed Beans (ViewScoped)
│   ├── dto/                # Data Transfer Objects
│   ├── entity/             # JPA Entities
│   ├── enums/              # Enum tanımları (Durum, Yardım Tipi, SGK vb.)
│   ├── exception/          # Özel istisna sınıfları
│   ├── mapper/             # MapStruct DTO-Entity dönüştürücüler
│   ├── repository/         # Spring Data JPA Repositories
│   ├── service/            # İş mantığı katmanı
│   │   └── report/         # Raporlama servisleri
│   └── util/               # Yardımcı araçlar
├── src/main/resources/
│   ├── database/
│   │   └── master-data.sql # Varsayılan referans verileri
│   ├── reports/            # JasperReports şablonları
│   ├── application.yml     # Ana konfigürasyon
│   └── application-jasper.yml
├── src/main/webapp/
│   ├── pages/              # XHTML sayfaları
│   ├── templates/          # Layout, sidebar, topbar
│   └── resources/          # CSS, JS, resimler
└── pom.xml
```

---

## Kurulum ve Çalıştırma

### Gereksinimler
- Java JDK 17+
- Maven 3.9+
- MySQL 8.0+

### 1. Veritabanını Hazırlayın
```sql
CREATE DATABASE sais_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Konfigürasyon
`src/main/resources/application.yml` dosyasındaki veritabanı bilgilerini güncelleyin:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sais_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Europe/Istanbul
    username: root
    password: sifreniz
```

> **Not:** `ddl-auto: create` ve `sql.init.mode: always` ayarları ilk çalıştırmada şema oluşturma ve örnek veri yükleme yapar. Üretim ortamında `ddl-auto: update` ve `sql.init.mode: never` olarak değiştirin.

### 3. Uygulamayı Başlatın
```bash
# Maven Wrapper ile
./mvnw spring-boot:run

# veya Maven ile
mvn spring-boot:run
```

### 4. Erişim
Tarayıcıda şu adresi açın:
```
http://localhost:8080/sais/
```

---

## MERNİS Entegrasyonu (Simülasyon)

**Sistem tamamen simülasyon modunda çalışır.** Gerçek MERNİS servisine bağlantı yoktur. TC Kimlik No girilerek rastgele ama tutarlı kişi bilgileri üretilir.

Simülasyon mantığı:
- TC'nin ilk 4 hanesi `seed` olarak kullanılır — aynı TC her zaman aynı sonuçları döner
- TC'nin son hanesine göre ikamet yeri belirlenir: `0-5` → Gebze içi, `6-9` → Gebze dışı
- MERNİS Yakınlık Servisi simülasyonu ile aile fertleri (eş, çocuk, anne vb.) otomatik üretilir

Gerçek MERNİS entegrasyonu ileride eklenebilir. Simülasyon modu, eğitim ve demo amaçlı mükemmel bir test ortamı sunar.

---

## İş Kuralları (Business Rules)

- Başvuru sahibi **Gebze'de ikamet etmelidir**.
- Başvuru sahibinin sonuçlanmamış başka bir müracaatı olamaz.
- **Komisyon kararlı** başvurular: aile fert + maddi durum + tutanak zorunlu
- **Komisyonsuz** başvurular: sadece yardım kararı yeterli
- Tahkikata sevk için en az bir aile ferdi kaydı olmalı
- Komisyona gönderim için tutanak ve maddi durum bilgisi zorunlu
- Sonuçlanmış müracaatlar güncellenemez/silinemez
- Soft delete aktif — veriler fiziksel olarak silinmez

---

## Ekran Görüntüleri ve Sayfalar

| Sayfa | Açıklama |
|-------|----------|
| `index.xhtml` | Dashboard — müracaat istatistikleri ve kısayollar |
| `pages/muracaat.xhtml` | Ana müracaat giriş formu |
| `pages/aile-fertleri.xhtml` | Aile ferdi yönetimi |
| `pages/maddi-durum.xhtml` | Gelir, borç, gayrimenkul bilgileri |
| `pages/tutanak.xhtml` | Tahkikat tutanağı ve ev görselleri |
| `pages/yardim-karar.xhtml` | Komisyon kararlı yardım kararları |
| `pages/yardim-komisyonsuz.xhtml` | Komisyonsuz yardım kararları |
| `raporlar.xhtml` | Raporlama ekranı |

---

## Katkıda Bulunma

Bu proje **eğitim ve referans amaçlı** herkese açıktır. Katkılar memnuniyetle karşılanır. Fork'layıp kendi ihtiyaçlarınıza göre geliştirebilirsiniz.

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/yeni-ozellik`)
3. Değişikliklerinizi commit edin (`git commit -am 'Yeni özellik'`)
4. Branch'inizi push edin (`git push origin feature/yeni-ozellik`)
5. Pull Request açın

---

## Lisans

Bu proje **MIT Lisansı** altında lisanslanmıştır. Detaylar için `LICENSE` dosyasına bakın.

Herkesin serbestçe kullanabilmesi, değiştirebilmesi ve dağıtabilmesi amacıyla açık kaynak olarak paylaşılmaktadır.

---

## Geliştirici

**Akin Kebabci** — Bu proje bir eğitim ve referans simülasyonu olarak geliştirilmiştir.

> *"Sosyal yardımda dijitalleşme: eğitim ve referans için açık kaynak bir çözüm."*
