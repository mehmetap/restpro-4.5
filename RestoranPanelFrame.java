import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RestoranPanelFrame extends JFrame {

    private final SiparisDAO siparisDAO = new SiparisDAO();
    private final KuryeDAO kuryeDAO = new KuryeDAO();
    private final RestoranDAO restoranDAO = new RestoranDAO();

    private final DefaultTableModel tableModel;
    private final JTable tablo;
    private final JLabel durumEtiketi = new JLabel(" ");
    private SiparisDurumu aktifFiltre = null; // null = Hepsi

    private final JButton btnOnayla = new JButton("Siparişi Onayla");
    private final JButton btnHazir = new JButton("Hazır");
    private final JButton btnKuryeyeVer = new JButton("Kuryeye Ver");
    private final JButton btnTeslimEdildi = new JButton("Teslim Edildi");
    private final JButton btnYenile = new JButton("Yenile");

    public RestoranPanelFrame() {
        super("Restoran Paneli - Paket Takip Yönetimi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 700);
        setLocationRelativeTo(null);

        String[] kolonlar = {"ID", "Sipariş No", "Müşteri", "Telefon", "Durum", "Tutar", "Ödeme", "Kurye", "Oluşturma"};
        tableModel = new DefaultTableModel(kolonlar, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tablo = new JTable(tableModel);
        tablo.setRowHeight(26);
        tablo.getSelectionModel().addListSelectionListener(e -> guncelleButonDurumlari());

        setLayout(new BorderLayout(8, 8));
        add(ustPanelOlustur(), BorderLayout.NORTH);
        add(new JScrollPane(tablo), BorderLayout.CENTER);
        add(altPanelOlustur(), BorderLayout.SOUTH);

        btnOnayla.addActionListener(e -> durumDegistir(SiparisDurumu.ONAYLANDI));
        btnHazir.addActionListener(e -> durumDegistir(SiparisDurumu.HAZIR));
        btnKuryeyeVer.addActionListener(e -> kuryeyeVer());
        btnTeslimEdildi.addActionListener(e -> durumDegistir(SiparisDurumu.TESLIM_EDILDI));
        btnYenile.addActionListener(e -> siparisleriYukle());

        siparisleriYukle();
        guncelleButonDurumlari();

        // Canli takip: her 5 saniyede bir listeyi tazele
        Timer zamanlayici = new Timer(true);
        zamanlayici.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                SwingUtilities.invokeLater(RestoranPanelFrame.this::siparisleriYukle);
            }
        }, 5000, 5000);
    }

    private JPanel ustPanelOlustur() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel sekmeler = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sekmeler.add(sekmeButonu("Hepsi", null));
        sekmeler.add(sekmeButonu("Onay Bekliyor", SiparisDurumu.ONAY_BEKLIYOR));
        sekmeler.add(sekmeButonu("Onaylandı", SiparisDurumu.ONAYLANDI));
        sekmeler.add(sekmeButonu("Hazırlanıyor", SiparisDurumu.HAZIRLANIYOR));
        sekmeler.add(sekmeButonu("Hazır", SiparisDurumu.HAZIR));
        sekmeler.add(sekmeButonu("Yolda", SiparisDurumu.KURYEDE));
        sekmeler.add(sekmeButonu("Teslim Edildi", SiparisDurumu.TESLIM_EDILDI));

        JPanel sagPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRestoranBilgisi = new JButton("Restoran Bilgileri");
        JButton btnKuryeYonetimi = new JButton("Kurye Yönetimi");
        btnRestoranBilgisi.addActionListener(e -> new RestoranBilgiDialog(this, restoranDAO).setVisible(true));
        btnKuryeYonetimi.addActionListener(e -> new KuryeYonetimDialog(this, kuryeDAO).setVisible(true));
        sagPanel.add(btnRestoranBilgisi);
        sagPanel.add(btnKuryeYonetimi);

        panel.add(sekmeler, BorderLayout.WEST);
        panel.add(sagPanel, BorderLayout.EAST);
        return panel;
    }

    private JButton sekmeButonu(String etiket, SiparisDurumu durum) {
        JButton b = new JButton(etiket);
        b.addActionListener(e -> {
            aktifFiltre = durum;
            siparisleriYukle();
        });
        return b;
    }

    private JPanel altPanelOlustur() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel butonlar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        butonlar.add(btnOnayla);
        butonlar.add(btnHazir);
        butonlar.add(btnKuryeyeVer);
        butonlar.add(btnTeslimEdildi);
        butonlar.add(btnYenile);
        panel.add(butonlar, BorderLayout.WEST);
        panel.add(durumEtiketi, BorderLayout.EAST);
        return panel;
    }

    private void siparisleriYukle() {
        try {
            List<Siparis> siparisler = (aktifFiltre == null)
                    ? siparisDAO.tumSiparisleriGetir()
                    : siparisDAO.durumaGoreGetir(aktifFiltre);

            tableModel.setRowCount(0);
            for (Siparis s : siparisler) {
                tableModel.addRow(new Object[]{
                        s.getId(), s.getSiparisNo(), s.getMusteriAdi(), s.getMusteriTelefon(),
                        s.getDurum().getEtiket(), s.getTutar(), s.getOdemeYontemi(),
                        s.getKuryeAdi() == null ? "-" : s.getKuryeAdi(), s.getOlusturmaTarihi()
                });
            }
            durumEtiketi.setText(siparisler.size() + " sipariş listelendi.");
        } catch (SQLException ex) {
            durumEtiketi.setText("Veritabanı hatası!");
            JOptionPane.showMessageDialog(this,
                    "Siparişler yüklenemedi:\n" + ex.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
        guncelleButonDurumlari();
    }

    private void guncelleButonDurumlari() {
        boolean secimVar = tablo.getSelectedRow() != -1;
        btnOnayla.setEnabled(secimVar);
        btnHazir.setEnabled(secimVar);
        btnKuryeyeVer.setEnabled(secimVar);
        btnTeslimEdildi.setEnabled(secimVar);
    }

    private int seciliSiparisId() {
        int satir = tablo.getSelectedRow();
        if (satir == -1) return -1;
        return (int) tableModel.getValueAt(satir, 0);
    }

    private void durumDegistir(SiparisDurumu yeniDurum) {
        int id = seciliSiparisId();
        if (id == -1) return;
        try {
            siparisDAO.durumGuncelle(id, yeniDurum);
            siparisleriYukle();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Durum güncellenemedi:\n" + ex.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void kuryeyeVer() {
        int id = seciliSiparisId();
        if (id == -1) return;
        try {
            List<Kurye> musaitKuryeler = kuryeDAO.musaitKuryeleriGetir();
            if (musaitKuryeler.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Müsait kurye bulunamadı.",
                        "Uyarı", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Kurye secilen = (Kurye) JOptionPane.showInputDialog(this,
                    "Siparişi teslim edecek kuryeyi seçin:", "Kurye Seç",
                    JOptionPane.PLAIN_MESSAGE, null, musaitKuryeler.toArray(), musaitKuryeler.get(0));
            if (secilen != null) {
                siparisDAO.kuryeAta(id, secilen.getId());
                siparisleriYukle();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Kurye atanamadı:\n" + ex.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RestoranPanelFrame().setVisible(true));
    }
}
