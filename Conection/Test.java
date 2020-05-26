package Conection;

import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        System.out.println("to trigger command use keyword \"curl\"  ");
        Options newOptionIdentifier = new Options();

        newOptionIdentifier.identifyRequestOptions("   curl  -i  --help  honeey !         ");

    }
}
