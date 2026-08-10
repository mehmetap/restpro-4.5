import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RestoranBilgiDialog extends JDialog {

    private final RestoranDAO restoranDAO;
    private RestoranBilgisi bilgi;

    private final JTextField txtAd = new JTextField(25);
    private final JTextField txtAdres = new JTextField(25);
    private final JTextField txtTelefon = new JTextField(25);
    private final JTextField txtEmail = new JTextField(25);
    private final JTextField txtVergiNo = new JTextField(25);
    private final JTextField txtCalismaSaatleri = new JTextField(25);

    public RestoranBilgiDialog(Frame owner, RestoranDAO restoranDAO) {
        super(owner, "Restoran Bilgileri", true);
        this.restoranDAO = restoranDAO;
        setSize(420, 320);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(6, 2, 6, 6));
        form.add(new JLabel("Restoran Adı:")); form.add(txtAd);
        form.add(new JLabel("Adres:")); form.add(txtAdres);
        form.add(new JLabel("Telefon:")); form.add(txtTelefon);
        form.add(new JLabel("E-posta:")); form.add(txtEmail);
        form.add(new JLabel("Vergi No:")); form.add(txtVergiNo);
        form.add(new JLabel("Çalışma Saatleri:")); form.add(txtCalismaSaatleri);
        add(form, BorderLayout.CENTER);

        JButton btnKaydet = new JButton("Kaydet");
        btnKaydet.addActionListener(e -> kaydet());
        JPanel altPanel = new JPanel();
        altPanel.add(btnKaydet);
        add(altPanel, BorderLayout.SOUTH);

        verileriYukle();
    }

    private void verileriYukle() {
        try {
            bilgi = restoranDAO.bilgiGetir();
            if (bilgi != null) {
                txtAd.setText(bilgi.getRestoranAdi());
                txtAdres.setText(bilgi.getAdres());
                txtTelefon.setText(bilgi.getTelefon());
                txtEmail.setText(bilgi.getEmail());
                txtVergiNo.setText(bilgi.getVergiNo());
                txtCalismaSaatleri.setText(bilgi.getCalismaSaatleri());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Bilgiler yüklenemedi:\n" + ex.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void kaydet() {
        if (bilgi == null) {
            JOptionPane.showMessageDialog(this, "Kayıtlı restoran bulunamadı.");
            return;
        }
        bilgi.setRestoranAdi(txtAd.getText());
        bilgi.setAdres(txtAdres.getText());
        bilgi.setTelefon(txtTelefon.getText());
        bilgi.setEmail(txtEmail.getText());
        bilgi.setVergiNo(txtVergiNo.getText());
        bilgi.setCalismaSaatleri(txtCalismaSaatleri.getText());
        try {
            restoranDAO.bilgiGuncelle(bilgi);
            JOptionPane.showMessageDialog(this, "Restoran bilgileri güncellendi.");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Kaydedilemedi:\n" + ex.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}
