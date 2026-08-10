import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SiparisDAO {

    private static final String SELECT_BASE =
        "SELECT s.id, s.siparis_no, s.musteri_id, s.musteri_adi, s.musteri_telefon, " +
        "s.teslimat_adresi, s.kurye_id, k.ad AS kurye_ad, k.soyad AS kurye_soyad, " +
        "s.durum, s.tutar, s.odeme_yontemi, s.siparis_detayi, s.olusturma_tarihi, s.guncelleme_tarihi " +
        "FROM siparisler s LEFT JOIN kuryeler k ON s.kurye_id = k.id ";

    /** Tum siparisleri en yeni once olacak sekilde getirir. */
    public List<Siparis> tumSiparisleriGetir() throws SQLException {
        List<Siparis> liste = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY s.olusturma_tarihi DESC";
        try (Connection con = DBConnection.getRestaurantConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(mapRow(rs));
            }
        }
        return liste;
    }

    /** Belirli bir duruma gore filtrelenmis siparisleri getirir. */
    public List<Siparis> durumaGoreGetir(SiparisDurumu durum) throws SQLException {
        List<Siparis> liste = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE s.durum = ? ORDER BY s.olusturma_tarihi DESC";
        try (Connection con = DBConnection.getRestaurantConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, durum.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapRow(rs));
                }
            }
        }
        return liste;
    }

    /** Siparis numarasina gore tek siparis getirir (musteri takip ekrani icin de kullanilabilir). */
    public Siparis siparisNoIleGetir(String siparisNo) throws SQLException {
        String sql = SELECT_BASE + "WHERE s.siparis_no = ?";
        try (Connection con = DBConnection.getRestaurantConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, siparisNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    /** Siparis durumunu gunceller. Kurye atamasi da ayni anda yapilabilir. */
    public void durumGuncelle(int siparisId, SiparisDurumu yeniDurum) throws SQLException {
        String sql = "UPDATE siparisler SET durum = ? WHERE id = ?";
        try (Connection con = DBConnection.getRestaurantConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, yeniDurum.name());
            ps.setInt(2, siparisId);
            ps.executeUpdate();
        }
    }

    /** Siparisi bir kuryeye atar ve durumunu KURYEDE yapar. */
    public void kuryeAta(int siparisId, int kuryeId) throws SQLException {
        String sql = "UPDATE siparisler SET kurye_id = ?, durum = ? WHERE id = ?";
        try (Connection con = DBConnection.getRestaurantConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, kuryeId);
            ps.setString(2, SiparisDurumu.KURYEDE.name());
            ps.setInt(3, siparisId);
            ps.executeUpdate();
        }
    }

    /** Yeni siparis olusturur (test / entegrasyon amacli). */
    public int siparisOlustur(Siparis s) throws SQLException {
        String sql = "INSERT INTO siparisler (siparis_no, musteri_id, musteri_adi, musteri_telefon, " +
                "teslimat_adresi, durum, tutar, odeme_yontemi, siparis_detayi) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getRestaurantConnection();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getSiparisNo());
            ps.setInt(2, s.getMusteriId());
            ps.setString(3, s.getMusteriAdi());
            ps.setString(4, s.getMusteriTelefon());
            ps.setString(5, s.getTeslimatAdresi());
            ps.setString(6, SiparisDurumu.ONAY_BEKLIYOR.name());
            ps.setBigDecimal(7, s.getTutar());
            ps.setString(8, s.getOdemeYontemi());
            ps.setString(9, s.getSiparisDetayi());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    private Siparis mapRow(ResultSet rs) throws SQLException {
        Siparis s = new Siparis();
        s.setId(rs.getInt("id"));
        s.setSiparisNo(rs.getString("siparis_no"));
        s.setMusteriId(rs.getInt("musteri_id"));
        s.setMusteriAdi(rs.getString("musteri_adi"));
        s.setMusteriTelefon(rs.getString("musteri_telefon"));
        s.setTeslimatAdresi(rs.getString("teslimat_adresi"));
        int kuryeId = rs.getInt("kurye_id");
        if (!rs.wasNull()) {
            s.setKuryeId(kuryeId);
            String kAd = rs.getString("kurye_ad");
            String kSoyad = rs.getString("kurye_soyad");
            if (kAd != null) s.setKuryeAdi(kAd + " " + kSoyad);
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
