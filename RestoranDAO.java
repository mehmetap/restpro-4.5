import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RestoranDAO {

    public RestoranBilgisi bilgiGetir() throws SQLException {
        String sql = "SELECT id, restoran_adi, adres, telefon, email, vergi_no, calisma_saatleri " +
                "FROM restoran_bilgileri LIMIT 1";
        try (Connection con = DBConnection.getRestaurantConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                RestoranBilgisi r = new RestoranBilgisi();
                r.setId(rs.getInt("id"));
                r.setRestoranAdi(rs.getString("restoran_adi"));
                r.setAdres(rs.getString("adres"));
                r.setTelefon(rs.getString("telefon"));
                r.setEmail(rs.getString("email"));
                r.setVergiNo(rs.getString("vergi_no"));
                r.setCalismaSaatleri(rs.getString("calisma_saatleri"));
                return r;
            }
        }
        return null;
    }

    public void bilgiGuncelle(RestoranBilgisi r) throws SQLException {
        String sql = "UPDATE restoran_bilgileri SET restoran_adi=?, adres=?, telefon=?, email=?, " +
                "vergi_no=?, calisma_saatleri=? WHERE id=?";
        try (Connection con = DBConnection.getRestaurantConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getRestoranAdi());
            ps.setString(2, r.getAdres());
            ps.setString(3, r.getTelefon());
            ps.setString(4, r.getEmail());
            ps.setString(5, r.getVergiNo());
            ps.setString(6, r.getCalismaSaatleri());
            ps.setInt(7, r.getId());
            ps.executeUpdate();
        }
    }
}
