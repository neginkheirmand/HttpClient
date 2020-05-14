package InformationHandling;

import GUI.FORM_DATA;
import GUI.Request;
import GUI.TYPE;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class LoadInfo {

    private ArrayList<Request> savedRequests = null;
    private boolean couldReadWithOutProblems ;
    private boolean couldReadPreferencesWithOutProblems ;
    private ArrayList<String> preferences = null;


    public LoadInfo() {
        couldReadWithOutProblems=true;
//        System.out.println(couldReadWithOutProblems+"1");
        File file = new File(".");
        savedRequests = new ArrayList<>();
        file = new File((new File(".")).getAbsolutePath() + "\\src\\InformationHandling\\SavedInformation\\RequestHistory.txt");
        if (file.exists()) {
            System.out.println("we have saved info1");
        } else {
            System.out.println("dont really have saved info");
            couldReadWithOutProblems=false;
            return;
        }
        //created or not what is important is that the file exists already
//        System.out.println(couldReadWithOutProblems+"2");


        try {
            Scanner fileReader = new Scanner(file);
            //first we write the number of requests
            String str = fileReader.nextLine();
            str = str.replace("\n", "").replace("\r", "").replaceAll("", "").trim();
//            System.out.println("1)number of requests= *"+str+"*");
            int numRequests=0;
//            System.out.println(couldReadWithOutProblems+"3");

            try {
                numRequests = Integer.parseInt(str);
            }catch(NumberFormatException exception){
                System.out.println("cant transform "+str+" to int");

            }

//            System.out.println(couldReadWithOutProblems+"4");
//            System.out.println("2)num of request = *" + numRequests+"*");
            for (int i = 0; i < numRequests; i++) {
//                System.out.println(couldReadWithOutProblems+"5");

                // 1- the name of request
                str = fileReader.nextLine();
                String nameOfRequest = str.replace("\n", "").replace("\r", "");
//                System.out.println(i+") name of request: *" + nameOfRequest+"*");
//                System.out.println(couldReadWithOutProblems+"6 i="+i);

                //the Type of request : GET
                str = fileReader.nextLine();
                str = str.replace("\n", "").replace("\r", "");
//                System.out.println(i+")method Request = *"+TYPE.getTYPE(str)+"*");
                TYPE typeOfRequest = TYPE.getTYPE(str);
                //the url "it could be nothing"
                str = fileReader.nextLine();
                String url = str.replace("\n", "").replace("\r", "");
//                System.out.println(i+")url = *"+url+"*");
//                System.out.println(couldReadWithOutProblems+"7 i="+i);
//                savedRequests.get(i).setUrl(str);
                //now the type of FORM DATA and after it depending of this type, the Object is to be written in the file
                str = fileReader.nextLine();
//                System.out.println(couldReadWithOutProblems+"8 i="+i);
                str = str.replace("\n", "").replace("\r", "");
//                System.out.println("(if the next is wrong check it)the Form Data is : *"+str+"*");
                FORM_DATA typeOfFormData = FORM_DATA.getFormData(str);
//                System.out.println(i+")FormData = "+typeOfFormData);
//                System.out.println(couldReadWithOutProblems+"9 i="+i);
                Request newRequest = new Request(nameOfRequest, typeOfRequest, url, typeOfFormData);
                newRequest.setSaved(true);
                savedRequests.add(newRequest);
//                fileReader.close();

                if (!readTheFormDataInFile(fileReader, typeOfFormData, i )) {
                    couldReadWithOutProblems=false;
//                    System.out.println("Could not save the info10");
                    return;
                }
//                System.out.println(couldReadWithOutProblems+"10 i="+i);

//                fileReader = new Scanner(file);
                //now the request's info
                str = fileReader.nextLine();
                str = str.replace("\n", "").replace("\r", "");
//                System.out.println(i+")have auth = "+str);
                if(str.equals("true")) {
                    savedRequests.get(i).setAuth(true);
                }else{
                    savedRequests.get(i).setAuth(false);
                }
//                System.out.println(couldReadWithOutProblems+"11 i="+i);

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
                if(!readHeaderInFile( fileReader, i )){
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

    public ArrayList<String> loadPreferences(){
        couldReadPreferencesWithOutProblems=true;
        File preferenceFile = new File((new File(".")).getAbsolutePath() + "\\src\\InformationHandling\\SavedInformation\\Preferences.txt");
        preferences = new ArrayList<>();

        if (preferenceFile.exists()) {
            System.out.println("we have saved preference in the load info class");
        } else {
            System.out.println("dont really have saved preference info in the load info class");
            couldReadPreferencesWithOutProblems=false;
            return null;
        }
//*
        try {
            Scanner fileReader = new Scanner(preferenceFile);
            //first we read the Follow Redirect
            String str = fileReader.nextLine();
            str = str.replace("\n", "").replace("\r", "").replaceAll("", "").trim();
            System.out.println("1-  *"+str+"*");
            preferences.add(str);

            //then the hide in tray system
            str = fileReader.nextLine();
            str = str.replace("\n", "").replace("\r", "").replaceAll("", "").trim();
            System.out.println("2-  *"+str+"*");
            preferences.add(str);

            //then the forground color
            str = fileReader.nextLine();
            str = str.replace("\n", "").replace("\r", "").replaceAll("", "").trim();
            System.out.println("3-  *"+str+"*");
            preferences.add(str);

            //then the forground color
            str = fileReader.nextLine();
            str = str.replace("\n", "").replace("\r", "").replaceAll("", "").trim();
            System.out.println("4-  *"+str+"*");
            preferences.add(str);

            System.out.println("done");
            fileReader.close();
            return preferences;
        } catch (IOException exception) {
            couldReadPreferencesWithOutProblems=false;
            System.out.println("Could not load the info8");
            return null;
        }
// */
    }

    public boolean getCouldReadWithOutProblems(){
        return couldReadWithOutProblems;
    }

    public boolean isCouldReadPreferencesWithOutProblems() {
        return couldReadPreferencesWithOutProblems;
    }

    public ArrayList<Request> getSavedRequests(){
        return savedRequests;
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
//                System.out.println("the name in the*"+i+"* pair is *"+name+"*");

                str =fileReader.nextLine();
                String value = str.replace("\n", "").replace("\r", "");
//                System.out.println("the value in the*"+i+"* pair is *"+value+"*");

                str =fileReader.nextLine();
                String enabled = str.replace("\n", "").replace("\r", "");
//                System.out.println("the pair*"+i+"* is enabled: *"+value+"*");
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
//            System.out.println("Json.numberOfLines= *"+numOfLines+"*");
            for(int i=0; i<numOfLines; i++) {
                JSON += fileReader.nextLine();
            }
//            System.out.println("JSON text = *"+JSON+"*");
            savedRequests.get(index).setFormDataInfo(JSON);
//                fileReader.close();
            return true;
        } else if (typeOfFormat.ordinal() == 2) {
            String pathPfFile = fileReader.nextLine();
            pathPfFile = pathPfFile.replace("\n", "").replace("\r", "");
//            System.out.println("path of file = *"+pathPfFile+"*");
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
            //loading the toke
            String str = fileReader.nextLine();
            String token = str.replace("\n", "").replace("\r", "");
//            System.out.println("   the token = *" + str + "*");

            str = fileReader.nextLine();
            String prefix = str.replace("\n", "").replace("\r", "");

//            System.out.println("   the prefix = *" + str + "*");
            str = fileReader.nextLine();
            String enabled = str.replace("\n", "").replace("\r", "");
//            System.out.println("   enabled = *" + str + "*");

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
//        System.out.println("the next thing readen is: *"+str+"* its either null or the number of query's");
        if(str.equals("null")){
//            System.out.println("was null so will return");
            return true;
        }
        int numberOfQueryPairs=0;
        try {
            numberOfQueryPairs = Integer.parseInt(str);
        }catch (NumberFormatException exception){
            System.out.println("cant transform "+str+" to int0");
        }
        ArrayList<String[]> queryInfo = new ArrayList<>();

//        System.out.println("the number of query pairs in the query tab is *"+numberOfQueryPairs+"*");
        for (int i = 0; i < numberOfQueryPairs; i++) {
//            System.out.println("query"+1);
            str = fileReader.nextLine();
            String name = str.replace("\n", "").replace("\r", "");
//            System.out.println("query"+2);
            str = fileReader.nextLine();
            String value = str.replace("\n", "").replace("\r", "");
//            System.out.println("query"+3);
            str = fileReader.nextLine();
            String enabled = str.replace("\n", "").replace("\r", "");
//            System.out.println("query"+4);
            String[] queryPair = {name, value, enabled};
//            System.out.println("query"+5);
            queryInfo.add(queryPair);
//            System.out.println("the query info is :*"+queryInfo.get(i)[0] + "-" + queryInfo.get(i)[1] + "-" + queryInfo.get(i)[2] + "*\n");
        }
        savedRequests.get(index).setQueryInfo(queryInfo);
//            fileReader.close();
        return true;

    }

    private boolean readHeaderInFile(Scanner fileReader, int index) {

//            fileReader = new Scanner(file);
        String str = fileReader.nextLine();
        str = str.replace("\n", "").replace("\r", "");
//        System.out.println("the next thing readen is: *"+str+"* its either null or the number of query's");
        if(str.equals("null")){
//            System.out.println("was null so will return");
            return true;
        }
        int numberOfQueryPairs=0;
        try {
            numberOfQueryPairs = Integer.parseInt(str);
        }catch (NumberFormatException exception){
            System.out.println("cant transform "+str+" to int0");
        }
        ArrayList<String[]> headerInfo = new ArrayList<>();

//        System.out.println("the number of query pairs in the query tab is *"+numberOfQueryPairs+"*");
        for (int i = 0; i < numberOfQueryPairs; i++) {
//            System.out.println("header"+1);
            str = fileReader.nextLine();
            String name = str.replace("\n", "").replace("\r", "");
//            System.out.println("header"+2);
            str = fileReader.nextLine();
            String value = str.replace("\n", "").replace("\r", "");
//            System.out.println("header"+3);
            str = fileReader.nextLine();
            String enabled = str.replace("\n", "").replace("\r", "");
//            System.out.println("header"+4);
            String[] queryPair = {name, value, enabled};
//            System.out.println("header"+5);
            headerInfo.add(queryPair);
//            System.out.println("the header info is :*"+headerInfo.get(i)[0] + "-" + headerInfo.get(i)[1] + "-" + headerInfo.get(i)[2] + "*\n");
        }
        savedRequests.get(index).setHeaderInfo(headerInfo);
//            fileReader.close();
        return true;

    }

    /**
     * this method is to make sure the loaded info is valuable
     * does it by printing the info loaded
     */
    private void paint(){
        for(int i=0; i<savedRequests.size(); i++){
            System.out.println("name: "+savedRequests.get(i).getNameOfRequest());
            System.out.println("TYPE: "+savedRequests.get(i).getTypeOfRequest());
            System.out.println("url: "+savedRequests.get(i).getUrl());
            System.out.println("FORMAT: "+savedRequests.get(i).getTypeOfData());
            if(savedRequests.get(i).getTypeOfData().equals(FORM_DATA.FORM_URL)) {
                System.out.println("Form Url: ");
                ArrayList<String[]> form = (ArrayList<String[]>)savedRequests.get(i).getFormDataInfo();
                for(int j=0; j<form.size(); j++){
                    System.out.println(j+")"+form.get(j)[0]+" "+form.get(j)[1]+" "+form.get(j)[2]);
                }
            }else if(savedRequests.get(i).getTypeOfData().equals(FORM_DATA.BINARY)){
                System.out.println("binary path: "+(String)savedRequests.get(i).getFormDataInfo());
            }else if(savedRequests.get(i).getTypeOfData().equals(FORM_DATA.JSON)){
                System.out.println("JSON text: "+(String)savedRequests.get(i).getFormDataInfo());
            }
            System.out.println("auth enabled?: "+savedRequests.get(i).getAuth());
            if(savedRequests.get(i).getAuth()) {
                System.out.println("auth: \n" + savedRequests.get(i).getAuthInfo()[0] + "  " + savedRequests.get(i).getAuthInfo()[1] + "   check: " + savedRequests.get(i).getAuthInfo()[2]);
            }else{
                System.out.println("no auth");
            }

            if(savedRequests.get(i).getQueryInfo()!=null){
                System.out.println("query: ");
                for(int j=0; j<savedRequests.get(i).getQueryInfo().size(); j++){
                    System.out.println(j+") "+savedRequests.get(i).getQueryInfo().get(j)[0]+"   "+savedRequests.get(i).getQueryInfo().get(j)[1]);
                }
            }else{
                System.out.println("no querys");
            }
            System.out.println();
            if(savedRequests.get(i).getHeaderInfo()!=null){
                System.out.println("header:");
                for(int j=0; j<savedRequests.get(i).getHeaderInfo().size(); j++){
                    System.out.println(j+") "+savedRequests.get(i).getHeaderInfo().get(j)[0]+"  "+savedRequests.get(i).getHeaderInfo().get(j)[1]);
                }
            }else{
                System.out.println("no headers");
            }
            System.out.println("-------------------------");
        }
    }

//    /*
    //this class works great :)
    public static void main(String[] args) {

        LoadInfo lo=new LoadInfo();
        System.out.println("is true? :"+lo.couldReadWithOutProblems);
        lo.paint();

        lo.loadPreferences();
        System.out.println(lo.couldReadPreferencesWithOutProblems);
    }
//    */
}