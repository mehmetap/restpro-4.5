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
1. MySQL sunucunuzda `customer_db.sql` ve `restaurant_db.sql` dosyasını çalıştırın.
2. `mysql-connector-j-x.x.x.jar` dosyasını indirin.
3. `DB_HOST`, `DB_PORT`, `DB_USER`, `DB_PASSWORD` ortam değişkenlerini ayarlayın (restoran-paneli ile aynı sunucu bilgileri).

## Derleme ve Çalıştırma
```bash
javac -encoding UTF-8 -d out *.java
java -cp "out:mysql-connector-j-x.x.x.jar" Main      # Linux/Mac
java -cp "out;mysql-connector-j-x.x.x.jar" Main      # Windows
```

## Özellikler
- Sipariş numarası ile sorgulama
- 5 adımlı görsel ilerleme çubuğu: Onaylandı → Hazırlanıyor → Hazır → Kurye Yolda → Teslim Edildi
- Kurye bilgisi atandığında otomatik gösterilir
- 4 saniyede bir otomatik yenilenen canlı takip (personel panelde durumu değiştirdiği anda müşteri ekranına yansır)
- Yeni müşteri kaydı penceresi (iletişim bilgileri `customer_db.users` tablosuna kaydedilir)


## Test ve bilinen eksikler

Yerel ortamda javac ve MySQL bulunmadığından derleme, SQL kurulumu ve arayüz entegrasyonu doğrulanmadı.
MusteriDAO parolaları düz metin saklıyor; parola özetleme ve mevcut hesaplar için geçiş tamamlanmadan gerçek kullanıcılarla kullanılmamalıdır.
