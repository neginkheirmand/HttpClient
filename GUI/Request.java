package GUI;

import java.util.ArrayList;




/**
 * this class creates an request
 * @author      Negin Kheirmand <venuskheirmand@gmail.com>
 * @version     1.1
 * @since       1.0
 */


public class Request {
    //the name of the request
    private String nameOfRequest="";
    //the type of the request
    private TYPE typeOfRequest=TYPE.GET;
    //the url passed to
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

    /**
     * constructor of the class
     * @param nameOfRequest the name of the request
     * @param typeOfRequest type of the request
     * @param url url of the request
     * @param formatOfData form data of the request
     */
    public Request(String nameOfRequest, TYPE typeOfRequest, String url, FORM_DATA formatOfData) {
        this.nameOfRequest = nameOfRequest;
        this.typeOfRequest = typeOfRequest;
        this.url = url;
        this.typeOfData = formatOfData;
        queryInfo=new ArrayList<>();
        headerInfo=new ArrayList<>();
        authInfo=new String[3];
    }

    /**
     * getter method
     * @return TYPE of request
     */
    public TYPE getTypeOfRequest() {
        return typeOfRequest;
    }

    /**
     * getter method
     * @return url of the request
     */
    public String getUrl() {
        return url;
    }

    /**
     * getter method
     * @return FORM DATA of the request
     */
    public FORM_DATA getTypeOfData() {
        return typeOfData;
    }

    /**
     * getter method
     * @return info about form data of the request
     */
    public Object getFormDataInfo() {
        return formDataInfo;
    }

    /**
     * getter method
     * @return auth Type of the request
     */
    public boolean getAuth() {
        return auth;
    }

    /**
     * getter method
     * @return auth info of the request
     */
    public String[] getAuthInfo() {
        return authInfo;
    }

    /**
     * getter method
     * @return query info of the request
     */
    public ArrayList<String[]> getQueryInfo() {
        return queryInfo;
    }

    /**
     * getter method
     * @return header info of the request
     */
    public ArrayList<String[]> getHeaderInfo() {
        return headerInfo;
    }

    /**
     * getter method
     * @return name of the request
     */
    public String getNameOfRequest() {
        return nameOfRequest;
    }

    /**
     * setter method
     * @return name of the request
     */
    public void setNameOfRequest(String nameOfRequest) {
        this.nameOfRequest = nameOfRequest;
    }

    /**
     * setter method
     * @return type of the request
     */
    public void setTypeOfRequest(TYPE typeOfRequest) {
        this.typeOfRequest = typeOfRequest;
    }

    /**
     * setter method
     * @return url  of the request
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * setter method
     * @return type of the request
     */
    public void setTypeOfData(FORM_DATA typeOfData) {
        this.typeOfData = typeOfData;
    }

    /**
     * setter method
     * @return auth of the request
     */
    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    /**
     * setter method
     * @return auth info of the request
     */
    public void setAuthInfo(String[] authInfo) {
        if(auth) {
            this.authInfo = authInfo;
        }
    }

    /**
     * setter method
     * @return query info of the request
     */
    public void setQueryInfo(ArrayList<String[]> queryInfo) {
        this.queryInfo = queryInfo;
    }

    /**
     * setter method
     * @return Headers info of the request
     */
    public void setHeaderInfo(ArrayList<String[]> headerInfo) {
        this.headerInfo = headerInfo;
    }

    /**
     * setter method
     * @return Form Data of the request
     */
    public void setFormDataInfo(Object formDataInfo){
        //the formDataInfo passed to this method is:
        //if UrlEncoded : ArrayList<String[]>
        //if JSON : String
        //if Binary : File
        this.formDataInfo = formDataInfo;
    }

    //costume methods
    public boolean isBearerTokenEnabled(){
        if(!auth) {
            return false;
        }else if(authInfo[2].equals("true")){
            return true;
        }else{
            return false;
        }
    }


}
