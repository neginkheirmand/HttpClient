package GUI;

import java.io.Serializable;
import java.util.ArrayList;




/**
 * this class creates an request
 * @author      Negin Kheirmand <venuskheirmand@gmail.com>
 * @version     1.1
 * @since       1.0
 */


public class Request implements java.io.Serializable {
    //the name of the request
    private String nameOfRequest="";
    //the type of the request
    private TYPE typeOfRequest=TYPE.GET;
    //the url passed to
    private String url="";
    //the first tab takes its info from here
    private MESSAGEBODY_TYPE typeOfData = MESSAGEBODY_TYPE.FORM_URL;
//the next one can be serialized
    private Object formDataInfo = null;
    //is there an authentication
    private boolean auth=false;
    private String[] authInfo=null;
    //the query tab
    private ArrayList<String[]> queryInfo = null;
    //the header tab
    private ArrayList<String[]> headerInfo = null;
    //this if this request is saved or not
    private boolean saved = false;

    //the outputFile if any, if not will remain as ""
    private String nameOutPutContainer = "";
    //the follow redirect
    private boolean followRedirect = false;


    //a pointer to the list of the Requests
    private static ArrayList<Request> listOfrequests;


    /**
     * constructor of the class
     * @param nameOfRequest the name of the request
     * @param typeOfRequest type of the request
     * @param url url of the request
     * @param formatOfData form data of the request
     */
    public Request(String nameOfRequest, TYPE typeOfRequest, String url, MESSAGEBODY_TYPE formatOfData) {
        this.nameOfRequest = nameOfRequest;
        this.typeOfRequest = typeOfRequest;
        this.url = url;
        this.typeOfData = formatOfData;
        queryInfo=new ArrayList<>();
        headerInfo=new ArrayList<>();
        authInfo=new String[3];
        if(typeOfData.equals(MESSAGEBODY_TYPE.FORM_URL)){
            this.formDataInfo = (ArrayList<String[]>) new ArrayList<String[]>();
        }else if(typeOfData.equals(MESSAGEBODY_TYPE.JSON)){
            this.formDataInfo = (String) "enter JSON format";
        }else if(typeOfData.equals(MESSAGEBODY_TYPE.BINARY)){
            this.formDataInfo = (String) "basically the path of the file";
        }
    }

    /**
     * default constructor
     */
    public Request(){

    }

    /**
     * getter method
     * @return boolean showing if the request is saved or not
     */
    public boolean isSaved(){
        return saved;
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
    public MESSAGEBODY_TYPE getTypeOfData() {
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
    public void setTypeOfRequest(int indexOfType) {
        this.typeOfRequest = TYPE.getTypeByIndex(indexOfType);
    }

    /**
     * setter for the TYPE of method for the request field
     * @param methodType
     */
    public void setType(TYPE methodType){
        this.typeOfRequest = methodType;
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
    public void setTypeOfData(int indexOfFormat) {

        this.typeOfData = MESSAGEBODY_TYPE.getFormByIndex(indexOfFormat) ;

        if(typeOfData.equals(MESSAGEBODY_TYPE.FORM_URL)){
            formDataInfo = new ArrayList<String []>();
        }else if(typeOfData.equals(MESSAGEBODY_TYPE.JSON)){
            formDataInfo = "";
        }else if(typeOfData.equals(MESSAGEBODY_TYPE.BINARY)){
            formDataInfo = "";
        }else if(typeOfData.equals(MESSAGEBODY_TYPE.MULTIPART_FORM)){
            formDataInfo = new ArrayList<String []>();
        }
    }

    /**
     * setter method
     * @return auth of the request
     */
    public void setAuth(boolean auth) {
        if(auth) {
            authInfo = new String[3];
        }else{
            authInfo=null;
        }
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
        if(formDataInfo == null){
            System.out.println("\033[0;34m"+"in line 244 of Request Class"+ "\033[0m");
        }

        this.formDataInfo = formDataInfo;

//        this.formDataInfo = formDataInfo;
    }


    /**
     * getter method
     * @return TYPE of request
     */
    public void setSaved(boolean save){
        saved=save;
        return;
    }

    //costume methods

    public void setTypeOfBody(MESSAGEBODY_TYPE bodyType){
        if(bodyType!=null) {
            typeOfData = bodyType;
        }
    }

    public boolean isBearerTokenEnabled(){
        if(!auth) {
            return false;
        }else if(authInfo[2].equals("true")){
            return true;
        }else{
            return false;
        }
    }

    public void setFollowRedirect(boolean followRedirect) {
        this.followRedirect = followRedirect;
    }

    public boolean getFollowRedirect() {
        return followRedirect;
    }

    public void setNameOutPutContainer(String nameOutPutContainer) {
        this.nameOutPutContainer = nameOutPutContainer;
    }

    public String getNameOutPutContainer() {
        return nameOutPutContainer;
    }

    public static void setListOfrequests(ArrayList<Request> list){
        listOfrequests = list;
    }

    public static ArrayList<Request> getListOfrequests(){
        return listOfrequests;
    }


    public static void main(String[] args) {
        Request newRequest = new Request("nameNewRequest" , TYPE.GET, "https://api.myproduct.com/v1/users", MESSAGEBODY_TYPE.FORM_URL);
        newRequest.setSaved(false);

        //since we are creating a new Request with the default format of FORM URL
        ArrayList<String[]> formDataInfo = new ArrayList<>();
        String[] formDataPair ={"new name1","new value1","true"};
        formDataInfo.add(formDataPair);
        newRequest.setFormDataInfo(formDataInfo);

        ArrayList<String[]> queryInfo = new ArrayList<>();
        String[] queryPair = {"new name2", "new value2", "false"};
        queryInfo.add(queryPair);
        newRequest.setQueryInfo(queryInfo);

        ArrayList<String[]> headerInfo = new ArrayList<>();
        String[] headerPair = {"new header 3", "new value 3", "true"};
        headerInfo.add(headerPair);
        newRequest.setQueryInfo(headerInfo);
//        System.out.println(newRequest.formDataInfo.getClass());

    }
}
