package GUI;

import GUI.Query.QueryPanel;

import java.util.ArrayList;

public class RequestGuiHandler {
    //first the body in case has any
    private ArrayList<String[]> formUrlEncoded = new ArrayList<>();
    private ArrayList<String[]> multiPartFormData = new ArrayList<>();
    private String jsonText = "";
    private String filePath = "";

    //the auth
    private String[] auth = new String[3];

    //query --atention: we dont create one here, we only set it

    //header
    private ArrayList<String[]> headers = new ArrayList<>();

}