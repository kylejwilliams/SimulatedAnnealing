
/**
 * Created by Kyle on 4/24/2016.
 *
 * Description: Implement any one version of simulated annealing (SA)-based clustering. Follow the rough framework of
 * the Maulik et al. paper but use SA instead of the GA. Use 100 randomly generated two dimensional points in
 * 1.0 <= x, y <= 100.0 and K = 3. Report the final intra- and inter-cluster distances.  I encourage you to search the
 * Internet for SA pseudocode and code (but write your own code). Report results from more than one run. In your
 * report, please state your neighbor-generation mechanism, the acceptance criterion, and the annealing schedule.
 * Make your results reproducible (be careful with your use of the random number generator).
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
