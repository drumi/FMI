package problem1;

import java.util.Arrays;
import java.util.Random;

public class UserSort {

    public static void main(String[] args) {
        Random random = new Random();

        int[] numbers = new int[random.nextInt(16) + 15];

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(-100, 100);
        }

        SortOrder sortOrder = new SortOrder();
        Sortable upward = sortOrder.getUpward();
        Sortable downward = sortOrder.getDownward();

        MySort mySortUpward = new MySort(upward);
        MySort mySortDownward = new MySort(downward);

        System.out.println("Unsorted: ");
        System.out.println(Arrays.toString(numbers));

        System.out.println("Sorted upward: ");
        mySortUpward.sort(numbers);
        System.out.println(Arrays.toString(numbers));

        System.out.println("Sorted downward: ");
        mySortDownward.sort(numbers);
        System.out.println(Arrays.toString(numbers));

    }

}
