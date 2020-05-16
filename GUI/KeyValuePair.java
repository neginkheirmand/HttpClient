package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class KeyValuePair extends JPanel {
    private static CreateGUI guiEnviornment;


    public KeyValuePair(CreateGUI gui, JPanel query, Color colorOfThemeBackground2, Color colorOfThemeBackground1, String colorOfThemeForground, GridBagConstraints constraints){
        super();
        JPanel thisJPanel = this;
        guiEnviornment = gui;
        this.setLayout(new GridBagLayout());
        GridBagConstraints eachInfoConstraints = new GridBagConstraints();
        eachInfoConstraints.gridy=0;
        eachInfoConstraints.gridx=0;
        eachInfoConstraints.weightx=1;
        eachInfoConstraints.weighty=1;
        eachInfoConstraints.insets=new Insets(0,2,0,2);
        eachInfoConstraints.anchor=GridBagConstraints.FIRST_LINE_START;
        eachInfoConstraints.fill=GridBagConstraints.BOTH;

        this.setBackground(colorOfThemeBackground2);
//        //first component the 3 lines
        JLabel _3_lines = new JLabel(new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\3-purple-lines-icon.png"));
        this.add(_3_lines, eachInfoConstraints);
        eachInfoConstraints.gridx++;
        //then the JTextField for the name
        JTextField nameTextField = new JTextField(" New Name");
        nameTextField.setPreferredSize(new Dimension(150, 35));
        nameTextField.setFont(new Font("Serif", Font.PLAIN, 15));
        nameTextField.setForeground(colorOfThemeBackground1);
        nameTextField.setBackground(colorOfThemeBackground2);
        nameTextField.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, colorOfThemeBackground1));
        nameTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(nameTextField.getText().equals(" New Name")) {
                    nameTextField.setText(" ");
                    System.out.println("clicked on the name");
//                //and add a new Pair of name and values
                    GridBagConstraints newGridConstraints = (GridBagConstraints) (constraints.clone());
                    newGridConstraints.gridy = constraints.gridy + 1;
                    guiEnviornment.createQueryTab(query, newGridConstraints);
                    guiEnviornment.getMainFrame().revalidate();
                    guiEnviornment.getMainFrame().repaint();
                }
            }
        });
        this.add(nameTextField, eachInfoConstraints);
        eachInfoConstraints.gridx++;
        //then the JTextField for the value
        JTextField valueTextField = new JTextField("New Value");
        valueTextField.setPreferredSize(new Dimension(150, 35));
        valueTextField.setFont(new Font("Serif", Font.PLAIN, 15));
        valueTextField.setForeground(colorOfThemeBackground1);
        valueTextField.setBackground(colorOfThemeBackground2);
        valueTextField.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, colorOfThemeBackground1));
        valueTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(valueTextField.getText().equals("New Value")) {
                    valueTextField.setText(" ");
                    System.out.println("clicked on the value");
                    //and add a new Pair of name and values
                    GridBagConstraints newGridConstraints = (GridBagConstraints) (constraints.clone());
                    newGridConstraints.gridy = constraints.gridy + 1;
//                newGridConstraints.weightx=1;
//                newGridConstraints.weighty=1;
//                constraints.weightx=0;
//                constraints.weighty=0;
                    guiEnviornment.createQueryTab(query, newGridConstraints);
                    guiEnviornment.getMainFrame().revalidate();
                    guiEnviornment.getMainFrame().repaint();
                }
            }
        });
        this.add(valueTextField, eachInfoConstraints);
        eachInfoConstraints.gridx++;
        //now the JCheckBox
        JCheckBox checkBox = new JCheckBox(" ", true);
        checkBox.setBackground(colorOfThemeBackground2);
        checkBox.setOpaque(false);
        this.add(checkBox, eachInfoConstraints);
        eachInfoConstraints.gridx++;
        //now the trash icon Button
        JButton trash = new JButton(new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\delete-icon1.png"));
        trash.setBackground(colorOfThemeBackground2);
        trash.setOpaque(false);
        trash.setPreferredSize(new Dimension(18, 18));
        trash.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("user clicked on the trash JButton");
                query.remove(thisJPanel);
                if (constraints.gridy == 0) {
                    guiEnviornment.createQueryTab(query, constraints);
                }
                guiEnviornment.getMainFrame().revalidate();
                guiEnviornment.getMainFrame().repaint();
            }
        });
        this.add(trash, eachInfoConstraints);
    }


}
