-- =====================================================
-- RESTORAN VERITABANI (restaurant_db)
-- Restoran bilgileri, kurye bilgileri ve siparisleri icerir
-- =====================================================

CREATE DATABASE IF NOT EXISTS restaurant_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_turkish_ci;

USE restaurant_db;

-- ------------------ RESTORAN BILGILERI ------------------
CREATE TABLE IF NOT EXISTS restoran_bilgileri (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    restoran_adi       VARCHAR(100) NOT NULL,
    adres              VARCHAR(255),
    telefon            VARCHAR(20),
    email              VARCHAR(100),
    vergi_no           VARCHAR(30),
    calisma_saatleri   VARCHAR(100)
) ENGINE=InnoDB;

INSERT INTO restoran_bilgileri (restoran_adi, adres, telefon, email, vergi_no, calisma_saatleri)
SELECT * FROM (SELECT 'Örnek Restoran' AS a, 'Antalya, Muratpaşa' AS b, '02123653403' AS c,
       'info@ornekrestoran.com' AS d, '1234567890' AS e, '10:00 - 23:00' AS f) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM restoran_bilgileri);

-- ------------------ KURYE BILGILERI ------------------
CREATE TABLE IF NOT EXISTS kuryeler (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    ad          VARCHAR(50) NOT NULL,
    soyad       VARCHAR(50) NOT NULL,
    telefon     VARCHAR(20) NOT NULL,
    arac_tipi   VARCHAR(30),
    durum       ENUM('MUSAIT','GOREVDE','IZINLI') DEFAULT 'MUSAIT'
) ENGINE=InnoDB;

INSERT INTO kuryeler (ad, soyad, telefon, arac_tipi, durum)
SELECT 'Mehmet', 'Yılmaz', '05051112233', 'Motosiklet', 'MUSAIT'
WHERE NOT EXISTS (SELECT 1 FROM kuryeler WHERE telefon = '05051112233');
INSERT INTO kuryeler (ad, soyad, telefon, arac_tipi, durum)
SELECT 'Ahmet', 'Demir', '05052223344', 'Motosiklet', 'MUSAIT'
WHERE NOT EXISTS (SELECT 1 FROM kuryeler WHERE telefon = '05052223344');

-- ------------------ SIPARISLER ------------------
CREATE TABLE IF NOT EXISTS siparisler (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    siparis_no          VARCHAR(30) NOT NULL UNIQUE,
    musteri_id          INT,                 -- customer_db.users.id (mantiksal referans)
    musteri_adi         VARCHAR(100) NOT NULL,
    musteri_telefon     VARCHAR(20)  NOT NULL,
    teslimat_adresi     VARCHAR(255),
    kurye_id            INT,
    durum               ENUM('ONAY_BEKLIYOR','ONAYLANDI','HAZIRLANIYOR','HAZIR','KURYEDE','TESLIM_EDILDI','IPTAL')
                             DEFAULT 'ONAY_BEKLIYOR',
    tutar               DECIMAL(10,2) NOT NULL DEFAULT 0,
    odeme_yontemi       VARCHAR(50),
    siparis_detayi      TEXT,
    olusturma_tarihi    DATETIME DEFAULT CURRENT_TIMESTAMP,
    guncelleme_tarihi   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (kurye_id) REFERENCES kuryeler(id)
) ENGINE=InnoDB;

-- Ornek test siparisi
INSERT INTO siparisler (siparis_no, musteri_id, musteri_adi, musteri_telefon, teslimat_adresi,
                         durum, tutar, odeme_yontemi, siparis_detayi)
VALUES ('SP-1001', 1, 'Ece G.', '05001112233', 'Antalya, Muratpaşa',
        'ONAY_BEKLIYOR', 285.00, 'Online Kredi Kartı',
        '1x Doritos''lu Mega Çiğ Köfte Dürüm Menü, 1x Pepsi (33cl), 1x Bardakta Turşu Suyu')
ON DUPLICATE KEY UPDATE tutar = VALUES(tutar);
