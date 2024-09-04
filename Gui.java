import javax.swing.*;
import java.awt.BorderLayout;
class Gui extends JFrame {
    private void setLaf(String lafClass) {
        try {
            UIManager.setLookAndFeel(lafClass);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            Logger.error(e);
        }
    }
    public Gui() {
        setTitle("Latte");
        setLaf("com.formdev.flatlaf.FlatLightLaf");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200);
        
        // Add components
        setLayout(new BorderLayout());
        
        JButton stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        add(stopButton, BorderLayout.PAGE_START);
        
        setVisible(true);
        setIconImage(Resources.getAsImage("icon.png"));
    }
}