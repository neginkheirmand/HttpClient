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

    public KeyValuePairBody(CreateGUI gui, Color colorOfThemeBackground2, Color colorOfThemeBackground1, String colorOfThemeForground, String name, String value, boolean checkboxBoolean, ArrayList<KeyValuePairBody> keyValuePairBody, JPanel body, Request request) {
        super();
        //adding to the arraylist
        keyValuePairBody.add(this);

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
                    nameTextField.setText(" ");
                }
                System.out.println("clicked on the header");
                if (keyValuePairBody.indexOf(newKeyValuePair) == keyValuePairBody.size() - 1) {
                    //and add a new Pair of Header and values

                    new KeyValuePairBody(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, null, null, true, keyValuePairBody, body, request);
                    setInfoToRequest(keyValuePairBody, request);
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
                    valueTextField.setText(" ");
                }
                System.out.println("clicked on the value");
                if (keyValuePairBody.indexOf(newKeyValuePair) == keyValuePairBody.size() - 1) {
                    //and add a new Pair of Header and values
                    new KeyValuePairBody(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, null, null, true, keyValuePairBody, body, request);
                    setInfoToRequest(keyValuePairBody, request);
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
                setInfoToRequest(keyValuePairBody, request);
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
                System.out.println("user clicked on the trash JButton");
                //remove component from the panel
                body.remove(newKeyValuePair);
                //remove component from the array list
                keyValuePairBody.remove(newKeyValuePair);

                setInfoToRequest(keyValuePairBody, request);
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
                setInfoToRequest(keyValuePairBody, request);
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
                setInfoToRequest(keyValuePairBody, request);
            }

        });

        //ading to the panel
        GridBagConstraints constraints1 = new GridBagConstraints();
        constraints1.gridy = keyValuePairBody.indexOf(newKeyValuePair);
        constraints1.gridx = 0;
        constraints1.weightx = 0;
        constraints1.weighty = 0;
        constraints1.fill = GridBagConstraints.HORIZONTAL;
        constraints1.anchor = GridBagConstraints.FIRST_LINE_START;
        body.add(this, constraints1);
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

    private void setInfoToRequest(ArrayList<KeyValuePairBody> keyValuePairBody, Request request) {
        if (request == null || keyValuePairBody == null) {
            return;
        }

        if (!request.getTypeOfData().equals(MESSAGEBODY_TYPE.FORM_URL) || !request.getTypeOfData().equals(MESSAGEBODY_TYPE.MULTIPART_FORM)) {
            return;
        }

        ArrayList<String[]> bodyData = new ArrayList<>();
        for (int i = 0; i < keyValuePairBody.size(); i++) {
            if (keyValuePairBody.get(i).getBody().getText().equals("New Name") && keyValuePairBody.get(i).getBody().getText().equals("New value")) {
                continue;
            }
            String info[] = null;
            if (keyValuePairBody.get(i).getEnabled().isSelected()) {
                String[] str = {keyValuePairBody.get(i).getBody().getText(), keyValuePairBody.get(i).getInfo().getText(), "true"};
                info = str;
            } else {
                String[] str = {keyValuePairBody.get(i).getBody().getText(), keyValuePairBody.get(i).getInfo().getText(), "false"};
                info = str;
            }
            bodyData.add(info);
        }

    }
}
