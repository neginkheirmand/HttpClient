package Conection;

import Bash.Response;
import GUI.MESSAGEBODY_TYPE;
import GUI.Request;
import GUI.TYPE;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MultiPartPut {
    Request putRequest;

    public MultiPartPut(Request putRequest) {
        this.putRequest = putRequest;
    }

    public void multipartPutRequest(String outPutFile, boolean followRedirect, boolean showresponseHeaders){
        if (putRequest == null || putRequest.getUrl() == null || putRequest.getUrl().length() == 0) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + " Problem with the request, try again");
            return;
        }
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;

//        String name = getName(filepath);
        try {
//            File file = new File(filepath);
//            FileInputStream fileInputStream = new FileInputStream(file);

            String urlStr = putRequest.getUrl() + "?";
            if (putRequest.getQueryInfo() != null && putRequest.getQueryInfo().size() != 0) {
                for (int i = 0; i < putRequest.getQueryInfo().size(); i++) {
                    if (putRequest.getQueryInfo().get(i)[2].equals("true")) {
                        urlStr += putRequest.getQueryInfo().get(i)[0] + "=" + putRequest.getQueryInfo().get(i)[1];
                    }
                    if (i != putRequest.getQueryInfo().size() - 1) {
                        urlStr += "&";
                    }
                }
            }
            URL url = new URL(urlStr);


            connection = (HttpURLConnection) url.openConnection();
            connection.setFollowRedirects(followRedirect);

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            //the headers
            if (putRequest.getHeaderInfo() != null && putRequest.getHeaderInfo().size() != 0) {
                for (int i = 0; i < putRequest.getHeaderInfo().size(); i++) {
                    if (putRequest.getHeaderInfo().get(i)[2].equals("true")) {
                        connection.setRequestProperty(putRequest.getHeaderInfo().get(i)[0], putRequest.getHeaderInfo().get(i)[1]);
                    }
                }
            }

            //the auth
            if (putRequest.getAuth() && putRequest.getAuthInfo() != null && (putRequest.getAuthInfo()[2]).equals("true")) {
                connection.setRequestProperty(putRequest.getAuthInfo()[0], putRequest.getAuthInfo()[1]);

            }

            outputStream = new DataOutputStream(connection.getOutputStream());

            if (putRequest.getFormDataInfo() != null && ((ArrayList<String[]>) putRequest.getFormDataInfo()).size() != 0) {
                for (int i = 0; i < ((ArrayList<String[]>) putRequest.getFormDataInfo()).size(); i++) {
                    if (((ArrayList<String[]>) putRequest.getFormDataInfo()).get(i)[2].equals("true")) {
                        if(((ArrayList<String[]>)putRequest.getFormDataInfo()).get(i)[0].contains("file")){
                            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                            outputStream.write(("Content-Disposition: form-data; filename=\"" + (new File(((ArrayList<String[]>)putRequest.getFormDataInfo()).get(i)[1])).getName() + "\"\r\nContent-Type: Auto\r\n\r\n").getBytes());
                            try {
                                BufferedInputStream tempBufferedInputStream = new BufferedInputStream(new FileInputStream(new File( ((ArrayList<String[]>)putRequest.getFormDataInfo()).get(i)[1])));
                                byte[] filesBytes = tempBufferedInputStream.readAllBytes();
                                outputStream.write(filesBytes);
                                outputStream.write("\r\n".getBytes());
                            } catch (IOException e) {
                                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "problem uploading the file");
                            }
                        }else {
                            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + ((ArrayList<String[]>) putRequest.getFormDataInfo()).get(i)[0] + "\"" + lineEnd);
                            outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                            outputStream.writeBytes(lineEnd);
                            outputStream.writeBytes(((ArrayList<String[]>) putRequest.getFormDataInfo()).get(i)[1]);
                            outputStream.writeBytes(lineEnd);
                        }
                    }
                }

                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            }
            inputStream = connection.getInputStream();
            result = this.convertStreamToString(inputStream);

            System.out.println();
            Response requestResponse = new Response(null, "", true, "", 0);
            if (outPutFile == null || outPutFile.length() == 0) {
                requestResponse.setOutputContainer(false);
            } else {
                requestResponse.setOutputContainer(true);
            }
            int status = connection.getResponseCode();
            System.out.println("PUT Response Status::  " + status);
            requestResponse.setStatusCode(status);


            //-i option
            if (showresponseHeaders) {
                Map<String, List<String>> map = connection.getHeaderFields();
                requestResponse.setResponseHeader(map);

                for (String key : map.keySet()) {
                    System.out.println(key + ": ");

                    List<String> values = map.get(key);

                    for (String aValue : values) {
                        System.out.println(aValue);
                    }
                }
            }

            requestResponse.setOutput(result);
            if (outPutFile == null || outPutFile.length() == 0) {

                if (result.length() == 0) {
                    System.out.println(" empty response");
                } else {
                    System.out.println(result);
                }
            } else {
                File outputContainer;
                if ((outPutFile.charAt(outPutFile.length() - 4) + outPutFile.charAt(outPutFile.length() - 3) + outPutFile.charAt(outPutFile.length() - 2) +
                        outPutFile.charAt(outPutFile.length() - 1) + "").equals(".txt")) {
                    outputContainer = new File(new File(".").getAbsolutePath() + "\\src\\InformationHandling\\SaveInfoBash\\" + outPutFile);
                } else {
                    outputContainer = new File(new File(".").getAbsolutePath() + "\\src\\InformationHandling\\SaveInfoBash\\" + outPutFile + ".txt");
                }

                requestResponse.setPathOutputFile(outputContainer.getAbsolutePath());

                //the next method sees if the parenrs of the file exist if not creates it and returnts true if already existed and false if created it
                outputContainer.getParentFile().mkdirs();
                if (!outputContainer.createNewFile()) {
                    System.out.println("\033[0;31m" + "Error: " + "\033[0m" + "unable to create output file");
                    System.out.println("A file with this name already exist, do you want to over-write on it? <Y/n> ");
                    String overWrite = "" + (new Scanner(System.in)).nextLine();

                    while (!overWrite.equals("Y") && !overWrite.equals("n")) {
                        overWrite = "" + (new Scanner(System.in)).nextLine();
                    }

                    if (overWrite.equals("Y")) {
                        FileWriter fileWriter = new FileWriter(outputContainer);
                        fileWriter.write(result.toString());
                        fileWriter.close();
                    } else {
                        System.out.println("retry again with a new name");
                    }
                } else {

                    FileWriter fileWriter = new FileWriter(outputContainer);
                    fileWriter.write(result.toString());
                    fileWriter.close();
                }
            }


