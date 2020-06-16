package GUI;


import org.apache.hc.core5.http.HttpStatus;

import javax.swing.*;
import java.awt.*;

public class StatusCodeLabel extends JLabel{
    //for success : 100
    private final static Color yellow = new java.awt.Color(240, 248, 14);
    //for success : 200
    private final static Color green = new java.awt.Color(0, 180, 2);
    //for redirects : 300
    private final static Color blue = new java.awt.Color(27, 106, 255);
    //for error : 400
    private final static Color red = new java.awt.Color(205, 52, 56);
    //for server-unhandle : 500
    private final static Color orange = new java.awt.Color(205, 110, 27);
    String statusStr = "";
    int statudCode = 0;


    public StatusCodeLabel(String statusStr,int status){
        super(statusStr, SwingConstants.CENTER);
        this.statusStr = statusStr;
        this.statudCode=status;

        if(status>=100 && status<104){
            this.setBackground(yellow);
//        }else if(status>=200 && status<300){
        }else if(status>=200 && status<207){
            this.setPreferredSize(new Dimension(80, 35));
            this.setBackground(green);
//        }else if(status>=300 && status<400){
        }else if(status>=300 && status<309){
            this.setPreferredSize(new Dimension(100, 35));
            this.setBackground(blue);
//        }else if(status>=400 && status<500){
        }else if(status>=400 && status<418){
            this.setBackground(red);
//        }else if(status>=500 && status<600){
        }else if(status>=500 && status<512){
            this.setBackground(orange);
        }else{
            this.setBackground(red);
        }

        this.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218, 218, 218)));
        this.setForeground(Color.white);
        this.setFont(new Font("Serif", Font.BOLD, 15));
        this.setOpaque(true);
    }

    public static String statusCodeText(int status){
        String statusStr = "";
//        if(status>=100 && status<200){
        if(status>=100 && status<104){
            statusStr+="Info"+status;

//        }else if(status>=200 && status<300){
        }else if(status>=200 && status<207){
            statusStr+="Ok"+status;
//        }else if(status>=300 && status<400){
        }else if(status>=300 && status<309){
            statusStr+="Redirect"+status;
//        }else if(status>=400 && status<500){
        }else if(status>=400 && status<418){
            statusStr+="Client Error"+status;
//        }else if(status>=500 && status<600){
        }else if(status>=500 && status<512){
            statusStr+="Server Error"+status;
        }else{
            statusStr="Not-supported";
            if(status==0){
                statusStr="";
            }
        }
        return statusStr;

    }

    public String getStatusStr() {
        return statusStr;
    }

    public int getStatudCode() {
        return statudCode;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public void setStatudCode(int statudCode) {
        this.statudCode = statudCode;
    }
}
