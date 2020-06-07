package Conection;

import GUI.FORM_DATA;
import GUI.Request;


import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import GUI.TYPE;

import java.util.Date;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.Header;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;


public class GetMethod {
    private Request getRequest;
    //afterwards we can put an Array list of responses so that we have the history of the request
    //but for that definitely have in mind the edit action on the request


    public GetMethod(Request getRequest) {
        this.getRequest = getRequest;
    }

    public void executeGet(String outPutFile, boolean followRedirect, boolean showResponseHeaders) {
        //first make sure the getRequest ahs been created
        if (getRequest == null) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Cannot execute the Get Request");
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
        if (getRequest.getUrl() == null || getRequest.getUrl().length() == 0) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " The url field is empty");
            return;
        }
        try {
            //-H and --headers option handled

            //creating the query parameters
            URIBuilder builder = new URIBuilder(getRequest.getUrl());

            if (getRequest.getQueryInfo() != null) {
                for (int i = 0; i < getRequest.getQueryInfo().size(); i++) {
                    if ((getRequest.getQueryInfo().get(i)[2]).equals("true")) {
                        builder.addParameter(getRequest.getQueryInfo().get(i)[0], getRequest.getQueryInfo().get(i)[1]);
                    }
                }
            }

            HttpGet httpGet = new HttpGet(builder.build());


            if (getRequest.getHeaderInfo() != null) {
                for (int i = 0; i < getRequest.getHeaderInfo().size(); i++) {
                    if (getRequest.getHeaderInfo().get(i)[0] != null && getRequest.getHeaderInfo().get(i)[1] != null && getRequest.getHeaderInfo().get(i)[2].equals("true")) {
                        httpGet.addHeader(getRequest.getHeaderInfo().get(i)[0], getRequest.getHeaderInfo().get(i)[1]);
                    }
                }

            }
            if (getRequest.getAuth() && getRequest.getAuthInfo() != null && (getRequest.getAuthInfo()[2]).equals("true")) {
                httpGet.addHeader(getRequest.getAuthInfo()[0], getRequest.getAuthInfo()[1]);
            }


            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            System.out.println("GET Response Status:: "
                    + httpResponse.getStatusLine().getStatusCode());


            //but did not close the reader container

            // print result
            //-O --output option handled
            Header[] headers = httpResponse.getAllHeaders();
            if (showResponseHeaders) {
                System.out.println("Response Headers");
                for (Header header : headers) {
                    System.out.println("Key : " + header.getName() + " ,Value : " + header.getValue() + "\n");
                }
            }


            if (outPutFile == null || outPutFile.length() == 0) {

                BufferedReader reader;
                try {
                    reader = new BufferedReader(new InputStreamReader(
                            httpResponse.getEntity().getContent()));
                } catch (NullPointerException nullPointerException) {
                    System.out.println("empty Response");
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
                String posFix = getContentType(headers, outPutFile);
                File outputContainer;
                if (getPosFix(outPutFile).length() == 0) {
                    outputContainer = new File(new File(".").getAbsolutePath() + "\\src\\InformationHandling\\SaveInfoBash\\" + outPutFile + posFix);
                } else {
                    outputContainer = new File(new File(".").getAbsolutePath() + "\\src\\InformationHandling\\SaveInfoBash\\" + outPutFile);
                }
                //the next method sees if the parenrs of the file exist if not creates it and returnts true if already existed and false if created it
                outputContainer.getParentFile().mkdirs();
                if (!outputContainer.createNewFile()) {
                    System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "unable to create output container file");
                    System.out.println("A file with this name already exist, do you want to over-write it? <Y/n>");
                    String overWrite = "" + (new Scanner(System.in)).nextLine();

                    while (!overWrite.equals("Y") && !overWrite.equals("n")) {
                        overWrite = "" + (new Scanner(System.in)).nextLine();
                    }

                    if (overWrite.equals("Y")) {

                        if (posFix.equals(".png") || posFix.equals(".html")) {
                            BufferedInputStream readerStream;
                            try {
                                readerStream = new BufferedInputStream(
                                        httpResponse.getEntity().getContent());

                            } catch (NullPointerException nullPointerException) {
                                System.out.println("empty Response ");
                                return;
                            }
                            writePngFile(readerStream, outputContainer.getAbsolutePath());
                            readerStream.close();
                        } else {

                            BufferedReader reader;
                            try {
                                reader = new BufferedReader(new InputStreamReader(
                                        httpResponse.getEntity().getContent()));
                            } catch (NullPointerException nullPointerException) {
                                System.out.println(" empty Response");
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
                    } else {
                        System.out.println("retry again with a new name");
                    }
                } else {
                    if (posFix.equals(".png") || posFix.equals(".html")) {
                        BufferedInputStream readerStream;
                        try {
                            readerStream = new BufferedInputStream(
                                    httpResponse.getEntity().getContent());
                        } catch (NullPointerException nullPointerException) {
                            System.out.println("empty Response");
                            return;
                        }
                        writePngFile(readerStream, outputContainer.getAbsolutePath());
                        readerStream.close();
                    } else {
                        //the default .txt will be

                        BufferedReader reader;
                        try {
                            reader = new BufferedReader(new InputStreamReader(
                                    httpResponse.getEntity().getContent()));

                        } catch (NullPointerException nullPointerException) {
                            System.out.println("empty Response");
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
            }
            httpResponse.close();
            httpClient.close();
        } catch (java.lang.IllegalArgumentException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL, check the spacing");
        } catch (org.apache.http.client.ClientProtocolException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL");
            if (!(getRequest.getUrl().charAt(0) + getRequest.getUrl().charAt(1) + getRequest.getUrl().charAt(2) + getRequest.getUrl().charAt(3) + getRequest.getUrl().charAt(4)
                    + getRequest.getUrl().charAt(5) + getRequest.getUrl().charAt(6) + "").equals("http://")
                    && !(getRequest.getUrl().charAt(0) + getRequest.getUrl().charAt(1) + getRequest.getUrl().charAt(2) + getRequest.getUrl().charAt(3) + getRequest.getUrl().charAt(4)
                    + getRequest.getUrl().charAt(5) + getRequest.getUrl().charAt(6) + getRequest.getUrl().charAt(7) + "").equals("https://")
            ) {
                System.out.println("      " + " URL should start with \"http://\" or \"https://\"");
            }
        } catch (java.net.UnknownHostException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem in finding available Port, Please check Your internet connection");
        } catch (IOException exception) {
            //the methods: execute/ getContent / readLine / close
            if (outPutFile == null || outPutFile.length() == 0) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem with writing in file ");
            } else {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Time out problem: please try again");
            }
        } catch (NullPointerException exception) {
            System.out.println("here");
        } catch (URISyntaxException e) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " url problem");
        }
    }

    public static String getContentType(Header[] headers, String outputName){
        if(getPosFix(outputName).length()!=0){
            return getPosFix(outputName);
        }else {
            String value = "";
            for (Header header : headers) {
                if (header.getName().equals("Content-Type")) {
                    value = header.getValue();
                }
            }
            if (value.equals("image/png")) {
                return ".png";
            } else if (value.contains("text/html")) {
                return ".html";
            } else if (value.contains("application/json")) {
                return ".json";
            } else {
                return ".txt";
            }
        }
    }

    public static String getPosFix(String outputName) {
        int n = outputName.length();
        if (n < 5) {
            return "";
        }
        if (("" + outputName.charAt(n - 4) + outputName.charAt(n - 3) + outputName.charAt(n - 2) + outputName.charAt(n - 1)).equals(".txt")) {
            return ".txt";
        } else if (("" + outputName.charAt(n - 4) + outputName.charAt(n - 3) + outputName.charAt(n - 2) + outputName.charAt(n - 1)).equals(".png")) {
            return ".png";
        } else if (("" + outputName.charAt(n - 5) + outputName.charAt(n - 4) + outputName.charAt(n - 3) + outputName.charAt(n - 2) + outputName.charAt(n - 1)).equals(".html")) {
            return ".html";
        } else if (("" + outputName.charAt(n - 5) + outputName.charAt(n - 4) + outputName.charAt(n - 3) + outputName.charAt(n - 2) + outputName.charAt(n - 1)).equals(".json")) {
            return ".json";
        }else {
            return "";
        }
    }

    private void writePngFile(BufferedInputStream readerContainer, String destinationPath) {
        try{
            FileOutputStream fileOS = new FileOutputStream(destinationPath);
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = readerContainer.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " problems with writing in the output file(image)");
        }finally {
            try{
                readerContainer.close();
            }catch (IOException exception){
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " problems with closing the output file");
            }

        }
    }

    public static void main(String[] args) {
        String url = "" + (new Scanner(System.in)).nextLine();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy--MM--dd hh-mm-ss");
        Date date = new Date();
        String nameOfOutputFile="output_["+formatter.format(date)+"]";
        Request testGetRequest = new Request("nameOfGETRequest" , TYPE.GET, url, FORM_DATA.FORM_URL);
        ArrayList<String[]> headers = new ArrayList<>();
        String[] header1 = {"Header1", "Value1", "true"};
        headers.add(header1);
        ArrayList<String[]> queryPatams = new ArrayList<>();
        String[] param1 = {"bgcolor","rgb(0,0,255)", "true"};
        String[] param2 = {"width","150", "true"};
        String[] param3 = {"height","150", "true"};
        String[] param4 = {"text","hello", "true"};
        queryPatams.add(param1);
        queryPatams.add(param2);
        queryPatams.add(param3);
        queryPatams.add(param4);
        testGetRequest.setQueryInfo(queryPatams);
        testGetRequest.setHeaderInfo(headers);
        GetMethod testgetMethod = new GetMethod(testGetRequest);
        testgetMethod.executeGet(nameOfOutputFile, true, true);
    }


}
