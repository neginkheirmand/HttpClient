package Conection;

import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class Options {
    private static HashMap<String, Integer> requestOptions;
    private ArrayList<Integer> requestNumberCommand;
    private ArrayList<String> requestStringCommand;


    public Options(){
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
        //Fail silently (no output at all) on HTTP errors
        requestOptions.put("--fail", 4);
        //Fail on first transfer error, do not continue
        requestOptions.put("--fail-early", 4);
        //Enable TLS False Start
        requestOptions.put("--false-start", 4);

        requestOptions.put("-O", 5);
        requestOptions.put("--output",5);

        requestOptions.put("-S", 6);
        requestOptions.put("--save", 6);

        requestOptions.put("-d", 7);
        requestOptions.put("-data", 7);

        requestOptions.put("-j", 8);
        requestOptions.put("--json", 8);

        requestOptions.put("--upload", 9);

        requestOptions.put("list", 10);

        requestNumberCommand = new ArrayList<>();
        requestStringCommand = new ArrayList<>();
    }


    private int getOptionNum(String inputOption){
        for(String str : requestOptions.keySet()){
            if(str.equals(inputOption)){
                System.out.println(requestOptions.get(str));
                return requestOptions.get(str);
            }
        }
        System.out.println("-1");
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

    private void showWarnings(){
        if(requestStringCommand.size()==0 &&requestNumberCommand.size()==0 ){
            System.out.println("invalid input, try again");
        }
        for(int i=0 ; i<requestNumberCommand.size(); i++){
            if(requestNumberCommand.get(i)==-1){
                System.out.println("cannot recognize command \""+requestStringCommand.get(i));
            }
        }
    }

    public void identifyRequestOptions(String inputCommand){
        requestStringCommand = splitToOptions(inputCommand);
        for(int i=0; i<requestStringCommand.size(); i++){
            requestNumberCommand.add(i, getOptionNum(requestStringCommand.get(i)) );
        }
        showWarnings();
    }
}
