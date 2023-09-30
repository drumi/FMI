package lab.problem1;

import java.util.Scanner;

public class CaesarCipherTest {

    private final static int SHIFT_LENGTH = 11;

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        CaesarCipher cipher = new CaesarCipher(SHIFT_LENGTH);

        int userChoice;

        do {
            System.out.print("Enter 1 to encode or 2 to decode: ");
            userChoice = Integer.parseInt(scanner.nextLine());
        } while (userChoice != 1 && userChoice != 2);


        if (userChoice == 1) {
            System.out.println("Enter text to encode: ");

            String text = scanner.nextLine();

            System.out.println("Encoded text: ");
            System.out.println(cipher.encode(text));
        }

        if (userChoice == 2) {
            System.out.println("Enter text to decode: ");

            String text = scanner.nextLine();

            System.out.println("Decoded text: ");
            System.out.println(cipher.decode(text));
        }

    }

}
