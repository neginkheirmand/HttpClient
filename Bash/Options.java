package Bash;

import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class Options {
    private static HashMap<String, Integer> requestOptions = null;
    private ArrayList<Integer> requestNumberCommand;
    private ArrayList<String> requestStringCommand;


    public Options(){
        if(requestOptions == null) {
            //should only be done the first time
            requestOptions = new HashMap<>();

            requestOptions.put("-M", 0);
            requestOptions.put("--method", 0);

            requestOptions.put("-H", 1);
            requestOptions.put("--headers", 1);

            //Include protocol response headers in the output
            requestOptions.put("-i", 2);
            requestOptions.put("--include", 2);
            //
            requestOptions.put("-h", 3);
            requestOptions.put("--help", 3);

            requestOptions.put("-f", 4);

            requestOptions.put("-O", 5);
            requestOptions.put("--output", 5);

            requestOptions.put("-S", 6);
            requestOptions.put("--save", 6);

            requestOptions.put("-d", 7);
            requestOptions.put("-data", 7);

            requestOptions.put("-j", 8);
            requestOptions.put("--json", 8);

            requestOptions.put("--upload", 9);

            requestOptions.put("list", 10);

            requestOptions.put("curl", 11);

            requestOptions.put("fire", 12);

        }

        requestNumberCommand = new ArrayList<>();
        requestStringCommand = new ArrayList<>();
    }


    private int getOptionNum(String inputOption){
        for(String str : requestOptions.keySet()){
            if(str.equals(inputOption)){
//                System.out.println(requestOptions.get(str));
                return requestOptions.get(str);
            }
        }
        if(inputOption.charAt(0)=='-'){
            System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" option "+inputOption+" not supported. Enter -h/--help to see the options supported.");
            if(inputOption.length()==2){
                if(inputOption.charAt(1)=='m'){
                    System.out.println("The most similar option is\n-M");
                }else if(inputOption.charAt(1)=='I'){
                    System.out.println("The most similar option is\n-i");
                }else if(inputOption.charAt(1)=='D'){
                    System.out.println("The most similar option is\n-d");
                }else if(inputOption.charAt(1)=='s'){
                    System.out.println("The most similar option is\n-S");
                }else if(inputOption.charAt(1)=='o'){
                    System.out.println("The most similar option is\n-O");
                }else if(inputOption.charAt(1)=='J'){
                    System.out.println("The most similar option is\n-j");
                }else if(inputOption.charAt(1)=='F'){
                    System.out.println("The most similar option is\n-f");
                }
            }
        }
//        System.out.println("-1");
        return -1;
    }

    private ArrayList<String> splitToOptions(String input){
        ArrayList<String> inputOptions = new ArrayList<>();
        String str = "";
        for(int i=0; i<input.length(); i++){
            if (input.charAt(i)==' '){
                //the last word ended
                if(str.length()>0){
                    //add as a part of the input
                    inputOptions.add(str);
                    str="";
                }
                continue;
            }
            //a non ' ' char
            str += input.charAt(i);
        }
        if(str.length()>0) {
            inputOptions.add(str);
        }
        return inputOptions;
    }

    //first check here if everything is OK
    private void showWarnings(){
        if(requestStringCommand.size()==0 &&requestNumberCommand.size()==0 ){
            System.out.println("invalid input, try again");
        }
        if(requestNumberCommand.get(0)!=11){
            System.out.println("command should start with \"curl\"");
        }
    }

    public void identifyRequestOptions(String inputCommand){
        requestStringCommand = splitToOptions(inputCommand);
        for(int i=0; i<requestStringCommand.size(); i++){
            requestNumberCommand.add(i, getOptionNum(requestStringCommand.get(i)) );
        }
        showWarnings();
    }

    public ArrayList<Integer> getRequestNumberCommand() {
        return requestNumberCommand;
    }

    public ArrayList<String> getRequestStringCommand() {
        return requestStringCommand;
    }
}
