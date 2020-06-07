package Bash;

import GUI.FORM_DATA;
import GUI.Request;
import GUI.TYPE;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Command {
    private ArrayList<Integer> numCommand;
    private ArrayList<String> strCommand;
    private static ArrayList<Command> savedRequests = new ArrayList<>();
    private Request request;
    private boolean showHeaders =false;
    private boolean commandLine = true;

    //the last command was a list command
    private static boolean list = false;


    public Command(String input, boolean commandLine) {
        //must set Request field later in the do command method
        request = new Request();
        this.commandLine = commandLine;

//        this.numCommand = numCommand;
//        this.strCommand = strCommand;
        Options option =new Options();
        option.identifyRequestOptions(input);
        if(commandLine){
            //using the command line
            System.out.println("\033[0;31m"+"Warning:"+"\033[0m"+"This version doesnt support output files with names containing the space char");
        }
        numCommand = (option).getRequestNumberCommand();
        strCommand = (option).getRequestStringCommand();
        doCommand();
    }


    public ArrayList<Integer> getNumCommand() {
        return numCommand;
    }

    public ArrayList<String> getStrCommand() {
        return strCommand;
    }

    public Request getRequest() {
        return request;
    }

    private boolean canApply() {
        if (numCommand.size() == 0) {
            return false;
        }
        if (numCommand.get(0) != 11) {
            //command must start with String "curl"
            return false;
        }
        return true;
    }

    private void doCommand() {
        if (!canApply()) {
            return;
        }
        //now we are sure the command starts with "curl" word so we move one index forward
        if (numCommand.get(1) == 10) {
            //wants to list
            list();
            list = true;
            return;
        } else if (numCommand.get(1) == 12) {
            System.out.println("its a fire command and can be done? :" + list);
            //its a fire command make sure this command is used right after the list command or else no output
            if (list) {
                list = false;
                fire();
            } else {
                System.out.println("the \"fire\" command should be used right after the \"list\" command");
            }
            return;
        }
        list=false;
        doMethod(this);
    }

    private void list() {
        String cross = "\033[0;31m" + "\u274C" + "\033[0m";
        String tick = "\033[0;31m" + "\u2713" + "\033[0m";

        if (savedRequests.size() == 0) {
            System.out.println("no requests saved!");
            return;
        }

        for (int i = 0; i < savedRequests.size(); i++) {
            System.out.printf("\033[0;31m" + (i + 1) + "\033[0m" + ". url:");
            //printing th url
            if (request.getUrl().length() == 0) {
                System.out.printf("\033[0;31m" + " no url saved\033[0m");
            } else {
                System.out.printf(request.getUrl());
            }

            System.out.printf("| ");
            //now the headers:
            if (request.getHeaderInfo()!=null && request.getHeaderInfo().size() != 0) {
                System.out.printf("headers: ");
                for (int j = 0; j < request.getHeaderInfo().size(); j++) {
                    System.out.printf(request.getHeaderInfo().get(j)[0] + ": " + request.getHeaderInfo().get(j)[1] + "enabled: ");
                    if (request.getHeaderInfo().get(j)[2].equals("true")) {
                        System.out.printf(tick);
                    } else {
                        System.out.printf(cross);
                    }
                }
            }

            System.out.printf("| ");
            //now the query
            if (request.getQueryInfo()!=null && request.getQueryInfo().size() != 0) {
                System.out.printf("query info: ");
                for (int j = 0; j < request.getQueryInfo().size(); j++) {
                    System.out.printf(request.getQueryInfo().get(j)[0] + ": " + request.getQueryInfo().get(j)[1] + "enabled: ");
                    if (request.getQueryInfo().get(j)[2].equals("true")) {
                        System.out.printf(tick);
                    } else {
                        System.out.printf(cross);
                    }
                }
            }

            System.out.printf("| ");
            System.out.printf("Method:" + TYPE.getColor(request.getTypeOfRequest()) + request.getTypeOfRequest().name() + "\033[0m");

            System.out.printf("| ");

            System.out.println("FormData: " + FORM_DATA.getName(request.getTypeOfData()));

        }

    }

    private void fire() {
        for (int i = 2; i < numCommand.size(); i++) {
            try {
                int numCommand = Integer.parseInt(strCommand.get(i));
                if (numCommand > savedRequests.size() || numCommand <= 0) {
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+"enter a valid command number, the list of requests has only " + savedRequests.size() + " requests");
                    list = true;
                    return;
                }
                doMethod(savedRequests.get(numCommand - 1));
            } catch (NumberFormatException exception) {
                System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" After the fire word the Command-Number should be written");
                list = true;
                return;
            }
        }
    }

    private static void doMethod(Command commandToDo) {

        //the first word in the command was curl
        //the second one should be the url
        //get the url
        if(commandToDo.numBodyType()>1){
            System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" Cannot have more than 1 type of message body");
            return;
        }

        for(int i = 1; i<commandToDo.getNumCommand().size(); i++){
            if(commandToDo.getNumCommand().get(i)==-1){
                if(commandToDo==null || commandToDo.getRequest()==null ){
                    System.out.println("Problems with the request");
                    return;
                }
                if( commandToDo.getRequest().getUrl()==null || commandToDo.getRequest().getUrl().length()==0 ){
                    //the url was not defined this is the url
                    commandToDo.getRequest().setUrl(commandToDo.getStrCommand().get(i));
//                }else{
//                    System.out.println("too many arguments for the specified options, try again.");
//                    return;
                }
            }else if(commandToDo.getNumCommand().get(i)==0) {
                //specify the method -M and --method
                //this option takes the next word too so we have to make sure there actually is a next word
                if (i == commandToDo.getNumCommand().size()-1 || commandToDo.getNumCommand().get(i+1)!=-1) {
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+"after -M/--method comes the type of method");
                    System.out.println("methods supported:");
                    System.out.println("POST\nPATCH\nPUT\nDELETE\nGET\nHEAD\nOPTION");
                    System.out.println("try again!");
                    return;
                } else {
                    i++;
                    TYPE methodOfRequest = TYPE.getTYPE(commandToDo.getStrCommand().get(i));
                    if(methodOfRequest == null){
                        //invalid method name
                        System.out.println("\033[0;31m"+"Error:"+"\033[0m"+"invalid method name");
                        System.out.println("methods supported:");
                        System.out.println("POST\nPATCH\nPUT\nDELETE\nGET\nHEAD\nOPTION");
                        System.out.println("try again!");
                        return;
                    }
                    //here we are sure the user entered a valid method so we set the method
                    commandToDo.getRequest().setType(methodOfRequest);
                    //the method was set
                }
            }else if(commandToDo.getNumCommand().get(i)==1){
                //its the headers : -H / --headers
                //this input option comes and after it the name o header and value come as an string
                //so we make sure actually is a next word in the String of the input
                if(i==commandToDo.getNumCommand().size()-1 || commandToDo.getNumCommand().get(i+1)!=-1){
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+"no headers specified\nPlease try again!");
                    return;
                }
                //in between " " (double quotation) and separated with ; (semicolon)
                i++;
                ArrayList<String[]> headerOfrequest = commandToDo.splitHeader(commandToDo.getStrCommand().get(i));
                //now to make sure the input is as expected:
                if(headerOfrequest==null){
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+"Invalid headers");
                    return;
                }
                commandToDo.getRequest().setHeaderInfo(headerOfrequest);

                //the header was specified
            }else if(commandToDo.getNumCommand().get(i)==2){
                //the -i/--include option
                //when this option is in the request command the response of this request must show
                commandToDo.showHeaders=true;
            }else if(commandToDo.getNumCommand().get(i)==3){
                //the -h/-help command
                System.out.println("\033[0;32m"+"Usage: curl [options...] <url>\n"+"\033[0m");

                // -d
                System.out.println("\033[0;32m"+"-d, --data"+"\033[0m"+" <data>   HTTP POST data\n" +
                        "     --data-binary <data> HTTP POST binary data\n" +
                        "     --data-urlencoded <data> HTTP POST data url encoded\n");
                //-f
                System.out.println("-f, --follow        follow redirection");
                //fire
                System.out.println("fire,               use after calling the \"curl list\" method to trigger saved request by their index of use");
                //-h
                System.out.println("-h, --help          This help text\n");
                //-H
                System.out.println("-H, --header <header/@file> Pass custom header(s) to server\n");
                //-i
                System.out.println("-i, --include       Include protocol response headers in the output\n");
                //-j
                System.out.println("-j,\n --json the format of the message body");
                //-M
                System.out.println("-M, --method        specify method request");
                //list
                System.out.println("list                list the saved requests");
                //-O
                System.out.println("-O, --output <file> Write to file instead of stdout");
                //-S
                System.out.println("-S, --save          save the request and add it to the list of saved requests");
                //--upload
                System.out.println("--upload  <absolute path>          upload existing file using its absolute path");
            }else if(commandToDo.getNumCommand().get(i)==4){
                commandToDo.getRequest().setFollowRedirect(true);
            }else if(commandToDo.getNumCommand().get(i)==5){
                //-O
                //put the response of the request in a file
                //in this case we have to check if the next word in the command is the name of the file or the next option to imply
                //first we see if there is any other word in the command after this one
                String nameOfOutputFile = "";
                if(i==commandToDo.getNumCommand().size()-1 || commandToDo.getNumCommand().get(i+1)!=-1){
                    //its the last one so the name of the file will be chosen automatically
                    SimpleDateFormat formatter = new SimpleDateFormat("ss--MM--hh--dd-MM-yyyy");
                    Date date = new Date();
                    nameOfOutputFile="output_["+formatter.format(date)+"]";

                }else{
                    //the chosen name by the user
                    i++;
                    for(int j=0; j<commandToDo.getStrCommand().get(i).length(); j++){
                        if( (j==0||j==commandToDo.getStrCommand().get(i).length()-1) && commandToDo.getStrCommand().get(i).charAt(j)=='\"' ){
                            continue;
                        }
                        nameOfOutputFile+=commandToDo.getStrCommand().get(i).charAt(j);
                    }
                }

                commandToDo.getRequest().setNameOutPutContainer(nameOfOutputFile);

            }else if(commandToDo.getNumCommand().get(i)==6){
                //-s the save
                commandToDo.getRequest().setSaved(true);
                //remember to do the save in the end
            }else if(commandToDo.getNumCommand().get(i)==7){
                //-d
                //but what if the user triggers both the -d and -j method at the very same time??
                //then this request cannot be done and we will just return

                //this is basically the multipart form data of the message body:
                //checking if the -d is the last one then means the message body will come in the next line but if its not and the next
                // string in the input is a String starting and ending with " means the message body came in the same line
                //if after the option of -d comes another option means
                String messageBody = "";
                if(i==commandToDo.getNumCommand().size()-1){
                    //its the last one so the body is in the next line
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+"Used -d option but did not specify the message body");
                }else if(commandToDo.getNumCommand().get(i+1)==-1){
                    //its the next String after this one
                    messageBody = commandToDo.getStrCommand().get(i+1);
                }else{
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+"wrong command, pay attention to the -d option");
                }
                ArrayList<String[]> formData = commandToDo.splitData(messageBody);
                if(formData==null){
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" Invalid body");
                    return;
                }
                //so we are sure we actually have an valid message body in multipart format
                System.out.println("valid message body");
                commandToDo.getRequest().setTypeOfBody(FORM_DATA.FORM_URL);
                commandToDo.getRequest().setFormDataInfo(formData);
            }else if(commandToDo.getNumCommand().get(i)==8){
                //-j
                //we should take the input
                String jsonMsBody = "";
                if(commandToDo.getNumCommand().size()-1==i || commandToDo.getNumCommand().get(i+1)!=-1){
                    //we are in the last word/option of the command
                    //so the user has to enter the json body message in the next line
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" used -j/--json option but did not specified the message body.");
                }else if(commandToDo.getNumCommand().get(i+1)==-1){
                    //the next string in the input is the json
                    jsonMsBody = commandToDo.getStrCommand().get(i+1);
                }

                try {
                    System.out.println("*"+jsonMsBody+"*");
                    JSONObject jsonObject = new JSONObject(jsonMsBody);
                    System.out.println(jsonObject.toString());
                }catch (JSONException err){
//                Log.d("Error", err.toString());
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" incorrect format of input.\nCorrect form example: \n" +
                            "curl ... -j/--json {key1 : value1, key2 : value2}\nor \ncurl ... --json/-j {\"key1\":\"value1\", \"key2\":\"value2\"}");
                    return;
                }
                //now we have the json object
                commandToDo.getRequest().setTypeOfBody(FORM_DATA.JSON);
                commandToDo.getRequest().setFormDataInfo(jsonMsBody);
            }else if(commandToDo.getNumCommand().get(i)==9){
                //--upload
                //in this part we wil be uploading a bin file
                String pathOfFile = "";
                if(commandToDo.getNumCommand().size()-1==i){
                    //we are in the last word/option of the command
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" Used --upload option but did not specified the path of file.");
                }else if(commandToDo.getNumCommand().get(i+1)==-1){
                    //the next string in the input is the path of file
                    pathOfFile = commandToDo.getStrCommand().get(i+1);
                }else{
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" Used --upload option but did not specified the path of file.");
                }
                File file = new File(pathOfFile);
                if(file.exists() && file.isFile()){
                    System.out.println("the file exist, HERE WE HAVE TO UPLOAD IT");
                    commandToDo.getRequest().setTypeOfBody(FORM_DATA.BINARY);
                    commandToDo.getRequest().setFormDataInfo(file.getAbsolutePath());
                }else if(file.exists() && file.isDirectory()){
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" invalid path -pointing to directory-");
                    return;
                }else{
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+"invalid path to file");
                    return;
                }
            }else if(commandToDo.getNumCommand().get(i)==13){
                //-a/--auth
                String auth = "";
                if(commandToDo.getNumCommand().size()-1==i){
                    //we are in the last word/option of the command
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" Used -a/--auth option but did not specified the token");
                }else if(commandToDo.getNumCommand().get(i+1)==-1){
                    //the next string in the input is bearer token
                    auth = commandToDo.getStrCommand().get(i+1);
                }else{
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" Used -a/--auth option but did not specified the bearer token");
                }
                commandToDo.getRequest().setAuth(true);
                String[] authArray = getAuthInfo(auth, commandToDo);
                if(authArray!=null) {
                    commandToDo.getRequest().setAuthInfo(authArray);
                }else{
                    return;
                }
            }else if(commandToDo.getNumCommand().get(i)==14){
                //-q/--query
                String query = "";
                if(commandToDo.getNumCommand().size()-1==i){
                    //we are in the last word/option of the command
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" Used -q/--query option but did not specified the params");
                }else if(commandToDo.getNumCommand().get(i+1)==-1){
                    //the next string in the input is String holding the query params
                    query = commandToDo.getStrCommand().get(i+1);
                }else{
                    System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" Used -q/--query option but did not specified the params");
                    return;
                }
                //separate with & so we will use the same function we did for the headers
                ArrayList<String[]> queryParams = commandToDo.splitData(query);
                if(queryParams!=null){
                    commandToDo.getRequest().setQueryInfo(queryParams);
                }else{
                    return;
                }
            }
        }
        if(commandToDo.getRequest().isSaved()){
            //gotta save the file
            savedRequests.add(commandToDo);
        }
        new Executer(commandToDo.getRequest(), commandToDo);
    }

    private static String[] getAuthInfo(String auth, Command commandToDo){
        String[] authInfo= new String[3];
        authInfo[0] = "Authorization";
        authInfo[1]="";
        if(auth.charAt(0)!='\"' && auth.charAt(auth.length()-1)!='\"') {
            authInfo[1] = auth;
        }else{
            for(int i=1; i<auth.length()-1; i++){
                authInfo[1]+=auth.charAt(i);
            }
        }
        authInfo[2] = "true";
        System.out.println(authInfo[1]);
        if(authInfo[1].length()==0){
            return null;
        }
        return authInfo;
    }

    private boolean getCommand(){
        return commandLine;
    }

    private int numBodyType(){
        int num=0;
        for(int i=0; i<numCommand.size(); i++){
            if(numCommand.get(i)==7 || numCommand.get(i)==8 || numCommand.get(i)==9 ){
                num++;
            }
        }
        return num;
    }

    public boolean getShowHeaders() {
        return showHeaders;
    }

    //still not sure if it actually works with ;
    private ArrayList<String[]> splitHeader(String input){
//        System.out.println("[0]="+input.charAt(0));
//        System.out.println("[n-1]="+input.charAt(input.length()-1));
        ArrayList<String[]> headers = new ArrayList<>();
        if(!commandLine) {
            if (input.length() == 0) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " did not enter headers but specified option -H/--headers");
                return null;
            }
            if (input.charAt(0) != '\"' || input.charAt(input.length() - 1) != '\"') {
                System.out.println("invalid header input, should be in between \" \" and each pair of" +
                        "\nheader and value separated with ; (semicolon) with no spaces in between");
                System.out.println("ex:\n\"header1=value1;header2=value2\"");
                return null;
            }
            String[] pair = new String[3];
            String temp = "";
            int pairNum = 0;
            for (int i = 1; i < input.length() - 1; i++) {
                //all the pairs are enabled so the pair[2] is "true"
                if (input.charAt(i) != '=' && input.charAt(i) != ';') {
                    temp += input.charAt(i);
                    if (i == input.length() - 2) {
                        if (pairNum == 1 && temp.length() != 0) {
                            pair[1] = temp;
                            //gotta ad it to the arraylist
                            pair[2] = "true";
                            headers.add(pair);
                            pair = new String[3];
                            temp = "";
                            pairNum = 0;
                        } else if (temp.length() == 0) {
                            System.out.println("You cannot have an empty string as value");
                            return null;
                        } else {
                            System.out.println("in between info and corresponding value there should be a = not a ; ");
                            System.out.println("You cannot have an empty string as value");
                            return null;
                        }
                    }

                } else if (input.charAt(i) == '=') {
                    //should enter this state at pairNum==0
                    if (pairNum == 0 && temp.length() != 0) {
                        pair[0] = temp;
                        temp = "";
                        pairNum++;
                    } else if (temp.length() == 0) {
                        System.out.println("You cannot have an empty string as header name");
                        return null;
                    } else {
                        System.out.println("invalid pair of header name and value, each pair separated by ; not =");
                        System.out.println("You cannot have an empty string as header name");
                        return null;
                    }
                } else if (input.charAt(i) == ';' || i == input.length() - 2) {
                    //should enter this in state pairNum==1
                    //here we have to check if the temp is made only with spaces
                    //we are gonna take out the spaces

                    if (pairNum == 1 && temp.length() != 0) {
                        pair[1] = temp;
                        //gotta ad it to the arraylist
                        pair[2] = "true";
                        headers.add(pair);
                        pair = new String[3];
                        temp = "";
                        pairNum = 0;
                    } else if (temp.length() == 0) {
                        System.out.println("You cannot have an empty string as value");
                        return null;
                    } else {
                        System.out.println("in between info and corresponding value there should be a = not a ; ");
                        System.out.println("You cannot have an empty string as value");
                        return null;
                    }
                }
            }
        }else {

            if (input.length() == 0) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " did not enter headers but specified option -H/--headers");
                return null;
            }
            String[] pair = new String[3];
            String temp = "";
            int pairNum = 0;
            for (int i = 0; i < input.length(); i++) {
                //all the pairs are enabled so the pair[2] is "true"
                if (input.charAt(i) != '=' && input.charAt(i) != ';') {
                    temp += input.charAt(i);
                    if (i == input.length() - 2) {
                        if (pairNum == 1 && temp.length() != 0) {
                            pair[1] = temp;
                            //gotta ad it to the arraylist
                            pair[2] = "true";
                            headers.add(pair);
                            pair = new String[3];
                            temp = "";
                            pairNum = 0;
                        } else if (temp.length() == 0) {
                            System.out.println("You cannot have an empty string as value");
                            return null;
                        } else {
                            System.out.println("in between info and corresponding value there should be a = not a ;");
                            System.out.println("You cannot have an empty string as value");
                            return null;
                        }
                    }

                } else if (input.charAt(i) == '=') {
                    //should enter this state at pairNum==0
                    if (pairNum == 0 && temp.length() != 0) {
                        pair[0] = temp;
                        temp = "";
                        pairNum++;
                    } else if (temp.length() == 0) {
                        System.out.println("You cannot have an empty string as header name");
                        return null;
                    } else {
                        System.out.println("invalid pair of header name and value, each pair separated by ; not = ");
                        System.out.println("You cannot have an empty string as header name");
                        return null;
                    }
                } else if (input.charAt(i) == ';' || i == input.length() - 2) {
                    //should enter this in state pairNum==1
                    //here we have to check if the temp is made only with spaces
                    //we are gonna take out the spaces

                    if (pairNum == 1 && temp.length() != 0) {
                        pair[1] = temp;
                        //gotta ad it to the arraylist
                        pair[2] = "true";
                        headers.add(pair);
                        pair = new String[3];
                        temp = "";
                        pairNum = 0;
                    } else if (temp.length() == 0) {
                        System.out.println("You cannot have an empty string as value ");
                        return null;
                    } else {
                        System.out.println("in between info and corresponding value there should be a = not a ;");
                        System.out.println("You cannot have an empty string as value");
                        return null;
                    }
                }
            }
        }
        return headers;
    }

    //still not sure if it actually works with &
    private ArrayList<String[]> splitData(String input){
        ArrayList<String[]> dataInfo = new ArrayList<>();
        if(!commandLine) {
            if (input.length() == 0) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " did not enter the message body but specified option -d/--data");
                return null;
            }
            if (input.charAt(0) != '\"' || input.charAt(input.length() - 1) != '\"') {
                System.out.println("invalid message body, should be in between \" \" separated by & with no spaces in between");
                return null;
            }
            //the form data (or the urlencoded
            String[] pair = new String[3];
            String temp = "";
            int pairNum = 0;
            for (int i = 1; i < input.length() - 1; i++) {
                //all the pairs are enabled
                if (input.charAt(i) != '=' && input.charAt(i) != '&') {
                    temp += input.charAt(i);
                    if (i == input.length() - 2) {
                        if (pairNum == 1 && temp.length() != 0) {
                            pair[1] = temp;
                            //gotta ad it to the arraylist
                            pair[2] = "true";
                            dataInfo.add(pair);
                            pair = new String[3];
                            temp = "";
                            pairNum = 0;
                        } else if (temp.length() == 0) {
                            System.out.println("You cannot have an empty string as value");
                            return null;
                        } else {
                            System.out.println("in between info and corresponding value there should be a = not a & ");
                            System.out.println("You cannot have an empty string as value");
                            return null;
                        }
                    }
                } else if (input.charAt(i) == '=') {
                    //should enter this state at pairNum==0
                    if (pairNum == 0 && temp.length() != 0) {
                        pair[0] = temp;
                        temp = "";
                        pairNum++;
                    } else if (temp.length() == 0) {
                        System.out.println("You cannot have an empty string as message body info");
                        return null;
                    } else {
                        System.out.println("invalid message body, each pair separated by & not =");
                        System.out.println("You cannot have an empty string as message body info");
                        return null;
                    }
                } else if (input.charAt(i) == '&') {
                    //should enter this in state pairNum==1
                    if (pairNum == 1 && temp.length() != 0) {
                        pair[1] = temp;
                        //gotta ad it to the arraylist
                        pair[2] = "true";
                        dataInfo.add(pair);
                        pair = new String[3];
                        temp = "";
                        pairNum = 0;
                    } else if (temp.length() == 0) {
                        System.out.println("You cannot have an empty string as value");
                        return null;
                    } else {
                        System.out.println("in between info and corresponding value there should be a = not a & ");
                        System.out.println("You cannot have an empty string as value");
                        return null;
                    }
                }
            }
        }else{
            if (input.length() == 0) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " did not enter the message body but specified option -d/--data");
                return null;
            }
            //the form data (or the urlencoded
            String[] pair = new String[3];
            String temp = "";
            int pairNum = 0;
            for (int i = 1; i < input.length() - 1; i++) {
                //all the pairs are enabled
                if (input.charAt(i) != '=' && input.charAt(i) != '&') {
                    temp += input.charAt(i);
                    if (i == input.length() - 2) {
                        if (pairNum == 1 && temp.length() != 0) {
                            pair[1] = temp;
                            //gotta ad it to the arraylist
                            pair[2] = "true";
                            dataInfo.add(pair);
                            pair = new String[3];
                            temp = "";
                            pairNum = 0;
                        } else if (temp.length() == 0) {
                            System.out.println(" You cannot have an empty string as value");
                            return null;
                        } else {
                            System.out.println("in between info and corresponding value there should be a = not a & ");
                            System.out.println("You cannot have an empty string as value");
                            return null;
                        }
                    }
                } else if (input.charAt(i) == '=') {
                    //should enter this state at pairNum==0
                    if (pairNum == 0 && temp.length() != 0) {
                        pair[0] = temp;
                        temp = "";
                        pairNum++;
                    } else if (temp.length() == 0) {
                        System.out.println("You cannot have an empty string as message body info ");
                        return null;
                    } else {
                        System.out.println("invalid message body, each pair separated by & not =");
                        System.out.println("You cannot have an empty string as message body info");
                        return null;
                    }
                } else if (input.charAt(i) == '&') {
                    //should enter this in state pairNum==1
                    if (pairNum == 1 && temp.length() != 0) {
                        pair[1] = temp;
                        //gotta ad it to the arraylist
                        pair[2] = "true";
                        dataInfo.add(pair);
                        pair = new String[3];
                        temp = "";
                        pairNum = 0;
                    } else if (temp.length() == 0) {
                        System.out.println("You cannot have an empty string as value");
                        return null;
                    } else {
                        System.out.println("in between info and corresponding value there should be a = not a &");
                        System.out.println("You cannot have an empty string as value");
                        return null;
                    }
                }
            }
        }
        return dataInfo;
    }

}