package GUI;

import javax.imageio.ImageIO;
import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;


public class Main {
    public static void main(String[] args) {
        new CreateGUI();
//        if(SystemTray.isSupported()) {
//            System.out.println("system tray supported");
//        }



//        JEditorPane jep = new JEditorPane();
//        jep.setEditable(false);
//
//        try {
//            jep.setPage("http://www.google.com");
//        }catch (IOException e) {
//            jep.setContentType("text/html");
//            jep.setText("<html>Could not load</html>");
//        }
//
//        JScrollPane scrollPane = new JScrollPane(jep);
//        JFrame f = new JFrame("Test HTML");
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.getContentPane().add(scrollPane);
//        f.setPreferredSize(new Dimension(800,600));
//        f.setVisible(true);




//        JFrame testFrame = new JFrame("NAME");
//        testFrame.setPreferredSize(new Dimension(200,300));
//        testFrame.setVisible(true);
//        testFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        URL url = new URL("https://i.stack.imgur.com/NoFA0.jpg");
//        BufferedImage img = ImageIO.read(url);
//        ImageIcon icon = new ImageIcon(img);
//        JPanel testPanel1 = new JPanel();
//        testPanel1.setBackground(Color.BLACK);
//        testPanel1.setOpaque(true);

//        JPanel testPanel2 = new JPanel();
//        testPanel2.setBackground(Color.RED);
//        jTabbedPaneTest.add("test Panel1", new JScrollPane(testPanel1));
//        for(int i=0; i<200; i++){
//            JButton newButton = new JButton("newButton"+i);
//            newButton.setOpaque(true);
//            newButton.setBackground(Color.CYAN);
//            testPanel1.add(newButton);
//        }
//        jTabbedPaneTest.add("test Panel2", testPanel2);
//        testFrame.add(testPanel1);
//        testFrame.pack();
//
//        JFrame testFrame1 = new JFrame("NAME");
//        testFrame1.setPreferredSize(new Dimension(200,300));
//        testFrame1.setVisible(true);
//        testFrame1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        JPanel testPanel2 = new JPanel();
//        testPanel2.setBackground(Color.BLACK);
//        testPanel2.setOpaque(false);
//        testFrame1.add(testPanel2);
//        String [] languages = {"hello","there"};
//        JComboBox comboBox = new JComboBox(elements);
//        comboBox.setRenderer(renderer);
//        testFrame.add(comboBox, BorderLayout.CENTER);
//        testFrame1.pack();
//        testFrame1.setLocationRelativeTo(null);
    }
}