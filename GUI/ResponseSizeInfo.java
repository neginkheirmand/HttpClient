package GUI;

import javax.swing.*;
import java.awt.*;

public class ResponseSizeInfo extends JLabel{
    private final Color white = new java.awt.Color(166, 166, 166);

    public ResponseSizeInfo(int size, Color colorOfForground){
        super(size+"B", SwingConstants.CENTER);
        this.setPreferredSize(new Dimension(40, 30));
        this.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218, 218, 218)));
        this.setForeground(colorOfForground);
        this.setFont(new Font("Serif", Font.BOLD, 15));
        this.setBackground(white);
        this.setOpaque(true);
    }
}
