package Bash;

import Conection.*;
import GUI.Request;
import GUI.TYPE;


/**
 * This class executes a command
 *
 * @author  Negin Kheirmand (98310023 - neginkheirmand@gmail.com)
 * @version 1.0
 */
public class Executer {


    /**
     * constructor of the class
     * @param request
     * @param command
     */
    public Executer(Request request, Command command){
        System.out.println("url : "+request.getUrl());

        if(request==null||command==null){
            System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" Problem with the request, try again");
            return;
        }else if(request.getTypeOfRequest().equals(TYPE.GET)){
            executerGet(request, command);
        }else if(request.getTypeOfRequest().equals(TYPE.POST)){
            executerPost(request, command);
        }else if(request.getTypeOfRequest().equals(TYPE.PUT)){
            executerPut(request, command);
        }else if(request.getTypeOfRequest().equals(TYPE.PATCH)){
            executerPatch(request, command);
        }else if(request.getTypeOfRequest().equals(TYPE.OPTION)){
            executerOption(request, command);
        }else if(request.getTypeOfRequest().equals(TYPE.DELETE)){
            executerDelete(request, command);
        }else if(request.getTypeOfRequest().equals(TYPE.HEAD)){
            executerHead(request, command);
        }else{
            System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" Problem with the request, try again");
            return;
        }

    }


    /**
     * constructor of the class
     * @param request
     */
    public Executer(Request request){
        System.out.println("url : "+request.getUrl());

        if(request==null){
            System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" Problem with the request, try again");
            return;
        }else if(request.getTypeOfRequest().equals(TYPE.GET)){
            executerGet(request);
        }else if(request.getTypeOfRequest().equals(TYPE.POST)){
            executerPost(request);
        }else if(request.getTypeOfRequest().equals(TYPE.PUT)){
            executerPut(request);
        }else if(request.getTypeOfRequest().equals(TYPE.PATCH)){
            executerPatch(request);
        }else if(request.getTypeOfRequest().equals(TYPE.OPTION)){
            executerOption(request);
        }else if(request.getTypeOfRequest().equals(TYPE.DELETE)){
            executerDelete(request);
        }else if(request.getTypeOfRequest().equals(TYPE.HEAD)){
            executerHead(request);
        }else{
            System.out.println("\033[0;31m"+"Error:"+"\033[0m"+" Problem with the request, try again");
            return;
        }

    }



    /**
     * this method executes the get requests
     * @param request the request to be done
     * @param command the command containing more info about the request
     */
    private void executerGet(Request request, Command command){
        GetMethod newGetMethod = new GetMethod(request);
        newGetMethod.executeGet(request.getNameOutPutContainer(), request.getFollowRedirect(), command.getShowHeaders());
    }


    private void executerPost(Request request, Command command){
        PostMethod newPostMethod = new PostMethod(request);
        newPostMethod.executePost(request.getNameOutPutContainer(), request.getFollowRedirect(), command.getShowHeaders());
    }


    private void executerPut(Request request, Command command){
        PutMethod newPostMethod = new PutMethod(request);
        newPostMethod.executePut(request.getNameOutPutContainer(), request.getFollowRedirect(), command.getShowHeaders());
    }


    private void executerPatch(Request request, Command command){
        PatchMethod newPostMethod = new PatchMethod(request);
        newPostMethod.executePatch(request.getNameOutPutContainer(), request.getFollowRedirect(), command.getShowHeaders());
    }

    private void executerOption(Request request, Command command){
        OptionMethod newPostMethod = new OptionMethod(request);
        newPostMethod.executeOption(request.getNameOutPutContainer(), request.getFollowRedirect(), command.getShowHeaders());
    }

    private void executerDelete(Request request, Command command){
        DeleteMethod newPostMethod = new DeleteMethod(request);
        newPostMethod.executeDelete(request.getNameOutPutContainer(), request.getFollowRedirect(), command.getShowHeaders());
    }

    private void executerHead(Request request, Command command){
        HeadMethod newPostMethod = new HeadMethod(request);
        newPostMethod.executeHead(request.getNameOutPutContainer(), request.getFollowRedirect(), command.getShowHeaders());
    }

    /**
     * this method executes the get requests but for the GUI
     * @param request the request to be done
     */
    private void executerGet(Request request){
        GetMethod newGetMethod = new GetMethod(request);
        newGetMethod.executeGet(request.getNameOutPutContainer(), request.getFollowRedirect(), true);
    }


    private void executerPost(Request request){
        PostMethod newPostMethod = new PostMethod(request);
        newPostMethod.executePost(request.getNameOutPutContainer(), request.getFollowRedirect(), true);
    }


    private void executerPut(Request request){
        PutMethod newPostMethod = new PutMethod(request);
        newPostMethod.executePut(request.getNameOutPutContainer(), request.getFollowRedirect(), true);
    }


    private void executerPatch(Request request){
        PatchMethod newPostMethod = new PatchMethod(request);
        newPostMethod.executePatch(request.getNameOutPutContainer(), request.getFollowRedirect(), true);
    }

    private void executerOption(Request request){
        OptionMethod newPostMethod = new OptionMethod(request);
        newPostMethod.executeOption(request.getNameOutPutContainer(), request.getFollowRedirect(), true);
    }

    private void executerDelete(Request request){
        DeleteMethod newPostMethod = new DeleteMethod(request);
        newPostMethod.executeDelete(request.getNameOutPutContainer(), request.getFollowRedirect(), true);
    }

    private void executerHead(Request request){
        HeadMethod newPostMethod = new HeadMethod(request);
        newPostMethod.executeHead(request.getNameOutPutContainer(), request.getFollowRedirect(), true);
    }

}
