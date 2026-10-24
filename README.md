# Restoran ve Müşteri Sipariş Takibi

Java Swing ve MySQL ile eğitim amaçlı masaüstü prototipi.
Müşteri ekranı sipariş numarasıyla durumu sorgular; restoran ekranı siparişleri
listeler, durum değiştirir ve kurye atar. Yenileme periyotları müşteri için
4, restoran için 5 saniyedir.

## Dosyalar

- `src/`: modeller, DAO sınıfları ve Swing ekranları.
- `sql/customer_db.sql`: müşteri şeması ve örnek kayıt.
- `sql/restaurant_db.sql`: restoran, kurye ve sipariş şeması.
- `Main`: müşteri ekranı; `RestoranMain`: personel ekranı.

## Kurulum

JDK 17, MySQL ve MySQL Connector/J gerekir. İki SQL dosyasını da çalıştırın:

```sh
mysql -u root -p < sql/customer_db.sql
mysql -u root -p < sql/restaurant_db.sql
```

Connector/J JAR dosyasını `lib/` klasörüne koyun. Bağlantı için ortam değişkenleri:

| Değişken | Varsayılan |
|---|---|
| `DB_HOST` | localhost |
| `DB_PORT` | 3306 |
| `DB_USER` | root |
| `DB_PASSWORD` | boş |

Linux/macOS: `export DB_PASSWORD='yerel-parolaniz'`
PowerShell: `$env:DB_PASSWORD='yerel-parolaniz'`
Parolayı kaynak dosyasına yazmayın. SQL dosyaları örnek veritabanı adlarını kullanır.

## Derleme ve çalıştırma

```sh
javac -encoding UTF-8 -d out src/*.java
# Linux/macOS
java -cp 'out:lib/*' Main
java -cp 'out:lib/*' RestoranMain
# Windows
java -cp 'out;lib/*' Main
java -cp 'out;lib/*' RestoranMain
```

## Doğrulama ve sınırlar

JDK 17 ile bütün Java kaynakları başarıyla derlendi.
Yerel MySQL sunucusu olmadan veritabanı akışları doğrulanamaz. `SP-1001`
örnek siparişiyle müşteri sorgulama ve personel durum değişikliğini test edin.
Bu sürümde müşteri parolaları düz metindir ve personel ekranında kimlik doğrulama
yoktur; gerçek müşteri verisiyle kullanmadan önce parola hashleme ve yetkilendirme gerekir.
Swing içindeki bazı veritabanı çağrıları arayüz iş parçacığında çalışır;
yavaş bağlantı ekranı bekletebilir. Bu bakım kapsamındaki derleme kontrolü bu
entegrasyon ve arayüz testlerinin yerine geçmez.
