package Conection;

import GUI.FORM_DATA;
import GUI.Request;
import GUI.TYPE;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
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



        CloseableHttpClient httpClient;
        if(followRedirect) {
            //method 1:
            //the next code does show you that you are being redirected
            httpClient = HttpClients.createDefault();
            //method 2:
            //the next code doesnt show you that you are being redirected
//        httpClient =  HttpClientBuilder.create()
//                .setRedirectStrategy(new LaxRedirectStrategy()).build();
        }else {
            httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
        }

        //we make sure the user has given a url
        if (headRequest.getUrl()==null || headRequest.getUrl().length() == 0) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " The url field is empty");
            return;
        }
        try {
            //-H and --headers option handled
            HttpHead httpHead = new HttpHead(headRequest.getUrl());

            if (headRequest.getHeaderInfo() != null) {
                for (int i = 0; i < headRequest.getHeaderInfo().size(); i++) {
                    if (headRequest.getHeaderInfo().get(i)[0] != null && headRequest.getHeaderInfo().get(i)[1] != null && headRequest.getHeaderInfo().get(i)[2].equals("true")) {
                        httpHead.addHeader(headRequest.getHeaderInfo().get(i)[0], headRequest.getHeaderInfo().get(i)[1]);
                    }
                }
            }

            CloseableHttpResponse httpResponse = httpClient.execute(httpHead);

            System.out.println("HEAD Response Status:: "
                    + httpResponse.getStatusLine().getStatusCode());


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

//            System.out.println("\033[0;31m"+" What does the next code? " + "\033[0m");
//            System.out.println("\nGet Response Header By Key ...\n");
//            String server = httpResponse.getFirstHeader("Server").getValue();
//
//            if (server == null) {
//                System.out.println("Key 'Server' is not found!");
//            } else {
//                System.out.println("Server - " + server);
//            }

            if (outPutFile == null || outPutFile.length() == 0) {
                System.out.println(response.toString());
                System.out.println(responseHeaders);
            } else {
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
                        System.out.println("retry again with a new name ");
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
            httpResponse.close();
            httpClient.close();
        } catch (java.lang.IllegalArgumentException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL, check the spacing");
        } catch (org.apache.http.client.ClientProtocolException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL");
        } catch (java.net.UnknownHostException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem in finding available Port, Please check Your internet connection");
        } catch (IOException exception) {
            //the methods: execute/ getContent / readLine / close
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem with writing in file ");
//        }catch (NullPointerException exception){
//            System.out.println("here");
        }
    }

    public static void main(String[] args) {
        String url = "" + (new Scanner(System.in)).nextLine();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy--MM--dd hh-mm-ss");
        Date date = new Date();
        String nameOfOutputFile="output_["+formatter.format(date)+"].txt";
        Request testheadRequest = new Request("nameOfRequest" , TYPE.HEAD, url, FORM_DATA.FORM_URL);
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
