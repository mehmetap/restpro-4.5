public enum SiparisDurumu {
    ONAY_BEKLIYOR("Onay Bekliyor", 0),
    ONAYLANDI("Onaylandı", 1),
    HAZIRLANIYOR("Hazırlanıyor", 2),
    HAZIR("Hazır", 3),
    KURYEDE("Kurye Yolda", 4),
    TESLIM_EDILDI("Teslim Edildi", 5),
    IPTAL("İptal / İade Edildi", -1);

    private final String etiket;
    private final int adim; // ilerleme cubugu icin sira numarasi

    SiparisDurumu(String etiket, int adim) {
        this.etiket = etiket;
        this.adim = adim;
    }

    public String getEtiket() { return etiket; }
    public int getAdim() { return adim; }

    public static SiparisDurumu fromDbValue(String dbValue) {
        return SiparisDurumu.valueOf(dbValue);
    }
}
