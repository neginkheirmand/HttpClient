package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RequestButton extends JButton {
    private int indexOfRequest;

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
    }

    public int getIndexOfRequest(){
        return indexOfRequest;
    }

}
