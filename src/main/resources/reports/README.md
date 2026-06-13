# JasperReports Kullanım Kılavuzu

## Genel Bakış

Bu proje JasperReports 6.21.0 ile entegre edilmiştir. PDF ve Excel formatında raporlar üretebilirsiniz.

## Yapı

```
src/main/resources/reports/
├── README.md                 # Bu dosya
├── personel-liste.jrxml     # Personel listesi rapor şablonu
└── muracaat-ozet.jrxml      # Müracaat özet rapor şablonu
```

## Hazır Raporlar

### 1. Personel Raporları

#### Aktif Personel Listesi (PDF)
```
GET /api/reports/personel/aktif-liste/pdf
```

#### Aktif Personel Listesi (Excel)
```
GET /api/reports/personel/aktif-liste/excel
```

#### Tahkikat Yetkilileri (PDF)
```
GET /api/reports/personel/tahkikat-yetkililer/pdf
```

#### Komisyon Üyeleri (PDF)
```
GET /api/reports/personel/komisyon-uyeleri/pdf
```

### 2. Müracaat Raporları

#### Müracaat Özet Raporu (PDF)
```
GET /api/reports/muracaat/ozet/pdf?baslangicTarihi=2024-01-01&bitisTarihi=2024-12-31
```

Parametreler:
- `baslangicTarihi` (opsiyonel): yyyy-MM-dd formatında
- `bitisTarihi` (opsiyonel): yyyy-MM-dd formatında

## Yeni Rapor Oluşturma

### 1. JRXML Şablonu Oluşturma

JasperSoft Studio kullanarak yeni bir `.jrxml` dosyası oluşturun ve `src/main/resources/reports/` klasörüne kaydedin.

#### Collection Veri Kaynağı Kullanan Rapor Örneği:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports
              http://jasperreports.sourceforge.net/xsd/jasperreport.xsd"
              name="ornek-rapor" pageWidth="595" pageHeight="842">

    <parameter name="RAPOR_BASLIGI" class="java.lang.String"/>
    
    <field name="ad" class="java.lang.String"/>
    <field name="soyad" class="java.lang.String"/>
    
    <!-- Rapor tasarımı buraya gelir -->
</jasperReport>
```

#### Veritabanı Sorgusu Kullanan Rapor Örneği:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports
              http://jasperreports.sourceforge.net/xsd/jasperreport.xsd"
              name="ornek-rapor" pageWidth="595" pageHeight="842">

    <parameter name="BASLANGIC_TARIHI" class="java.sql.Date"/>
    
    <queryString>
        <![CDATA[
            SELECT id, ad, soyad FROM personel 
            WHERE aktif = true
            AND olusturma_tarihi >= $P{BASLANGIC_TARIHI}
        ]]>
    </queryString>
    
    <field name="id" class="java.lang.Long"/>
    <field name="ad" class="java.lang.String"/>
    
    <!-- Rapor tasarımı buraya gelir -->
</jasperReport>
```

### 2. Controller Metodunu Ekleme

```java
@GetMapping("/yeni-rapor/pdf")
public ResponseEntity<byte[]> getYeniRaporPdf() {
    try {
        // Veri hazırlama
        List<Entity> dataList = service.getData();
        
        // Parametreler
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("RAPOR_BASLIGI", "YENİ RAPOR");
        parameters.put("RAPOR_TARIHI", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        
        // Collection ile rapor üretme
        byte[] pdfBytes = reportService.generatePdfReportFromCollection(
            "reports/yeni-rapor.jrxml",
            parameters,
            dataList
        );
        
        // veya Veritabanı sorgusu ile rapor üretme
        // byte[] pdfBytes = reportService.generatePdfReportFromDb(
        //     "reports/yeni-rapor.jrxml",
        //     parameters
        // );
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "yeni-rapor.pdf");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfBytes);
            
    } catch (Exception e) {
        log.error("Rapor oluşturma hatası", e);
        return ResponseEntity.internalServerError().build();
    }
}
```

