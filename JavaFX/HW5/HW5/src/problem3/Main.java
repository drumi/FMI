package problem3;

import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {

        IntStream.range(1, 101)
            .mapToObj(String::valueOf)
            .reduce((acc, el) -> acc + "#" + el)
            .ifPresent(System.out::println);
    }
}
