package GUI;

/**
 * different types of the Form Data
 */
public enum FORM_DATA{
    FORM_URL,
    JSON,
    BINARY;

    /**
     * this method takes a form data and return it in String mode
     * @param formData the form data passed to the method
     * @return the string version of the form data
     */
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

    /**
     * this method takes a form dataas an string and return it in FORM_DATA mode
     * @param formData the string passed to the function
     * @return the FORM_DATA returned
     */
    public static int getIndex(String formData){
        if(formData.equals(FORM_URL+"")){
            return 0;
        }else if(formData.equals(JSON+"")){
            return 1;
        }else if(formData.equals(BINARY+"")){
            return 2;
        }else{
            return -1;
        }
    }
}