## ReportService Metodları

### PDF Rapor Üretme

#### Collection ile:
```java
byte[] generatePdfReportFromCollection(
    String reportPath,           // reports/ornek.jrxml
    Map<String, Object> parameters,  // Rapor parametreleri
    Collection<?> dataSource     // Veri listesi
)
```

#### Veritabanı Sorgusu ile:
```java
byte[] generatePdfReportFromDb(
    String reportPath,           // reports/ornek.jrxml
    Map<String, Object> parameters   // Rapor parametreleri
)
```

### Excel Rapor Üretme

#### Collection ile:
```java
byte[] generateExcelReportFromCollection(
    String reportPath,
    Map<String, Object> parameters,
    Collection<?> dataSource
)
```

#### Veritabanı Sorgusu ile:
```java
byte[] generateExcelReportFromDb(
    String reportPath,
    Map<String, Object> parameters
)
```

## Önemli Notlar

1. **JRXML Dosya Yolu**: `resources/reports/` klasörüne göre belirtilmelidir
2. **Entity/DTO Alanları**: JRXML şablonundaki field isimleri Java sınıfındaki field isimleriyle birebir eşleşmelidir
3. **Parametreler**: Rapor parametreleri büyük harfle yazılmalıdır (örn: `RAPOR_BASLIGI`)
4. **Tarih Formatı**: API parametrelerinde ISO date format kullanılır: `yyyy-MM-dd`
5. **Veritabanı Sorguları**: MySQL syntax'ına uygun olmalıdır

## JasperSoft Studio Kurulumu

1. [JasperSoft Studio](https://community.jaspersoft.com/project/jaspersoft-studio/releases) indirin
2. Rapor tasarlamak için `.jrxml` dosyalarını açın
3. Preview özelliğini kullanarak raporu test edin
4. Derlenmiş `.jasper` dosyalarını commit'lemeyin (sadece `.jrxml` dosyaları)

## Türkçe Karakter Desteği

JRXML dosyalarında encoding olarak UTF-8 kullanılır:
```xml
<?xml version="1.0" encoding="UTF-8"?>
```

Font desteği için şablonda font tanımlaması yapabilirsiniz:
```xml
<style name="DefaultStyle" isDefault="true" fontName="DejaVu Sans"/>
```

## Örnek Kullanım

### Frontend (JavaScript/React)
```javascript
// PDF indirme
const downloadPdf = async () => {
    const response = await fetch('/api/reports/personel/aktif-liste/pdf');
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'personel-liste.pdf';
    a.click();
};

// Excel indirme
const downloadExcel = async () => {
    const response = await fetch('/api/reports/personel/aktif-liste/excel');
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'personel-liste.xlsx';
    a.click();
};

// Parametreli rapor
const downloadMuracaatRaporu = async (startDate, endDate) => {
    const url = `/api/reports/muracaat/ozet/pdf?baslangicTarihi=${startDate}&bitisTarihi=${endDate}`;
    const response = await fetch(url);
    const blob = await response.blob();
    const url2 = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url2;
    a.download = 'muracaat-ozet.pdf';
    a.click();
};
```

## Sorun Giderme

### Rapor Oluşturulurken Hata
- JRXML dosya yolunu kontrol edin
- Field isimlerinin entity/DTO ile eşleştiğinden emin olun
- Loglarda detaylı hata mesajını inceleyin

### Türkçe Karakterler Görünmüyor
- Encoding UTF-8 olarak ayarlanmış mı kontrol edin
- Font desteği ekleyin

### Veritabanı Sorgusu Çalışmıyor
- SQL syntax'ını kontrol edin
- Parameter isimleri doğru yazılmış mı kontrol edin
- Veritabanı bağlantısının aktif olduğundan emin olun

## İletişim

Sorun veya önerileriniz için geliştirme ekibiyle iletişime geçin.

