package GUI;

public enum TYPE{
    POST,
    PATCH,
    PUT,
    DELETE,
    GET,
    HEAD,
    OPTION;
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
