package Bash;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;


public class Test {

    public static void main(String[] args) {
        Options newOptionIdentifier = new Options();

        while (true) {
            String input = (new Scanner(System.in)).nextLine();
            new Command(input);
        }


//        while (true) {
//
//        }


    }
}
