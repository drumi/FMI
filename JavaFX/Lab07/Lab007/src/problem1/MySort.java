package problem1;

public class MySort {

    private Sortable callback;

    public MySort() {
        this(new Sortable() {
            @Override
            public boolean greater(int a, int b) {
                return a > b;
            }
        });
    }

    public MySort(Sortable callback) {
        setCallback(callback);
    }

    public Sortable getCallback() {
        return callback;
    }

    public void setCallback(Sortable callback) {
        this.callback = callback != null ? callback : new Sortable() {
            @Override
            public boolean greater(int a, int b) {
                return a > b;
            }
        };
    }

    public void sort(int[] numbers) {
        int swap;

        for (int i = 0; i < numbers.length - 1; i++) {
            for (int j = 0; j < numbers.length - 1 - i; j++) {
                if (callback.greater(numbers[j], numbers[j + 1])) {
                    swap = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = swap;
                }
            }
        }
    }

}
