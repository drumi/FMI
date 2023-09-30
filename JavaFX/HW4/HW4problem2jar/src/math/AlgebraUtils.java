package math;

public class AlgebraUtils {

    private AlgebraUtils() {
    }

    public static double[][] matrixMultiply(double[][] a, double[][] b) {
        int firstRows = a.length;
        int firstCols = a[0].length;
        int secondRows = b.length;
        int secondCols = b[0].length;

        if (firstRows!= secondCols)
            return null;

        double[][] result = new double[firstRows][secondCols];

        for (int i = 0; i < firstRows; i++) {
            for (int j = 0; j < secondCols; j++) {
                result[i][j] = dotProduct(a, b, i, j);
            }
        }

        return result;
    }

    private static double dotProduct(double[][] a, double[][] b, int rowIndex, int colIndex) {
        double result = 0.0;

        for (int i = 0; i < a.length; i++)
            result += a[rowIndex][i] * b[i][colIndex];

        return result;
    }

}
