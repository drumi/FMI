import java.util.Scanner;

public class RouteCipherTest {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Enter key: ");
        int key = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter 1 to encrypt and 2 to decrypt: ");
        int choice;

        do {
            choice = Integer.parseInt(scanner.nextLine());
        } while (choice != 1 && choice != 2);

        System.out.print("Enter text: ");
        String text = scanner.nextLine();

        RouteCipher cipher = new RouteCipher(key);

        if (choice == 1) {
            System.out.println("Encrypted text: " + cipher.encrypt(text));
        } else {
            System.out.println("Decrypted text: " + cipher.decrypt(text));
        }
    }

}
