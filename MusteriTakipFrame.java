import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class MusteriTakipFrame extends JFrame {

    private final SiparisTakipDAO takipDAO = new SiparisTakipDAO();

    private final JTextField txtSiparisNo = new JTextField(15);
    private final JButton btnSorgula = new JButton("Siparişimi Sorgula");
    private final JLabel lblSiparisNo = new JLabel("-");
    private final JLabel lblMusteri = new JLabel("-");
    private final JLabel lblAdres = new JLabel("-");
    private final JLabel lblTutar = new JLabel("-");
    private final JLabel lblKurye = new JLabel("-");
    private final JLabel lblSonGuncelleme = new JLabel("-");
    private final JLabel[] adimEtiketleri = new JLabel[5];
    private final String[] adimlar = {"Onaylandı", "Hazırlanıyor", "Hazır", "Kurye Yolda", "Teslim Edildi"};

    private String izlenenSiparisNo = null;
    private Timer canliTakipZamanlayici;

    public MusteriTakipFrame() {
        super("Sipariş Takip - Canlı Durum");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(aramaPaneliOlustur(), BorderLayout.NORTH);
        add(adimPaneliOlustur(), BorderLayout.CENTER);
        add(detayPaneliOlustur(), BorderLayout.SOUTH);

        btnSorgula.addActionListener(e -> siparisiSorgula());
        txtSiparisNo.addActionListener(e -> siparisiSorgula());

        // her 4 saniyede bir izlenen siparisin durumunu otomatik tazele -> canli takip
        canliTakipZamanlayici = new Timer(true);
        canliTakipZamanlayici.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                if (izlenenSiparisNo != null) {
                    SwingUtilities.invokeLater(() -> siparisiGuncelle(izlenenSiparisNo));
                }
            }
        }, 4000, 4000);
    }

    private JPanel aramaPaneliOlustur() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Sipariş Numaranız:"));
        panel.add(txtSiparisNo);
        panel.add(btnSorgula);
        JButton btnKayit = new JButton("Yeni Müşteri Kaydı");
        btnKayit.addActionListener(e -> new MusteriKayitDialog(this).setVisible(true));
        panel.add(btnKayit);
        return panel;
    }

    private JPanel adimPaneliOlustur() {
        JPanel panel = new JPanel(new GridLayout(1, adimlar.length, 4, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Sipariş Durumu"));
        for (int i = 0; i < adimlar.length; i++) {
            JLabel lbl = new JLabel(adimlar[i], SwingConstants.CENTER);
            lbl.setOpaque(true);
            lbl.setBackground(Color.LIGHT_GRAY);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 12f));
            adimEtiketleri[i] = lbl;
            panel.add(lbl);
        }
        return panel;
    }

    private JPanel detayPaneliOlustur() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 4, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Sipariş Detayı"));
        panel.add(new JLabel("Sipariş No:")); panel.add(lblSiparisNo);
        panel.add(new JLabel("Müşteri:")); panel.add(lblMusteri);
        panel.add(new JLabel("Teslimat Adresi:")); panel.add(lblAdres);
        panel.add(new JLabel("Tutar:")); panel.add(lblTutar);
        panel.add(new JLabel("Kurye:")); panel.add(lblKurye);
        panel.add(new JLabel("Son Güncelleme:")); panel.add(lblSonGuncelleme);
        return panel;
    }

    private void siparisiSorgula() {
        String no = txtSiparisNo.getText().trim();
        if (no.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen sipariş numarası girin.");
            return;
        }
        izlenenSiparisNo = no;
        siparisiGuncelle(no);
    }

    private void siparisiGuncelle(String siparisNo) {
        try {
            Siparis s = takipDAO.siparisNoIleGetir(siparisNo);
            if (s == null) {
                JOptionPane.showMessageDialog(this, "Bu numarayla bir sipariş bulunamadı.");
                izlenenSiparisNo = null;
                return;
            }
            lblSiparisNo.setText(s.getSiparisNo());
            lblMusteri.setText(s.getMusteriAdi() + " (" + s.getMusteriTelefon() + ")");
            lblAdres.setText(s.getTeslimatAdresi() == null ? "-" : s.getTeslimatAdresi());
            lblTutar.setText(s.getTutar() + " TL - " + s.getOdemeYontemi());
            lblKurye.setText(s.getKuryeAdi() == null ? "Henüz atanmadı" : s.getKuryeAdi() + " (" + s.getKuryeTelefon() + ")");
            lblSonGuncelleme.setText(String.valueOf(s.getGuncellemeTarihi()));
            adimlariGuncelle(s.getDurum());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Sorgulama sırasında hata oluştu:\n" + ex.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adimlariGuncelle(SiparisDurumu durum) {
        int aktifAdim = durum.getAdim(); // 1..5 basarili, 0 = onay bekliyor, -1 = iptal
        for (int i = 0; i < adimEtiketleri.length; i++) {
            int buAdim = i + 1; // 1'den basliyor: Onaylandi=1 .. Teslim Edildi=5
            if (durum == SiparisDurumu.IPTAL) {
                adimEtiketleri[i].setBackground(Color.LIGHT_GRAY);
            } else if (buAdim <= aktifAdim) {
                adimEtiketleri[i].setBackground(new Color(76, 175, 80)); // yesil
            } else {
                adimEtiketleri[i].setBackground(Color.LIGHT_GRAY);
            }
        }
        if (durum == SiparisDurumu.IPTAL) {
            adimEtiketleri[0].setBackground(new Color(211, 47, 47)); // kirmizi uyari
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MusteriTakipFrame().setVisible(true));
    }
}
