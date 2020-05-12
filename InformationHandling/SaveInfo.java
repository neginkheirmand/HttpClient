package InformationHandling;

import GUI.FORM_DATA;
import GUI.Request;
import GUI.TYPE;

import java.io.FileWriter;   // Import the FileWriter class
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
                writeTheFormDataInFile(fileWriter, typeOfFormData, requestsInfo.get(i).getFormDataInfo());

            }
            fileWriter.close();
        }catch(IOException exception){
            System.out.println("Could not save the info");
            return;
        }

    }

    private boolean writeTheFormDataInFile(FileWriter fileWriter, String typeOfFormat, Object format) {
        if (FORM_DATA.getIndex(typeOfFormat) == 0) {
            ArrayList<String[]> formURLEncoded = (ArrayList<String[]>) format;
            try {
                fileWriter.write(formURLEncoded.size() + "\n");
                for (int i = 0; i < formURLEncoded.size(); i++) {

                    String name = (formURLEncoded.get(i))[0];
                    String value = (formURLEncoded.get(i))[1];
                    String enabled = (formURLEncoded.get(i))[2];
                    fileWriter.write(name + "-" + value + "-" + enabled + "\n");
                }
            } catch (IOException exception) {
                System.out.println("not able to save in the file");
                return false;
            }
            return true;
        } else if (FORM_DATA.getIndex(typeOfFormat) == 1) {
            String JSON = (String) format;
            try {
                fileWriter.write(getNumOfLines(JSON) + "\n");
                for

            } catch (IOException exception) {
                System.out.println("not able to save the file");
                return false;
            }
            return true;
        } else if (FORM_DATA.getIndex(typeOfFormat) == 2) {

            return true;
        } else {
            System.out.println("not able to save the file");
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
//        new SaveInfo();
    }
}


