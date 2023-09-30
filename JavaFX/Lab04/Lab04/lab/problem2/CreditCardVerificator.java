package lab.problem2;

import java.util.Scanner;

public class CreditCardVerificator {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter a credit card number: ");

        String cc = input.next();

        char[] creditCard = cc.toCharArray();

        if (creditCard.length < 13 || creditCard.length > 16) {
            System.out.println("Card is not valid");

            return;
        }

        int firstDigit = toNumber(creditCard[0]);
        int secondDigit = toNumber(creditCard[1]);

        if (firstDigit < 4 || firstDigit > 6) {
            if (firstDigit != 3 || secondDigit != 7) {
                System.out.println("Card is not valid");

                return;
            }
        }

        int sum = 0;
        int newDigit;

        for (int i = creditCard.length - 2; i >= 0 ; i -= 2) {
            newDigit = toNumber(creditCard[i]) * 2;

            if (newDigit > 9)
                newDigit = newDigit / 10 + newDigit % 10;

            sum += newDigit;
        }

        for (int i = creditCard.length - 1; i >= 0 ; i -= 2) {
            sum += toNumber(creditCard[i]);
        }


        if (sum % 10 != 0) {
            System.out.println("Invalid credit card");

            return;
        }

        System.out.println("Valid card");

    }

    private static int toNumber(char c) {
        return Character.getNumericValue(c);
    }

}
