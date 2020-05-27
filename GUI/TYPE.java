package GUI;

import java.awt.*;

public enum TYPE{
    POST,
    PATCH,
    PUT,
    DELETE,
    GET,
    HEAD,
    OPTION;

    /**
     * this method takes a type as an TYPE and return it in String mode
     * @param type the TYPE  to the function
     * @return the String returned
     */
    public static String getName(TYPE type){
        if(type.equals(POST)){
            return "POST";
        }else if(type.equals(PATCH)){
            return "PATCH";
        }else if(type.equals(PUT)){
            return "PUT";
        }else if(type.equals(DELETE)){
            return "DELETE";
        }else if(type.equals(GET)){
            return "GET";
        }else if(type.equals(HEAD)){
            return "HEAD";
        }else if(type.equals(OPTION)){
            return "OPTION";
        }else{
            return null;
        }
    }

    public static TYPE getTypeByIndex(int index){
        if(index==0){
            return POST;
        }else if(index==1){
            return PATCH;
        }else if(index==2){
            return PUT;
        }else if(index==3){
            return DELETE;
        }else if(index==4){
            return GET;
        }else if(index==5){
            return HEAD;
        }else if(index==6){
            return OPTION;
        }else if(index==7){
            //the costume method
            return null;
        }
        return null;
    }

    /**
     * this method takes a TYPE as an string and return it in TYPE mode
     * @param type the string passed to the function
     * @return the Type returned
     */
    public static TYPE getTYPE(String type){
        if(type.equals("POST")){
            return POST;
        }else if(type.equals("PATCH")){
            return PATCH;
        }else if(type.equals("PUT")){
            return PUT;
        }else if(type.equals("DELETE")){
            return DELETE;
        }else if(type.equals("GET")){
            return GET;
        }else if(type.equals("HEAD")){
            return HEAD;
        }else if(type.equals("OPTION")){
            return OPTION;
        }else{
            return null;
        }
    }

    public static String getColor(TYPE method) {
        if (method.equals(GET)) {
            return "\033[1;35m";
        } else if (method.equals(POST)) {
            return "\033[1;32m";
        } else if (method.equals(PATCH)) {
            return "\033[1;33m";
        } else if (method.equals(PUT)) {
            return "\033[1;37m";
        } else if (method.equals(DELETE)) {
            return "\033[1;31m";
        } else if (method.equals(OPTION)) {
            return "\033[1;34m";
        } else if (method.equals(HEAD)) {
            return "\033[1;36m";
        }
        return "";
    }

}
