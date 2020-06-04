package Conection;

import GUI.FORM_DATA;
import GUI.Request;
import GUI.TYPE;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PatchMethod {
    private Request patchRequest;
    //afterwards we can put an Array list of responses so that we have the history of the request
    //but for that definitely have in mind the edit action on the request


    public PatchMethod(Request patchRequest) {
        this.patchRequest = patchRequest;
    }

    public void executePatch(String outPutFile, boolean followRedirect, boolean showresponseHeaders){

        //Create an HttpClient object
        CloseableHttpClient httpclient = HttpClients.createDefault();

        if(patchRequest==null) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "problems in setting the request");
            return;
        }

        try{
            //Create an HttpGet object
            HttpPatch httpPatch;
            if( patchRequest.getUrl() != null && !patchRequest.getUrl().equals("") ) {
                httpPatch = new HttpPatch(patchRequest.getUrl());
            }else{
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid url");
                return;
            }

            //the headers:
            if (patchRequest.getHeaderInfo() != null) {
                for (int i = 0; i < patchRequest.getHeaderInfo().size(); i++) {
                    if (patchRequest.getHeaderInfo().get(i)[0] != null && patchRequest.getHeaderInfo().get(i)[1] != null && patchRequest.getHeaderInfo().get(i)[2].equals("true")) {
                        httpPatch.addHeader(patchRequest.getHeaderInfo().get(i)[0], patchRequest.getHeaderInfo().get(i)[1]);
                    }
                }
            }




            //in case its form url encoded
            if(patchRequest.getTypeOfData().equals(FORM_DATA.FORM_URL)) {
                List<NameValuePair> params = null;
                if (patchRequest.getFormDataInfo() != null) {
                    params = new ArrayList<NameValuePair>();
                    System.out.println(patchRequest.getFormDataInfo().getClass());
                    for (int i = 0; i < ((ArrayList<String[]>) patchRequest.getFormDataInfo()).size(); i++) {
                        if (((ArrayList<String[]>) patchRequest.getFormDataInfo()).get(i)[2].equals("true") &&
                                ((ArrayList<String[]>) patchRequest.getFormDataInfo()).get(i)[0]!=null &&
                                ((ArrayList<String[]>) patchRequest.getFormDataInfo()).get(i)[1] !=null ) {
                            params.add(new BasicNameValuePair(((ArrayList<String[]>) patchRequest.getFormDataInfo()).get(i)[0],
                                    ((ArrayList<String[]>) patchRequest.getFormDataInfo()).get(i)[1]));
                        }
                    }
                }
                if (params != null) {
                    httpPatch.setEntity(new UrlEncodedFormEntity(params));
                }
            }else if(patchRequest.getTypeOfData().equals(FORM_DATA.JSON)) {
                //in case is json we can use of .setHeader(HttpHeaders.CONTENT_TYPE, "application/json") in the HttpPatch
                if( patchRequest.getFormDataInfo()!=null && !((String)patchRequest.getFormDataInfo()).equals("") ){
                    StringEntity entity = new StringEntity((String)patchRequest.getFormDataInfo());
//                    StringEntity entity = new StringEntity((String)patchRequest.getFormDataInfo(), ContentType.APPLICATION_FORM_URLENCODED);
                    httpPatch.setEntity(entity);
                    httpPatch.setHeader("Accept", "application/json");
                    httpPatch.setHeader("Content-type", "application/json");
                }
            }else if(patchRequest.getTypeOfData().equals(FORM_DATA.BINARY)){
                //this part is done with the multi part form data too
                if(patchRequest.getFormDataInfo()!=null) {
                    File uploadFile = new File((String) patchRequest.getFormDataInfo());
                    if(uploadFile.exists() && uploadFile.isFile()) {
                        FileEntity fileToUpload = new FileEntity(uploadFile, ContentType.DEFAULT_BINARY);
//                        FileEntity fileToUpload = new FileEntity(uploadFile, ContentType.APPLICATION_OCTET_STREAM);
//                        FileEntity fileToUpload = new FileEntity(uploadFile, ContentType.MULTIPART_FORM_DATA);
                        httpPatch.setEntity(fileToUpload);
                    }else{
                        System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "File not found");
                        return;
                    }
                }else{
                    System.out.println("\033[0;31m" + "Line 99 of class PatchMethod" + "\033[0m" );
                    return;
                }
            }
            //we have to add the option of form data/multipart form


            //Execute the patch request
            CloseableHttpResponse httpResponse= httpclient.execute(httpPatch);
            System.out.println("PATCH Response Status:: "
                    + httpResponse.getStatusLine().getStatusCode());


//            /*




            try{


                BufferedReader reader ;
                try {
                    reader = new BufferedReader(new InputStreamReader(
                            httpResponse.getEntity().getContent()));
                }catch (NullPointerException nullPointerException){
                    System.out.println("empty Response");
                    return;
                }


                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine+"\n");
                }
                reader.close();

                // print result
                //-O --output option handled
                Header[] headers = httpResponse.getAllHeaders();
                String responseHeaders = "";
                if(showresponseHeaders) {
                    for (Header header : headers) {
                        responseHeaders += "Key : " + header.getName() + " ,Value : " + header.getValue() + "\n";
                        System.out.println("Key : " + header.getName() + " ,Value : " + header.getValue());
                    }
                }

                if (outPutFile == null || outPutFile.length() == 0) {
                    System.out.println(response.toString());
                    System.out.println(responseHeaders);
                }else {
                    //if the -O option is used
                    File outputContainer = new File(new File(".").getAbsolutePath() + "\\src\\InformationHandling\\SaveInfoBash\\" + outPutFile);
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

                            FileWriter fileWriter = new FileWriter(outputContainer);
                            fileWriter.write(response.toString());
                            if (showresponseHeaders) {
                                fileWriter.append(responseHeaders);
                            }
                            fileWriter.close();
                        }else {
                            System.out.println("retry again with a new name");
                        }
                    }else {
                        FileWriter fileWriter = new FileWriter(outputContainer);
                        fileWriter.write(response.toString());
                        if (showresponseHeaders) {
                            fileWriter.append(responseHeaders);
                        }
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
        Request patchRequest = new Request("nameOfRequest", TYPE.PATCH, url , FORM_DATA.FORM_URL);
        ArrayList<String[]> headers = new ArrayList<>();
        String[] header1 = {"Header1", "Value1", "false"};
        String[] header2 = {"Header2", "Value2", "true"};
        String[] header3 = {"Header3", "Value3", "false"};
        headers.add(header1);
        headers.add(header2);
        headers.add(header3);
        patchRequest.setHeaderInfo(headers);
        PatchMethod postCommand = new PatchMethod(patchRequest);
//        postCommand.executePost("", true, true);
        postCommand.executePatch("", true, true);
    }

}
