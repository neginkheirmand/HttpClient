package Conection;

import Bash.Response;
import GUI.MESSAGEBODY_TYPE;
import GUI.Request;
import GUI.TYPE;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

public class MultiPartPost {
    private Request postRequest;

    public MultiPartPost(Request postRequest) {
        this.postRequest = postRequest;
    }


    public static void bufferOutFormData(ArrayList<String[]> body, String boundary, BufferedOutputStream bufferedOutputStream) throws IOException {
        for (int i = 0; i < body.size(); i++) {
            bufferedOutputStream.write(("--" + boundary + "\r\n").getBytes());
            if (body.get(i)[0].contains("file")) {
                bufferedOutputStream.write(("Content-Disposition: form-data; filename=\"" + (new File(body.get(i)[1])).getName() + "\"\r\nContent-Type: Auto\r\n\r\n").getBytes());
                try {
                    BufferedInputStream tempBufferedInputStream = new BufferedInputStream(new FileInputStream(new File(body.get(i)[1])));
                    byte[] filesBytes = tempBufferedInputStream.readAllBytes();
                    bufferedOutputStream.write(filesBytes);
                    bufferedOutputStream.write("\r\n".getBytes());
                } catch (IOException e) {
                    System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "problem uploading the file");
                }
            } else {
                bufferedOutputStream.write(("Content-Disposition: form-data; name=\"" + body.get(i)[0] + "\"\r\n\r\n").getBytes());
                bufferedOutputStream.write((body.get(i)[1] + "\r\n").getBytes());
            }
        }
        bufferedOutputStream.write(("--" + boundary + "--\r\n").getBytes());
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }

    public static void formData(Request request, String outPutFile, boolean followRedirects, boolean showHeaders) {
        BufferedInputStream bufferedInputStream;
        try {
            Long start = new Date().getTime();

            if(request.getResponse()==null){
                request.setResponse(new Response());
            }
            if (request == null) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "invalid request");
                return;
            }
            if (request.getUrl() == null || request.getUrl().length() == 0) {
                System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "invalid url");
                return;
            }
            String query = "";
            if (request.getQueryInfo() != null && request.getQueryInfo().size() != 0) {
                query += "?";
                for (int i = 0; i < request.getQueryInfo().size(); i++) {
                    if (request.getQueryInfo().get(i)[2].equals("true")) {
                        query += request.getQueryInfo().get(i)[0] + "=" + request.getQueryInfo().get(i)[1];
                    }
                    if (i != request.getQueryInfo().size()) {
                        query += "&";
                    }
                }
            }
            URL url = new URL(request.getUrl() + query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(followRedirects) {
                connection.setInstanceFollowRedirects(true);
            }else{
                connection.setInstanceFollowRedirects(false);
            }
            String boundary = System.currentTimeMillis() + "";
            if (request.getTypeOfRequest().equals(TYPE.POST)) {
                connection.setRequestMethod("POST");
            } else {
                return;
            }
            connection.setDoOutput(true);
//            connection.setRequestProperty("accept", "");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            //the headers
            if (request.getHeaderInfo() != null && request.getHeaderInfo().size() != 0) {
                for (int i = 0; i < request.getHeaderInfo().size(); i++) {
                    if (request.getHeaderInfo().get(i)[2].equals("true")) {
                        connection.setRequestProperty(request.getHeaderInfo().get(i)[0], request.getHeaderInfo().get(i)[1]);
                    }
                }
            }

            //the auth
//            [0]= Authorization
//            [1]= the token
//            [2]= true or false
            if (request.getAuthInfo() != null && request.getAuthInfo()[0] != null && request.getAuthInfo()[1] != null && request.getAuthInfo()[2].equals("true")) {
                connection.setRequestProperty(request.getAuthInfo()[0], request.getAuthInfo()[1]);
            }

            BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
            bufferOutFormData((ArrayList<String[]>) request.getFormDataInfo(), boundary, outputStream);
            outputStream.flush();
            if(outputStream!=null){
                outputStream.close();
            }
            int statusCode = connection.getResponseCode();
            Long end = new Date().getTime();

            System.out.println("POST Response Status:: " + statusCode);

            request.getResponse().setStatusCode(statusCode);
            request.getResponse().setTimeTaken(end-start);
            Long sizeOfContent = connection.getContentLength()+ 0L;
            if(sizeOfContent==-1L){
                sizeOfContent=0L;
            }
            request.getResponse().setContentSize(sizeOfContent);
            System.out.println("the content size is :"+ request.getResponse().getContentSize());

            if(statusCode<400) {
                System.out.println("Response Headers");
                Map<String, List<String>> map = connection.getHeaderFields();
                request.getResponse().setResponseHeader(map);
                String contentType = "";
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    if(entry!=null && entry.getKey()!=null) {
                        if (showHeaders) {
                            System.out.println("Key : " + entry.getKey()
                                    + " ,Value : " + entry.getValue());
                        }
                        if (entry.getKey().contains("Content-Type")) {
                            contentType = entry.getValue().toString();
                        }
                    }
                }

                if (outPutFile == null || outPutFile.length() == 0) {
                    bufferedInputStream = new BufferedInputStream(connection.getInputStream());
                    System.out.println(new String(bufferedInputStream.readAllBytes()));
                    System.out.println(connection.getResponseCode());
                    System.out.println(connection.getHeaderFields());
                    if(bufferedInputStream!=null) {
                        bufferedInputStream.close();
                    }

                } else {
                    //if the -O option is used
                    String posFix = "";
                    if (contentType.contains("image/png")) {
                        posFix = ".png";
                    } else if (contentType.contains("text/html")) {
                        posFix = ".html";
                    } else if (contentType.contains("application/json")) {
                        posFix = ".json";
                    } else {
                        posFix = ".txt";
                    }
                    File outputContainer;
                    if (outPutFile.contains(posFix)) {
                        outputContainer = new File(new File(".").getAbsolutePath() + "\\src\\InformationHandling\\SaveInfoBash\\" + outPutFile);
                    } else {
                        outputContainer = new File(new File(".").getAbsolutePath() + "\\src\\InformationHandling\\SaveInfoBash\\" + outPutFile + posFix);
                    }

                    //the next method sees if the parents of the file exist if not creates it and returns true if already existed and false if created it
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
                                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            } catch (NullPointerException nullPointerException) {
                                System.out.println("empty Response  ");
                                return;
                            }

                            String inputLine;
                            StringBuffer response = new StringBuffer();

                            while ((inputLine = reader.readLine()) != null) {
                                response.append(inputLine + "\n");
                            }
                            if(reader!=null) {
                                reader.close();
                            }
                            FileWriter fileWriter = new FileWriter(outputContainer);
                            fileWriter.write(response.toString());
                            fileWriter.close();
                        } else {
                            System.out.println("retry again with a new name");
                        }
                    } else {

                        BufferedReader reader;
                        try {
                            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        } catch (NullPointerException nullPointerException) {
                            System.out.println("Empty Response     ");
                            return;
                        }

                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = reader.readLine()) != null) {
                            response.append(inputLine + "\n");
                        }
                        if(reader!=null) {
                            reader.close();
                        }

                        request.getResponse().setOutput(response.toString());
                        request.getResponse().setOutputContainer(true);
                        request.getResponse().setPathOutputFile(outputContainer.getAbsolutePath());
                        FileWriter fileWriter = new FileWriter(outputContainer);
                        fileWriter.write(response.toString());
                        fileWriter.close();
                    }
                }
            }else{
                bufferedInputStream = new BufferedInputStream(connection.getErrorStream());
                System.out.println(new String(bufferedInputStream.readAllBytes()));
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getHeaderFields());
                if(bufferedInputStream!=null) {
                    bufferedInputStream.close();
                }
            }
            connection.disconnect();
        } catch (java.net.MalformedURLException exc) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "incorrect url");

        } catch (ProtocolException e) {
            System.out.println("\033[0;31m" + "Error:" + "\033[0m" + "invalid request method");

        } catch (IOException e) {
            System.out.println("\033[0;31m" + "Error" + "\033[0m" + " Cannot make this request");

        }
    }


    public static void main(String[] args) {
//        Request postRequest = new Request("name", TYPE.POST, "https://webhook.site/d476bb71-fb13-4ceb-ab31-7923e71d3b18", MESSAGEBODY_TYPE.MULTIPART_FORM);
        Request postRequest = new Request("name", TYPE.POST, "http://apapi.haditabatabaei.ir/tests/post/formdata", MESSAGEBODY_TYPE.MULTIPART_FORM);
//        Request postRequest = new Request("name", TYPE.POST, "http://httpbin.org/post", MESSAGEBODY_TYPE.MULTIPART_FORM);
        ArrayList<String[]> bodyContainer = new ArrayList<>();
        String[] data1 = {"key1", "Value1", "true"};
//        String[] data2 = {"file", "C:\\Users\\venus\\Desktop\\uni\\barnameneVC pishrafte\\ProjeMid\\src\\InformationHandling\\SaveInfoBash\\hello.txt", "true"};
        String[] data3 = {"key3", "Value3", "true"};
        String[] data4 = {"key4", "Value4", "true"};
        bodyContainer.add(data1);
//        bodyContainer.add(data2);
        bodyContainer.add(data3);
        bodyContainer.add(data4);
        postRequest.setFormDataInfo(bodyContainer);


        ArrayList<String[]> headers = new ArrayList<>();
        String header1[] = {"header1", "value1", "true"};
        String header2[] = {"header2", "value2", "true"};
        String header3[] = {"header3", "value3", "true"};
        headers.add(header1);
        headers.add(header2);
        headers.add(header3);
        postRequest.setHeaderInfo(headers);

        ArrayList<String[]> queryParams = new ArrayList<>();
        String[] param1 = {"param1", "query1", "true"};
        String[] param2 = {"param2", "query2", "true"};
        queryParams.add(param1);
        queryParams.add(param2);
        postRequest.setQueryInfo(queryParams);


        formData(postRequest, "hello.txt", true, true);
    }


}