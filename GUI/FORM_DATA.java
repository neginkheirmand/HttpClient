package GUI;

public enum FORM_DATA{
    FORM_URL,
    JSON,
    BINARY;
    public static String getName(FORM_DATA formData){
        if(formData.equals(FORM_URL)){
            return "FORM_URL";
        }else if(formData.equals(JSON)){
            return "JSON";
        }else if(formData.equals(BINARY)){
            return "BINARY";
        }else{
            return null;
        }
    }
}
