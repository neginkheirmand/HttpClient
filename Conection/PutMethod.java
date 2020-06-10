package Conection;

import Bash.Response;
import GUI.Request;


import GUI.MESSAGEBODY_TYPE;

import java.io.File;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import java.io.*;

import GUI.TYPE;
import org.apache.http.Header;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;


public class PutMethod {


    private Request putRequest;
    //afterwards we can put an Array list of responses so that we have the history of the request
    //but for that definitely have in mind the edit action on the request


    public PutMethod(Request putRequest) {
        this.putRequest = putRequest;
    }

    public void executePut(String outPutFile, boolean followRedirect, boolean showresponseHeaders) {


        Response requestResponse = new Response(null, "", true, "", 0);
        if(outPutFile==null || outPutFile.length()==0) {
            requestResponse.setOutputContainer(false);
        }else{
            requestResponse.setOutputContainer(true);
        }

        CloseableHttpClient httpClient;
        if (followRedirect) {
            //method 1:
            //the next code does show you that you are being redirected
            httpClient = HttpClients.createDefault();
            //method 2:
            //the next code doesnt show you that you are being redirected
//        httpClient =  HttpClientBuilder.create()
//                .setRedirectStrategy(new LaxRedirectStrategy()).build();
        } else {
            httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
        }

        if (putRequest == null) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "problems in setting the request");
            return;
        }

        try {


            if(putRequest.getTypeOfData().equals(MESSAGEBODY_TYPE.MULTIPART_FORM)) {
                MultiPartPut multiPartPut = new MultiPartPut(putRequest);
                try {
                    multiPartPut.multipartPutRequest(outPutFile, followRedirect, showresponseHeaders);
                }catch (ParseException exception){
                    System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "problems in setting the request");
                }catch (IOException exception){
                    System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "problems in setting the request");
                }
                return;
            }

            //Create an HttpGet object
            HttpPut httpPut;
            if (putRequest.getUrl() != null && !putRequest.getUrl().equals("")) {
                //creating the query parameters
                URIBuilder builder = new URIBuilder(putRequest.getUrl());

                if (putRequest.getQueryInfo() != null) {
                    for (int i = 0; i < putRequest.getQueryInfo().size(); i++) {
                        if ((putRequest.getQueryInfo().get(i)[2]).equals("true")) {
                            builder.addParameter(putRequest.getQueryInfo().get(i)[0], putRequest.getQueryInfo().get(i)[1]);
                        }
                    }
                }
                httpPut = new HttpPut(builder.build());
            } else {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid url");
                return;
            }

            //the headers of this put request:
            if (putRequest.getHeaderInfo() != null) {
                for (int i = 0; i < putRequest.getHeaderInfo().size(); i++) {
                    if (putRequest.getHeaderInfo().get(i)[0] != null && putRequest.getHeaderInfo().get(i)[1] != null && putRequest.getHeaderInfo().get(i)[2].equals("true")) {
                        httpPut.addHeader(putRequest.getHeaderInfo().get(i)[0], putRequest.getHeaderInfo().get(i)[1]);
                    }
                }
            }


            //--auth
            if (putRequest.getAuth() && putRequest.getAuthInfo() != null && (putRequest.getAuthInfo()[2]).equals("true")) {
                httpPut.addHeader(putRequest.getAuthInfo()[0], putRequest.getAuthInfo()[1]);
            }

            //in case its form url encoded
            if (putRequest.getTypeOfData().equals(MESSAGEBODY_TYPE.FORM_URL)) {
                System.out.println("its form url encoded");
                httpPut.addHeader("Content-Type", "application/x-www-form-urlencoded");
                List<NameValuePair> params = null;
                if (putRequest.getFormDataInfo() != null) {
                    System.out.println(putRequest.getFormDataInfo().getClass());
                    params = new ArrayList<NameValuePair>();
                    for (int i = 0; i < ((ArrayList<String[]>) putRequest.getFormDataInfo()).size(); i++) {
                        if (((ArrayList<String[]>) putRequest.getFormDataInfo()).get(i)[2].equals("true") &&
                                ((ArrayList<String[]>) putRequest.getFormDataInfo()).get(i)[0] != null &&
                                ((ArrayList<String[]>) putRequest.getFormDataInfo()).get(i)[1] != null) {
                            params.add(new BasicNameValuePair(((ArrayList<String[]>) putRequest.getFormDataInfo()).get(i)[0],
                                    ((ArrayList<String[]>) putRequest.getFormDataInfo()).get(i)[1]));
                        }
                    }
                }
                if (params != null) {
                    httpPut.setEntity(new UrlEncodedFormEntity(params));
                }
            } else if (putRequest.getTypeOfData().equals(MESSAGEBODY_TYPE.JSON)) {
                //in case is json we can use of .setHeader(HttpHeaders.CONTENT_TYPE, "application/json") in the httpPut
                if (putRequest.getFormDataInfo() != null && !((String) putRequest.getFormDataInfo()).equals("")) {
                    StringEntity entity = new StringEntity((String) putRequest.getFormDataInfo());
//                    StringEntity entity = new StringEntity((String)putRequest.getFormDataInfo(), ContentType.APPLICATION_FORM_URLENCODED);
                    httpPut.setEntity(entity);
                    httpPut.setHeader("Accept", "application/json");
                    httpPut.setHeader("Content-type", "application/json");
                }
            } else if (putRequest.getTypeOfData().equals(MESSAGEBODY_TYPE.BINARY)) {
                //this part is done with the multi part form data too
                if (putRequest.getFormDataInfo() != null) {
                    File uploadFile = new File((String) putRequest.getFormDataInfo());
                    if (uploadFile.exists() && uploadFile.isFile()) {
                        FileEntity fileToUpload = new FileEntity(uploadFile, ContentType.DEFAULT_BINARY);
//                        FileEntity fileToUpload = new FileEntity(uploadFile, ContentType.APPLICATION_OCTET_STREAM);
//                        FileEntity fileToUpload = new FileEntity(uploadFile, ContentType.MULTIPART_MESSAGEBODY_TYPE);
                        httpPut.setEntity(fileToUpload);
                    } else {
                        System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "File not found");
                        return;
                    }
                } else {
                    System.out.println("\033[0;31m" + "Line 115 of class PutMethod" + "\033[0m");
                    return;
                }
            }
            /*
            else if (putRequest.getTypeOfData().equals(MESSAGEBODY_TYPE.MULTIPART_FORM)) {
                List<NameValuePair> body = null;
                System.out.println(0);
//                httpPost.setHeader("Content-Type", "multipart/form-data; boundary="+ "*****" + Long.toString(System.currentTimeMillis()) + "*****");
                if (putRequest.getFormDataInfo() != null) {
                    System.out.println(1);
                    body = new ArrayList<NameValuePair>();
                    for (int i = 0; i < ((ArrayList<String[]>) putRequest.getFormDataInfo()).size(); i++) {
                        if (((ArrayList<String[]>) putRequest.getFormDataInfo()).get(i)[2].equals("true") &&
                                ((ArrayList<String[]>) putRequest.getFormDataInfo()).get(i)[0] != null &&
                                ((ArrayList<String[]>) putRequest.getFormDataInfo()).get(i)[1] != null) {
                            body.add(new BasicNameValuePair(((ArrayList<String[]>) putRequest.getFormDataInfo()).get(i)[0],
                                    ((ArrayList<String[]>) putRequest.getFormDataInfo()).get(i)[1]));
                        }
                    }
                    System.out.println(12);
//                    InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
                    HttpEntity entity = EntityBuilder.create()
                            .setContentType(ContentType.MULTIPART_FORM_DATA)
                            .setParameters(body)
//                            .setStream(new BufferedInputStream())
                            .build();

                    System.out.println(123);
                    httpPut.setEntity(entity);

                }

            }

            */

            //Execute the put request
            CloseableHttpResponse httpResponse = httpClient.execute(httpPut);
            System.out.println("PUT Response Status:: "
                    + httpResponse.getStatusLine().getStatusCode());
            requestResponse.setStatusCode(httpResponse.getStatusLine().getStatusCode());

