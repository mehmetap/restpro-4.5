public class Kurye {
    private int id;
    private String ad;
    private String soyad;
    private String telefon;
    private String aracTipi;
    private String durum; // MUSAIT, GOREVDE, IZINLI

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }

    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getAracTipi() { return aracTipi; }
    public void setAracTipi(String aracTipi) { this.aracTipi = aracTipi; }

    public String getDurum() { return durum; }
    public void setDurum(String durum) { this.durum = durum; }

    public String getAdSoyad() { return ad + " " + soyad; }

    @Override
    public String toString() {
        return getAdSoyad() + " (" + telefon + ")";
    }
}
