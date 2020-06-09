package Conection;

import GUI.MESSAGEBODY_TYPE;
import GUI.Request;
import GUI.TYPE;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.FileEntity;
import org.apache.hc.core5.net.URIBuilder;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

public class MultiPartPatch {

    private Request patchRequest;
    //afterwards we can put an Array list of responses so that we have the history of the request
    //but for that definitely have in mind the edit action on the request


    public MultiPartPatch(Request patchRequest) {
        this.patchRequest = patchRequest;
    }

    public void executePatch(String outPutFile, boolean followRedirect, boolean showresponseHeaders) {

        //Create an HttpClient object
        CloseableHttpClient httpclient = HttpClients.createDefault();

        if (patchRequest == null) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "problems in setting the request");
            return;
        }

        try {
            //Create an HttpGet object
            HttpPatch httpPatch;
            if (patchRequest.getUrl() != null && !patchRequest.getUrl().equals("")) {
                //creating the query parameters
                URIBuilder builder = new URIBuilder(patchRequest.getUrl());

                if (patchRequest.getQueryInfo() != null) {
                    for (int i = 0; i < patchRequest.getQueryInfo().size(); i++) {
                        if ((patchRequest.getQueryInfo().get(i)[2]).equals("true")) {
                            builder.addParameter(patchRequest.getQueryInfo().get(i)[0], patchRequest.getQueryInfo().get(i)[1]);
                        }
                    }
                }
                httpPatch = new HttpPatch(builder.build());
            } else {
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

            //--auth
            if (patchRequest.getAuth() && patchRequest.getAuthInfo() != null && (patchRequest.getAuthInfo()[2]).equals("true")) {
                httpPatch.addHeader(patchRequest.getAuthInfo()[0], patchRequest.getAuthInfo()[1]);
            }


            //this part is for MULTIPART_FORM_DATA
            if (patchRequest.getFormDataInfo() != null) {

            } else {
                System.out.println("\033[0;31m" + "Line 99 of class PatchMethod" + "\033[0m");
                return;
            }



            //Execute the patch request
            CloseableHttpResponse httpResponse = httpclient.execute(httpPatch);
            System.out.println("PATCH Response Status:: "
                    + httpResponse.getCode());


            try {

                // print result
                //-O --output option handled
                Header[] headers = httpResponse.getHeaders();
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
                        System.out.println("Empty Response      ");
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
                    if (GetMethod.getPosFix(outPutFile).length() == 0) {
                        outputContainer = new File(new File(".").getAbsolutePath() + "\\src\\InformationHandling\\SaveInfoBash\\" + outPutFile + posFix);
                    } else {
                        outputContainer = new File(new File(".").getAbsolutePath() + "\\src\\InformationHandling\\SaveInfoBash\\" + outPutFile);
                    }
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
                                System.out.println("empty Response    ");
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
                            System.out.println("Empty Response   ");
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
            } finally {
                httpResponse.close();
            }
        } catch (java.lang.IllegalArgumentException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL, check the spacing");
        } catch (org.apache.http.client.ClientProtocolException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL");
            if (!(patchRequest.getUrl().charAt(0) + patchRequest.getUrl().charAt(1) + patchRequest.getUrl().charAt(2) + patchRequest.getUrl().charAt(3) + patchRequest.getUrl().charAt(4)
                    + patchRequest.getUrl().charAt(5) + patchRequest.getUrl().charAt(6) + "").equals("http://")
                    && !(patchRequest.getUrl().charAt(0) + patchRequest.getUrl().charAt(1) + patchRequest.getUrl().charAt(2) + patchRequest.getUrl().charAt(3) + patchRequest.getUrl().charAt(4)
                    + patchRequest.getUrl().charAt(5) + patchRequest.getUrl().charAt(6) + patchRequest.getUrl().charAt(7) + "").equals("https://")
            ) {
                System.out.println("      "  + "URL should start with \"http://\" or \"https://\"      ");
            }
        } catch (java.net.UnknownHostException exception) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem in finding available Port, Please check Your internet connection");
        } catch (IOException exception) {
            //the methods: execute/ getContent / readLine / close
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem with writing in file ");
        } catch (NullPointerException exception) {
            System.out.println("\033[0;31m" + "Error" + "\033[0m");
        } catch (URISyntaxException e) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem with the query params");
        } finally {
            try {
                httpclient.close();
            } catch (java.lang.IllegalArgumentException exception) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL, check the spacing");
            } catch (org.apache.http.client.ClientProtocolException exception) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Invalid URL");
                if (!(patchRequest.getUrl().charAt(0) + patchRequest.getUrl().charAt(1) + patchRequest.getUrl().charAt(2) + patchRequest.getUrl().charAt(3) + patchRequest.getUrl().charAt(4)
                        + patchRequest.getUrl().charAt(5) + patchRequest.getUrl().charAt(6) + "").equals("http://")
                        && !(patchRequest.getUrl().charAt(0) + patchRequest.getUrl().charAt(1) + patchRequest.getUrl().charAt(2) + patchRequest.getUrl().charAt(3) + patchRequest.getUrl().charAt(4)
                        + patchRequest.getUrl().charAt(5) + patchRequest.getUrl().charAt(6) + patchRequest.getUrl().charAt(7) + "").equals("https://")
                ) {
                    System.out.println("      "  + "URL should start with \"http://\" or \"https://\"    ");
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


    public static void main(String[] args) {

        String url = ""+(new Scanner(System.in)).nextLine();
        Request patchRequest = new Request("nameOfRequest", TYPE.PATCH, url , MESSAGEBODY_TYPE.MULTIPART_FORM);
        ArrayList<String[]> headers = new ArrayList<>();
        String[] header1 = {"Header1", "Value1", "false"};
        String[] header2 = {"Header2", "Value2", "true"};
        String[] header3 = {"Header3", "Value3", "false"};
        headers.add(header1);
        headers.add(header2);
        headers.add(header3);
        patchRequest.setHeaderInfo(headers);
        ArrayList<String[]> body = new ArrayList<>();
        String[] data1={"message1", "body1", "true"};
        String[] data2={"message2", "body2", "true"};
        String[] data3={"message3", "body3", "true"};
        String[] data4={"message4", "body4", "true"};
        body.add(data1);
        body.add(data2);
        body.add(data3);
        body.add(data4);
        patchRequest.setFormDataInfo(body);
        MultiPartPatch postCommand = new MultiPartPatch(patchRequest);
//        postCommand.executePost("", true, true);
        postCommand.executePatch("name", true, true);
    }



}
