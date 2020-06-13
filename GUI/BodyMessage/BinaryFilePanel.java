package GUI.BodyMessage;

import GUI.CreateGUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class BinaryFilePanel {

    private CreateGUI gui;
    public BinaryFilePanel(BodyMessage body, Color colorOfThemeBackground1, Color colorOfThemeBackground2, String colorOfThemeForground, CreateGUI gui, String pathOfFile){
        this.gui=gui;
        body.removeAll();
        //its "Binary File"
        JLabel description = new JLabel("SELECTED FILE");
        description.setForeground(colorOfThemeBackground1);
        description.setBackground(colorOfThemeBackground2);
        description.setOpaque(false);
        description.setFont(new Font("Serif", Font.BOLD, 15));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipady = 0;
        constraints.ipadx = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.LAST_LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        body.add(description, constraints);

        JTextArea selectedFile;
        if(pathOfFile==null || pathOfFile.length()==0) {
            selectedFile = new JTextArea("No file selected");
        }else{
            selectedFile = new JTextArea("No file selected");
        }
        selectedFile.setOpaque(true);
        selectedFile.setForeground(colorOfThemeBackground1);
        selectedFile.setBackground(new java.awt.Color(73, 73, 73));
        selectedFile.setBorder(BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        selectedFile.setPreferredSize(new Dimension(150, 30));
        selectedFile.setFont(new Font("Serif", Font.BOLD, 20));
        selectedFile.setEditable(false);
        selectedFile.getDocument().addDocumentListener(new DocumentListener() {

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
                body.setPathOfFile(selectedFile.getText());
                body.setBodyToRequest();
            }
        });
        JScrollPane fileScrollPane = new JScrollPane(selectedFile);
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        body.add(fileScrollPane, constraints);


//            the reset file only enabled if a file is chosen
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        JButton resetFile = new JButton("Reset File", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\reset-icon.png"));
        resetFile.setBackground(colorOfThemeBackground2);
        resetFile.setForeground(colorOfThemeBackground1);
        resetFile.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        resetFile.setEnabled(false);
        resetFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("file reseted");
                resetFile.setEnabled(false);
                resetFile.setForeground(new java.awt.Color(94, 94, 94));
                resetFile.setFont(new Font("Serif", Font.BOLD, 20));
                resetFile.setBackground(colorOfThemeBackground2);
                selectedFile.setText("No file selected");
            }
        });
        body.add(resetFile, constraints);


        //first i will add the JFileChooser and then the Reset File JButton
        constraints.gridx = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        //the JFileChooser and its JButton

        JFileChooser fileChooser = new JFileChooser();
        JButton fileChooserButton = new JButton("Choose File ", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\choose-file-icon1.png"));
        fileChooserButton.setBackground(colorOfThemeBackground2);
        fileChooserButton.setForeground(colorOfThemeBackground1);
        fileChooserButton.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
//            fileChooserButton
        fileChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnVal = fileChooser.showOpenDialog(gui.getMainFrame());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    System.out.println("You chose to open this file: " + fileChooser.getSelectedFile().getName());
                    selectedFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
                } else {
                    System.out.println("didnt want to open the file");
                }
                if (selectedFile.getText().equals("No file selected")) {
                    resetFile.setEnabled(false);
                    resetFile.setForeground(new java.awt.Color(94, 94, 94));
                    resetFile.setFont(new Font("Serif", Font.BOLD, 20));
                    resetFile.setBackground(colorOfThemeBackground2);
                    resetFile.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground2));
                } else {
                    resetFile.setEnabled(true);
                    resetFile.setForeground(new java.awt.Color(138, 138, 138));
                    resetFile.setFont(new Font("DialogInput", Font.PLAIN, 18));
                    resetFile.setBackground(new java.awt.Color(94, 94, 94));
                    resetFile.setBorder(BorderFactory.createLineBorder(new java.awt.Color(164, 164, 164)));
                }
                gui.getMainFrame().revalidate();
                gui.getMainFrame().repaint();
                gui.getMainFrame().pack();

            }
        });
        body.add(fileChooserButton, constraints);

    }
}
