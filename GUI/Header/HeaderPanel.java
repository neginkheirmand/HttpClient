package GUI.Header;

import GUI.CreateGUI;
import GUI.Request;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

public class HeaderPanel extends JPanel{

    ArrayList<KeyValuePairHeader> headers = new ArrayList<>();
    Request request = null;


    public HeaderPanel(Color colorOfThemeBackground1, Color colorOfThemeBackground2, CreateGUI gui, String colorOfThemeForground, Request request){
        super();
        this.request = request;
        JPanel header = this;
        header.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        header.setBackground(colorOfThemeBackground2);
        header.setLayout(new GridBagLayout());
        GridBagConstraints headerConstraints = new GridBagConstraints();
        headerConstraints.gridx = 0;
        headerConstraints.gridy = 0;
        headerConstraints.weightx = 1;
        headerConstraints.weighty = 0;
//        headerConstraints.ipadx = 0;
//        headerConstraints.ipady = 0;
        headerConstraints.insets = new Insets(0, 0, 0, 0);
        headerConstraints.fill = GridBagConstraints.HORIZONTAL;
        headerConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        if(request.getHeaderInfo()==null || request.getHeaderInfo().size() == 0){
            createHeaderTab( headerConstraints, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, gui, null, null, true);
        }else {
            ArrayList<String[]> headers = request.getHeaderInfo();
            for (int i = 0; i < headers.size(); i++) {
//                System.out.println("h= "+headers.get(i)[0]+" v="+headers.get(i)[1]);
                headerConstraints.gridy=i;
                if (headers.get(i)[2].equals("true")) {
                    createHeaderTab( headerConstraints, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, gui, headers.get(i)[0], headers.get(i)[1], true);
                }else{
                    createHeaderTab( headerConstraints, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, gui, headers.get(i)[0], headers.get(i)[1], false);
                }
            }
        }
    }


    public HeaderPanel(){
        super();
        JLabel empty = new JLabel("Please select a request");
        this.add(empty);
    }


    /**
     * @param constraints
     * @param colorOfThemeBackground2
     * @param colorOfThemeBackground1
     * @param colorOfThemeForground
     * @param gui
     * @param headerName
     * @param headerValue
     * @param isSelected
     */
    public void createHeaderTab(GridBagConstraints constraints, Color colorOfThemeBackground2, Color colorOfThemeBackground1, String colorOfThemeForground, CreateGUI gui, String headerName, String headerValue, boolean isSelected) {

        JPanel newKeyValuePair = new KeyValuePairHeader(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, headerName, headerValue, isSelected, this);
        this.add(newKeyValuePair, constraints);
    }

    public void setHeaderToRequest(){
        if(request==null){
            System.out.println("Please choose a request");
            return;
        }
        ArrayList<String[]> header = new ArrayList<>();
        for(int i=0; i<headers.size(); i++){
            String name = headers.get(i).getname().getText();
            String value = headers.get(i).getValue().getText();
            String[] newParam;
            if(headers.get(i).getEnabled().isSelected()) {
                String[] param = {name, value, "true"};
                newParam=param;
            }else{
                String[] param = {name, value, "false"};
                newParam=param;
            }
            System.out.println("the header in the gui"+name+"  "+value+"  "+newParam[2]);
            if(!name.equals("New Header") && !value.equals("New Value")) {
                header.add(newParam);
            }
        }
        request.setHeaderInfo(header);
        return;
    }


    public ArrayList<KeyValuePairHeader> getHeaders() {
        return headers;
    }
}
