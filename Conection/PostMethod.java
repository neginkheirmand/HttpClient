package Conection;

import GUI.FORM_DATA;
import GUI.Request;
import GUI.TYPE;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import java.io.*;
import org.apache.http.Header;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class PostMethod {
    private Request postRequest;
    //afterwards we can put an Array list of responses so that we have the history of the request
    //but for that definitely have in mind the edit action on the request


    public PostMethod(Request postRequest) {
        this.postRequest = postRequest;
    }

    public void executePost(String outPutFile, boolean followRedirect, boolean showresponseHeaders){

        //Create an HttpClient object
        CloseableHttpClient httpclient = HttpClients.createDefault();

        if(postRequest==null) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "problems in setting the request");
            return;
        }

        try{
            //Create an HttpGet object
            HttpPost httpPost;
            if( postRequest.getUrl() != null && !postRequest.getUrl().equals("") ) {
                httpPost = new HttpPost(postRequest.getUrl());
            }else{
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid url");
                return;
            }

            //the headers:
            if (postRequest.getHeaderInfo() != null) {
                for (int i = 0; i < postRequest.getHeaderInfo().size(); i++) {
                    if (postRequest.getHeaderInfo().get(i)[0] != null && postRequest.getHeaderInfo().get(i)[1] != null && postRequest.getHeaderInfo().get(i)[2].equals("true")) {
                        httpPost.addHeader(postRequest.getHeaderInfo().get(i)[0], postRequest.getHeaderInfo().get(i)[1]);
                    }
                }
            }




            //in case its form url encoded
            if(postRequest.getTypeOfData().equals(FORM_DATA.FORM_URL)) {
                List<NameValuePair> params = null;
                if (postRequest.getFormDataInfo() != null) {
                    params = new ArrayList<NameValuePair>();
                    System.out.println(postRequest.getFormDataInfo().getClass());
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
                    StringEntity entity = new StringEntity((String)postRequest.getFormDataInfo());
//                    StringEntity entity = new StringEntity((String)postRequest.getFormDataInfo(), ContentType.APPLICATION_FORM_URLENCODED);
                    httpPost.setEntity(entity);
                    httpPost.setHeader("Accept", "application/json");
                    httpPost.setHeader("Content-type", "application/json");
                }
            }else if(postRequest.getTypeOfData().equals(FORM_DATA.BINARY)){
                //this part is done with the multi part form data too
                if(postRequest.getFormDataInfo()!=null) {
                    File uploadFile = new File((String) postRequest.getFormDataInfo());
                    if(uploadFile.exists() && uploadFile.isFile()) {
                        FileEntity fileToUpload = new FileEntity(uploadFile, ContentType.DEFAULT_BINARY);
//                        FileEntity fileToUpload = new FileEntity(uploadFile, ContentType.APPLICATION_OCTET_STREAM);
//                        FileEntity fileToUpload = new FileEntity(uploadFile, ContentType.MULTIPART_FORM_DATA);
                        httpPost.setEntity(fileToUpload);
                    }else{
                        System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "File not found");
                        return;
                    }
                }else{
                    System.out.println("\033[0;31m" + "Line 99 of class PostMethod" + "\033[0m" );
                    return;
                }
            }
            //we have to add the option of form data/multipart form


            //Execute the Post request
            CloseableHttpResponse httpResponse= httpclient.execute(httpPost);
            System.out.println("POST Response Status:: "
                    + httpResponse.getStatusLine().getStatusCode());


            try{

                // print result
                //-O --output option handled
                Header[] headers = httpResponse.getAllHeaders();
                if(showresponseHeaders) {
                    for (Header header : headers) {
                        System.out.println("Key : " + header.getName() + " ,Value : " + header.getValue());
                    }
                }

                if (outPutFile == null || outPutFile.length() == 0) {

                    BufferedReader reader ;
                    try {
                        reader = new BufferedReader(new InputStreamReader(
                                httpResponse.getEntity().getContent()));
                    }catch (NullPointerException nullPointerException){
                        System.out.println("empty Response    ");
                        return;
                    }

                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = reader.readLine()) != null) {
                        response.append(inputLine+"\n");
                    }
                    reader.close();
                    System.out.println(response.toString());
                }else {
                    //if the -O option is used
                    String posFix = GetMethod.getContentType(headers, outPutFile);
                    File outputContainer;
                    if(GetMethod.getPosFix(outPutFile).length()==0) {
                        outputContainer = new File(new File(".").getAbsolutePath() + "\\src\\InformationHandling\\SaveInfoBash\\" + outPutFile + posFix);
                    }else{
                        outputContainer = new File(new File(".").getAbsolutePath() + "\\src\\InformationHandling\\SaveInfoBash\\" + outPutFile);
                    }

                    //the next method sees if the parenrs of the file exist if not creates it and returnts true if already existed and false if created it
                    outputContainer.getParentFile().mkdirs();
                    if (!outputContainer.createNewFile()) {
                        System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "unable to create output file");
                        System.out.println("A file with this name already exist, do you want to over-write on it? <Y/n>");
                        String overWrite = "" +(new Scanner(System.in)).nextLine();

                        while( !overWrite.equals("Y") && !overWrite.equals("n") ){
                            overWrite = "" +(new Scanner(System.in)).nextLine();
                        }

                        if(overWrite.equals("Y")){

                            BufferedReader reader ;
                            try {
                                reader = new BufferedReader(new InputStreamReader(
                                        httpResponse.getEntity().getContent()));
                            }catch (NullPointerException nullPointerException){
                                System.out.println("empty Response  ");
                                return;
                            }

                            String inputLine;
                            StringBuffer response = new StringBuffer();

                            while ((inputLine = reader.readLine()) != null) {
                                response.append(inputLine+"\n");
                            }
                            reader.close();

                            FileWriter fileWriter = new FileWriter(outputContainer);
                            fileWriter.write(response.toString());
                            fileWriter.close();
                        }else {
                            System.out.println("retry again with a new name");
                        }
                    }else {

                        BufferedReader reader ;
                        try {
                            reader = new BufferedReader(new InputStreamReader(
                                    httpResponse.getEntity().getContent()));
                        }catch (NullPointerException nullPointerException){
                            System.out.println("Empty Response     ");
                            return;
                        }

                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = reader.readLine()) != null) {
                            response.append(inputLine+"\n");
                        }
                        reader.close();

                        FileWriter fileWriter = new FileWriter(outputContainer);
                        fileWriter.write(response.toString());
                        fileWriter.close();
                    }
                }
            }finally{
                httpResponse.close();
            }
        } catch (java.lang.IllegalArgumentException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL, check the spacing");
        } catch (org.apache.http.client.ClientProtocolException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL");
        } catch (java.net.UnknownHostException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem in finding available Port, Please check Your internet connection");
        } catch (IOException exception) {
            //the methods: execute/ getContent / readLine / close
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem with writing in file ");
        }catch (NullPointerException exception){
            System.out.println("\033[0;31m" + "Error" + "\033[0m" );
        }finally{
            try {
                httpclient.close();
            } catch (java.lang.IllegalArgumentException exception) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL, check the spacing");
            } catch (org.apache.http.client.ClientProtocolException exception) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL");
            } catch (java.net.UnknownHostException exception) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem in finding available Port, Please check Your internet connection");
            } catch (IOException exception) {
                //the methods: execute/ getContent / readLine / close
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem with writing in file ");
            }catch (NullPointerException exception){
                System.out.println("\033[0;31m" + "Error" + "\033[0m" );
            }
        }
    }

    public static void main(String[] args) {

        String url = ""+(new Scanner(System.in)).nextLine();
        Request postRequest = new Request("nameOfRequest", TYPE.POST, url , FORM_DATA.FORM_URL);
        ArrayList<String[]> headers = new ArrayList<>();
        String[] header1 = {"Header1", "Value1", "false"};
        String[] header2 = {"Header2", "Value2", "true"};
        String[] header3 = {"Header3", "Value3", "true"};
        headers.add(header1);
        headers.add(header2);
        headers.add(header3);
        postRequest.setHeaderInfo(headers);
        PostMethod postCommand = new PostMethod(postRequest);
//        postCommand.executePost("", true, true);
        postCommand.executePost("", true, true);
    }

}
