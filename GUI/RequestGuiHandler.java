package GUI;

import java.util.ArrayList;

public class RequestGuiHandler {
    //first the body in case has any
    private ArrayList<String[]> formUrlEncoded = new ArrayList<>();
    private ArrayList<String[]> multiPartFormData = new ArrayList<>();
    private String jsonText = "";
    private String filePath = "";

    //the auth
    private String[] auth =new String[3];

    //query
    private ArrayList<String[]> queryParams = new ArrayList<>();

    //header
    private ArrayList<String[]> headers = new ArrayList<>();

    private String url = "";

    private TYPE typeRequest = TYPE.GET;



    public ArrayList<String[]> getFormUrlEncoded() {
        return formUrlEncoded;
    }

    public ArrayList<String[]> getMultiPartFormData() {
        return multiPartFormData;
    }

    public String getJsonText() {
        return jsonText;
    }

    public String getFilePath() {
        return filePath;
    }

    public String[] getAuth() {
        return auth;
    }

    public ArrayList<String[]> getQueryParams() {
        return queryParams;
    }

    public ArrayList<String[]> getHeaders() {
        return headers;
    }

    public void setFormUrlEncoded(ArrayList<String[]> formUrlEncoded) {
        this.formUrlEncoded = formUrlEncoded;
    }

    public void setMultiPartFormData(ArrayList<String[]> multiPartFormData) {
        this.multiPartFormData = multiPartFormData;
    }

    public void setJsonText(String jsonText) {
        this.jsonText = jsonText;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setAuth(String[] auth) {
        this.auth = auth;
    }

    public void setQueryParams(ArrayList<String[]> queryParams) {
        this.queryParams = queryParams;
    }

    public void setHeaders(ArrayList<String[]> headers) {
        this.headers = headers;
    }


    public static void setInfoToGui(Request request) {

    }
}
