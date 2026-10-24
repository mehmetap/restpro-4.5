import javax.swing.SwingUtilities;

public class RestoranMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RestoranPanelFrame().setVisible(true));
    }
}
