package GUI;

/**
 * different types of the Form Data
 */
public enum MESSAGEBODY_TYPE{
    FORM_URL,
    JSON,
    BINARY,
    MULTIPART_FORM;
    /**
     * this method takes a form data and return it in String mode
     * @param formData the form data passed to the method
     * @return the string version of the form data
     */
    public static String getName(MESSAGEBODY_TYPE formData){
        if(formData.equals(FORM_URL)){
            return "FORM_URL";
        }else if(formData.equals(JSON)){
            return "JSON";
        }else if(formData.equals(BINARY)){
            return "BINARY";
        }else if(formData.equals(MULTIPART_FORM)){
            return "MULTIPART_FORM";
        }
        return "what the";
    }

    /**
     * this method takes a form data as an string and return it in MESSAGEBODY_TYPE.ordinal() mode
     * @param formData the string passed to the function
     * @return the MESSAGEBODY_TYPE.ordinal() returned
     */
    public static int getIndex(String formData){
        if(formData.equals(FORM_URL+"")){
            return 0;
        }else if(formData.equals(JSON+"")){
            return 1;
        }else if(formData.equals(BINARY+"")){
            return 2;
        }else if(formData.equals(MULTIPART_FORM+"")){
            return 3;
        }{
            return -1;
        }
    }


    public static MESSAGEBODY_TYPE getFormByIndex(int index){
        if(index==0){
            return FORM_URL;
        }else if(index == 1){
            return JSON;
        }else if(index == 3){
            return BINARY;
        }else if(index == 4){
            return MULTIPART_FORM;
        }
        return null;
    }


    /**
     * this method takes a form data as an string and return it in MESSAGEBODY_TYPE mode
     * @param formData the string passed to the function
     * @return the MESSAGEBODY_TYPE returned
     */
    public static MESSAGEBODY_TYPE getFormData(String formData){
        if(formData.equals(FORM_URL+"")){
            return FORM_URL;
        }else if(formData.equals(JSON+"")){
            return JSON;
        }else if(formData.equals(BINARY+"")){
            return BINARY;
        }else if(formData.equals(MULTIPART_FORM+"")){
            return MULTIPART_FORM;
        }else{
            return null;
        }
    }


}
