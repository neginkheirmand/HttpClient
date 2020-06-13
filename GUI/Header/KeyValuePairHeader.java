package GUI.Header;

import GUI.CreateGUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

public class KeyValuePairHeader extends JPanel{
    private static CreateGUI guiEnviornment;

    private JTextField name;
    private JTextField value;
    private JCheckBox enabled;

    public KeyValuePairHeader(CreateGUI gui, Color colorOfThemeBackground2, Color colorOfThemeBackground1, String colorOfThemeForground, String headerName, String headerValue, boolean isSelected, HeaderPanel headerPanel){
        super();
        guiEnviornment=gui;
        JPanel newKeyValuePair = this;
        headerPanel.getHeaders().add(this);
//        newKeyValuePair.setPreferredSize(new Dimension());
        newKeyValuePair.setLayout(new FlowLayout());
        newKeyValuePair.setBackground(colorOfThemeBackground2);
//        //first component the 3 lines
        JLabel _3_lines = new JLabel(new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\3-grey-lines1-icon.png"));
        newKeyValuePair.add(_3_lines);
        //then the JTextField for the Header
        JTextField headerTextField;
        if(headerName==null) {
            headerTextField = new JTextField("New Header");
        }else{
            headerTextField = new JTextField(headerName);
        }
        name = headerTextField;
        headerTextField.setPreferredSize(new Dimension(150, 35));
        headerTextField.setFont(new Font("Serif", Font.PLAIN, 15));
        headerTextField.setForeground(colorOfThemeBackground1);
        headerTextField.setBackground(colorOfThemeBackground2);
        headerTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorOfThemeBackground1));
//        boolean alreadyCreated = false;
        headerTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("on header");
                if(headerTextField.getText().equals("New Header")) {
                    headerTextField.setText(" ");
                }
                //and add a new Pair of Header and values
                System.out.println(" headerPanel.getHeaders().indexOf(newKeyValuePair) ="+headerPanel.getHeaders().indexOf(newKeyValuePair));
                System.out.println("headerPanel.getHeaders().size() = "+headerPanel.getHeaders().size());

                if(headerPanel.getHeaders().indexOf(newKeyValuePair)==headerPanel.getHeaders().size()-1) {
                    GridBagConstraints headerConstraints = new GridBagConstraints();
                    headerConstraints.gridx = 0;
                    headerConstraints.gridy = headerPanel.getHeaders().indexOf(newKeyValuePair)+1;
                    headerConstraints.weightx = 0;
                    headerConstraints.weighty = 0;
//        headerConstraints.ipadx = 0;
//        headerConstraints.ipady = 0;
                    headerConstraints.insets = new Insets(0, 0, 0, 0);
                    headerConstraints.fill = GridBagConstraints.HORIZONTAL;
                    headerConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
                    headerPanel.createHeaderTab( headerConstraints, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, gui, null, null, true);
                    headerPanel.setHeaderToRequest();
                    gui.getMainFrame().revalidate();
                    gui.getMainFrame().repaint();
                }
            }
        });
        newKeyValuePair.add(headerTextField);
        //then the JTextField for the value
        JTextField valueTextField;
        if(headerValue==null) {
            valueTextField = new JTextField("New Value");
        }else{
            valueTextField = new JTextField(headerValue);
        }
        value = valueTextField;
        valueTextField.setPreferredSize(new Dimension(150, 35));
        valueTextField.setFont(new Font("Serif", Font.PLAIN, 15));
        valueTextField.setForeground(colorOfThemeBackground1);
        valueTextField.setBackground(colorOfThemeBackground2);
        valueTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorOfThemeBackground1));
        valueTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("on value");
                if(valueTextField.getText().equals("New Value")) {
                    valueTextField.setText(" ");
                }
                //and add a new Pair of Header and values
                if(headerPanel.getHeaders().indexOf(newKeyValuePair)==headerPanel.getHeaders().size()-1) {
                    GridBagConstraints headerConstraints = new GridBagConstraints();
                    headerConstraints.gridx = 0;
                    headerConstraints.gridy = headerPanel.getHeaders().indexOf(newKeyValuePair)+1;
                    headerConstraints.weightx = 0;
                    headerConstraints.weighty = 0;
//        headerConstraints.ipadx = 0;
//        headerConstraints.ipady = 0;
                    headerConstraints.insets = new Insets(0, 0, 0, 0);
                    headerConstraints.fill = GridBagConstraints.HORIZONTAL;
                    headerConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
                    headerPanel.createHeaderTab( headerConstraints, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, gui, null, null, true);
                    headerPanel.setHeaderToRequest();
                    gui.getMainFrame().revalidate();
                    gui.getMainFrame().repaint();
                }
            }
        });
        newKeyValuePair.add(valueTextField);
        //now the JCheckBox
        JCheckBox checkBox = new JCheckBox(" ", isSelected);
        checkBox.setBackground(colorOfThemeBackground2);
        checkBox.setOpaque(false);
        enabled = checkBox;
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                headerPanel.setHeaderToRequest();
            }
        });
        newKeyValuePair.add(checkBox);
        //now the trash icon Button
        JButton trash = new JButton(new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\delete-icon1.png"));
        trash.setBackground(colorOfThemeBackground2);
        trash.setOpaque(false);
        trash.setPreferredSize(new Dimension(18, 18));
        trash.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                headerPanel.remove(newKeyValuePair);
                headerPanel.getHeaders().remove(newKeyValuePair);
                System.out.println("size of headers"+headerPanel.getHeaders().size());
                if (/*params.indexOf(thisJPanel)==0 && */headerPanel.getHeaders().size()==0) {
                    GridBagConstraints headerConstraints = new GridBagConstraints();
                    headerConstraints.gridx = 0;
                    headerConstraints.gridy = 0;
                    headerConstraints.weightx = 0;
                    headerConstraints.weighty = 0;
//        headerConstraints.ipadx = 0;
//        headerConstraints.ipady = 0;
                    headerConstraints.insets = new Insets(0, 0, 0, 0);
                    headerConstraints.fill = GridBagConstraints.HORIZONTAL;
                    headerConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
                    headerPanel.createHeaderTab( headerConstraints, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, gui, null, null, true);
                }
                headerPanel.setHeaderToRequest();
                guiEnviornment.getMainFrame().revalidate();
                guiEnviornment.getMainFrame().repaint();
            }
        });
        newKeyValuePair.add(trash);

        headerTextField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLabel(e);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLabel(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLabel(e);
            }

            private void updateLabel(DocumentEvent e) {
                headerPanel.setHeaderToRequest();
            }
        });
        valueTextField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLabel(e);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLabel(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLabel(e);
            }

            private void updateLabel(DocumentEvent e) {
                headerPanel.setHeaderToRequest();
            }
        });
    }


    public JTextField getname() {
        return name;
    }

    public JTextField getValue() {
        return value;
    }

    public JCheckBox getEnabled() {
        return enabled;
    }
}
