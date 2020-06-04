package Bash;

import Conection.*;
import GUI.Request;
import GUI.TYPE;

public class Executer {

    public Executer(Request request, Command command){
        System.out.println("url"+request.getUrl());

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

}
