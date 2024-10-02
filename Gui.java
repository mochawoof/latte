//latte Gui v1.1
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Gui {
    public static boolean on = false;
    public static boolean started = false;
    private static JFrame thisFrame;
    public static void error(String msg) {
        // New thread prevents the main one from freezing
        new Thread() {
            public void run() {
                JOptionPane.showMessageDialog(thisFrame, msg, "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }.start();
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("Latte " + Main.version);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (Main.server != null) {
                    Main.server.stop(0);
                }
                System.exit(0);
            }
        });
        thisFrame = frame;
        
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            Main.error(e);
        }
        
        JPanel panel = new JPanel();
        frame.add(panel, BorderLayout.CENTER);
        panel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.LEFT, VerticalFlowLayout.LEFT, 0, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Control Panel"));
        
        JLabel pathLabel = new JLabel("Path:");

        JButton pathButton = new JButton(Main.path);
        pathButton.setPreferredSize(new Dimension(300, pathButton.getPreferredSize().height));
        pathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!started) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        java.io.File chosenFile = chooser.getSelectedFile();
                        if (chosenFile.exists() && chosenFile.isDirectory()) {
                            pathButton.setText(chosenFile.getAbsolutePath());
                        }
                    }
                }
            }
        });

        JLabel portLabel = new JLabel("Port:");

        JTextField portField = new JTextField(Integer.toString(Main.port));
        portField.setPreferredSize(new Dimension(300, portField.getPreferredSize().height));

        JButton startStopButton = new JButton("Start server");
        startStopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!started) {
                    Main.main(new String[] {pathButton.getText(), portField.getText()});
                    Gui.started = true;
                    startStopButton.setText("Stop");
                } else {
                    Main.server.stop(0);
                    Gui.started = false;
                    startStopButton.setText("Start");
                }
            }
        });

        panel.add(pathLabel);
        panel.add(pathButton);
        panel.add(portLabel);
        panel.add(portField);
        panel.add(startStopButton);
        
        frame.pack();
        frame.setVisible(true);
        on = true;
    }
}