//            /*

            try {
                // print result
                //-O --output option handled
                Header[] headers = httpResponse.getAllHeaders();
                requestResponse.setResponseHeaders(headers);

                if (showresponseHeaders) {
                    for (Header header : headers) {
                        System.out.println("Key : " + header.getName() + " ,Value : " + header.getValue());
                    }
                }

                if (outPutFile == null || outPutFile.length() == 0) {

                    BufferedReader reader;
                    try {
                        reader = new BufferedReader(new InputStreamReader(
                                httpResponse.getEntity().getContent()));
                    } catch (NullPointerException nullPointerException) {
                        System.out.println("Empty Response");
                        return;
                    }

                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = reader.readLine()) != null) {
                        response.append(inputLine + "\n");
                    }
                    reader.close();

                    requestResponse.setOutput(response.toString());
                    System.out.println(response.toString());
                } else {
                    //if the -O option is used
                    String posFix = GetMethod.getContentType(headers, outPutFile);
                    File outputContainer;
                    if (GetMethod.getPosFix(outPutFile).length() == 0) {
                        outputContainer = new File(new File(".").getAbsolutePath() + "\\src\\InformationHandling\\SaveInfoBash\\" + outPutFile + posFix);
                    } else {
                        outputContainer = new File(new File(".").getAbsolutePath() + "\\src\\InformationHandling\\SaveInfoBash\\" + outPutFile);
                    }

                    requestResponse.setPathOutputFile(outputContainer.getAbsolutePath());

                    //the next method sees if the parenrs of the file exist if not creates it and returnts true if already existed and false if created it
                    outputContainer.getParentFile().mkdirs();
                    if (!outputContainer.createNewFile()) {
                        System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "unable to create output file");
                        System.out.println("A file with this name already exist, do you want to over-write on it? <Y/n>");
                        String overWrite = "" + (new Scanner(System.in)).nextLine();

                        while (!overWrite.equals("Y") && !overWrite.equals("n")) {
                            overWrite = "" + (new Scanner(System.in)).nextLine();
                        }

                        if (overWrite.equals("Y")) {

                            BufferedReader reader;
                            try {
                                reader = new BufferedReader(new InputStreamReader(
                                        httpResponse.getEntity().getContent()));
                            } catch (NullPointerException nullPointerException) {
                                System.out.println(" Empty Response  ");
                                return;
                            }

                            String inputLine;
                            StringBuffer response = new StringBuffer();

                            while ((inputLine = reader.readLine()) != null) {
                                response.append(inputLine + "\n");
                            }
                            reader.close();


                            FileWriter fileWriter = new FileWriter(outputContainer);
                            fileWriter.write(response.toString());
                            fileWriter.close();
                        } else {
                            System.out.println("retry again with a new name");
                        }
                    } else {

                        BufferedReader reader;
                        try {
                            reader = new BufferedReader(new InputStreamReader(
                                    httpResponse.getEntity().getContent()));
                        } catch (NullPointerException nullPointerException) {
                            System.out.println("Empty Response        ");
                            return;
                        }

                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = reader.readLine()) != null) {
                            response.append(inputLine + "\n");
                        }
                        reader.close();

                        FileWriter fileWriter = new FileWriter(outputContainer);
                        fileWriter.write(response.toString());
                        requestResponse.setOutput(response.toString());
                        fileWriter.close();
                    }
                }
            } finally {
                try {
                    httpResponse.close();
                } catch (IOException ioexception) {
                    System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "problem with closing the reponse");
                }
            }
        } catch (java.lang.IllegalArgumentException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL, check the spacing");
        } catch (org.apache.http.client.ClientProtocolException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL");
            if (!(putRequest.getUrl().charAt(0) + putRequest.getUrl().charAt(1) + putRequest.getUrl().charAt(2) + putRequest.getUrl().charAt(3)
                    + putRequest.getUrl().charAt(4) + putRequest.getUrl().charAt(5) + putRequest.getUrl().charAt(6) + "").equals("http://")
                    && !(putRequest.getUrl().charAt(0) + putRequest.getUrl().charAt(1) + putRequest.getUrl().charAt(2) + putRequest.getUrl().charAt(3)
                    + putRequest.getUrl().charAt(4) + putRequest.getUrl().charAt(5) + putRequest.getUrl().charAt(6) + putRequest.getUrl().charAt(7)
                    + "").equals("https://")
            ) {
                System.out.println("      "  + "URL should start with \"http://\" or \"https://\"        ");
            }
        } catch (java.net.UnknownHostException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem in finding available Port, Please check Your internet connection");
        } catch (IOException exception) {
//            the methods: execute/ getContent / readLine / close
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem with writing in file ");
        } catch (NullPointerException exception) {
            System.out.println("\033[0;31m" + "Error" + "\033[0m");
        } catch (URISyntaxException e) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem in setting the query params ");
        } finally {
            try {
                httpClient.close();
                putRequest.setResponse(requestResponse);
            } catch (java.lang.IllegalArgumentException exception) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL, check the spacing");
            } catch (org.apache.http.client.ClientProtocolException exception) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL");
                if (!(putRequest.getUrl().charAt(0) + putRequest.getUrl().charAt(1) + putRequest.getUrl().charAt(2) + putRequest.getUrl().charAt(3)
                        + putRequest.getUrl().charAt(4) + putRequest.getUrl().charAt(5) + putRequest.getUrl().charAt(6) + "").equals("http://")
                        && !(putRequest.getUrl().charAt(0) + putRequest.getUrl().charAt(1) + putRequest.getUrl().charAt(2) + putRequest.getUrl().charAt(3)
                        + putRequest.getUrl().charAt(4) + putRequest.getUrl().charAt(5) + putRequest.getUrl().charAt(6) + putRequest.getUrl().charAt(7)
                        + "").equals("https://")
                ) {
                    System.out.println("      "  + "URL should start with \"http://\" or \"https://\"            ");
                }
            } catch (java.net.UnknownHostException exception) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem in finding available Port, Please check Your internet connection");
            } catch (IOException exception) {
                //the methods: execute/ getContent / readLine / close
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem with closing the client");
            } catch (NullPointerException exception) {
                System.out.println("\033[0;31m" + "Error" + "\033[0m");
            }
        }
    }



    public static void main(String[] args) {
        String url = ""+(new Scanner(System.in)).nextLine();
        Request putRequest = new Request("nameOfRequest", TYPE.PUT, url , MESSAGEBODY_TYPE.FORM_URL);
        ArrayList<String[]> headers = new ArrayList<>();
        String[] header1 = {"Header1", "Value1", "true"};
        String[] header2 = {"Header2", "Value2", "false"};
        String[] header3 = {"Header3", "Value3", "true"};
        headers.add(header1);
        headers.add(header2);
        headers.add(header3);
        putRequest.setHeaderInfo(headers);
        PutMethod putCommand = new PutMethod(putRequest);
        putCommand.executePut("put.txt", true, true);
    }




}
