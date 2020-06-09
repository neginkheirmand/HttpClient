package Conection;

//import org.apache.hc.client5.http.classic.methods.HttpPost;
//import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
//import org.apache.hc.client5.http.impl.classic.HttpClients;
//import org.apache.hc.core5.http.ContentType;
//import org.apache.hc.core5.http.HttpEntity;
//
//import java.io.File;
//import java.io.IOException;
//
//public class MultiPartPost {
//    public static void main(String[] args) {
//        try {
//            CloseableHttpClient client = HttpClients.createDefault();
//            HttpPost httpPost = new HttpPost("http://apapi.haditabatabaei.ir/tests/post/formdata");
//
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            builder.addTextBody("username", "John");
//            builder.addTextBody("password", "pass");
//            builder.addBinaryBody(
//                    "file", new File("test.txt"), ContentType.APPLICATION_OCTET_STREAM, "file.ext");
//
//            HttpEntity multipart = builder.build();
//            httpPost.setEntity(multipart);
//
//            System.out.println("1");
//            CloseableHttpResponse response = client.execute(httpPost);
//            System.out.println("2");
//            client.close();
//            System.out.println("3");
////        }catch (IOException exception){
////            System.out.println("ioexception");
//        }
//    }
//}

import GUI.MESSAGEBODY_TYPE;
import GUI.Request;
import GUI.TYPE;
import org.apache.commons.logging.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

public class MultiPartPost {
    Request postRequest;

    public MultiPartPost(Request postRequest) {
        this.postRequest = postRequest;
    }

    public void multipartPostRequest(/*, String filepath, String filefield*/) throws ParseException, IOException {
        if(postRequest==null || postRequest.getUrl()==null || postRequest.getUrl().length()==0){
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

            URL url = new URL(postRequest.getUrl());
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            //the headers
            if(postRequest.getHeaderInfo()!=null && postRequest.getHeaderInfo().size()!=0){
                for(int i=0; i<postRequest.getHeaderInfo().size(); i++){
                    if(postRequest.getHeaderInfo().get(i)[2].equals("true")){
                        connection.setRequestProperty(postRequest.getHeaderInfo().get(i)[0], postRequest.getHeaderInfo().get(i)[1]);
                    }
                }
            }

            outputStream = new DataOutputStream(connection.getOutputStream());
            /*
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + name + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);


             */
            // Upload POST Data

            if(postRequest.getFormDataInfo()!=null && ((ArrayList<String[]>)postRequest.getFormDataInfo()).size()!=0) {
                for (int i = 0; i < ((ArrayList<String[]>)postRequest.getFormDataInfo()).size(); i++) {
                    if(((ArrayList<String[]>)postRequest.getFormDataInfo()).get(i)[2].equals("true")) {
                        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                        outputStream.writeBytes("Content-Disposition: form-data; name=\"" + ((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[0] + "\"" + lineEnd);
                        outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                        outputStream.writeBytes(lineEnd);
                        outputStream.writeBytes(((ArrayList<String[]>) postRequest.getFormDataInfo()).get(i)[1]);
                        outputStream.writeBytes(lineEnd);
                    }
                }

                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            }
            inputStream = connection.getInputStream();
            result = this.convertStreamToString(inputStream);

            System.out.println(connection.getResponseCode());
            System.out.println("result*"+result+"*");
//            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return ;
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
/*
    private String getName(String filepath){
        if(filepath==null || filepath.length()==0){
            return null;
        }
        String name="";
        for(int i=0 ; i<filepath.length(); i++){
            name+=filepath.charAt(i);
            if(filepath.charAt(i)=='\\'){
                name="";
            }
        }
        if(name.length()==0){
            return null;
        }
        return name;
    }

 */


    public static void main(String[] args) {
        Request postRequest = new Request("name", TYPE.POST, "http://apapi.haditabatabaei.ir/tests/post/formdata", MESSAGEBODY_TYPE.MULTIPART_FORM);
        ArrayList<String[]> formdata = new ArrayList<>();
        String[] data1 = {"key1", "Value1", "true"};
        String[] data2 = {"key2", "Value2", "true"};
        String[] data3 = {"key3", "Value3", "true"};
        String[] data4 = {"key4", "Value4", "true"};
        formdata.add(data1);
        formdata.add(data2);
        formdata.add(data3);
        formdata.add(data4);
        postRequest.setFormDataInfo(formdata);

        MultiPartPost mpp = new MultiPartPost(postRequest);
        try {
            mpp.multipartPostRequest();
        }catch (Exception exception){
            System.out.println("exception~");
        }
    }

}