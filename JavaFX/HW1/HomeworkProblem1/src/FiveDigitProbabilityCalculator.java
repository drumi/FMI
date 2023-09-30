public class FiveDigitProbabilityCalculator {

    // Specified ranges for the five-digit number
    private static final int FIRST_DIGIT_START_INTERVAL = 3;
    private static final int FIRST_DIGIT_END_INTERVAL = 9;

    private static final int SECOND_DIGIT_START_INTERVAL = 2;
    private static final int SECOND_DIGIT_END_INTERVAL = 8;

    private static final int THIRD_DIGIT_START_INTERVAL = 4;
    private static final int THIRD_DIGIT_END_INTERVAL = 9;

    private static final int FOURTH_DIGIT_START_INTERVAL = 1;
    private static final int FOURTH_DIGIT_END_INTERVAL = 6;

    private static final int FIFTH_DIGIT_START_INTERVAL = 6;
    private static final int FIFTH_DIGIT_END_INTERVAL = 9;

    private static final int DIVIDEND = 12;

    private static int createFiveDigitNumber(int first, int second, int third, int fourth, int fifth) {
        return first * 10_000 +
                second * 1_000 +
                third * 100 +
                fourth * 10 +
                fifth;
    }

    // Counts all numbers that are divisible by the dividend, with digits in the specified ranges
    public static int countAllNumbersInRangeThatAreDivisibleByTheDividend() {
        int numbersDivisibleByTheDividend = 0;

        for (int i = FIRST_DIGIT_START_INTERVAL; i <= FIRST_DIGIT_END_INTERVAL; i++)
            for (int j = SECOND_DIGIT_START_INTERVAL; j <= SECOND_DIGIT_END_INTERVAL; j++)
                for (int k = THIRD_DIGIT_START_INTERVAL; k <= THIRD_DIGIT_END_INTERVAL; k++)
                    for (int l = FOURTH_DIGIT_START_INTERVAL; l <= FOURTH_DIGIT_END_INTERVAL; l++)
                        for (int m = FIFTH_DIGIT_START_INTERVAL; m <= FIFTH_DIGIT_END_INTERVAL; m++) {
                            int currentNumber = createFiveDigitNumber(i,j,k,l,m);

                            if (currentNumber % DIVIDEND == 0)
                                numbersDivisibleByTheDividend++;
                        }

        return numbersDivisibleByTheDividend;
    }

    // Counts all numbers with digits in the specified ranges
    public static int countAllNumbersInRange() {
        int totalNumbersCount = 0;

        for (int i = FIRST_DIGIT_START_INTERVAL; i <= FIRST_DIGIT_END_INTERVAL; i++)
            for (int j = SECOND_DIGIT_START_INTERVAL; j <= SECOND_DIGIT_END_INTERVAL; j++)
                for (int k = THIRD_DIGIT_START_INTERVAL; k <= THIRD_DIGIT_END_INTERVAL; k++)
                    for (int l = FOURTH_DIGIT_START_INTERVAL; l <= FOURTH_DIGIT_END_INTERVAL; l++)
                        for (int m = FIFTH_DIGIT_START_INTERVAL; m <= FIFTH_DIGIT_END_INTERVAL; m++)
                            totalNumbersCount++;

        return totalNumbersCount;
    }

    // Calculates the probability that a five-digit number, with digits in the specified ranges, is divisible by the dividend
    public static double calculateProbability() {
        double allNumbers = countAllNumbersInRange();
        double numbersDivisibleByTheDividend = countAllNumbersInRangeThatAreDivisibleByTheDividend();

        return numbersDivisibleByTheDividend / allNumbers;
    }

    // Counts all five-digit number that are divisible by the dividend
    public static int countFiveDigitNumbersThatAreDivisibleByTheDividend() {
        int totalNumbersDivisibleByTheDividend = 0;

        for (int i = 1; i <= 9; i++)
            for (int j = 0; j <= 9; j++)
                for (int k = 0; k <= 9; k++)
                    for (int l = 0; l <= 9; l++)
                        for (int m = 0; m <= 9; m++) {
                            int currentNumber = createFiveDigitNumber(i,j,k,l,m);

                            if (currentNumber % DIVIDEND == 0)
                                totalNumbersDivisibleByTheDividend++;
                        }

        return totalNumbersDivisibleByTheDividend;
    }

    public static void main(String[] args) {
        double probability = calculateProbability();
        int countOfAllNumbersDivisibleByTwelve = countFiveDigitNumbersThatAreDivisibleByTheDividend();
        int countOfAllNumbersWithFiveDigitsInRange = countAllNumbersInRange();
        int countAllNumbersInRangeThatAreDivisibleByTwelve= countAllNumbersInRangeThatAreDivisibleByTheDividend();

        System.out.printf(
                "Probability of number with the specified qualities to be divisible by 12: %.2f%n" +
                "Count of all five-digit numbers that are divisible by 12: %d%n" +
                "Count of all five-digit numbers that are in the specified range: %d%n" +
                "Count of all five-digit numbers with the specified qualities that are divisible by 12: %d%n",
                probability,
                countOfAllNumbersDivisibleByTwelve,
                countOfAllNumbersWithFiveDigitsInRange,
                countAllNumbersInRangeThatAreDivisibleByTwelve
        );
    }
}