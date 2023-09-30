package problem4;

import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonteCarlo {

    public static void main(String[] args) {

        Random random = new Random();

        var result = Stream.generate(() -> random.nextInt(1, 7))
            .limit(6_000_000)
            .collect(
                Collectors.groupingBy(
                    Integer::intValue,
                    TreeMap::new,
                    Collectors.counting()
                )
            );

        System.out.println("Face Frequency");
        for (var entry : result.entrySet()) {
            System.out.printf("%d    %d%n", entry.getKey(), entry.getValue());
        }
    }
}
