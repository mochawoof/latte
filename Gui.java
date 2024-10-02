//latte Gui v1.1
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Gui {
    public static boolean on = false;
    public static boolean started = false;
    public static void error(String msg) {
        JOptionPane.showMessageDialog(null, msg, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
    public static void main(String[] args) {
        on = true;
        JFrame frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("Latte " + Main.version);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            Main.error(e);
        }
        
        frame.setVisible(true);
        
        JPanel panel = new JPanel();
        frame.add(panel, BorderLayout.CENTER);
        panel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.LEFT, VerticalFlowLayout.LEFT, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Control Panel"));
        
        JLabel pathLabel = new JLabel("Path:");
        JTextField pathField = new JTextField(Main.path);
        pathField.setPreferredSize(new Dimension(300, pathField.getPreferredSize().height));
        JLabel portLabel = new JLabel("Port:");
        JTextField portField = new JTextField(Integer.toString(Main.port));
        portField.setPreferredSize(new Dimension(300, portField.getPreferredSize().height));
        JButton startStopButton = new JButton("Start server");
        startStopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!started) {
                    Main.main(new String[] {pathField.getText(), portField.getText()});
                    Gui.started = true;
                    startStopButton.setText("Stop");
                } else {
                    Main.server.stop(0);
                    frame.dispose();
                }
            }
        });
        panel.add(pathLabel);
        panel.add(pathField);
        panel.add(portLabel);
        panel.add(portField);
        panel.add(startStopButton);
        
        frame.pack();
        frame.revalidate();
        frame.repaint();
    }
}
