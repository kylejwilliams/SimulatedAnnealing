
/**
 * Created by Kyle on 4/24/2016.
 */
public class Main {
    private static int numIter = 100;
    private static String output = "";

    public static void main(String[] args) {
        for (int iter = 0; iter < numIter; iter++) {
            output = "";

            if (iter % 3 == 0) output += "Fizz";
            if (iter % 5 == 0) output += "Buzz";

            System.out.println(iter + ": " + output);
        }
    }
}
