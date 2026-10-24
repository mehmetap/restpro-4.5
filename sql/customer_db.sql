-- =====================================================
-- MUSTERI VERITABANI (customer_db)
-- Sadece kullanici bilgileri ve iletisim bilgileri icerir
-- =====================================================

CREATE DATABASE IF NOT EXISTS customer_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_turkish_ci;

USE customer_db;

CREATE TABLE IF NOT EXISTS users (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    ad              VARCHAR(50)  NOT NULL,
    soyad           VARCHAR(50)  NOT NULL,
    telefon         VARCHAR(20)  NOT NULL UNIQUE,
    email           VARCHAR(100),
    adres           VARCHAR(255),
    sifre           VARCHAR(255) NOT NULL,
    kayit_tarihi    DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Ornek test kullanicisi (sifre: 1234 -> gercek projede hash'lenmelidir)
INSERT INTO users (ad, soyad, telefon, email, adres, sifre)
VALUES ('Ece', 'G.', '05001112233', 'ece@example.com', 'Antalya, Muratpasa', '1234')
ON DUPLICATE KEY UPDATE ad = VALUES(ad);
