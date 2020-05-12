package InformationHandling;

import GUI.Request;

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
                fileWriter.write(TYPE.getName());



            }
            fileWriter.close();
        }catch(IOException exception){
            System.out.println("Could not save the info");
            return;
        }

    }


    public static void main(String[] args) {
//        new SaveInfo();
    }
}


