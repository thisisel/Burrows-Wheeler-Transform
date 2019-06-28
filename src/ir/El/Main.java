package ir.El;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {

        /*     encode      */
        System.out.println("enter your string to be encoded: ");

        Scanner scanner = new Scanner(System.in);
        String string = scanner.next();

        BurrowsWheelerTransform bwt = new BurrowsWheelerTransform();
        bwt.setInputStr(string);

        System.out.println("here is the transform: " + bwt.getEncodedOutput());


        /*     decode      */
        System.out.println("enter your string to be decoded: ");
        String string2 = scanner.next();

        BurrowsWheelerTransform bwt2 = new BurrowsWheelerTransform();
        bwt2.setInputStr(string2);

        System.out.println("Here is the decoded  text: ");
        System.out.println(String.copyValueOf(bwt2.getDecodedOutPut()));


    }
}
