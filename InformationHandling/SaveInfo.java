package InformationHandling;

import GUI.MESSAGEBODY_TYPE;
import GUI.Request;
import GUI.TYPE;

import java.io.FileNotFoundException;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * this class saves the info passed to it
 * @author      Negin Kheirmand <venuskheirmand@gmail.com>
 * @version     1.1
 * @since       1.0
 */
public class SaveInfo {

    public SaveInfo(ArrayList<Request> requestsInfo) {
//        File file = new File((new File(".")).getAbsolutePath() + "\\src\\InformationHandling.SavedInformation\\RequestHistory.txt");
        File file = new File(".");
        try {
            file = new File((new File(".")).getAbsolutePath()+"\\src\\InformationHandling\\SavedInformation\\RequestHistory.txt");
            if(file.createNewFile()) {
                System.out.println("was created");
            }else{
                System.out.println("wasnt created");
            }
            //created or not what is important is that the file exists already

        } catch (IOException exception) {
            System.out.println("Could not save the info");
            return;
        }

        try {
            FileWriter fileWriter = new FileWriter(file);
            //first we write the number of requests
            fileWriter.write(requestsInfo.size()+"\n");
            for(int i=0; i<requestsInfo.size(); i++) {
                // 1- the name of request
                fileWriter.write(requestsInfo.get(i).getNameOfRequest()+"\n");
                //the Type of request : GET
                fileWriter.write(TYPE.getName(requestsInfo.get(i).getTypeOfRequest())+"\n");
                //the url "it could be nothing"
                fileWriter.write(requestsInfo.get(i).getUrl()+"\n");
                //now the type of FORM DATA and after it depending of this type, the Object is to be written in the file
                String typeOfFormData = MESSAGEBODY_TYPE.getName(requestsInfo.get(i).getTypeOfData());
                System.out.println("*"+typeOfFormData);
                fileWriter.write(typeOfFormData+"\n");
                fileWriter.close();
                if(!writeTheFormDataInFile(file, typeOfFormData, requestsInfo.get(i).getFormDataInfo())){
                    System.out.println("Could not save the info10");
                    return;
                }
                fileWriter = new FileWriter(file, true);
                //now the request's info
                fileWriter.write(requestsInfo.get(i).getAuth()+"\n");
                fileWriter.close();
                if((requestsInfo.get(i).getAuth()+"").equals("true")){
                    //we call the write bearer token as its auth
                    if(! writeAuthInFile(file, requestsInfo.get(i).getAuthInfo())){
                        return;
                    }
                }
                //now the query
                if(requestsInfo.get(i).getQueryInfo()!=null){
                    if(!writeQueryInFile(file, requestsInfo.get(i).getQueryInfo())){
                        return;
                    }
                }else{
                    fileWriter = new FileWriter(file, true);
                    fileWriter.write("null\n");
                    fileWriter.close();
                }

                //now the header
                if(requestsInfo.get(i).getHeaderInfo()!=null){
                    writeQueryInFile(file, requestsInfo.get(i).getHeaderInfo());
                }else{
                    fileWriter = new FileWriter(file, true);
                    fileWriter.write("null\n");
                    fileWriter.close();
                }
                //the info about the request has been written
                fileWriter = new FileWriter(file, true);
                fileWriter.write("----\n");
            }
            System.out.println("done");
            fileWriter.close();
        }catch(IOException exception){
            System.out.println("Could not save the info8");
            return;
        }

    }

