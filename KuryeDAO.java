import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KuryeDAO {

    public List<Kurye> tumKuryeleriGetir() throws SQLException {
        List<Kurye> liste = new ArrayList<>();
        String sql = "SELECT id, ad, soyad, telefon, arac_tipi, durum FROM kuryeler ORDER BY ad";
        try (Connection con = DBConnection.getRestaurantConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(mapRow(rs));
            }
        }
        return liste;
    }

    public List<Kurye> musaitKuryeleriGetir() throws SQLException {
        List<Kurye> liste = new ArrayList<>();
        String sql = "SELECT id, ad, soyad, telefon, arac_tipi, durum FROM kuryeler WHERE durum = 'MUSAIT' ORDER BY ad";
        try (Connection con = DBConnection.getRestaurantConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(mapRow(rs));
            }
        }
        return liste;
    }

    public void kuryeEkle(Kurye k) throws SQLException {
        String sql = "INSERT INTO kuryeler (ad, soyad, telefon, arac_tipi, durum) VALUES (?,?,?,?,?)";
        try (Connection con = DBConnection.getRestaurantConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, k.getAd());
            ps.setString(2, k.getSoyad());
            ps.setString(3, k.getTelefon());
            ps.setString(4, k.getAracTipi());
            ps.setString(5, k.getDurum() == null ? "MUSAIT" : k.getDurum());
            ps.executeUpdate();
        }
    }

    public void durumGuncelle(int kuryeId, String yeniDurum) throws SQLException {
        String sql = "UPDATE kuryeler SET durum = ? WHERE id = ?";
        try (Connection con = DBConnection.getRestaurantConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, yeniDurum);
            ps.setInt(2, kuryeId);
            ps.executeUpdate();
        }
    }

    private Kurye mapRow(ResultSet rs) throws SQLException {
        Kurye k = new Kurye();
        k.setId(rs.getInt("id"));
        k.setAd(rs.getString("ad"));
        k.setSoyad(rs.getString("soyad"));
        k.setTelefon(rs.getString("telefon"));
        k.setAracTipi(rs.getString("arac_tipi"));
        k.setDurum(rs.getString("durum"));
        return k;
    }
}
