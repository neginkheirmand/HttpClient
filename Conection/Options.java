package Conection;

import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Options {
    private static HashMap<String, Integer> requestOptions;

    public Options(){
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

    }

    public boolean isOption(String inputOption){
        for(String str : requestOptions.keySet()){
            if(str.equals(inputOption)){
                System.out.println("was able to recognize as an option \""+str+"\"");
                return true;
            }
        }
        System.out.println("didnt recognize as an option \""+inputOption+"\"");
        return false;
    }

    public static ArrayList<String> splitToOptions(String input){
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
            if(str.length()>0) {
                str += input.charAt(i);
            }
        }
        inputOptions.add(str);
        return inputOptions;
    }
}