    public void savePreferences( ArrayList<String> preferences){
        File preferenceFile;
        try {
            preferenceFile = new File((new File(".")).getAbsolutePath()+"\\src\\InformationHandling\\SavedInformation\\Preferences.txt");
            if(preferenceFile.createNewFile()) {
                System.out.println("was created");
            }else{
                System.out.println("wasnt created");

            }
            //created or not what is important is that the file exists already

        } catch (IOException exception) {
            System.out.println("Could not save the preference info in the save class");
            return;
        }

        try {
            FileWriter fileWriter = new FileWriter(preferenceFile);
            //first we write the follow redirect
            fileWriter.write(preferences.get(0)+"\n");
            //first we write the tray in system
            fileWriter.write(preferences.get(1)+"\n");
            //first we write the color of forground
            fileWriter.write(preferences.get(2)+"\n");
            //first we write the color of background
            fileWriter.write(preferences.get(3)+"\n");
            System.out.println("done in the save class for the preference");
            fileWriter.close();
            return;
        }catch(IOException exception){
            System.out.println("Could not save the info about preference in the save file class");
            return;
        }

    }

    //if its json write the number of lines before writing the actual code
    private boolean writeTheFormDataInFile(File file, String typeOfFormat, Object format) {
        try {
            FileWriter fileWriter = new FileWriter(file, true);
//            if (MESSAGEBODY_TYPE.getIndex(typeOfFormat) == 0) {
            if (MESSAGEBODY_TYPE.FORM_URL.toString().equals(typeOfFormat)) {
                ArrayList<String[]> formURLEncoded = (ArrayList<String[]>) format;
                try {
                    fileWriter.write(formURLEncoded.size() + "\n");
                    for (int i = 0; i < formURLEncoded.size(); i++) {

                        String name = (formURLEncoded.get(i))[0];
                        String value = (formURLEncoded.get(i))[1];
                        String enabled = (formURLEncoded.get(i))[2];
                        fileWriter.write(name + "\n" + value + "\n" + enabled + "\n");
                    }
                    fileWriter.close();
                } catch (IOException exception) {
                    System.out.println("not able to save in the file3");
                    return false;
                }
                return true;
//            } else if (MESSAGEBODY_TYPE.getIndex(typeOfFormat) == 1) {
            } else if (MESSAGEBODY_TYPE.JSON.toString().equals(typeOfFormat)) {
                //its JSON
                System.out.println(format.getClass());
                String JSON = (String) format;
                try {
                    fileWriter.write(getNumOfLines(JSON) + "\n");
                    fileWriter.write(JSON + "\n");
                    fileWriter.close();
                } catch (IOException exception) {
                    System.out.println("not able to save the file4");
                    return false;
                }
                return true;
//            } else if (MESSAGEBODY_TYPE.getIndex(typeOfFormat) == 3) {
            } else if (MESSAGEBODY_TYPE.BINARY.toString().equals(typeOfFormat)) {
                //its BINARY
                try {
                    String pathPfFile = (String) format;
                    fileWriter.write(pathPfFile + "\n");
                } catch (IOException exception) {
                    System.out.println("not able to save the file5");
                    return false;
                }
                fileWriter.close();
                return true;
            } else {
                System.out.println("compatible type passed to the write method");
                return false;
            }
        }catch (IOException exception ) {
            System.out.println("couldnt open the file7");
            return false;
        }catch (NullPointerException exception){
            System.out.println("couldnt open the file8");
            return false;

        }
    }

    private boolean writeAuthInFile(File file, String[] authInfo){
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(authInfo[0] + "\n");
            fileWriter.write(authInfo[1] + "\n");
            fileWriter.write(authInfo[2] + "\n");
            fileWriter.close();
            return true;
        }catch(IOException exception){
            System.out.println("could not save the info2");
            return false;
        }
    }

    private boolean writeQueryInFile(File file, ArrayList<String[]> queryInfo){
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file, true);
            System.out.println( queryInfo.size());
            //should save the number of pair so we now when to stop
            fileWriter.write(queryInfo.size()+"\n");
            for (int i = 0; i < queryInfo.size(); i++) {
                fileWriter.write(queryInfo.get(i)[0]+"\n"+queryInfo.get(i)[1]+"\n"+queryInfo.get(i)[2]+"\n");
                System.out.println( queryInfo.get(i)[0]+"\n"+queryInfo.get(i)[1]+"\n"+queryInfo.get(i)[2]+"\n");
            }
