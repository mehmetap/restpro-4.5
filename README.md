# Müşteri Sipariş Takip Uygulaması

Bu uygulama müşterinin kendi siparişinin durumunu **canlı** olarak (4 saniyede bir otomatik güncellenerek) görebilmesi için ayrı bir arayüzdür. Restoran panelinden bağımsız çalışır; siparişleri sadece **okur**, değiştirmez.

## Dosyalar
- `Main.java` – Başlangıç noktası
- `DBConnection.java` – MySQL bağlantı ayarları
- `Siparis.java`, `SiparisDurumu.java` – Sipariş modeli
- `SiparisTakipDAO.java` – Sipariş numarası / telefon ile sorgulama (salt okunur)
- `Musteri.java`, `MusteriDAO.java` – Müşteri kayıt / giriş işlemleri (`customer_db`)
- `MusteriTakipFrame.java` – Ana arayüz: sipariş no girip canlı durum takibi
- `MusteriKayitDialog.java` – Yeni müşteri kaydı (ad, soyad, telefon, e-posta, adres)

## Kurulum
1. MySQL sunucunuzda `veritabani/customer_db.sql` dosyasını çalıştırın.
2. `mysql-connector-j-x.x.x.jar` dosyasını indirin.
3. `src/DBConnection.java` içindeki `HOST`, `KULLANICI`, `SIFRE` alanlarını güncelleyin (restoran-paneli ile aynı sunucu bilgileri).

## Derleme ve Çalıştırma
```bash
cd musteri-takip/src
javac -d ../out *.java
cd ../out
java -cp .:mysql-connector-j-x.x.x.jar Main      # Linux/Mac
java -cp .;mysql-connector-j-x.x.x.jar Main      # Windows
```

## Özellikler
- Sipariş numarası ile sorgulama
- 5 adımlı görsel ilerleme çubuğu: Onaylandı → Hazırlanıyor → Hazır → Kurye Yolda → Teslim Edildi
- Kurye bilgisi atandığında otomatik gösterilir
- 4 saniyede bir otomatik yenilenen canlı takip (personel panelde durumu değiştirdiği anda müşteri ekranına yansır)
- Yeni müşteri kaydı penceresi (iletişim bilgileri `customer_db.users` tablosuna kaydedilir)
