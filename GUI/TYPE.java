package GUI;

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

}
