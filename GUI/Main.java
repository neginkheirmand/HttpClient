package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;




public class Main {
    public static void main(String[] args) {
        new CreateGUI();

        JFrame testFrame = new JFrame("NAME");
        testFrame.setSize(new Dimension(200,300));
        testFrame.setVisible(true);
        testFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        JTabbedPane jTabbedPaneTest = new JTabbedPane();
//        JPanel testPanel1 = new JPanel();
//        testPanel1.setBackground(Color.BLUE);
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
//        testFrame.add(jTabbedPaneTest);

        Object elements[][] = {
                { new Font("Serif", Font.BOLD , 15), new java.awt.Color(47 , 198, 102), "POST" },
                { new Font("Serif", Font.BOLD , 15), new java.awt.Color(239, 255, 20), "PATCH" },
                { new Font("Serif", Font.BOLD , 15), new java.awt.Color(255 , 161, 20), "PUT" },
                { new Font("Serif", Font.BOLD , 15), new java.awt.Color(255,20,20), "DELETE" },
                { new Font("Serif", Font.BOLD , 15), new java.awt.Color( 123, 104, 238), "GET" }

        };
        ListCellRenderer renderer = new CellRendererForComboBox();
//        String [] languages = {"hello","there"};
        JComboBox comboBox = new JComboBox(elements);
        comboBox.setRenderer(renderer);
        testFrame.add(comboBox, BorderLayout.CENTER);
        testFrame.pack();
    }
}