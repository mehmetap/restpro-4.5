import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Siparis {
    private int id;
    private int musteriId;
    private Integer kuryeId;
    private String siparisNo;
    private String musteriAdi;
    private String musteriTelefon;
    private String teslimatAdresi;
    private String kuryeAdi;
    private String kuryeTelefon;
    private SiparisDurumu durum;
    private BigDecimal tutar;
    private String odemeYontemi;
    private String siparisDetayi;
    private LocalDateTime olusturmaTarihi;
    private LocalDateTime guncellemeTarihi;

    public int getMusteriId() { return musteriId; }
    public void setMusteriId(int musteriId) { this.musteriId = musteriId; }
    public Integer getKuryeId() { return kuryeId; }
    public void setKuryeId(Integer kuryeId) { this.kuryeId = kuryeId; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSiparisNo() { return siparisNo; }
    public void setSiparisNo(String siparisNo) { this.siparisNo = siparisNo; }

    public String getMusteriAdi() { return musteriAdi; }
    public void setMusteriAdi(String musteriAdi) { this.musteriAdi = musteriAdi; }

    public String getMusteriTelefon() { return musteriTelefon; }
    public void setMusteriTelefon(String musteriTelefon) { this.musteriTelefon = musteriTelefon; }

    public String getTeslimatAdresi() { return teslimatAdresi; }
    public void setTeslimatAdresi(String teslimatAdresi) { this.teslimatAdresi = teslimatAdresi; }

    public String getKuryeAdi() { return kuryeAdi; }
    public void setKuryeAdi(String kuryeAdi) { this.kuryeAdi = kuryeAdi; }

    public String getKuryeTelefon() { return kuryeTelefon; }
    public void setKuryeTelefon(String kuryeTelefon) { this.kuryeTelefon = kuryeTelefon; }

    public SiparisDurumu getDurum() { return durum; }
    public void setDurum(SiparisDurumu durum) { this.durum = durum; }

    public BigDecimal getTutar() { return tutar; }
    public void setTutar(BigDecimal tutar) { this.tutar = tutar; }

    public String getOdemeYontemi() { return odemeYontemi; }
    public void setOdemeYontemi(String odemeYontemi) { this.odemeYontemi = odemeYontemi; }

    public String getSiparisDetayi() { return siparisDetayi; }
    public void setSiparisDetayi(String siparisDetayi) { this.siparisDetayi = siparisDetayi; }

    public LocalDateTime getOlusturmaTarihi() { return olusturmaTarihi; }
    public void setOlusturmaTarihi(LocalDateTime olusturmaTarihi) { this.olusturmaTarihi = olusturmaTarihi; }

    public LocalDateTime getGuncellemeTarihi() { return guncellemeTarihi; }
    public void setGuncellemeTarihi(LocalDateTime guncellemeTarihi) { this.guncellemeTarihi = guncellemeTarihi; }
}
