package Conection;

import GUI.MESSAGEBODY_TYPE;
import GUI.Request;
import GUI.TYPE;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpOptions;
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

public class OptionMethod {


    private Request optionRequest;
    //afterwards we can put an Array list of responses so that we have the history of the request
    //but for that definitely have in mind the edit action on the request


    public OptionMethod(Request optionRequest) {
        this.optionRequest = optionRequest;

    }

    public void executeOption(String outPutFile, boolean followRedirect, boolean showresponseHeaders) {
        //first make sure the optionRequest ahs been created
        if (optionRequest == null) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Cannot execute the OPTION Request");
            return;
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
        if (optionRequest.getUrl() == null || optionRequest.getUrl().length() == 0) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " The url field is empty");
            return;
        }
        try {
            //-H and --headers option handled

            //--query
            //creating the query parameters
            URIBuilder builder = new URIBuilder(optionRequest.getUrl());

            if (optionRequest.getQueryInfo() != null) {
                for (int i = 0; i < optionRequest.getQueryInfo().size(); i++) {
                    if ((optionRequest.getQueryInfo().get(i)[2]).equals("true")) {
                        builder.addParameter(optionRequest.getQueryInfo().get(i)[0], optionRequest.getQueryInfo().get(i)[1]);
                    }
                }
            }

            HttpOptions httpOption = new HttpOptions(builder.build());

            if (optionRequest.getHeaderInfo() != null) {
                for (int i = 0; i < optionRequest.getHeaderInfo().size(); i++) {
                    if (optionRequest.getHeaderInfo().get(i)[0] != null && optionRequest.getHeaderInfo().get(i)[1] != null && optionRequest.getHeaderInfo().get(i)[2].equals("true")) {
                        httpOption.addHeader(optionRequest.getHeaderInfo().get(i)[0], optionRequest.getHeaderInfo().get(i)[1]);
                    }
                }
            }

            //--auth
            if (optionRequest.getAuth() && optionRequest.getAuthInfo() != null && (optionRequest.getAuthInfo()[2]).equals("true")) {
                httpOption.addHeader(optionRequest.getAuthInfo()[0], optionRequest.getAuthInfo()[1]);
            }


            CloseableHttpResponse httpResponse = httpClient.execute(httpOption);

            System.out.println("OPTION Response Status:: "
                    + httpResponse.getStatusLine().getStatusCode());


            // print result
            //-O --output option handled
            Header[] headers = httpResponse.getAllHeaders();
            if (showresponseHeaders) {
                for (Header header : headers) {
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
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new InputStreamReader(
                            httpResponse.getEntity().getContent()));
                } catch (NullPointerException nullPointerException) {
                    System.out.println("empty response");
                    return;
                }

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine + "\n");
                }
                reader.close();
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
                //the next method sees if the parenrs of the file exist if not creates it and returnts true if already existed and false if created it
                outputContainer.getParentFile().mkdirs();
                if (!outputContainer.createNewFile()) {
                    System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "unable to create output file");
                    System.out.println("A file with this name already exist, Do you want to over-write on it? <Y/n>");
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
                            System.out.println(" Empty Response ");
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
                        System.out.println("Retry again with a new name");
                    }
                } else {
                    BufferedReader reader;
                    try {
                        reader = new BufferedReader(new InputStreamReader(
                                httpResponse.getEntity().getContent()));
                    } catch (NullPointerException nullPointerException) {
                        System.out.println("Empty Response  ");
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
                }
            }
            httpResponse.close();
            httpClient.close();
        } catch (java.lang.IllegalArgumentException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL, check the spacing");
        } catch (org.apache.http.client.ClientProtocolException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL");
            if (!(optionRequest.getUrl().charAt(0) + optionRequest.getUrl().charAt(1) + optionRequest.getUrl().charAt(2) + optionRequest.getUrl().charAt(3) + optionRequest.getUrl().charAt(4)
                    + optionRequest.getUrl().charAt(5) + optionRequest.getUrl().charAt(6) + "").equals("http://")
                    && !(optionRequest.getUrl().charAt(0) + optionRequest.getUrl().charAt(1) + optionRequest.getUrl().charAt(2) + optionRequest.getUrl().charAt(3) + optionRequest.getUrl().charAt(4)
                    + optionRequest.getUrl().charAt(5) + optionRequest.getUrl().charAt(6) + optionRequest.getUrl().charAt(7) + "").equals("https://")
            ) {
                System.out.println("      "  + "URL should start with \"http://\" or \"https://\"  ");
            }
        } catch (java.net.UnknownHostException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem in finding available Port, Please check Your internet connection");
        } catch (IOException exception) {
            //the methods: execute/ getContent / readLine / close
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem with writing in file ");
        } catch (NullPointerException exception) {
            System.out.println("here");
        } catch (URISyntaxException e) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problems setting the query params ");
        }
    }

    public static void main(String[] args) {
        String url = "" + (new Scanner(System.in)).nextLine();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy--MM--dd hh-mm-ss");
        Date date = new Date();
        String nameOfOutputFile="output_["+formatter.format(date)+"].txt";
        Request testoptionRequest = new Request("nameOfRequest" , TYPE.OPTION, url, MESSAGEBODY_TYPE.FORM_URL);
        ArrayList<String[]> headers = new ArrayList<>();
        String[] header1 = {"Header1", "Value1", "true"};
        String[] header2 = {"Header2", "Value2", "true"};
        String[] header3 = {"Header3", "Value3", "true"};
        headers.add(header1);
        headers.add(header2);
        headers.add(header3);
        testoptionRequest.setHeaderInfo(headers);
        OptionMethod testgetMethod = new OptionMethod(testoptionRequest);
        testgetMethod.executeOption(nameOfOutputFile, true, true);
    }




}
