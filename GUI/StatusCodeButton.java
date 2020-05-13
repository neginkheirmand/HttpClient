package GUI;

import javax.swing.*;
import java.awt.*;

public class StatusCodeButton extends JLabel {
    private final static Color green= new java.awt.Color(0, 180, 2);
    private final static Color red= new java.awt.Color(205, 71, 78);
    String status;
    boolean successfulAnswer;


    public StatusCodeButton(String status, boolean successfulAnswer){
        super(status, SwingConstants.CENTER);
        this.status = status;
        this.successfulAnswer = successfulAnswer;
        if(successfulAnswer){
            this.setBackground(green);
        }else{
            this.setBackground(red);
        }
        this.setPreferredSize(new Dimension(80, 35));
        this.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218, 218, 218)));
        this.setForeground(Color.white);
        this.setFont(new Font("Serif", Font.BOLD, 15));
        this.setOpaque(true);
    }

    public String getStatus() {
        return status;
    }
}
