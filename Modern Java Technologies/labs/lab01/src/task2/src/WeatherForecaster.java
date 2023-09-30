public class WeatherForecaster {

    public static int[] getsWarmerIn(int[] temperatures) {
        if (temperatures.length <= 1) {
            return new int[temperatures.length];
        }

        int[] valueStack = createStackArray(temperatures.length);
        int[] indexesOfValuesStack = createStackArray(temperatures.length);
        int[] result = new int[temperatures.length];

        push(valueStack, temperatures[0]);
        push(indexesOfValuesStack, 0);

        for (int i = 1; i < temperatures.length; i++) {
            int current = temperatures[i];

            while (!empty(valueStack) && current > top(valueStack)) {
                pop(valueStack);
                int poppedIndex = pop(indexesOfValuesStack);
                result[poppedIndex] = i - poppedIndex;
            }

            push(valueStack, current);
            push(indexesOfValuesStack, i);
        }

        /*
        * All remaining indexes in indexesOfValuesStack must put DEFAULT_VALUE in result[index].
        * Since DEFAULT_VALUE is 0, which is same as int's default value, no code is required.
        */

        return result;
    }

    private static void push(int[] stack, int elem) {
        int topStackIndex;
        stack[stack.length - 1]++;
        topStackIndex = stack[stack.length - 1];
        stack[topStackIndex] = elem;
    }

    private static int pop(int[] stack) {
        int popped = top(stack);
        stack[stack.length - 1]--;
        return popped;
    }

    private static int top(int[] stack) {
        int topStackIndex = stack[stack.length - 1];
        return stack[topStackIndex];
    }

    private static boolean empty(int[] stack) {
        int topStackIndex = stack[stack.length - 1];
        return topStackIndex == -1;
    }

    private static int[] createStackArray(int size) {
        int[] stack = new int[size + 1];
        stack[size] = -1;
        return stack;
    }

}