package GUI.Query;

import GUI.CreateGUI;
import GUI.Request;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class QueryPanel extends JPanel {

    ArrayList<KeyValuePairQuery> keyValuePairs = new ArrayList<>();
    Request request = null;

    public QueryPanel(Color colorOfThemeBackground1, Color colorOfThemeBackground2, Request request, CreateGUI gui, String colorOfThemeForground) {
        super();
        this.request = request;
        JPanel queryPanel = this;
        queryPanel.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        queryPanel.setBackground(colorOfThemeBackground2);
        queryPanel.setLayout(new GridBagLayout());
        GridBagConstraints queryTabConstraints = new GridBagConstraints();
        queryTabConstraints.gridx = 0;
        queryTabConstraints.gridy = 0;
        queryTabConstraints.weightx = 1;
        queryTabConstraints.weighty = 0;
        queryTabConstraints.fill = GridBagConstraints.HORIZONTAL;
        queryTabConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        queryTabConstraints.insets = new Insets(5, 0, 0, 5);
        createQueryTab( queryTabConstraints, gui, colorOfThemeBackground1, colorOfThemeBackground2, colorOfThemeForground);
    }


    /**
     * agar request akhar bud yani info dare khode request va bayad load koni az ruye request
     * @param colorOfThemeBackground1
     * @param colorOfThemeBackground2
     * @param gui
     * @param colorOfThemeForground
     * @param request
     */
    public QueryPanel(Color colorOfThemeBackground1, Color colorOfThemeBackground2, CreateGUI gui, String colorOfThemeForground, Request request) {
        super();
        this.request = request;
        JPanel queryPanel = this;
        queryPanel.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        queryPanel.setBackground(colorOfThemeBackground2);
        queryPanel.setLayout(new GridBagLayout());
        GridBagConstraints queryTabConstraints = new GridBagConstraints();
        queryTabConstraints.gridx = 0;
        queryTabConstraints.gridy = 0;
        queryTabConstraints.weightx = 1;
        queryTabConstraints.weighty = 0;
        queryTabConstraints.fill = GridBagConstraints.HORIZONTAL;
        queryTabConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        queryTabConstraints.insets = new Insets(5, 0, 0, 5);
        ArrayList<String[]> loadedQueryParams = request.getQueryInfo();
        for(int i=0; i<loadedQueryParams.size(); i++) {
            if (loadedQueryParams.get(i)[2].equals("true")) {
                queryTabConstraints.gridy=i;
                loadQueryTab(queryTabConstraints, gui, colorOfThemeBackground1, colorOfThemeBackground2, colorOfThemeForground, loadedQueryParams.get(i)[0], loadedQueryParams.get(i)[1], true);
            }else{
                queryTabConstraints.gridy=i;
                loadQueryTab(queryTabConstraints, gui, colorOfThemeBackground1, colorOfThemeBackground2, colorOfThemeForground, loadedQueryParams.get(i)[0], loadedQueryParams.get(i)[1], false);
            }
        }
        queryTabConstraints.gridy = queryTabConstraints.gridy+1;
        loadQueryTab(queryTabConstraints, gui, colorOfThemeBackground1, colorOfThemeBackground2, colorOfThemeForground, null, null, false);

    }


    /**
     *
     * @param constraints
     * @param gui
     * @param colorOfThemeBackground1
     * @param colorOfThemeBackground2
     * @param colorOfThemeForground
     */
    public void createQueryTab( GridBagConstraints constraints, CreateGUI gui, Color colorOfThemeBackground1, Color colorOfThemeBackground2, String colorOfThemeForground) {
        JPanel newNameValuePair;
        newNameValuePair = new KeyValuePairQuery(gui, this, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, keyValuePairs);
        this.add(newNameValuePair, constraints);
    }

    public void loadQueryTab( GridBagConstraints constraints, CreateGUI gui, Color colorOfThemeBackground1, Color colorOfThemeBackground2, String colorOfThemeForground, String name, String value, boolean isSelected){
        JPanel newNameValuePair;
        newNameValuePair = new KeyValuePairQuery(gui, this, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, keyValuePairs, name, value, isSelected);
        this.add(newNameValuePair, constraints);
    }

    public QueryPanel(){
        JLabel empty = new JLabel("Please select a request");
        this.add(empty);
    }

    /**
     * this method by does not add the params with the default string and the ones with unchecked check box
     */
    public void setParamsForRequest(){
        if(request==null){
            System.out.println("Please choose a request");
            return;
        }
        ArrayList<String[]> queryParams = new ArrayList<>();
        for(int i=0; i<keyValuePairs.size(); i++){
            String name = keyValuePairs.get(i).getname().getText();
            String value = keyValuePairs.get(i).getValue().getText();
            String[] newParam;
            if(keyValuePairs.get(i).getEnabled().isSelected()) {
                String[] param = {name, value, "true"};
                newParam=param;
            }else{
                String[] param = {name, value, "false"};
                newParam=param;
            }
            System.out.println("the params in the gui"+name+"  "+value+"  "+newParam[2]);
            if(!name.equals(" New Name") && !value.equals("New Value")) {
                queryParams.add(newParam);
            }
        }
        request.setQueryInfo(queryParams);
        return;
    }
}