//            fileWriter.flush();
            fileWriter.close();
            return true;
        }catch (FileNotFoundException exception){
            System.out.println("not able to save the file1");
            return false;
        }catch(IOException exception){
            System.out.println("not able to save the file1");
            return false;
        }
    }

    private int getNumOfLines(String str){
        if(str==null||str.equals("")){
            return 0;
        }
        int num=0;
        for(int i=0; i<str.length(); i++){
            if((str.charAt(i)+"").equals("\n")){
                num++;
            }
        }
        return  num;
    }

    public static void main(String[] args) {

        Request request1 = new Request("name Of request1", TYPE.GET, "www.google1.com", MESSAGEBODY_TYPE.JSON);
        request1.setAuth(true);
        String[] authInfo1 = {"bearer token", "prefix", "true"};
        request1.setAuthInfo(authInfo1);
        ArrayList<String[]> headers1 = new ArrayList<>();
        String[] header1={
                "new header", "new value", "true"
        };
        headers1.add(header1);
        request1.setHeaderInfo(headers1);
        request1.setQueryInfo(null);
        request1.setFormDataInfo("this text written as the code in Json ");


        Request request2 = new Request("name Of request2", TYPE.PATCH, "www.google2.com", MESSAGEBODY_TYPE.BINARY);
        request2.setAuth(true);
        String[] authInfo2 = {"bearer token", "prefix", "true"};
        request2.setAuthInfo(authInfo2);
        ArrayList<String[]> headers2 = new ArrayList<>();
        String[] header21={
                "new header1", "new value1", "true"
        };
        String[] header22={
                "new header2", "new value2", "true"
        };
        headers2.add(header21);
        headers2.add(header22);
        request2.setHeaderInfo(headers2);
        String[] query2={
                "new name query2", "new value2", "true"
        };
        ArrayList<String []> queryPairs = new ArrayList<>();
        queryPairs.add(query2);
        request2.setQueryInfo(queryPairs);
        request2.setFormDataInfo("this is supposed to be the path of a file");


        Request request3 = new Request("name Of request3", TYPE.PUT, "www.google3.com", MESSAGEBODY_TYPE.FORM_URL);
        request3.setAuth(false);
        ArrayList<String[]> headers3 = new ArrayList<>();
        String[] header31={
                "new header1", "new value1", "true"
        };
        String[] header32={
                "new header2", "new value2", "true"
        };
        String[] header33={
                "new header3", "new value3", "true"
        };
        String[] header34={
                "new header4", "new value4", "true"
        };
        headers3.add(header31);
        headers3.add(header32);
        headers3.add(header33);
        headers3.add(header34);
        request3.setHeaderInfo(headers3);
        String[] query31={
                "new name query3 1", "new value3 1", "true"
        };
        String[] query32={
                "new name query3 2", "new value3 2", "true"
        };

        ArrayList<String []> queryPairs3 = new ArrayList<>();
        queryPairs3.add(query31);
        queryPairs3.add(query32);
        request3.setQueryInfo(queryPairs3);
        ArrayList<String[]> formUrl = new ArrayList<>();
        String[] form1={
                "new form url 1", "new value 1", "true"
        };
        String[] form2={
                "new form url 2", "new value 2", "true"
        };
        String[] form3={
                "new form url 3", "new value 3", "true"
        };

        formUrl.add(form1);
        formUrl.add(form2);
        formUrl.add(form3);
        request3.setFormDataInfo(formUrl);



        ArrayList<Request> myRequests = new ArrayList<>();
        myRequests.add(request1);
        myRequests.add(request2);
        myRequests.add(request3);



        SaveInfo newSaveInfo = new SaveInfo(myRequests);
        ArrayList<String> preferencesArrayList = new ArrayList<>();
        preferencesArrayList.add("false");
        preferencesArrayList.add("false");
        preferencesArrayList.add("purple");
        preferencesArrayList.add("dark");
        newSaveInfo.savePreferences(preferencesArrayList);

    }
}


