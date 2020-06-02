package Conection;

import GUI.FORM_DATA;
import GUI.Request;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import GUI.TYPE;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;


public class PostMethod {
    private Request postRequest;
    //afterwards we can put an Array list of responses so that we have the history of the request
    //but for that definitly have in mind the edit action on the request


    public PostMethod(Request getRequest) {
        this.postRequest = getRequest;
    }

    public void executePost(String outPutFile, boolean followRedirect, boolean showresponseHeaders){

        //Create an HttpClient object
        CloseableHttpClient httpclient = HttpClients.createDefault();

        if(postRequest==null) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "problem with setting the request");
            return;
        }

        try{
            //Create an HttpGet object
            HttpPost httpPost = new HttpPost();
            if( postRequest.getUrl() != null && !postRequest.getUrl().equals("") ) {
                httpPost = new HttpPost(postRequest.getUrl());
            }else{
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid url");
                System.out.println("this is the url"+ postRequest.getUrl());
                return;
            }
            //Execute the Post request
            CloseableHttpResponse httpresponse = httpclient.execute(httpPost);


            //in case its form url encoded
            if(postRequest.getTypeOfData().equals(FORM_DATA.FORM_URL)) {
                List<NameValuePair> params = null;
                if (postRequest.getFormDataInfo() != null) {
                    params = new ArrayList<NameValuePair>();
                    for (int i = 0; i < ((ArrayList<String[]>) postRequest.getFormDataInfo()).size(); i++) {
                        if (((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[2].equals("true") &&
                                ((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[0]!=null &&
                                ((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[1] !=null ) {
                            params.add(new BasicNameValuePair(((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[0],
                                    ((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[1]));
                        }
                    }
                }
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
            }else if(postRequest.getTypeOfData().equals(FORM_DATA.JSON)) {
                //in case is json we can use of .setHeader(HttpHeaders.CONTENT_TYPE, "application/json") in the HttpPost
                if( postRequest.getFormDataInfo()!=null && !((String)postRequest.getFormDataInfo()).equals("") ){

                }

            }




            try{
                Scanner sc = new Scanner(httpresponse.getEntity().getContent());
                while(sc.hasNext()) {
                    System.out.println(sc.nextLine());
                }
                System.out.println("this is the status code:  "+httpresponse.getStatusLine().getStatusCode());
            }finally{
                httpresponse.close();
            }
        }catch (Exception exception) {
            System.out.println(exception);
        }finally{
            try {
                httpclient.close();
            }catch (Exception exception){
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "problem closing the httpClient");
            }
        }
    }

    public static void main(String[] args) {

        String url = ""+(new Scanner(System.in)).nextLine();
        Request postRequest = new Request("nameOfRequest", TYPE.POST, url , FORM_DATA.FORM_URL);
        postRequest.setUrl(url);
        PostMethod postCommand = new PostMethod(postRequest);
        postCommand.executePost("", true, true);
    }

}
