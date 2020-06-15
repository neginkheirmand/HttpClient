package GUI.BodyMessage;

import GUI.CreateGUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class JsonPanel {
    private CreateGUI guiEnviornment;
    private BodyMessage bodyMessage;

    public JsonPanel( Color colorOfThemeBackground1, Color colorOfThemeBackground2, CreateGUI gui, BodyMessage bodyMessage, String json){
        //first we delete all the component of the body previously added
//        bodyMessage.removeAll();
        this.guiEnviornment = gui;
        this.bodyMessage = bodyMessage;
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.fill = GridBagConstraints.BOTH;

        JTextField bodyReqJSON = new JTextField();
        if(json!=null){
            bodyReqJSON.setText(json);
        }
        bodyReqJSON.setSize(new Dimension(600, 900));
        bodyReqJSON.setPreferredSize(new Dimension(600, 900));
        bodyReqJSON.setFont(new Font("DialogInput", Font.PLAIN, 15));
        bodyReqJSON.setForeground(colorOfThemeBackground1);
        bodyReqJSON.setBackground(colorOfThemeBackground2);
        bodyReqJSON.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        bodyReqJSON.getDocument().addDocumentListener(new DocumentListener() {

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
                bodyMessage.setJsonContainer(bodyReqJSON.getText());
            }
        });
        bodyMessage.add(bodyReqJSON, constraints);
    }



}
