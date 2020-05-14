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
    private JScrollPane ScroolPanelPointer;
    private JPanel forthPanel;
    private GridBagConstraints forthPanelConstraints;

    //the url and save and send button area of the request
    private JTabbedPane fifthPanel;
    private final GridBagConstraints fifthPanelConstraints;

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


        //the forth JPanel(JScrollPane) which contains the requests and the collections  --> since some bugs could show up will leave this constraints alone


        //the fifth JPanel(JTabbedPane) with the body message on its tabs
        fifthPanelConstraints= new GridBagConstraints();
        fifthPanelConstraints.gridx = 1;
        fifthPanelConstraints.gridy = 1;
        //growing constant
        fifthPanelConstraints.weightx = 1;
        fifthPanelConstraints.weighty = 1;
        fifthPanelConstraints.anchor = GridBagConstraints.LINE_START;
        fifthPanelConstraints.fill = GridBagConstraints.BOTH;
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


    //the forth part which is the classifier of requests and collections
    public JScrollPane getScroolPanelPointer() {
        return ScroolPanelPointer;
    }

    public void setScroolPanelPointer(JScrollPane scroolPanelPointer) {
        ScroolPanelPointer = scroolPanelPointer;
    }

    public JPanel getForthPanel() {
        return forthPanel;
    }

    public void setForthPanel(JPanel forthPanel) {
        this.forthPanel = forthPanel;
    }

    public GridBagConstraints getForthPanelConstraints() {
        return forthPanelConstraints;
    }

    public void setForthPanelConstraints(GridBagConstraints constraints){
        forthPanelConstraints = constraints;
    }

    //create fifth JPanel(JTabbedPane)
    public JTabbedPane getFifthPanel() {
        return fifthPanel;
    }

    public void setFifthPanel(JTabbedPane fifthPanel) {
        this.fifthPanel = fifthPanel;
    }

    public GridBagConstraints getFifthPanelConstraints() {
        return fifthPanelConstraints;
    }
}
