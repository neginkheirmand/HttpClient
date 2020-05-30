package Bash;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        Options newOptionIdentifier = new Options();

        /*
        while (true) {
            String input = (new Scanner(System.in)).nextLine();
            System.out.println("\"" + input + "\"");
            new Command(input);
        }
        */
        for(int i=0; ; i++){
//            if(i%2==0) {
//                System.out.println("massage body separated with &");
//                String input = (new Scanner(System.in)).nextLine();
//                ArrayList<String[]> data = new Command().splitData(input);
//                if(data==null){
//                    System.out.println("nul :|");
//                }else{
//                    System.out.println("the size of the arraylist is ="+data.size());
//                    for(int k=0; k<data.size(); k++) {
//                        for (int j = 0; j < 3; j++) {
//                            System.out.printf( data.get(k)[j]+"  ");
//                        }
//                        System.out.println();
//                    }
//                }

//            }else{
                System.out.println("headers separated with ;");
                String input = (new Scanner(System.in)).nextLine();
                ArrayList<String[]> headers = new Command().splitHeader(input);
                if(headers==null){
                    System.out.println("nul :|");
                }else{
                    for(int k=0; k<headers.size(); k++) {
                        System.out.printf( headers.get(k)[0]+"  "+headers.get(k)[1]+"  "+headers.get(k)[2]+"  ");
                        System.out.println();
                    }
                }
//
//            }
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
