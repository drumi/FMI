package problem1;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MonteCarloEggScenario {

    private int numberOfSimulations;
    private EggMachine eggMachine = new EggMachine();

    public MonteCarloEggScenario(int numberOfSimulations) {
        setNumberOfSimulations(numberOfSimulations);
    }

    public double runSimulations() {
        var data = new ArrayList<Integer>();

        for (int i = 0; i < numberOfSimulations; i++) {
            data.add(getAllEggs());
        }

        return data.stream().mapToInt(Integer::intValue).average().getAsDouble() / 100;
    }

    public int getNumberOfSimulations() {
        return numberOfSimulations;
    }

    private int getAllEggs() {
        Set<Integer> eggs = new TreeSet<>();
        var eggMachine = new EggMachine();

        int cost = 0;
        while (eggs.size() != 10) {
            cost += 50;
            eggs.add(eggMachine.generateEgg());
        }

        return cost;
    }

    private void setNumberOfSimulations(int numberOfSimulations) {
        if (numberOfSimulations > 0)
            this.numberOfSimulations = numberOfSimulations;
        else
            this.numberOfSimulations = 10;
    }
}
