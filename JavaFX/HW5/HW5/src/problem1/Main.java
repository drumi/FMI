package problem1;

public class Main {

    public static void main(String[] args) {
        MonteCarloEggScenario scenario = new MonteCarloEggScenario(1000);

        double average = scenario.runSimulations();

        System.out.printf("Average cost: %.2f", average);
    }

}
