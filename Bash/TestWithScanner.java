package Bash;

import java.util.Scanner;


public class TestWithScanner {

    public static void main(String[] args) {
        Options newOptionIdentifier = new Options();

        while (true) {
            String input="";
            try {
                input = (new Scanner(System.in)).nextLine();
            }catch (java.lang.IndexOutOfBoundsException exception){
                System.out.println("Please enter valid input");
            }
            new Command(input);
        }



    }
}
