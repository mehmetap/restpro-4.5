import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MusteriDAO {

    /** Telefon numarasina gore giris kontrolu yapar. Basariliysa Musteri, degilse null doner. */
    public Musteri girisYap(String telefon, String sifre) throws SQLException {
        String sql = "SELECT id, ad, soyad, telefon, email, adres FROM users WHERE telefon = ? AND sifre = ?";
        try (Connection con = DBConnection.getCustomerConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, telefon);
            ps.setString(2, sifre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Musteri m = new Musteri();
                    m.setId(rs.getInt("id"));
                    m.setAd(rs.getString("ad"));
                    m.setSoyad(rs.getString("soyad"));
                    m.setTelefon(rs.getString("telefon"));
                    m.setEmail(rs.getString("email"));
                    m.setAdres(rs.getString("adres"));
                    return m;
                }
            }
        }
        return null;
    }

    /** Yeni musteri kaydi olusturur (ad, soyad, telefon, email, adres, sifre). */
    public int kayitOl(Musteri m, String sifre) throws SQLException {
        String sql = "INSERT INTO users (ad, soyad, telefon, email, adres, sifre) VALUES (?,?,?,?,?,?)";
        try (Connection con = DBConnection.getCustomerConnection();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getAd());
            ps.setString(2, m.getSoyad());
            ps.setString(3, m.getTelefon());
            ps.setString(4, m.getEmail());
            ps.setString(5, m.getAdres());
            ps.setString(6, sifre);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }
}
