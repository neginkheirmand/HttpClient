import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Run {

    public static void uploadBinary() {
        try {
            URL url = new URL("http://apapi.haditabatabaei.ir/tests/post/binary");
            File haditabatabaei = new File("haditabatabaei.txt");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(connection.getOutputStream());
            BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(haditabatabaei));
            bufferedOutputStream.write(fileInputStream.readAllBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
            System.out.println(new String(bufferedInputStream.readAllBytes()));
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getHeaderFields());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        uploadBinary();
//        formData();
    }
}