//            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            putRequest.setResponse(requestResponse);
            return;
        } catch (Exception e) {
//            Log.e("MultipartRequest", "Multipart Form Upload Error");
            e.printStackTrace();
            System.out.println("error");
            return;
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Request putRequest = new Request("name", TYPE.PUT, "https://webhook.site/d476bb71-fb13-4ceb-ab31-7923e71d3b18", MESSAGEBODY_TYPE.MULTIPART_FORM);
//        Request putRequest = new Request("name", TYPE.PUT, "http://apapi.haditabatabaei.ir/tests/put/formdata/100", MESSAGEBODY_TYPE.MULTIPART_FORM);
        ArrayList<String[]> formdata = new ArrayList<>();
        String[] data1 = {"key1", "Value1", "true"};
        String[] data2 = {"file", "C:\\Users\\venus\\Desktop\\uni\\barnameneVC pishrafte\\ProjeMid\\src\\InformationHandling\\SaveInfoBash\\hello.txt", "true"};
        String[] data3 = {"key3", "Value3", "true"};
        String[] data4 = {"key4", "Value4", "true"};
        formdata.add(data1);
        formdata.add(data2);
        formdata.add(data3);
        formdata.add(data4);
        putRequest.setFormDataInfo(formdata);


        ArrayList<String[]> headers = new ArrayList<>();
        String header1[] = {"header1", "value1", "true"};
        String header2[] = {"header2", "value2", "true"};
        String header3[] = {"header3", "value3", "true"};
        headers.add(header1);
        headers.add(header2);
        headers.add(header3);
        putRequest.setHeaderInfo(headers);

        ArrayList<String[]> queryParams = new ArrayList<>();
        String[] param1 = {"param1", "query1", "true"};
        String[] param2 = {"param2", "query2", "true"};
        queryParams.add(param1);
        queryParams.add(param2);
        putRequest.setQueryInfo(queryParams);

        MultiPartPut mpp = new MultiPartPut(putRequest);
        try {
            mpp.multipartPutRequest("output", true, true);
        } catch (Exception exception) {
            System.out.println("exception~");
        }
    }

}