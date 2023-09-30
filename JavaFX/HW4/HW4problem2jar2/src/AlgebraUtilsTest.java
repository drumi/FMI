import math.AlgebraUtils;

import java.util.Scanner;

public class AlgebraUtilsTest {

    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        double[][] a = new double[3][3];
        double[][] b = new double[3][3];

        System.out.print("Enter matrix1 3x3: ");
        acquireInput(a);
        System.out.print("Enter matrix2 3x3: ");
        acquireInput(b);

        double[][] c = AlgebraUtils.matrixMultiply(a, b);

        printMultiplication(a, b, c);
    }

    private static void acquireInput(double[][] a) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                a[i][j] = input.nextDouble();
            }
        }
    }

    private static void printMultiplication(double[][] a, double[][] b, double[][] c) {
        print(a[0], "    ");
        print(b[0], "    ");
        print(c[0], "\n");

        print(a[1], " *  ");
        print(b[1], " =  ");
        print(c[1], "\n");

        print(a[2], "    ");
        print(b[2], "    ");
        print(c[2], "\n");
    }

    private static void print(double[] a, String end) {

        for (double d : a) {
            System.out.printf("%.2f ", d);
        }

        System.out.print(end);
    }
}
