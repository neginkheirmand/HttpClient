package Bash;

import GUI.FORM_DATA;
import GUI.Request;
import GUI.TYPE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Command {
    private ArrayList<Integer> numCommand;
    private ArrayList<String> strCommand;
    private static ArrayList<Command> savedRequests = new ArrayList<>();
    private Request request;
    private boolean showHeaders =false;

    //the last command was a list command
    private static boolean list = false;


    public Command(String input) {
        //must set Request field later in the do command method
        request = new Request();
        //INO BADAN BARDAR
        savedRequests.add(this);

//        this.numCommand = numCommand;
//        this.strCommand = strCommand;
        Options option =new Options();
        option.identifyRequestOptions(input);
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
                    System.out.println("enter a valid command number, the list of requests has only " + savedRequests.size() + " requests");
                    list = true;
                    return;
                }
                doMethod(savedRequests.get(numCommand - 1));
            } catch (NumberFormatException exception) {
                System.out.println("after the fire word the Command-Number should be written");
                list = true;
                return;
            }
        }
    }

    private void doMethod(Command commandToDo) {

        boolean save = false;

        //the first word in the command was curl
        //the second one should be the url
        //get the url
        String urlAddress = "";
        for(int i =1; i<commandToDo.getNumCommand().size(); i++){
            if(commandToDo.getNumCommand().get(i)==-1){
                if(urlAddress.length()==0){
                    //the url was not defined this is the url
                    urlAddress = commandToDo.getStrCommand().get(i);
                }else{
                    System.out.println("too many arguments for the specified options, try again.");
                    return;
                }
            }else if(commandToDo.getNumCommand().get(i)==0) {
                //specify the method -M and --method
                //this option takes the next word too so we have to make sure there actually is a next word
                if (i == commandToDo.getNumCommand().size()-1) {
                    System.out.println("after -M/--method comes the type of method");
                    System.out.println("methods supported:");
                    System.out.println("POST\nPATCH\nPUT\nDELETE\nGET\nHEAD\nOPTION");
                    System.out.println("try again!");
                    return;
                } else {
                    i++;
                    TYPE methodOfRequest = TYPE.getTYPE(commandToDo.getStrCommand().get(i));
                    if(methodOfRequest == null){
                        //invalid method name
                        System.out.println("invalid method name");
                        System.out.println("methods supported:");
                        System.out.println("POST\nPATCH\nPUT\nDELETE\nGET\nHEAD\nOPTION");
                        System.out.println("try again!");
                        return;
                    }
                    //here we are sure the user entered a valid method so we set the method
                    request.setType(methodOfRequest);
                    //the method was set
                }
            }else if(commandToDo.getNumCommand().get(i)==1){
                //its the headers : -H / --headers
                //this input option comes and after it the name o header and value come as an string
                //so we make sure actually is a next word in the String of the input
                if(i==commandToDo.getNumCommand().size()-1){
                    System.out.println("no headers specified\nPlease try again!");
                    return;
                }
                //in between " " (double quotation) and separated with ; (semicolon)
                i++;
                ArrayList<String[]> headerOfrequest = splitHeader(commandToDo.getStrCommand().get(i));
                //now to make sure the input is as expected:
                if(headerOfrequest==null){
                    return;
                }
                request.setHeaderInfo(headerOfrequest);

                //the header was specified
            }else if(commandToDo.getNumCommand().get(i)==2){
                //the -i/--include option
                //when this option is in the request command the response of this request must show
                showHeaders=true;
            }else if(commandToDo.getNumCommand().get(i)==3){
                //the -h/-help command
                System.out.println("\033[0;32m"+"Usage: curl [options...] <url>\n"+"\033[0m");

                // -d
                System.out.println("\033[0;32m"+"-d"+"\033[0m"+", --data <data>   HTTP POST data\n" +
                        "\033[0;32m"+"no support for this"+"\033[0m"+
                        "     --data-binary <data> HTTP POST binary data\n" +
                        "     --data-urlencode <data> HTTP POST data url encoded\n");
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
                System.out.println("follow redirect still not working");
            }else if(commandToDo.getNumCommand().get(i)==5){
                //put the response of the request in a file
                //in this case we have to check if the next word in the command is the name of the file or the next option to imply
                //first we see if there is any other word in the command after this one
                String nameOfOutputFile = "";
                if(i==commandToDo.getNumCommand().size()-1 || commandToDo.getNumCommand().get(i+1)!=-1){
                    //its the last one so the name of the file will be chosen automatically

                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = new Date();
                    nameOfOutputFile="output_["+formatter.format(date)+"]";

                }else{
                    //the chosen name by the user
                    nameOfOutputFile = commandToDo.getStrCommand().get(++i);
                }
                //put in output file with the specified name
                System.out.println("-o option still not working");
            }else if(commandToDo.getNumCommand().get(i)==6){
                //-s the save
                save = true;
                //remember to do the save in the end
            }
        }
        if(save){
            //gotta save the file
            savedRequests.add(this);
        }
        System.out.println("this command should be done:");
        System.out.println(commandToDo.getNumCommand());
        System.out.println(commandToDo.getStrCommand());
        System.out.println(commandToDo.getRequest());
    }

    private ArrayList<String[]> splitHeader(String input){
        if(input.charAt(0)=='\"' && input.charAt(input.length()-1)=='\"'){
            System.out.println("invalid header input, should be in between \" \" and each pair of" +
                    "\nheader and value separated with ; (semicolon) with no spaces in between");
            System.out.println("ex:\n\"header1=value1;header2=value2\"");
            return null;
        }
        ArrayList<String[]> headers = new ArrayList<>();
        String[] pair = new String[3];
        String temp = "";
        int pairNum=0;
        for(int i=0; i<input.length()-1; i++){
            //all the pairs are enabled
            if(input.charAt(i)!='='&&input.charAt(i)!=';'){
                temp+=input.charAt(i);
            }else{
                pair[pairNum] = temp;
                temp="";
                pairNum++;
                if(pairNum==2){
                    //gotta ad it to the arraylist
                    pair[2]="true";
                    headers.add(pair);
                    pair = new String[3];
                    pairNum=0;
                }
            }
        }
        headers.add(pair);
        return headers;
    }



}