package Bash;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        Options newOptionIdentifier = new Options();

        while (true) {
            String input = (new Scanner(System.in)).nextLine();
            System.out.println("\"" + input + "\"");
            new Command(input);
        }
//        System.out.println("hello thereee");
//        Command command1 = new Command("hello thereee");
//        System.out.println("          -----------------------             ");
//        System.out.println(" what are u doing ??");
//        Command command2 = new Command(" what are u doing ??");
//        System.out.println("          -----------------------             ");
//        System.out.println(" curl url u doing ??");
//        Command command3 = new Command(" curl url u doing ??");
//        System.out.println("          -----------------------             ");
//        System.out.println(" curl list u doing ??");
//        Command command4 = new Command(" curl list u doing ??");
//        System.out.println("          -----------------------             ");
//        System.out.println(" curl fire u doing ??");
//        Command command5 = new Command(" curl fire u doing ??");
//        System.out.println("          -----------------------             ");
//        System.out.println(" curl list");
//        Command command6 = new Command(" curl list");
//        System.out.println("          -----------------------             ");
//        System.out.println(" curl fire 1 ");
//        Command command8 = new Command(" curl fire 1 ");
//        System.out.println("          -----------------------             ");
    }
}
