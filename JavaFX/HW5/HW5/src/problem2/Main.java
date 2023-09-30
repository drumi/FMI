package problem2;

import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
       var list = Stream.of("aBc", "d", "ef", "123456")
                         .map(String::toUpperCase)
                         .sorted()
                         .toList();

        for (String s : list) {
            System.out.println(s);
        }
    }
}
