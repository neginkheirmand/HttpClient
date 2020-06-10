package Conection;

import Bash.Response;
import GUI.MESSAGEBODY_TYPE;
import GUI.Request;
import GUI.TYPE;


import java.io.File;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import java.io.*;
import org.apache.http.Header;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

public class  PostMethod {
    private Request postRequest;
    //afterwards we can put an Array list of responses so that we have the history of the request
    //but for that definitely have in mind the edit action on the request


    public PostMethod(Request postRequest) {
        this.postRequest = postRequest;
    }

    public void executePost(String outPutFile, boolean followRedirect, boolean showresponseHeaders) {


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
        if (postRequest == null) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "problems in setting the request");
            return;
        }

        try {
            if(postRequest.getTypeOfData().equals(MESSAGEBODY_TYPE.MULTIPART_FORM)) {
                MultiPartPost multiPartPost = new MultiPartPost(postRequest);
                try {
                    multiPartPost.multipartPostRequest(outPutFile, followRedirect, showresponseHeaders);
                }catch (ParseException exception){
                    System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "problems in setting the request");
                }catch (IOException exception){
                    System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "problems in setting the request");
                }
                return;
            }

            //Create an HttpGet object
            //creating the query parameters
            HttpParams queryParams = null;
            if (postRequest.getQueryInfo() != null) {
                queryParams = new BasicHttpParams();
                for (int i = 0; i < postRequest.getQueryInfo().size(); i++) {
                    if ((postRequest.getQueryInfo().get(i)[2]).equals("true")) {
                        queryParams.setParameter(postRequest.getQueryInfo().get(i)[0], postRequest.getQueryInfo().get(i)[1]);
                    }
                }
            }
            HttpPost httpPost;
            if (postRequest.getUrl() != null && !postRequest.getUrl().equals("")) {
                //creating the query parameters
                URIBuilder builder = new URIBuilder(postRequest.getUrl());

                if (postRequest.getQueryInfo() != null) {
                    for (int i = 0; i < postRequest.getQueryInfo().size(); i++) {
                        if ((postRequest.getQueryInfo().get(i)[2]).equals("true")) {
                            builder.addParameter(postRequest.getQueryInfo().get(i)[0], postRequest.getQueryInfo().get(i)[1]);
                        }
                    }
                }
                httpPost = new HttpPost(builder.build());
                if (queryParams != null) {
                    System.out.println("setting params");
                    httpPost.setParams(queryParams);
                }
            } else {
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


            //--auth
            if (postRequest.getAuth() && postRequest.getAuthInfo() != null && (postRequest.getAuthInfo()[2]).equals("true")) {
                httpPost.addHeader(postRequest.getAuthInfo()[0], postRequest.getAuthInfo()[1]);
            }


            //in case its form url encoded
            if (postRequest.getTypeOfData().equals(MESSAGEBODY_TYPE.FORM_URL)) {
                List<NameValuePair> params = null;
                if (postRequest.getFormDataInfo() != null) {
                    params = new ArrayList<NameValuePair>();
                    for (int i = 0; i < ((ArrayList<String[]>) postRequest.getFormDataInfo()).size(); i++) {
                        if (((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[2].equals("true") &&
                                ((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[0] != null &&
                                ((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[1] != null) {
                            params.add(new BasicNameValuePair(((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[0],
                                    ((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[1]));
                        }
                    }
                }
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
            } else if (postRequest.getTypeOfData().equals(MESSAGEBODY_TYPE.JSON)) {
                //in case is json we can use of .setHeader(HttpHeaders.CONTENT_TYPE, "application/json") in the HttpPost
                if (postRequest.getFormDataInfo() != null && !((String) postRequest.getFormDataInfo()).equals("")) {
                    StringEntity entity = new StringEntity((String) postRequest.getFormDataInfo());
//                    StringEntity entity = new StringEntity((String)postRequest.getFormDataInfo(), ContentType.APPLICATION_FORM_URLENCODED);
                    httpPost.setEntity(entity);
                    httpPost.setHeader("Accept", "application/json");
                    httpPost.setHeader("Content-type", "application/json");
                }
            } else if (postRequest.getTypeOfData().equals(MESSAGEBODY_TYPE.BINARY)) {
                //this part is done with the multi part form data too
                if (postRequest.getFormDataInfo() != null) {
                    File uploadFile = new File((String) postRequest.getFormDataInfo());
                    if (uploadFile.exists() && uploadFile.isFile()) {
                        FileEntity fileToUpload = new FileEntity(uploadFile, ContentType.DEFAULT_BINARY);
//                        FileEntity fileToUpload = new FileEntity(uploadFile, ContentType.APPLICATION_OCTET_STREAM);
//                        FileEntity fileToUpload = new FileEntity(uploadFile, ContentType.MULTIPART_MESSAGEBODY_TYPE);
                        httpPost.setEntity(fileToUpload);
                    } else {
                        System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "File not found");
                        return;
                    }
                } else {
                    System.out.println("\033[0;31m" + "Line 99 of class PostMethod" + "\033[0m");
                    return;
                }
            }/* else if (postRequest.getTypeOfData().equals(MESSAGEBODY_TYPE.MULTIPART_FORM)) {
                List<NameValuePair> body = null;
                System.out.println(0);
//                httpPost.setHeader("Content-Type", "multipart/form-data; boundary="+ "*****" + Long.toString(System.currentTimeMillis()) + "*****");
                if (postRequest.getFormDataInfo() != null) {
                    System.out.println(1);
                    body = new ArrayList<NameValuePair>();
                    for (int i = 0; i < ((ArrayList<String[]>) postRequest.getFormDataInfo()).size(); i++) {
                        if (((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[2].equals("true") &&
                                ((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[0] != null &&
                                ((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[1] != null) {
                            body.add(new BasicNameValuePair(((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[0],
                                    ((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[1]));
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
                    httpPost.setEntity(entity);

                }

            }
            //we have to add the option of form data/multipart form


            */

            //Execute the Post request
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            System.out.println("POST Response Status:: "
                    + httpResponse.getStatusLine().getStatusCode());
            requestResponse.setStatusCode(httpResponse.getStatusLine().getStatusCode());


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
                        System.out.println("empty Response    ");
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
                                System.out.println("empty Response  ");
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
                            System.out.println("Empty Response     ");
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
                httpResponse.close();
            }
        } catch (java.lang.IllegalArgumentException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL, check the spacing");
        } catch (org.apache.http.client.ClientProtocolException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL");
            if (!(postRequest.getUrl().charAt(0) + postRequest.getUrl().charAt(1) + postRequest.getUrl().charAt(2) + postRequest.getUrl().charAt(3)
                    + postRequest.getUrl().charAt(4) + postRequest.getUrl().charAt(5) + postRequest.getUrl().charAt(6) + "").equals("http://")
                    && !(postRequest.getUrl().charAt(0) + postRequest.getUrl().charAt(1) + postRequest.getUrl().charAt(2) + postRequest.getUrl().charAt(3)
                    + postRequest.getUrl().charAt(4) + postRequest.getUrl().charAt(5)
                    + postRequest.getUrl().charAt(6) + postRequest.getUrl().charAt(7) + "").equals("https://")
            ) {
                System.out.println("      "  + " URL should start with \"http://\" or \"https://\" ");
            }
        } catch (java.net.UnknownHostException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem in finding available Port, Please check Your internet connection");
        } catch (IOException exception) {
            //the methods: execute/ getContent / readLine / close
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem with writing in file ");
        } catch (NullPointerException exception) {
            System.out.println("\033[0;31m" + "Error" + "\033[0m");
        } catch (URISyntaxException e) {
            System.out.println("\033[0;31m" + "Error" + "\033[0m" + " Problems in setting the query ");
        } finally {
            try {
                httpClient.close();
                postRequest.setResponse(requestResponse);
            } catch (java.lang.IllegalArgumentException exception) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL, check the spacing");
            } catch (org.apache.http.client.ClientProtocolException exception) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL");
                if (!(postRequest.getUrl().charAt(0) + postRequest.getUrl().charAt(1) + postRequest.getUrl().charAt(2) + postRequest.getUrl().charAt(3)
                        + postRequest.getUrl().charAt(4) + postRequest.getUrl().charAt(5) + postRequest.getUrl().charAt(6) + "").equals("http://")
                        && !(postRequest.getUrl().charAt(0) + postRequest.getUrl().charAt(1) + postRequest.getUrl().charAt(2) + postRequest.getUrl().charAt(3)
                        + postRequest.getUrl().charAt(4) + postRequest.getUrl().charAt(5)
                        + postRequest.getUrl().charAt(6) + postRequest.getUrl().charAt(7) + "").equals("https://")
                ) {
                    System.out.println("      "  + "URL should start with \"http://\" or \"https://\"   ");
                }
            } catch (java.net.UnknownHostException exception) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem in finding available Port, Please check Your internet connection");
            } catch (IOException exception) {
                //the methods: execute/ getContent / readLine / close
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem with writing in file ");
            } catch (NullPointerException exception) {
                System.out.println("\033[0;31m" + "Error" + "\033[0m");
            }
        }
    }

    public static void main(String[] args) {

        String url = ""+(new Scanner(System.in)).nextLine();
        Request postRequest = new Request("nameOfRequest", TYPE.POST, url , MESSAGEBODY_TYPE.MULTIPART_FORM);
        ArrayList<String[]> headers = new ArrayList<>();
        String[] header3 = {"Header3", "Value3", "true"};
        headers.add(header3);
        postRequest.setHeaderInfo(headers);
        ArrayList<String[]> queryPatams = new ArrayList<>();
        String[] param1 = {"bgcolor","rgb(0,0,0)", "true"};
        String[] param2 = {"width","150", "true"};
        String[] param3 = {"height","150", "true"};
        String[] param4 = {"text","hello", "true"};
        queryPatams.add(param1);
        queryPatams.add(param2);
        queryPatams.add(param3);
        queryPatams.add(param4);
        postRequest.setQueryInfo(queryPatams);
        ArrayList<String[]> body = new ArrayList<>();
        String[] data1 = {"Header1", "Value1", "true"};
        String[] data2 = {"Header2", "Value2", "true"};
        String[] data3 = {"Header3", "Value3", "true"};
        String[] data4 = {"Header4", "Value4", "true"};
        body.add(data1);
        body.add(data2);
        body.add(data3);
        body.add(data4);
        postRequest.setFormDataInfo(body);

        PostMethod postCommand = new PostMethod(postRequest);
//        postCommand.executePost("", true, true);
        postCommand.executePost("picture.png", true, true);
    }

}
