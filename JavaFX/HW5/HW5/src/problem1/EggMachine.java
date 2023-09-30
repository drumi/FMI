package problem1;

import java.util.Random;

public class EggMachine {

    private Random random = new Random();

    public int generateEgg() {
        return random.nextInt(10);
    }

}
