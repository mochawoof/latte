import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File;

import java.net.URI;
public class Gui {
    public static boolean on = false;
    public static boolean started = false;
    private static JFrame thisFrame;
    public static void error(String msg) {
        // New thread prevents the main one from freezing
        new Thread() {
            public void run() {
                JOptionPane.showMessageDialog(thisFrame, msg, "Error", JOptionPane.ERROR_MESSAGE);
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
        frame.setIconImage(Resources.getAsImage("images/icon_128.png"));
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
        panel.add(pathLabel);

        JButton pathButton = new JButton(Main.path);
        pathButton.setPreferredSize(new Dimension(300, pathButton.getPreferredSize().height));
        pathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File chosenFile = chooser.getSelectedFile();
                    if (chosenFile.exists() && chosenFile.isDirectory()) {
                        pathButton.setText(chosenFile.getAbsolutePath());
                    }
                }
            }
        });
        panel.add(pathButton);

        JLabel portLabel = new JLabel("Port:");
        panel.add(portLabel);

        JTextField portField = new JTextField(Integer.toString(Main.port));
        portField.setPreferredSize(new Dimension(300, portField.getPreferredSize().height));
        panel.add(portField);

        JPanel bottomButtonsPanel = new JPanel();
        bottomButtonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.add(bottomButtonsPanel);
        
        // Preload icons
        // Icons are 20x20 to enlarge button
        ImageIcon playIcon = Resources.getAsImageIcon("images/play.png");
        ImageIcon stopIcon = Resources.getAsImageIcon("images/stop.png");
        
        JButton startStopButton = new JButton("Start Server");
        startStopButton.setIcon(playIcon);
        
        // openInBrowserButton is defined before startStopButton's action listener to prevent syntax error
        JButton openInBrowserButton = new JButton("Open in Browser");
        openInBrowserButton.setIcon(Resources.getAsImageIcon("images/open_in_browser.png"));
        
        startStopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!started) {
                    Main.main(new String[] {pathButton.getText(), portField.getText()});
                    Gui.started = true;
                    
                    startStopButton.setIcon(stopIcon);
                    
                    pathButton.setEnabled(false);
                    portField.setEnabled(false);
                    openInBrowserButton.setEnabled(true);
                    
                    startStopButton.setText("Stop");
                } else {
                    if (Main.server != null) {
                        Main.server.stop(0);
                    }
                    Gui.started = false;
                    
                    startStopButton.setIcon(playIcon);
                    
                    pathButton.setEnabled(true);
                    portField.setEnabled(true);
                    openInBrowserButton.setEnabled(false);
                    
                    startStopButton.setText("Start");
                }
            }
        });
        bottomButtonsPanel.add(startStopButton);
        
        openInBrowserButton.setEnabled(false);
        openInBrowserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("http://localhost:" + portField.getText()));
                } catch (Exception ex) {
                    Main.error(ex);
                }
            }
        });
        bottomButtonsPanel.add(openInBrowserButton);
        
        frame.pack();
        frame.setVisible(true);
        on = true;
    }
}
