package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RequestButton extends JButton {
    private int indexOfRequest;
    private Request request = null;
    private CreateGUI GUI = null;
    private final static Color dark1 = new java.awt.Color(128,128,128);


    public RequestButton(int index, String nameOfRequest, ImageIcon icon, int height, Color colorOfThemeBackground2, Color colorOfThemeBackground1){
        super(nameOfRequest, icon);
        indexOfRequest = index;
        setMaximumSize(new Dimension(this.getPreferredSize().width, height));
        setMinimumSize(new Dimension(this.getPreferredSize().width, height));
        setPreferredSize(new Dimension(this.getPreferredSize().width,height));
        setSize(new Dimension(this.getPreferredSize().width, height));
        setBackground(colorOfThemeBackground2);
        setOpaque(true);
        setForeground(colorOfThemeBackground1);
        setBorder(BorderFactory.createLineBorder(colorOfThemeBackground2));

        JPopupMenu editRequest = new JPopupMenu();
        JMenuItem renameRequest=new JMenuItem("Rename Request");
        renameRequest.setMnemonic(KeyEvent.VK_R);
        renameRequest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //here you have to create a panel for the user to write the new name of the request
                //rename this
                createRenamerRequestPanel(colorOfThemeBackground2, colorOfThemeBackground1);
            }
        });
        editRequest.add(renameRequest);
        JMenuItem deleteRequest = new JMenuItem("Delete", KeyEvent.VK_D);
        deleteRequest.setForeground(Color.RED);
        deleteRequest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Request.getListOfrequests().remove(indexOfRequest);
                GUI.updateFrame();
            }
        });
        editRequest.add(deleteRequest);
        this.setComponentPopupMenu(editRequest);


    }


    private void createRenamerRequestPanel(Color colorOfThemeBackground2, Color colorOfThemeBackground1){
        JFrame newRequest = new JFrame("Edit Name Of Request");
        newRequest.setLocation(800,500);
        newRequest.setVisible(true);
//        newRequest.setPreferredSize(new Dimension(300, 100));
        newRequest.setBackground(colorOfThemeBackground2);
        newRequest.setForeground(colorOfThemeBackground1);

        JTextField nameTextField =  new JTextField("Enter the new name of the Request");
        nameTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (nameTextField.getText().equals("Enter the new name of the Request")) {
                    nameTextField.setText("");
                }
            }
        });
        nameTextField.setPreferredSize(new Dimension(300,50));
        nameTextField.setBackground(colorOfThemeBackground1);
        nameTextField.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1, 1, true));
        if(colorOfThemeBackground1.equals(dark1)){
            nameTextField.setForeground(Color.WHITE);
        }else{
            nameTextField.setForeground(Color.BLACK);
        }
        nameTextField.setOpaque(true);
        nameTextField.setVisible(true);
        nameTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    String newNameRequest = "new Request";
                    newNameRequest = nameTextField.getText();
                    newRequest.dispatchEvent(new WindowEvent(new JFrame(), WindowEvent.WINDOW_CLOSING));
                    request.setNameOfRequest(newNameRequest);
                    //inja bayad update konim
                    GUI.updateFrame();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        newRequest.add(nameTextField, BorderLayout.NORTH);

        JButton changeButton = new JButton("Change");

        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newNameRequest = "new Request";
                newNameRequest = nameTextField.getText();
                newRequest.dispatchEvent(new WindowEvent(new JFrame(), WindowEvent.WINDOW_CLOSING));
                request.setNameOfRequest(newNameRequest);
                //inja bayad update konim
                GUI.updateFrame();
            }
        });
        changeButton.setBackground(colorOfThemeBackground2);
        changeButton.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1, 1, true));
        if(colorOfThemeBackground1.equals(dark1)){
            changeButton.setForeground(Color.WHITE);
        }else{
            changeButton.setForeground(Color.BLACK);
        }
        changeButton.setPreferredSize(new Dimension(300,50));
        newRequest.add(changeButton, BorderLayout.SOUTH);
        newRequest.setVisible(true);
        newRequest.pack();
    }


    public int getIndexOfRequest(){
        return indexOfRequest;
    }

    public void setRequest(Request request){
        this.request = request;
    }

    public void setGUI(CreateGUI GUI) {
        this.GUI = GUI;
    }
}
