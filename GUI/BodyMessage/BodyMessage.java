package GUI.BodyMessage;

import GUI.CreateGUI;
import GUI.MESSAGEBODY_TYPE;
import GUI.Request;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class BodyMessage extends JPanel {
    //this class is meant to handle the body message in case there is any
    //can handle multipart form data and form url encoded
    ArrayList<KeyValuePairBody> bodyData = new ArrayList<>();
    String jsonContainer;
    String pathOfFile;

    Request request = null;

    /**
     * for requests with no body message
     * @param request
     */
    public BodyMessage(Request request) {
        super();
        this.request = request;
        //in case the method of the request does not support body message
        String methodRequest = request.getTypeOfRequest().toString();
        JLabel empty = new JLabel("The "+methodRequest+" can't have body message.");
        this.add(empty);
    }

    /**
     * if no request is selected
     */
    public BodyMessage(){
        super();
        this.add(new JLabel("Please choose one of the requests"));
    }

    /**
     * if no request is selected
     */
    public BodyMessage(CreateGUI gui, Request request, Color colorOfThemeBackground1, Color colorOfThemeBackground2, String colorOfThemeForground){
        super();
        this.request=request;
        JPanel thisBody = this;
        thisBody.setLayout(new GridBagLayout());
        if(request.getTypeOfData().equals(MESSAGEBODY_TYPE.FORM_URL) || request.getTypeOfData().equals(MESSAGEBODY_TYPE.MULTIPART_FORM)) {
            if(request.getFormDataInfo()==null || ((ArrayList<String[]>)request.getFormDataInfo()).size()==0){
                //create a new one
                createNewKeyValue(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, null, null, true, true);
            }else{
                //load the existing ones
                for(int i=0; i<((ArrayList<String[]>)request.getFormDataInfo()).size();i++) {
                    if(((ArrayList<String[]>)request.getFormDataInfo()).get(i)[2].equals("true")) {
                        createNewKeyValue(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, ((ArrayList<String[]>) request.getFormDataInfo()).get(i)[0], ((ArrayList<String[]>) request.getFormDataInfo()).get(i)[1], true, true);
                    }else{
                        createNewKeyValue(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, ((ArrayList<String[]>) request.getFormDataInfo()).get(i)[0], ((ArrayList<String[]>) request.getFormDataInfo()).get(i)[1], false, true);
                    }
                }
                createNewKeyValue(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, null, null, true, true);
            }
        }else if(request.getTypeOfData().equals(MESSAGEBODY_TYPE.JSON)) {
            new JsonPanel(colorOfThemeBackground1, colorOfThemeBackground2, gui, this, ((String)request.getFormDataInfo()));
        }else if(request.getTypeOfData().equals(MESSAGEBODY_TYPE.BINARY)) {
            new BinaryFilePanel(this, colorOfThemeBackground1, colorOfThemeBackground2, colorOfThemeForground, gui, ((String)request.getFormDataInfo()));
        }
    }


    public void createNewKeyValue(CreateGUI gui, Color colorOfThemeBackground2, Color colorOfThemeBackground1, String colorOfThemeForground, String body, String data, boolean isEnabled, boolean deleteComponents){
          new KeyValuePairBody(gui, colorOfThemeBackground2, colorOfThemeBackground1, colorOfThemeForground, this, body, data, isEnabled, deleteComponents);
    }


    public void setBodyToRequest() {
        if (request == null) {
            System.out.println("Please choose a request");
            return;
        }
        //first we se what is the type of request
        if (request.getTypeOfData().equals(MESSAGEBODY_TYPE.FORM_URL) || request.getTypeOfData().equals(MESSAGEBODY_TYPE.MULTIPART_FORM)) {
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
                if (!name.equals("New Name") && !value.equals("New value")) {
                    bodyInfo.add(newPair);
                }
            }
            request.setFormDataInfo(bodyInfo);
        } else if (request.getTypeOfData().equals(MESSAGEBODY_TYPE.BINARY)) {
            request.setFormDataInfo(pathOfFile);
        } else if (request.getTypeOfData().equals(MESSAGEBODY_TYPE.JSON)) {
            request.setFormDataInfo(jsonContainer);
        }
    }


    public ArrayList<KeyValuePairBody> getBodyData() {
        return bodyData;
    }

    public void setJsonContainer(String  jsonContainer) {
        this.jsonContainer = jsonContainer;
    }

    public void setPathOfFile(String pathOfFile) {
        this.pathOfFile = pathOfFile;
    }
}