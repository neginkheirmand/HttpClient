package Bash;
import java.util.Scanner;

public class TestWithCommandLine {


    public static void main(String[] args) {
        Options newOptionIdentifier = new Options();

        String input = "";
        for (int i = 0; i < args.length; i++) {
            input += args[i] + " ";
        }
        System.out.println(input);
        new Command(input);


    }
}