import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SiparisTakipDAO {

    private static final String SELECT_BASE =
        "SELECT s.id, s.siparis_no, s.musteri_adi, s.musteri_telefon, s.teslimat_adresi, " +
        "k.ad AS kurye_ad, k.soyad AS kurye_soyad, k.telefon AS kurye_telefon, " +
        "s.durum, s.tutar, s.odeme_yontemi, s.siparis_detayi, s.olusturma_tarihi, s.guncelleme_tarihi " +
        "FROM siparisler s LEFT JOIN kuryeler k ON s.kurye_id = k.id ";

    /** Siparis numarasina gore sorgular. Musteri takip ekraninin ana fonksiyonu. */
    public Siparis siparisNoIleGetir(String siparisNo) throws SQLException {
        String sql = SELECT_BASE + "WHERE s.siparis_no = ?";
        try (Connection con = DBConnection.getRestaurantConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, siparisNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    /** Telefon numarasina gore musterinin en son siparisini getirir. */
    public Siparis sonSiparisiTelefonIleGetir(String telefon) throws SQLException {
        String sql = SELECT_BASE + "WHERE s.musteri_telefon = ? ORDER BY s.olusturma_tarihi DESC LIMIT 1";
        try (Connection con = DBConnection.getRestaurantConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, telefon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    private Siparis mapRow(ResultSet rs) throws SQLException {
        Siparis s = new Siparis();
        s.setId(rs.getInt("id"));
        s.setSiparisNo(rs.getString("siparis_no"));
        s.setMusteriAdi(rs.getString("musteri_adi"));
        s.setMusteriTelefon(rs.getString("musteri_telefon"));
        s.setTeslimatAdresi(rs.getString("teslimat_adresi"));
        String kAd = rs.getString("kurye_ad");
        if (kAd != null) {
            s.setKuryeAdi(kAd + " " + rs.getString("kurye_soyad"));
            s.setKuryeTelefon(rs.getString("kurye_telefon"));
        }
        s.setDurum(SiparisDurumu.fromDbValue(rs.getString("durum")));
        s.setTutar(rs.getBigDecimal("tutar"));
        s.setOdemeYontemi(rs.getString("odeme_yontemi"));
        s.setSiparisDetayi(rs.getString("siparis_detayi"));
        Timestamp olusturma = rs.getTimestamp("olusturma_tarihi");
        if (olusturma != null) s.setOlusturmaTarihi(olusturma.toLocalDateTime());
        Timestamp guncelleme = rs.getTimestamp("guncelleme_tarihi");
        if (guncelleme != null) s.setGuncellemeTarihi(guncelleme.toLocalDateTime());
        return s;
    }
}
