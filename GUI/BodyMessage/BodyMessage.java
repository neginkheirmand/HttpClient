package GUI.BodyMessage;

import GUI.CreateGUI;
import GUI.MESSAGEBODY_TYPE;
import GUI.Request;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BodyMessage extends JPanel {
    //this class is meant to handle the body message in case there is any
    //can handle multipart form data and form url encoded
    ArrayList<KeyValuePairBody> bodyData = new ArrayList<>();

    Request request = null;

    public BodyMessage(Request request) {
        super();
        this.request = request;
    }

    /**
     * if no request is selected
     */
    public void createBodyMessage(CreateGUI gui, Color colorOfThemeBackground1, Color colorOfThemeBackground2, String colorOfThemeForground) {
        JPanel thisBody = this;
        thisBody.setLayout(new GridBagLayout());

        try {

        if (request.getFormDataInfo() == null || ((ArrayList<String[]>) request.getFormDataInfo()).size() == 0) {
            //create a new one
            createNewKeyValue(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, null, null, true);
        } else {
            //load the existing ones
            for (int i = 0; i < ((ArrayList<String[]>) request.getFormDataInfo()).size(); i++) {
                if (((ArrayList<String[]>) request.getFormDataInfo()).get(i)[2].equals("true")) {
                    createNewKeyValue(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, ((ArrayList<String[]>) request.getFormDataInfo()).get(i)[0], ((ArrayList<String[]>) request.getFormDataInfo()).get(i)[1], true);
                } else {
                    createNewKeyValue(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, ((ArrayList<String[]>) request.getFormDataInfo()).get(i)[0], ((ArrayList<String[]>) request.getFormDataInfo()).get(i)[1], false);
                }
            }
            createNewKeyValue(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, null, null, true);
        }
        } catch (java.lang.ClassCastException exception) {
            request.setFormDataInfo(new ArrayList<String[]>());
            createNewKeyValue(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, null, null, true);
        }
    }

    public void createNewKeyValue(CreateGUI gui, Color colorOfThemeBackground2, Color colorOfThemeBackground1, String colorOfThemeForground, String body, String data, boolean isEnabled) {
          KeyValuePairBody newPair = new KeyValuePairBody(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, body, data, isEnabled, this);

        //ading to the panel
        GridBagConstraints constraints1 = new GridBagConstraints();
        constraints1.gridy = bodyData.indexOf(newPair);
        constraints1.gridx = 0;
        constraints1.weightx = 0;
        constraints1.weighty = 0;
        constraints1.fill = GridBagConstraints.HORIZONTAL;
        constraints1.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(newPair, constraints1);
    }


    public void setBodyToRequest() {
        ArrayList<String[]> bodyInfo = new ArrayList<>();
        for (int i = 0; i < bodyData.size(); i++) {
            String name = bodyData.get(i).getBody().getText();
            String value = bodyData.get(i).getInfo().getText();
            String[] newPair;
            if (bodyData.get(i).getEnabled().isSelected()) {
                String[] pair = {name, value, "true"};
                newPair = pair;
            } else {
                String[] pair = {name, value, "false"};
                newPair = pair;
            }
            if (!name.equals("New Name") || !value.equals("New value")) {
                bodyInfo.add(newPair);
            }
        }
        request.setFormDataInfo(bodyInfo);
    }


    public ArrayList<KeyValuePairBody> getBodyData() {
        return bodyData;
    }
}