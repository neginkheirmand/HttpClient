package GUI;

import javax.swing.*;
import java.awt.*;

public class ResponseTimeInfo extends JLabel {

    private final Color white = new java.awt.Color(166, 166, 166);

    public ResponseTimeInfo(Long time, Color colorOfForground){
        super(time+"ms", SwingConstants.CENTER);
        this.setPreferredSize(new Dimension(80, 30));
        this.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218, 218, 218)));
        this.setForeground(colorOfForground);
        this.setFont(new Font("Serif", Font.BOLD, 15));
        this.setBackground(white);
        this.setOpaque(true);
    }



}
