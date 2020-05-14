package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class InsomniaDisplayPanel extends JLabel {

    public InsomniaDisplayPanel(String colorOfThemeForground){
        super("Insomnia", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\my-app-icon111.png"), SwingConstants.CENTER);
        this.setBounds(new Rectangle(100, 100));
        this.setMaximumSize(new Dimension(100, 100));
        this.setForeground(Color.white);
        this.setFont(new Font("SansSerif", Font.BOLD, 23));
        if(colorOfThemeForground.equals("\\purple")) {
            this.setBackground(new java.awt.Color(123, 104, 238));
        }else{
            this.setBackground(new java.awt.Color(61, 189, 238));
        }
        this.setOpaque(true);
    }


}
