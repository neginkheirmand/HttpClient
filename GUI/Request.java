package GUI;

import java.util.ArrayList;

enum TYPE{
    POST,
    PATCH,
    PUT,
    DELETE,
    GET,
    HEAD,
    OPTION
}


enum FORM_DATA{
    FORM_URL,
    JSON,
    BINARY
}

public class Request {
    private String nameOfRequest="";
    private TYPE typeOfRequest=TYPE.GET;
    //shouldent the next be a URL?
    private String url="";
    //the first tab takes its info from here
    private FORM_DATA typeOfData= FORM_DATA.FORM_URL;
    private Object formDataInfo=null;
    //is there an authentication
    private boolean auth=false;
    private String[] authInfo=null;
    //the query tab
    private ArrayList<String[]> queryInfo = null;
    //the header tab
    private ArrayList<String[]> headerInfo = null;

    public Request(String nameOfRequest, TYPE typeOfRequest, String url, FORM_DATA formatOfData) {
        this.nameOfRequest = nameOfRequest;
        this.typeOfRequest = typeOfRequest;
        this.url = url;
        this.typeOfData = formatOfData;
        queryInfo=new ArrayList<>();
        headerInfo=new ArrayList<>();
        authInfo=new String[3];
    }

    public TYPE getTypeOfRequest() {
        return typeOfRequest;
    }

    public String getUrl() {
        return url;
    }

    public FORM_DATA getTypeOfData() {
        return typeOfData;
    }

    public Object getFormDataInfo() {
        return formDataInfo;
    }

    public boolean getAuth() {
        return auth;
    }

    public String[] getAuthInfo() {
        return authInfo;
    }

    public ArrayList<String[]> getQueryInfo() {
        return queryInfo;
    }

    public ArrayList<String[]> getHeaderInfo() {
        return headerInfo;
    }

    public String getNameOfRequest() {
        return nameOfRequest;
    }

    public void setNameOfRequest(String nameOfRequest) {
        this.nameOfRequest = nameOfRequest;
    }

    public void setTypeOfRequest(TYPE typeOfRequest) {
        this.typeOfRequest = typeOfRequest;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTypeOfData(FORM_DATA typeOfData) {
        this.typeOfData = typeOfData;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public void setAuthInfo(String[] authInfo) {
        this.authInfo = authInfo;
    }

    public void setQueryInfo(ArrayList<String[]> queryInfo) {
        this.queryInfo = queryInfo;
    }

    public void setHeaderInfo(ArrayList<String[]> headerInfo) {
        this.headerInfo = headerInfo;
    }

    public void setFormDataInfo(Object formDataInfo){
        //the formDataInfo passed to this method is:
        //if UrlEncoded : ArrayList<String[]>
        //if JSON : String
        //if Binary : File

        this.formDataInfo = formDataInfo;
    }


}
