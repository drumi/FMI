package problem2;

import java.util.Random;

public class Sequence {

    private Object[] obs;
    private int next = 0;
    public Sequence(int size) {
        obs = new Object[size];
    }

    public void add(Object x) {
        if(next < obs.length) {
            obs[next] = x;
            next++;
        }
    }


    private class SselectorForward implements SelectorForward {

        private int i = 0;
        public boolean end() {
            return i == obs.length;
        }
        public Object current() {
            return obs[i];
        }
        public void next() {
            if(i < obs.length) i++;
        }
    } // end of inner class
    public SelectorForward getSelectorForward() {
        return new SselectorForward();
    }


    private class SselectorBackward implements SelectorBackward {

        private int i = obs.length - 1;

        public boolean begin() {
            return i == -1;
        }
        public Object current() {
            return obs[i];
        }
        public void previous() {
            if(i > -1) i--;
        }
    } // end of inner class

    private class SselectorTwoWay implements SelectorTwoWay {

        private int i;

        public SselectorTwoWay(int i) {
            this.i = i;
        }

        public boolean begin() {
            return i == -1;
        }

        public Object current() {
            return obs[i];
        }

        public void previous() {
            if(i > -1) i--;
        }

        public boolean end() {
            return i == obs.length;
        }

        public void next() {
            if(i < obs.length) i++;
        }

    } // end of inner class

    public SelectorTwoWay getSelectorTwoWay() {
        return new SselectorTwoWay(0);
    }

    public SelectorBackward getSelectorBackward() {
        return new SselectorBackward();
    }

    public static void main(String[] args) {
        final int SIZE = 8;

        Sequence sequence = new Sequence(SIZE);

        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            sequence.add(random.nextInt(10, 101));
        }

        System.out.println("First to last: ");
        SelectorForward forward = sequence.getSelectorForward();
        while (!forward.end()) {
            System.out.println(forward.current());
            forward.next();
        }

        System.out.println("\nLast to first: ");
        SelectorBackward backward = sequence.getSelectorBackward();
        while (!backward.begin()) {
            System.out.println(backward.current());
            backward.previous();
        }

        System.out.println("\nTwo way to finish: ");
        SelectorTwoWay twoWay = sequence.getSelectorTwoWay();
        while (!twoWay.end()) {
            System.out.println(twoWay.current());
            twoWay.next();
        }

        twoWay.previous();
        System.out.println("\nTwo way to start: ");
        while (!twoWay.begin()) {
            System.out.println(twoWay.current());
            twoWay.previous();
        }
    }

}
