import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MusteriKayitDialog extends JDialog {

    private final MusteriDAO musteriDAO = new MusteriDAO();

    private final JTextField txtAd = new JTextField(20);
    private final JTextField txtSoyad = new JTextField(20);
    private final JTextField txtTelefon = new JTextField(20);
    private final JTextField txtEmail = new JTextField(20);
    private final JTextField txtAdres = new JTextField(20);
    private final JPasswordField txtSifre = new JPasswordField(20);

    public MusteriKayitDialog(Frame owner) {
        super(owner, "Yeni Müşteri Kaydı", true);
        setSize(400, 320);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(6, 2, 6, 6));
        form.add(new JLabel("Ad:")); form.add(txtAd);
        form.add(new JLabel("Soyad:")); form.add(txtSoyad);
        form.add(new JLabel("Telefon:")); form.add(txtTelefon);
        form.add(new JLabel("E-posta:")); form.add(txtEmail);
        form.add(new JLabel("Adres:")); form.add(txtAdres);
        form.add(new JLabel("Şifre:")); form.add(txtSifre);
        add(form, BorderLayout.CENTER);

        JButton btnKaydet = new JButton("Kayıt Ol");
        btnKaydet.addActionListener(e -> kaydet());
        JPanel altPanel = new JPanel();
        altPanel.add(btnKaydet);
        add(altPanel, BorderLayout.SOUTH);
    }

    private void kaydet() {
        if (txtAd.getText().isBlank() || txtTelefon.getText().isBlank() || txtSifre.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Ad, telefon ve şifre zorunludur.");
            return;
        }
        Musteri m = new Musteri();
        m.setAd(txtAd.getText());
        m.setSoyad(txtSoyad.getText());
        m.setTelefon(txtTelefon.getText());
        m.setEmail(txtEmail.getText());
        m.setAdres(txtAdres.getText());
        try {
            int id = musteriDAO.kayitOl(m, new String(txtSifre.getPassword()));
            if (id > 0) {
                JOptionPane.showMessageDialog(this, "Kayıt başarılı! Müşteri No: " + id);
                dispose();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Kayıt yapılamadı:\n" + ex.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}
