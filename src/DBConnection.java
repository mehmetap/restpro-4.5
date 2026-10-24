import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * MySQL baglanti ayarlari.
 * DB_HOST, DB_PORT, DB_USER ve DB_PASSWORD ortam degiskenlerini ayarlayin.
 * (Restoran paneli ile ayni sunucu/degerler kullanilmalidir.)
 */
public class DBConnection {

    private static final String HOST = env("DB_HOST", "localhost");
    private static final String PORT = env("DB_PORT", "3306");
    private static final String KULLANICI = env("DB_USER", "root");
    private static final String SIFRE = env("DB_PASSWORD", "");

    private static final String RESTAURANT_DB = "restaurant_db";
    private static final String CUSTOMER_DB = "customer_db";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver bulunamadi. mysql-connector-j.jar dosyasini classpath'e ekleyin.");
        }
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isEmpty() ? fallback : value;
    }

    private static String buildUrl(String dbName) {
        return "jdbc:mysql://" + HOST + ":" + PORT + "/" + dbName
                + "?useSSL=false&serverTimezone=Europe/Istanbul&characterEncoding=UTF-8";
    }

    /** Siparis durumunu okumak icin restoran veritabanina baglanti (musteri takip icin salt-okunur kullanim) */
    public static Connection getRestaurantConnection() throws SQLException {
        return DriverManager.getConnection(buildUrl(RESTAURANT_DB), KULLANICI, SIFRE);
    }

    /** Musteri kayit / giris islemleri icin */
    public static Connection getCustomerConnection() throws SQLException {
        return DriverManager.getConnection(buildUrl(CUSTOMER_DB), KULLANICI, SIFRE);
    }
}
