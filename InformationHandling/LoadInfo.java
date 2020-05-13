package InformationHandling;

import GUI.FORM_DATA;
import GUI.Request;
import GUI.TYPE;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class LoadInfo {

    private ArrayList<Request> savedRequests;
    private static boolean couldReadWithOutProblems ;

    public LoadInfo() {
        couldReadWithOutProblems=true;
        System.out.println(couldReadWithOutProblems+"1");
        File file = new File(".");
        savedRequests = new ArrayList<>();
        file = new File((new File(".")).getAbsolutePath() + "\\src\\InformationHandling\\SavedInformation\\RequestHistory.txt");
        if (file.exists()) {
            System.out.println("we have saved info");
        } else {
            System.out.println("dont really have saved info");
            couldReadWithOutProblems=false;
            return;
        }
        //created or not what is important is that the file exists already
        System.out.println(couldReadWithOutProblems+"2");


        try {
            Scanner fileReader = new Scanner(file);
            //first we write the number of requests
            String str = fileReader.nextLine();
            str = str.replace("\n", "").replace("\r", "").replaceAll("", "").trim();
            System.out.println("1)number of requests= *"+str+"*");
            int numRequests=0;
//            System.out.println(couldReadWithOutProblems+"3");

            try {
                numRequests = Integer.parseInt(str);
            }catch(NumberFormatException exception){
                System.out.println("cant transform "+str+" to int");

            }

//            System.out.println(couldReadWithOutProblems+"4");
            System.out.println("2)num of request = *" + numRequests+"*");
            for (int i = 0; i < numRequests; i++) {
//                System.out.println(couldReadWithOutProblems+"5");

                // 1- the name of request
                str = fileReader.nextLine();
                String nameOfRequest = str.replace("\n", "").replace("\r", "");
                System.out.println(i+") name of request: *" + nameOfRequest+"*");
//                System.out.println(couldReadWithOutProblems+"6 i="+i);

                //the Type of request : GET
                str = fileReader.nextLine();
                str = str.replace("\n", "").replace("\r", "");
                System.out.println(i+")method Request = *"+TYPE.getTYPE(str)+"*");
                TYPE typeOfRequest = TYPE.getTYPE(str);
                //the url "it could be nothing"
                str = fileReader.nextLine();
                String url = str.replace("\n", "").replace("\r", "");
                System.out.println(i+")url = *"+url+"*");
//                System.out.println(couldReadWithOutProblems+"7 i="+i);
//                savedRequests.get(i).setUrl(str);
                //now the type of FORM DATA and after it depending of this type, the Object is to be written in the file
                str = fileReader.nextLine();
//                System.out.println(couldReadWithOutProblems+"8 i="+i);
                str = str.replace("\n", "").replace("\r", "");
//                System.out.println("(if the next is wrong check it)the Form Data is : *"+str+"*");
                FORM_DATA typeOfFormData = FORM_DATA.getFormData(str);
                System.out.println(i+")FormData = "+typeOfFormData);
//                System.out.println(couldReadWithOutProblems+"9 i="+i);
                Request newRequest = new Request(nameOfRequest, typeOfRequest, url, typeOfFormData);
                savedRequests.add(newRequest);
//                fileReader.close();

                if (!readTheFormDataInFile(fileReader, typeOfFormData, i)) {
                    couldReadWithOutProblems=false;
                    System.out.println("Could not save the info10");
                    return;
                }
//                System.out.println(couldReadWithOutProblems+"10 i="+i);

//                fileReader = new Scanner(file);
                //now the request's info
                str = fileReader.nextLine();
                str = str.replace("\n", "").replace("\r", "");
                System.out.println(i+")have auth = "+str);
                if(str.equals("true")) {
                    savedRequests.get(i).setAuth(true);
                }else{
                    savedRequests.get(i).setAuth(false);
                }
//                System.out.println(couldReadWithOutProblems+"11 i="+i);

//                fileReader.close();
                //str right now is an string of the auth field of the request
                if (str.equals("true")) {
//                    System.out.println(couldReadWithOutProblems+"111 i="+i);
                    //we call the write bearer token as its auth
                    if(!readAuthInFile(fileReader, i)){
                        System.out.println("got stuck in one line");
                        couldReadWithOutProblems=false;
                        return;
                    }
//                    System.out.println(couldReadWithOutProblems+"113 i="+i);
                }
                //now the query
//                System.out.println(couldReadWithOutProblems+"114 i="+i);
                if( !readQueryInFile( fileReader, i )){
//                    System.out.println(couldReadWithOutProblems+"115 i="+i);
                    couldReadWithOutProblems = false;
                    return;
                }
//                System.out.println(couldReadWithOutProblems+"12 i="+i);

                //now the header
                if(!readQueryInFile( fileReader, i )){
                    couldReadWithOutProblems=false;
                    System.out.println("Could not load the info10");
                    return;
                }
//                System.out.println(couldReadWithOutProblems+"13 i="+i);

                //the info about the request has been written
//                fileReader = new Scanner(file);
                fileReader.nextLine();
            }
//            System.out.println(couldReadWithOutProblems+"16");

            System.out.println("done");
            fileReader.close();
          return;
        } catch (IOException exception) {
            couldReadWithOutProblems=false;
            System.out.println("Could not load the info8");
            return;
        }

    }

    private boolean readTheFormDataInFile(Scanner fileReader, FORM_DATA typeOfFormat, int index) {

//            Scanner fileReader = new Scanner(file);
        if (typeOfFormat.ordinal() == 0) {
            System.out.println("the type of Format data is Url encoded");
            //Form URL Encoded
            ArrayList<String[]> formURLEncoded = new ArrayList<String[]>();
            String str =fileReader.nextLine();
            str = str.replace("\n", "").replace("\r", "");
            int numOfPairs=0;
            try {
                numOfPairs = Integer.parseInt(str);
            }catch(NumberFormatException exception){
                System.out.println("cant transform "+str+" to int");
                return false;
            }
            System.out.println("has *"+numOfPairs+"* number of pairs");
            for (int i = 0; i < numOfPairs; i++) {
                str =fileReader.nextLine();
                String name = str.replace("\n", "").replace("\r", "");
                System.out.println("the name in the*"+i+"* pair is *"+name+"*");

                str =fileReader.nextLine();
                String value = str.replace("\n", "").replace("\r", "");
                System.out.println("the value in the*"+i+"* pair is *"+value+"*");

                str =fileReader.nextLine();
                String enabled = str.replace("\n", "").replace("\r", "");
                System.out.println("the pair*"+i+"* is enabled: *"+value+"*");
                String[] formInfo={name, value, enabled};
                formURLEncoded.add(formInfo);
            }
            savedRequests.get(index).setFormDataInfo(formURLEncoded);
//                fileReader.close();
            return true;
        } else if (typeOfFormat.ordinal()==1) {
            //its in JSON format
            String JSON ="";
            String str = fileReader.nextLine();
            str = str.replace("\n", "").replace("\r", "");
            int numOfLines=0;
            try {
                numOfLines = Integer.parseInt(str) + 1;
            }catch(NumberFormatException exception){
                System.out.println("cant transform "+str+" to int");
                return false;
            }
            System.out.println("Json.numberOfLines= *"+numOfLines+"*");
            for(int i=0; i<numOfLines; i++) {
                JSON += fileReader.nextLine();
            }
            System.out.println("JSON text = *"+JSON+"*");
            savedRequests.get(index).setFormDataInfo(JSON);
//                fileReader.close();
            return true;
        } else if (typeOfFormat.ordinal() == 2) {
            String pathPfFile = fileReader.nextLine();
            pathPfFile = pathPfFile.replace("\n", "").replace("\r", "");
            System.out.println("path of file = *"+pathPfFile+"*");
            savedRequests.get(index).setFormDataInfo(pathPfFile);
//                fileReader.close();
            return true;
        } else {
            couldReadWithOutProblems=false;
            System.out.println("not able to load the file6");
            return false;
        }
    }


    private boolean readAuthInFile(Scanner fileReader, int index) {

//            Scanner fileReader = new Scanner(file);
        try {
            String str = fileReader.nextLine();
            String token = str.replace("\n", "").replace("\r", "");
            System.out.println("   the token = *" + token + "*");
            str = fileReader.nextLine();
            String prefix = str.replace("\n", "").replace("\r", "");
            System.out.println("   the prefix = *" + token + "*");
            str = fileReader.nextLine();
            String enabled = str.replace("\n", "").replace("\r", "");
            System.out.println("   enabled = *" + token + "*");
            String[] authInfo = {token, prefix, enabled};
            savedRequests.get(index).setAuthInfo(authInfo);
            return true;
        }catch(Exception exception){
            System.out.println("got stuck in one line");
            return false;
        }

    }


    private boolean readQueryInFile(Scanner fileReader, int index) {

//            fileReader = new Scanner(file);
        String str = fileReader.nextLine();
        str = str.replace("\n", "").replace("\r", "");
        if(str.equals("null")){
            return true;
        }
        int numberOfQueryPairs=0;
        try {
            numberOfQueryPairs = Integer.parseInt(str);
        }catch (NumberFormatException exception){
            System.out.println("cant transform "+str+" to int0");
        }
        ArrayList<String[]> queryInfo = new ArrayList<>();

        System.out.println("the number of query pairs in the query tab is *"+numberOfQueryPairs+"*");
        for (int i = 0; i < numberOfQueryPairs; i++) {

            str = fileReader.nextLine();
            String name = str.replace("\n", "").replace("\r", "");
            str = fileReader.nextLine();
            String value = str.replace("\n", "").replace("\r", "");
            str = fileReader.nextLine();
            String enabled = str.replace("\n", "").replace("\r", "");
            String[] queryPair = {name, value, enabled};
            System.out.println("the query info is :*"+queryInfo.get(i)[0] + "-" + queryInfo.get(i)[1] + "-" + queryInfo.get(i)[2] + "*\n");
            queryInfo.add(queryPair);
        }
        savedRequests.get(index).setQueryInfo(queryInfo);
//            fileReader.close();
        return true;

    }


    public static void main(String[] args) {

        new LoadInfo();
        System.out.println("is true? :"+couldReadWithOutProblems);
    }
}