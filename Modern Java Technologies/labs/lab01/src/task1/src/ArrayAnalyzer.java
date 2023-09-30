public class ArrayAnalyzer {

    public static boolean isMountainArray(int[] array) {
        if (array.length < 3) {
            return false;
        } 

        int idx = array.length;

        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] == array[i + 1]) {
                return false;
            }
            if (array[i] > array[i + 1]) {
                idx = i;
                break;
            }
        }

        for (int i = idx; i < array.length - 1; i++) {
            if (array[i] <= array[i + 1]) {
                return false;
            }
        }

        return true;
    }

}