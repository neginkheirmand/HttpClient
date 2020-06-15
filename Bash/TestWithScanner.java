package Bash;

import java.util.Scanner;


public class TestWithScanner {

    public static void main(String[] args) {
        Options newOptionIdentifier = new Options();
        Command.loadIndo();
        while(true) {
            String input="";
            try {
                input = (new Scanner(System.in)).nextLine();
                if(input.equals("curl close") || input.equals("curl exit")){
                    break;
                }
                new Command(input, false);
            }catch (java.lang.IndexOutOfBoundsException exception){
                System.out.println("Please enter valid input");
            }
            Command.saveInfo();
        }
    }
}
