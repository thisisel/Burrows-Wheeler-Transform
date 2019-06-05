package ir.El;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        System.out.println("enter your string: ");

        Scanner scanner = new Scanner(System.in);
        String string = scanner.next();

        BurrowsWheelerTransform bwt = new BurrowsWheelerTransform();
        bwt.setInputStr(string);

        System.out.println("here is the transform: " + bwt.getEncodedOutput());

    }
}
