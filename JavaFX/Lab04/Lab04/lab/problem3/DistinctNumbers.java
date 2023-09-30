package lab.problem3;

import java.util.ArrayList;
import java.util.Scanner;

public class DistinctNumbers {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Integer> distinct = new ArrayList<>();

        System.out.println("Въведи поредица от числа, завършваща с 0: ");

        int number;

        do {
            number = scanner.nextInt();

            if (!distinct.contains(number))
                distinct.add(number);

        } while (number != 0);

        distinct.remove(distinct.size() - 1);

        System.out.println("Недублиращи се числа: " + distinct);
    }

}
