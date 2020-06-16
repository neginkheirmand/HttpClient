package GUI.Query;

import GUI.CreateGUI;
import GUI.Request;

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

public class KeyValuePairQuery extends JPanel {
    private static CreateGUI guiEnviornment;

    private JTextField name;
    private JTextField value;
    private JCheckBox enabled;

    public KeyValuePairQuery(CreateGUI gui, QueryPanel query, Color colorOfThemeBackground2, Color colorOfThemeBackground1, String colorOfThemeForground, ArrayList<KeyValuePairQuery> params) {
        super();
        //we add this to the arraylist
        params.add(this);
        JPanel thisJPanel = this;
        guiEnviornment = gui;
        this.setLayout(new GridBagLayout());
        GridBagConstraints eachInfoConstraints = new GridBagConstraints();
        eachInfoConstraints.gridy = 0;
        eachInfoConstraints.gridx = 0;
        eachInfoConstraints.weightx = 1;
        eachInfoConstraints.weighty = 0;
        eachInfoConstraints.insets = new Insets(0, 2, 0, 2);
        eachInfoConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        eachInfoConstraints.fill = GridBagConstraints.BOTH;

        this.setBackground(colorOfThemeBackground2);
//        //first component the 3 lines
        JLabel _3_lines = new JLabel(new ImageIcon((new File(".").getAbsolutePath()) + "\\src\\GUI\\resource" + colorOfThemeForground + "\\3-purple-lines-icon.png"));
        this.add(_3_lines, eachInfoConstraints);
        eachInfoConstraints.gridx++;
        //then the JTextField for the name
        JTextField nameTextField = new JTextField(" New Name");
        name = nameTextField;
        nameTextField.setPreferredSize(new Dimension(150, 35));
        nameTextField.setFont(new Font("Serif", Font.PLAIN, 15));
        nameTextField.setForeground(colorOfThemeBackground1);
        nameTextField.setBackground(colorOfThemeBackground2);
        nameTextField.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, colorOfThemeBackground1));
        nameTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (nameTextField.getText().equals(" New Name")) {
                    nameTextField.setText("");
                }
                System.out.println("index of this ="+params.indexOf(thisJPanel)+"  and the size of params="+params.size());
                if (params.indexOf(thisJPanel) == params.size()-1) {
                    GridBagConstraints newGridConstraints = new GridBagConstraints();
                    newGridConstraints.gridx = 0;
                    newGridConstraints.gridy = params.indexOf(thisJPanel) + 1;
                    newGridConstraints.weightx = 1;
                    newGridConstraints.weighty = 0;
                    newGridConstraints.fill = GridBagConstraints.HORIZONTAL;
                    newGridConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
                    newGridConstraints.insets = new Insets(5, 0, 0, 5);
                    query.createQueryTab(newGridConstraints, gui, colorOfThemeBackground1, colorOfThemeBackground2, colorOfThemeForground);
                    query.setParamsForRequest();
                    guiEnviornment.getMainFrame().revalidate();
                    guiEnviornment.getMainFrame().repaint();
                }
            }
        });


        this.add(nameTextField, eachInfoConstraints);
        eachInfoConstraints.gridx++;
        //then the JTextField for the value
        JTextField valueTextField = new JTextField("New Value");
        value = valueTextField;
        valueTextField.setPreferredSize(new Dimension(150, 35));
        valueTextField.setFont(new Font("Serif", Font.PLAIN, 15));
        valueTextField.setForeground(colorOfThemeBackground1);
        valueTextField.setBackground(colorOfThemeBackground2);
        valueTextField.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, colorOfThemeBackground1));
        valueTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (valueTextField.getText().equals("New Value")) {
                    valueTextField.setText("");
                    System.out.println("clicked on the value");
                }
                if (params.indexOf(thisJPanel) == params.size()-1) {
                    GridBagConstraints newGridConstraints = new GridBagConstraints();
                    newGridConstraints.gridx = 0;
                    newGridConstraints.gridy = params.indexOf(thisJPanel)+1;
                    newGridConstraints.weightx = 1;
                    newGridConstraints.weighty = 0;
                    newGridConstraints.fill = GridBagConstraints.HORIZONTAL;
                    newGridConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
                    newGridConstraints.insets = new Insets(5, 0, 0, 5);
                    query.createQueryTab(newGridConstraints, gui, colorOfThemeBackground1, colorOfThemeBackground2, colorOfThemeForground);
                    query.setParamsForRequest();
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
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                query.setParamsForRequest();
            }
        });
        enabled = checkBox;
        this.add(checkBox, eachInfoConstraints);
        eachInfoConstraints.gridx++;
        //now the trash icon Button
        JButton trash = new JButton(new ImageIcon((new File(".").getAbsolutePath()) + "\\src\\GUI\\resource" + colorOfThemeForground + "\\delete-icon1.png"));
        trash.setBackground(colorOfThemeBackground2);
        trash.setOpaque(false);
        trash.setPreferredSize(new Dimension(18, 18));


        //now the change listeners
        //checkBox

        trash.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                query.remove(thisJPanel);
                params.remove(thisJPanel);
                System.out.println(params.size());
                if (/*params.indexOf(thisJPanel)==0 && */params.size()==0) {
                    GridBagConstraints queryTabConstraints = new GridBagConstraints();
                    queryTabConstraints.gridx = 0;
                    queryTabConstraints.gridy = 0;
                    queryTabConstraints.weightx = 1;
                    queryTabConstraints.weighty = 0;
                    queryTabConstraints.fill = GridBagConstraints.HORIZONTAL;
                    queryTabConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
                    queryTabConstraints.insets = new Insets(5, 0, 0, 5);
                    query.createQueryTab(queryTabConstraints, gui, colorOfThemeBackground1, colorOfThemeBackground2, colorOfThemeForground);
                }
                query.setParamsForRequest();
                guiEnviornment.getMainFrame().revalidate();
                guiEnviornment.getMainFrame().repaint();
            }
        });

        nameTextField.getDocument().addDocumentListener(new DocumentListener() {

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
                query.setParamsForRequest();
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
                query.setParamsForRequest();
            }
        });

        this.add(trash, eachInfoConstraints);
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

    //for the loading info

    public KeyValuePairQuery(CreateGUI gui, QueryPanel query, Color colorOfThemeBackground2, Color colorOfThemeBackground1, String colorOfThemeForground, ArrayList<KeyValuePairQuery> params, String loadName, String loadValue, boolean isSelected) {
        super();
        //we add this to the arraylist
        params.add(this);
        JPanel thisJPanel = this;
        guiEnviornment = gui;
        this.setLayout(new GridBagLayout());
        GridBagConstraints eachInfoConstraints = new GridBagConstraints();
        eachInfoConstraints.gridy = 0;
        eachInfoConstraints.gridx = 0;
        eachInfoConstraints.weightx = 1;
        eachInfoConstraints.weighty = 0;
        eachInfoConstraints.insets = new Insets(0, 2, 0, 2);
        eachInfoConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        eachInfoConstraints.fill = GridBagConstraints.BOTH;

        this.setBackground(colorOfThemeBackground2);
//        //first component the 3 lines
        JLabel _3_lines = new JLabel(new ImageIcon((new File(".").getAbsolutePath()) + "\\src\\GUI\\resource" + colorOfThemeForground + "\\3-purple-lines-icon.png"));
        this.add(_3_lines, eachInfoConstraints);
        eachInfoConstraints.gridx++;
        //then the JTextField for the name
        JTextField nameTextField;
        if(loadName!=null) {
            nameTextField = new JTextField(loadName);
        }else {
            nameTextField = new JTextField(" New Name");
        }
        name = nameTextField;
        nameTextField.setPreferredSize(new Dimension(150, 35));
        nameTextField.setFont(new Font("Serif", Font.PLAIN, 15));
        nameTextField.setForeground(colorOfThemeBackground1);
        nameTextField.setBackground(colorOfThemeBackground2);
        nameTextField.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, colorOfThemeBackground1));
        nameTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (nameTextField.getText().equals(" New Name")) {
                    nameTextField.setText("");
                }
                System.out.println("index of this ="+params.indexOf(thisJPanel)+"  and the size of params="+params.size());
                if (params.indexOf(thisJPanel) == params.size()-1) {
                    GridBagConstraints newGridConstraints = new GridBagConstraints();
                    newGridConstraints.gridx = 0;
                    newGridConstraints.gridy = params.indexOf(thisJPanel)+1;
                    newGridConstraints.weightx = 1;
                    newGridConstraints.weighty = 0;
                    newGridConstraints.fill = GridBagConstraints.HORIZONTAL;
                    newGridConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
                    newGridConstraints.insets = new Insets(5, 0, 0, 5);
                    query.createQueryTab(newGridConstraints, gui, colorOfThemeBackground1, colorOfThemeBackground2, colorOfThemeForground);
                    query.setParamsForRequest();
                    guiEnviornment.getMainFrame().revalidate();
                    guiEnviornment.getMainFrame().repaint();
                }
            }
        });


        this.add(nameTextField, eachInfoConstraints);
        eachInfoConstraints.gridx++;
        //then the JTextField for the value
        JTextField valueTextField;
        if(loadValue!=null) {
            valueTextField = new JTextField(loadValue);
        }else{
            valueTextField = new JTextField("New Value");
        }
        value = valueTextField;
        valueTextField.setPreferredSize(new Dimension(150, 35));
        valueTextField.setFont(new Font("Serif", Font.PLAIN, 15));
        valueTextField.setForeground(colorOfThemeBackground1);
        valueTextField.setBackground(colorOfThemeBackground2);
        valueTextField.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, colorOfThemeBackground1));
        valueTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (valueTextField.getText().equals("New Value")) {
                    valueTextField.setText("");
                }
                if (params.indexOf(thisJPanel) == params.size()-1) {
                    GridBagConstraints newGridConstraints = new GridBagConstraints();
                    newGridConstraints.gridx = 0;
                    newGridConstraints.gridy = params.indexOf(thisJPanel)+1;
                    newGridConstraints.weightx = 1;
                    newGridConstraints.weighty = 0;
                    newGridConstraints.fill = GridBagConstraints.HORIZONTAL;
                    newGridConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
                    newGridConstraints.insets = new Insets(5, 0, 0, 5);

                    query.createQueryTab(newGridConstraints, gui, colorOfThemeBackground1, colorOfThemeBackground2, colorOfThemeForground);
                    query.setParamsForRequest();
                    guiEnviornment.getMainFrame().revalidate();
                    guiEnviornment.getMainFrame().repaint();

                }
            }
        });
        this.add(valueTextField, eachInfoConstraints);
        eachInfoConstraints.gridx++;
        //now the JCheckBox
        JCheckBox checkBox;
        if(loadName!=null) {
            checkBox = new JCheckBox(" ", isSelected);
        }else{
            checkBox = new JCheckBox(" ", true);
        }
        checkBox.setBackground(colorOfThemeBackground2);
        checkBox.setOpaque(false);
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                query.setParamsForRequest();
            }
        });
        enabled = checkBox;
        this.add(checkBox, eachInfoConstraints);
        eachInfoConstraints.gridx++;
        //now the trash icon Button
        JButton trash = new JButton(new ImageIcon((new File(".").getAbsolutePath()) + "\\src\\GUI\\resource" + colorOfThemeForground + "\\delete-icon1.png"));
        trash.setBackground(colorOfThemeBackground2);
        trash.setOpaque(false);
        trash.setPreferredSize(new Dimension(18, 18));


        //now the change listeners
        //checkBox

        trash.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                query.remove(thisJPanel);
                params.remove(thisJPanel);
                System.out.println(params.size());
                if (/*params.indexOf(thisJPanel) == 0 && */params.size()==0) {
                    GridBagConstraints queryTabConstraints = new GridBagConstraints();
                    queryTabConstraints.gridx = 0;
                    queryTabConstraints.gridy = 0;
                    queryTabConstraints.weightx = 1;
                    queryTabConstraints.weighty = 0;
                    queryTabConstraints.fill = GridBagConstraints.HORIZONTAL;
                    queryTabConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
                    queryTabConstraints.insets = new Insets(5, 0, 0, 5);

                    query.createQueryTab(queryTabConstraints, gui, colorOfThemeBackground1, colorOfThemeBackground2, colorOfThemeForground);
                }
                query.setParamsForRequest();
                guiEnviornment.getMainFrame().revalidate();
                guiEnviornment.getMainFrame().repaint();
            }
        });

        nameTextField.getDocument().addDocumentListener(new DocumentListener() {

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
                query.setParamsForRequest();
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
                query.setParamsForRequest();
            }
        });

        this.add(trash, eachInfoConstraints);
    }

}
