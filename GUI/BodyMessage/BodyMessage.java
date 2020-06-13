//package GUI.BodyMessage;
//
//import GUI.CreateGUI;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.File;
//import java.util.ArrayList;
//
//public class BodyMessage extends JPanel {
//    //this class is meant to handle the body message in case there is any
//    //can handle multipart form data and form url encoded
//    ArrayList<KeyValuePairBody> bodyData = new ArrayList<>();
//
//    public BodyMessage(int bodyType, int timesChanged, CreateGUI gui, Color colorOfThemeBackground1, Color colorOfThemeBackground2, String colorOfThemeForground, ) {
//
//        super();
//        JPanel body = new JPanel();
//        body.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
//        body.setBackground(colorOfThemeBackground2);
//
//        body.setLayout(new GridBagLayout());
//        if (timesChanged > 0) {
//            try {
////                Component toRemove = body.getComponent(timesChanged-1);
////                toRemove.setVisible(false);
////                toRemove.setMaximumSize(new Dimension(0,0));
////                toRemove.setPreferredSize(new Dimension(0,0));
////                toRemove.setMinimumSize(new Dimension(0,0));
//                body.removeAll();
////            GridBagLayout layout = (GridBagLayout) body.getLayout();
////            GridBagConstraints gbc = layout.getConstraints(toRemove);
////                body.remove(0);
////                body.remove(timesChanged-1);
//                body.setLayout(new GridBagLayout());
//            } catch (ArrayIndexOutOfBoundsException e) {
//                System.out.println(e.getStackTrace());
//            }
//        }
//        GridBagConstraints constraints = new GridBagConstraints();
//        constraints.gridx = 0;
//        constraints.gridy = 0;
//        constraints.insets = new Insets(5, 5, 5, 5);
//        constraints.weightx = 1;
//        constraints.weighty = 1;
//        constraints.anchor = GridBagConstraints.PAGE_START;
//        constraints.fill = GridBagConstraints.BOTH;
//        Dimension sizeOfNow = gui.getMainFrame().getSize();
//        Point locationOfNow = gui.getMainFrame().getLocationOnScreen();
//
//        if (bodyType == 0) {
//            //its "From URL Encoded
//            GridBagConstraints constraints1 = new GridBagConstraints();
//            constraints1.gridy = 0;
//            constraints1.gridx = 0;
//            constraints1.weightx = 0;
//            constraints1.weighty = 0;
//            constraints1.fill = GridBagConstraints.HORIZONTAL;
//            constraints1.anchor = GridBagConstraints.FIRST_LINE_START;
//            createFormData(body, constraints1);
//            gui.getMainFrame().revalidate();
//            gui.getMainFrame().repaint();
//            gui.getMainFrame().pack();
//
//        } else if (bodyType == 1) {
//            //its "JSON"
//            JTextArea bodyReqJSON = new JTextArea();
//            bodyReqJSON.setSize(new Dimension(600, 900));
//            bodyReqJSON.setPreferredSize(new Dimension(600, 900));
//            bodyReqJSON.setFont(new Font("DialogInput", Font.PLAIN, 15));
//            bodyReqJSON.setForeground(colorOfThemeBackground1);
//            bodyReqJSON.setBackground(colorOfThemeBackground2);
//            bodyReqJSON.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
//            body.add(bodyReqJSON, constraints);
//        } else if (bodyType == 3) {
//            //its "Binary File"
//            JLabel description = new JLabel("SELECTED FILE");
//            description.setForeground(colorOfThemeBackground1);
//            description.setBackground(colorOfThemeBackground2);
//            description.setOpaque(false);
//            description.setFont(new Font("Serif", Font.BOLD, 15));
//
//            constraints.ipady = 0;
//            constraints.ipadx = 0;
//            constraints.insets = new Insets(10, 10, 10, 10);
//            constraints.anchor = GridBagConstraints.LAST_LINE_START;
//            constraints.fill = GridBagConstraints.HORIZONTAL;
//            body.add(description, constraints);
//
//            JTextArea selectedFile = new JTextArea("No file selected");
//            selectedFile.setOpaque(true);
//            selectedFile.setForeground(colorOfThemeBackground1);
//            selectedFile.setBackground(new java.awt.Color(73, 73, 73));
//            selectedFile.setBorder(BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
//            selectedFile.setPreferredSize(new Dimension(150, 30));
//            selectedFile.setFont(new Font("Serif", Font.BOLD, 20));
//            selectedFile.setEditable(false);
//            JScrollPane fileScrollPane = new JScrollPane(selectedFile);
//            constraints.gridy = 1;
//            constraints.gridwidth = 2;
//            constraints.anchor = GridBagConstraints.CENTER;
//            body.add(fileScrollPane, constraints);
//
//
////            the reset file only enabled if a file is chosen
//            constraints.gridwidth = 1;
//            constraints.gridx = 0;
//            constraints.gridy = 2;
//            constraints.anchor = GridBagConstraints.FIRST_LINE_START;
//            JButton resetFile = new JButton("Resest File", new ImageIcon((new File(".").getAbsolutePath()) + "\\src\\GUI\\resource" + colorOfThemeForground + "\\reset-icon.png"));
//            resetFile.setBackground(colorOfThemeBackground2);
//            resetFile.setForeground(colorOfThemeBackground1);
//            resetFile.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
//            resetFile.setEnabled(false);
//            resetFile.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    System.out.println("file reseeted");
//                    resetFile.setEnabled(false);
//                    resetFile.setForeground(new java.awt.Color(94, 94, 94));
//                    resetFile.setFont(new Font("Serif", Font.BOLD, 20));
//                    resetFile.setBackground(colorOfThemeBackground2);
//                    selectedFile.setText("No file selected");
//                }
//            });
//            body.add(resetFile, constraints);
//
//
//            //first i will add the JFileChooser and then the Reset File JButton
//            constraints.gridx = 1;
//            constraints.gridwidth = 1;
//            constraints.anchor = GridBagConstraints.FIRST_LINE_START;
//            //the JFileChooser and its JButton
//
//            JFileChooser fileChooser = new JFileChooser();
//            JButton fileChooserButton = new JButton("Choose File ", new ImageIcon((new File(".").getAbsolutePath()) + "\\src\\GUI\\resource" + colorOfThemeForground + "\\choose-file-icon1.png"));
//            fileChooserButton.setBackground(colorOfThemeBackground2);
//            fileChooserButton.setForeground(colorOfThemeBackground1);
//            fileChooserButton.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
////            fileChooserButton
//            fileChooserButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//                    int returnVal = fileChooser.showOpenDialog(gui.getMainFrame());
//                    if (returnVal == JFileChooser.APPROVE_OPTION) {
//                        System.out.println("You chose to open this file: " + fileChooser.getSelectedFile().getName());
//                        selectedFile.setText(fileChooser.getSelectedFile().getName());
//                    } else {
//                        System.out.println("didnt want to open the file");
//                    }
//                    if (selectedFile.getText().equals("No file selected")) {
//                        resetFile.setEnabled(false);
//                        resetFile.setForeground(new java.awt.Color(94, 94, 94));
//                        resetFile.setFont(new Font("Serif", Font.BOLD, 20));
//                        resetFile.setBackground(colorOfThemeBackground2);
//                        resetFile.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground2));
//                    } else {
//                        resetFile.setEnabled(true);
//                        resetFile.setForeground(new java.awt.Color(138, 138, 138));
//                        resetFile.setFont(new Font("DialogInput", Font.PLAIN, 18));
//                        resetFile.setBackground(new java.awt.Color(94, 94, 94));
//                        resetFile.setBorder(BorderFactory.createLineBorder(new java.awt.Color(164, 164, 164)));
//                    }
//                    gui.getMainFrame().revalidate();
//                    gui.getMainFrame().repaint();
//                    gui.getMainFrame().pack();
//
//                }
//            });
//            body.add(fileChooserButton, constraints);
//
//
//        } else if (bodyType == 4) {
//            //it form data
//            GridBagConstraints constraints1 = new GridBagConstraints();
//            constraints1.gridy = 0;
//            constraints1.gridx = 0;
//            constraints1.weightx = 0;
//            constraints1.weighty = 0;
//            constraints1.fill = GridBagConstraints.HORIZONTAL;
//            constraints1.anchor = GridBagConstraints.FIRST_LINE_START;
//            createFormData(body, constraints1);
//            gui.getMainFrame().revalidate();
//            gui.getMainFrame().repaint();
//            gui.getMainFrame().pack();
//
//        }
//        gui.getMainFrame().revalidate();
//        gui.getMainFrame().repaint();
//        gui.getMainFrame().pack();
//        //a small bug here that i dont know how to fix// never mind now i have an idea but dont have the time
//        gui.getMainFrame().setPreferredSize(sizeOfNow);
//        gui.getMainFrame().setLocation(locationOfNow);
//    }
//
//    public BodyMessage(){
//        super();
//        //in case the method of the request does not support body message
//        JLabel empty = new JLabel("This Tab is only for put, patch and post methods");
//        this.add(empty);
//    }
//}
