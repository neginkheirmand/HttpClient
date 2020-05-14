package GUI;

import javax.swing.*;
import java.awt.*;

public class PanelHandler {
    private JLabel firstPanel;
    private final GridBagConstraints firstPanelConstraints;





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

    }

    public void setFirstPanel(JLabel insomniaPanel){
        firstPanel = insomniaPanel;
    }



}
