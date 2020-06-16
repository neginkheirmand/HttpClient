package GUI.BodyMessage;

import GUI.CreateGUI;
import GUI.MESSAGEBODY_TYPE;
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

public class KeyValuePairBody extends JPanel {
    CreateGUI gui;
    JTextField body;
    JTextField info;
    JCheckBox enabled;

    public KeyValuePairBody(CreateGUI gui, Color colorOfThemeBackground2, Color colorOfThemeBackground1, String colorOfThemeForground, String name, String value, boolean checkboxBoolean, BodyMessage body) {
        super();
        //adding to the arraylist
        body.getBodyData().add(this);

        JPanel newKeyValuePair = this;
        //newKeyValuePair.setPreferredSize(new Dimension());
        newKeyValuePair.setLayout(new FlowLayout());
        newKeyValuePair.setBackground(colorOfThemeBackground2);


        //first component the 3 lines
        JLabel _3_lines = new JLabel(new ImageIcon((new File(".").getAbsolutePath()) + "\\src\\GUI\\resource" + colorOfThemeForground + "\\3-purple-lines-icon.png"));
        newKeyValuePair.add(_3_lines);
        //then the JTextField for the Header
        JTextField nameTextField;
        if (name == null) {
            nameTextField = new JTextField("New Name");
        } else {
            nameTextField = new JTextField(name);
        }
        this.body = nameTextField;
        nameTextField.setPreferredSize(new Dimension(150, 35));
        nameTextField.setFont(new Font("Serif", Font.PLAIN, 15));
        nameTextField.setForeground(colorOfThemeBackground1);
        nameTextField.setBackground(colorOfThemeBackground2);
        nameTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorOfThemeBackground1));
//        boolean alreadyCreated = false;
        nameTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (nameTextField.getText().equals("New Name")) {
                    nameTextField.setText("");
                }
                System.out.println("clicked on the header");
                if (body.getBodyData().indexOf(newKeyValuePair) == body.getBodyData().size() - 1) {
                    //and add a new Pair of Header and values
                    body.createNewKeyValue(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, null, null, true);
                    body.setBodyToRequest();
                    gui.getMainFrame().revalidate();
                    gui.getMainFrame().repaint();
                }
            }
        });
        newKeyValuePair.add(nameTextField);
        //then the JTextField for the value

        JTextField valueTextField;
        if (value == null) {
            valueTextField = new JTextField("New value");
        } else {
            valueTextField = new JTextField(value);
        }
        this.info = valueTextField;
        valueTextField.setPreferredSize(new Dimension(150, 35));
        valueTextField.setFont(new Font("Serif", Font.PLAIN, 15));
        valueTextField.setForeground(colorOfThemeBackground1);
        valueTextField.setBackground(colorOfThemeBackground2);
        valueTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorOfThemeBackground1));
        valueTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (valueTextField.getText().equals("New value")) {
                    valueTextField.setText("");
                }
                System.out.println("clicked on the value");
                if (body.getBodyData().indexOf(newKeyValuePair) == body.getBodyData().size() - 1) {
                    //and add a new Pair of Header and values
                    body.createNewKeyValue(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, null, null, true);
                    body.setBodyToRequest();
                    gui.getMainFrame().revalidate();
                    gui.getMainFrame().repaint();
                }
            }
        });

        newKeyValuePair.add(valueTextField);
        //now the JCheckBox
        JCheckBox checkBox = new JCheckBox(" ", checkboxBoolean);
        checkBox.setBackground(colorOfThemeBackground2);

        checkBox.setOpaque(false);
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                body.setBodyToRequest();
            }
        });
        this.enabled = checkBox;
        newKeyValuePair.add(checkBox);
        //now the trash icon Button
        JButton trash = new JButton(new ImageIcon((new File(".").getAbsolutePath()) + "\\src\\GUI\\resource" + colorOfThemeForground + "\\delete-icon1.png"));
        trash.setBackground(colorOfThemeBackground2);
        trash.setOpaque(false);
        trash.setPreferredSize(new Dimension(18, 18));
        trash.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //remove component from the panel
                body.remove(newKeyValuePair);
                //remove component from the array list
                body.getBodyData().remove(newKeyValuePair);

                if(body.getBodyData().indexOf(newKeyValuePair)== body.getBodyData().size()-1){
                    body.createNewKeyValue(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, null, null, true);
                }
                body.setBodyToRequest();
                gui.getMainFrame().revalidate();
                gui.getMainFrame().repaint();
            }
        });
        newKeyValuePair.add(trash);
//        body.add(newKeyValuePair, constraints);


        nameTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            public void update() {
                body.setBodyToRequest();
            }
        });


        valueTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                body.setBodyToRequest();
            }

        });

    }

    public JTextField getBody() {
        return body;
    }

    public JTextField getInfo() {
        return info;
    }

    public JCheckBox getEnabled() {
        return enabled;
    }
}
