package problem1;

public class SortOrder {

    public Sortable getUpward() {
        return new Upward();
    }

    public Sortable getDownward() {
        return new Downward();
    }

    private class Upward implements Sortable {

        @Override
        public boolean greater(int a, int b) {
            return a > b;
        }

    }


    private static class Downward implements Sortable {

        @Override
        public boolean greater(int a, int b) {
            return a < b;
        }

    }

}
