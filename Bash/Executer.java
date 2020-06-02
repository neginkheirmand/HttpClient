package Bash;

import GUI.Request;

import java.io.File;
import java.util.ArrayList;

public class Executer {


    /**
     * this method is for command like "curl -o "nameOfOutPutFile" -M GET -d "some valid message body here"
     * @param request
     * @param nameOutPut
     * @param messageBody
     */
    public void executerGet(Request request, String nameOutPut, ArrayList<String[]> messageBody){

    }

    /**
     * this method is for get requests with the command "curl -M GET -j "some valid json message body" -o "OutPutFileName" "
     * @param request
     * @param nameOutPut
     * @param jsonBody
     */
    public void executerGet(Request request, String nameOutPut, String jsonBody){

    }


    /**
     * this me
     * @param request
     * @param nameOutPut
     * @param uploadFile
     */
    public void executerGet(Request request, String nameOutPut, File uploadFile){

    }


    public void executerPost(){

    }


    public void executerPut(){

    }


    public void executerPatch(){

    }

    public void executerDelete(){

    }

}
