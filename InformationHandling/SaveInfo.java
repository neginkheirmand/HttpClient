package InformationHandling;

import GUI.FORM_DATA;
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
                String typeOfFormData = FORM_DATA.getName(requestsInfo.get(i).getTypeOfData());
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


    //if its json write the number of lines before writing the actual code
    private boolean writeTheFormDataInFile(File file, String typeOfFormat, Object format) {
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            if (FORM_DATA.getIndex(typeOfFormat) == 0) {
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
            } else if (FORM_DATA.getIndex(typeOfFormat) == 1) {

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
            } else if (FORM_DATA.getIndex(typeOfFormat) == 2) {
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
                System.out.println("not able to save the file6");
                return false;
            }
        }catch (IOException exception) {
            System.out.println("couldnt open the file7");
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
        int num=0;
        for(int i=0; i<str.length(); i++){
            if((str.charAt(i)+"").equals("\n")){
                num++;
            }
        }
        return  num;
    }

    public static void main(String[] args) {

        Request request1 = new Request("name Of request1", TYPE.GET, "www.google1.com", FORM_DATA.JSON);
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


        Request request2 = new Request("name Of request2", TYPE.PATCH, "www.google2.com", FORM_DATA.BINARY);
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


        ArrayList<Request> myRequests = new ArrayList<>();
        myRequests.add(request1);
        myRequests.add(request2);




        new SaveInfo(myRequests);


    }
}


