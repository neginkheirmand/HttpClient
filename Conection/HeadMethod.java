package Conection;

import Bash.Response;
import GUI.MESSAGEBODY_TYPE;
import GUI.Request;
import GUI.TYPE;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class HeadMethod {

    private Request headRequest;
    //afterwards we can put an Array list of responses so that we have the history of the request
    //but for that definitely have in mind the edit action on the request


    public HeadMethod(Request headRequest) {
        this.headRequest = headRequest;
    }

    public void executeHead(String outPutFile, boolean followRedirect, boolean showresponseHeaders) {
        //first make sure the headRequest has been created
        if (headRequest == null) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Cannot execute the Head Request");
            return;
        }


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

        //we make sure the user has given a url
        if (headRequest.getUrl() == null || headRequest.getUrl().length() == 0) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " The url field is empty");
            return;
        }
        try {
            //-H and --headers option handled

            //creating the query parameters
            URIBuilder builder = new URIBuilder(headRequest.getUrl());

            //--query
            if (headRequest.getQueryInfo() != null) {
                for (int i = 0; i < headRequest.getQueryInfo().size(); i++) {
                    if ((headRequest.getQueryInfo().get(i)[2]).equals("true")) {
                        builder.addParameter(headRequest.getQueryInfo().get(i)[0], headRequest.getQueryInfo().get(i)[1]);
                    }
                }
            }

            HttpHead httpHead = new HttpHead(builder.build());

            //--headers
            if (headRequest.getHeaderInfo() != null) {
                for (int i = 0; i < headRequest.getHeaderInfo().size(); i++) {
                    if (headRequest.getHeaderInfo().get(i)[0] != null && headRequest.getHeaderInfo().get(i)[1] != null && headRequest.getHeaderInfo().get(i)[2].equals("true")) {
                        httpHead.addHeader(headRequest.getHeaderInfo().get(i)[0], headRequest.getHeaderInfo().get(i)[1]);
                    }
                }
            }

            //--auth
            if (headRequest.getAuth() && headRequest.getAuthInfo() != null && (headRequest.getAuthInfo()[2]).equals("true")) {
                httpHead.addHeader(headRequest.getAuthInfo()[0], headRequest.getAuthInfo()[1]);
            }


            Long start = new Date().getTime();
            CloseableHttpResponse httpResponse = httpClient.execute(httpHead);
            Long end = new Date().getTime();



            System.out.println("HEAD Response Status:: "
                    + httpResponse.getStatusLine().getStatusCode());
            requestResponse.setStatusCode(httpResponse.getStatusLine().getStatusCode());
            Long content_lengh = httpResponse.getEntity().getContentLength();
            Long time_taken = end - start;
            requestResponse.setContentSize(content_lengh);
            requestResponse.setTimeTaken(time_taken);



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
                    System.out.println("empty Response ");
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
                            System.out.println(" Empty Response");
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
                        System.out.println("retry again with a new name ");
                    }
                } else {

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

                    FileWriter fileWriter = new FileWriter(outputContainer);
                    fileWriter.write(response.toString());
                    requestResponse.setOutput(response.toString());
                    fileWriter.close();
                }
            }
            httpResponse.close();
            httpClient.close();
            headRequest.setResponse(requestResponse);
        } catch (java.lang.IllegalArgumentException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL, check the spacing");
        } catch (org.apache.http.client.ClientProtocolException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL");
            if (!(headRequest.getUrl().charAt(0) + headRequest.getUrl().charAt(1) + headRequest.getUrl().charAt(2) + headRequest.getUrl().charAt(3) + headRequest.getUrl().charAt(4)
                    + headRequest.getUrl().charAt(5) + headRequest.getUrl().charAt(6) + "").equals("http://")
                    && !(headRequest.getUrl().charAt(0) + headRequest.getUrl().charAt(1) + headRequest.getUrl().charAt(2) + headRequest.getUrl().charAt(3) + headRequest.getUrl().charAt(4)
                    + headRequest.getUrl().charAt(5) + headRequest.getUrl().charAt(6) + headRequest.getUrl().charAt(7) + "").equals("https://")
            ) {
                System.out.println("      "  + "URL should start with \"http://\" or \"https://\" ");
            }
        } catch (java.net.UnknownHostException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem in finding available Port, Please check Your internet connection");
        } catch (IOException exception) {
            //the methods: execute/ getContent / readLine / close
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem with writing in file ");
//        }catch (NullPointerException exception){
//            System.out.println("here");
        } catch (URISyntaxException e) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "Problem with the query params");
        }
    }

    public static void main(String[] args) {
        String url = "" + (new Scanner(System.in)).nextLine();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy--MM--dd hh-mm-ss");
        Date date = new Date();
        String nameOfOutputFile="output_["+formatter.format(date)+"].txt";
        Request testheadRequest = new Request("nameOfRequest" , TYPE.HEAD, url, MESSAGEBODY_TYPE.FORM_URL);
        ArrayList<String[]> headers = new ArrayList<>();
        String[] header1 = {"Header1", "Value1", "true"};
        String[] header2 = {"Header2", "Value2", "true"};
        String[] header3 = {"Header3", "Value3", "true"};
        headers.add(header1);
        headers.add(header2);
        headers.add(header3);
        testheadRequest.setHeaderInfo(headers);
        HeadMethod testheadMethod = new HeadMethod(testheadRequest);
        testheadMethod.executeHead(nameOfOutputFile, true, true);
    }

}
