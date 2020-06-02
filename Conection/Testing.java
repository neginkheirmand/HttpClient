package Conection;

import java.io.IOException;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;



public class Testing {



    public static void main(String[] args) {


        //Create an HttpClient object
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try{
            //Create an HttpGet object
            HttpGet httpget = new HttpGet("http://www.tutorialspoint.com/");

            //Execute the Get request
            CloseableHttpResponse httpresponse = httpclient.execute(httpget);

            try{
                Scanner sc = new Scanner(httpresponse.getEntity().getContent());
                while(sc.hasNext()) {
                    System.out.println(sc.nextLine());
                }
            }finally{
                httpresponse.close();
            }
        }
        catch(ClientProtocolException hello) {
            System.out.println("1");
        }catch (IOException except){
            System.out.println("2");
        }finally {
            try {

                httpclient.close();
            }catch (IOException exception){
                System.out.println(3);
            }
        }



    }



}


