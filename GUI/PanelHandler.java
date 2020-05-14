package GUI;

import javax.swing.*;
import java.awt.*;

public class PanelHandler {



    //the insomnia diplay area
    private JLabel firstPanel;
    private final GridBagConstraints firstPanelConstraints;

    //the url and save and send button area of the request
    private JPanel secondPanel;
    private final GridBagConstraints secondPanelConstraints;

    //the url and save and send button area of the request
    private  JPanel thirdPanel;
    private final GridBagConstraints thirdPanelConstraints;

    //the url and save and send button area of the request
    private JPanel forthPanel;
    private GridBagConstraints forthPanelConstraints;

    //the url and save and send button area of the request
    private JPanel fifthPanel;
    private GridBagConstraints fifthPanelConstraints;

    //the url and save and send button area of the request
    private JPanel sixthPanel;
    private GridBagConstraints sixthPanelConstraints;







    public PanelHandler(){

        //Preaprin the constraints of the first panel (The Insomnia Display Area)
        firstPanelConstraints = new GridBagConstraints();
        //preparing the Insomnia label to be add
        firstPanelConstraints.fill = GridBagConstraints.BOTH;
        //(0,0)
        firstPanelConstraints.gridx = 0;
        firstPanelConstraints.gridy = 0;
        firstPanelConstraints.gridwidth = 1;
        firstPanelConstraints.gridheight = 1;
        //growing constant
        firstPanelConstraints.weightx = 1;
        firstPanelConstraints.weighty = -1;
        //padding sides
        firstPanelConstraints.ipady = 0;
        firstPanelConstraints.ipadx = 0;
        firstPanelConstraints.anchor = GridBagConstraints.FIRST_LINE_START;


        //Preparing the constraints for the second panel where the url and save and send button will be
        secondPanelConstraints = new GridBagConstraints();
        secondPanelConstraints.gridx = 1;
        secondPanelConstraints.gridy = 0;
        //growing constant
        secondPanelConstraints.weightx = 1;
        secondPanelConstraints.weighty = -1;
        secondPanelConstraints.gridheight = 1;
        secondPanelConstraints.gridwidth = 1;
        secondPanelConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        secondPanelConstraints.fill = GridBagConstraints.BOTH;




        //Preparing the constraints for the third upper panel, the one containing the status code and ...
        thirdPanelConstraints = new GridBagConstraints();
        thirdPanelConstraints.gridx = 2;
        thirdPanelConstraints.gridy = 0;
        //growing constant
        thirdPanelConstraints.weightx = 1;
        thirdPanelConstraints.weighty = -1;
        thirdPanelConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        thirdPanelConstraints.fill = GridBagConstraints.BOTH;
    }



    //the first Upper JPanel
    public GridBagConstraints getFirstPanelConstraints() {
        return firstPanelConstraints;
    }

    public void setFirstPanel(JLabel insomniaPanel){
        firstPanel = insomniaPanel;
    }

    public JLabel getFirstPanel() {
        return firstPanel;
    }


    //the second Upper JPanel
    public JPanel getSecondPanel() {
        return secondPanel;
    }

    public void setSecondPanel(JPanel secondPanel) {
        this.secondPanel = secondPanel;
    }

    public GridBagConstraints getSecondPanelConstraints() {
        return secondPanelConstraints;
    }


    //the third Upper JPanel
    public JPanel getThirdPanel() {
        return thirdPanel;
    }

    public void setThirdPanel(JPanel thirdPanel) {
        this.thirdPanel = thirdPanel;
    }

    public GridBagConstraints getThirdPanelConstraints() {
        return thirdPanelConstraints;
    }



    
}
