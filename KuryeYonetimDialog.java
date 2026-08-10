import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class KuryeYonetimDialog extends JDialog {

    private final KuryeDAO kuryeDAO;
    private final DefaultTableModel tableModel;
    private final JTable tablo;

    public KuryeYonetimDialog(Frame owner, KuryeDAO kuryeDAO) {
        super(owner, "Kurye Yönetimi", true);
        this.kuryeDAO = kuryeDAO;
        setSize(600, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(8, 8));

        String[] kolonlar = {"ID", "Ad Soyad", "Telefon", "Araç Tipi", "Durum"};
        tableModel = new DefaultTableModel(kolonlar, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tablo = new JTable(tableModel);
        add(new JScrollPane(tablo), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(1, 5, 4, 4));
        JTextField txtAd = new JTextField();
        JTextField txtSoyad = new JTextField();
        JTextField txtTelefon = new JTextField();
        JTextField txtArac = new JTextField();
        JButton btnEkle = new JButton("Kurye Ekle");
        formPanel.add(txtAd);
        formPanel.add(txtSoyad);
        formPanel.add(txtTelefon);
        formPanel.add(txtArac);
        formPanel.add(btnEkle);

        btnEkle.addActionListener(e -> {
            if (txtAd.getText().isBlank() || txtTelefon.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Ad ve telefon zorunludur.");
                return;
            }
            Kurye k = new Kurye();
            k.setAd(txtAd.getText());
            k.setSoyad(txtSoyad.getText());
            k.setTelefon(txtTelefon.getText());
            k.setAracTipi(txtArac.getText());
            k.setDurum("MUSAIT");
            try {
                kuryeDAO.kuryeEkle(k);
                txtAd.setText(""); txtSoyad.setText(""); txtTelefon.setText(""); txtArac.setText("");
                kuryeleriYukle();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Kurye eklenemedi:\n" + ex.getMessage(),
                        "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel ustFormWrapper = new JPanel(new BorderLayout());
        ustFormWrapper.add(new JLabel("  Ad          Soyad          Telefon          Araç Tipi"), BorderLayout.NORTH);
        ustFormWrapper.add(formPanel, BorderLayout.CENTER);
        add(ustFormWrapper, BorderLayout.NORTH);

        JPanel altPanel = new JPanel();
        JButton btnMusait = new JButton("Müsait Yap");
        JButton btnGorevde = new JButton("Görevde Yap");
        JButton btnIzinli = new JButton("İzinli Yap");
        altPanel.add(btnMusait);
        altPanel.add(btnGorevde);
        altPanel.add(btnIzinli);
        add(altPanel, BorderLayout.SOUTH);

        btnMusait.addActionListener(e -> durumGuncelle("MUSAIT"));
        btnGorevde.addActionListener(e -> durumGuncelle("GOREVDE"));
        btnIzinli.addActionListener(e -> durumGuncelle("IZINLI"));

        kuryeleriYukle();
    }

    private void kuryeleriYukle() {
        try {
            List<Kurye> kuryeler = kuryeDAO.tumKuryeleriGetir();
            tableModel.setRowCount(0);
            for (Kurye k : kuryeler) {
                tableModel.addRow(new Object[]{k.getId(), k.getAdSoyad(), k.getTelefon(), k.getAracTipi(), k.getDurum()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Kuryeler yüklenemedi:\n" + ex.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void durumGuncelle(String yeniDurum) {
        int satir = tablo.getSelectedRow();
        if (satir == -1) {
            JOptionPane.showMessageDialog(this, "Önce bir kurye seçin.");
            return;
        }
        int id = (int) tableModel.getValueAt(satir, 0);
        try {
            kuryeDAO.durumGuncelle(id, yeniDurum);
            kuryeleriYukle();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Güncellenemedi:\n" + ex.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}
