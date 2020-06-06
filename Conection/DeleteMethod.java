package Conection;


import java.io.*;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import GUI.FORM_DATA;
import GUI.Request;
import GUI.TYPE;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;


public class DeleteMethod {


    private Request deleteRequest = null ;
    //afterwards we can put an Array list of responses so that we have the history of the request
    //but for that definitely have in mind the edit action on the request


    public DeleteMethod(Request deleteRequest) {
        this.deleteRequest = deleteRequest ;
    }

    public void executeDelete(String outPutFile, boolean followRedirect, boolean showresponseHeaders){

        //first make sure the deleteRequest ahs been created
        if (deleteRequest == null) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Cannot execute the DELETE Request");
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
        if (deleteRequest.getUrl()==null || deleteRequest.getUrl().length() == 0) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " The url field is empty");
            return;
        }
        try {
            //-H and --headers option handled

            //creating the query parameters
            URIBuilder builder = new URIBuilder(deleteRequest.getUrl());

            if(deleteRequest.getQueryInfo()!=null) {
                for(int i=0; i<deleteRequest.getQueryInfo().size(); i++){
                    if((deleteRequest.getQueryInfo().get(i)[2]).equals("true")) {
                        builder.addParameter(deleteRequest.getQueryInfo().get(i)[0], deleteRequest.getQueryInfo().get(i)[1]);
                    }
                }
            }

            HttpDelete httpDelete = new HttpDelete(builder.build());

            //--headers
            if (deleteRequest.getHeaderInfo() != null) {
                for (int i = 0; i < deleteRequest.getHeaderInfo().size(); i++) {
                    if (deleteRequest.getHeaderInfo().get(i)[0] != null && deleteRequest.getHeaderInfo().get(i)[1] != null && deleteRequest.getHeaderInfo().get(i)[2].equals("true")) {
                        httpDelete.addHeader(deleteRequest.getHeaderInfo().get(i)[0], deleteRequest.getHeaderInfo().get(i)[1]);
                    }
                }
            }

            //--auth
            if(deleteRequest.getAuth() && deleteRequest.getAuthInfo()!=null && (deleteRequest.getAuthInfo()[2]).equals("true")) {
                httpDelete.addHeader(deleteRequest.getAuthInfo()[0], deleteRequest.getAuthInfo()[1]);
            }

            CloseableHttpResponse httpResponse = httpClient.execute(httpDelete);

            System.out.println("DELETE Response Status:: "
                    + httpResponse.getStatusLine().getStatusCode());



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
                    System.out.println(" empty Response");
                    return;
                }


                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine+"\n");
                }
                reader.close();
                System.out.println(response.toString());
            } else {
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
                            System.out.println(" empty response");
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
                        System.out.println("empty response");
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
        }catch (NullPointerException exception){
            System.out.println("here");
        } catch (URISyntaxException e) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem with the query params");
        }
    }


    public static void main(String[] args) {

        String url = "" + (new Scanner(System.in)).nextLine();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy--MM--dd hh-mm-ss");
        Date date = new Date();
        String nameOfOutputFile="output_["+formatter.format(date)+"].txt";
        Request testDeleteRequest = new Request("nameOfRequest" , TYPE.DELETE, url, FORM_DATA.FORM_URL);
        ArrayList<String[]> headers = new ArrayList<>();
        String[] header1 = {"Header1", "Value1", "true"};
        String[] header2 = {"Header2", "Value2", "false"};
        String[] header3 = {"Header3", "Value3", "true"};
        headers.add(header1);
        headers.add(header2);
        headers.add(header3);
        testDeleteRequest.setHeaderInfo(headers);
        DeleteMethod testDeleteMethod = new DeleteMethod(testDeleteRequest);
        testDeleteMethod .executeDelete(nameOfOutputFile, true, true);

    }

}
