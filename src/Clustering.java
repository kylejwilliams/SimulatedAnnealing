
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Clustering {
    private ArrayList<ArrayList<Point>> clusters = new ArrayList<>();
    private Data data;
    private final int SEED = 27;
    private final int NUM_CLUSTERS = 3;
    private final int NUM_POINTS = 100;

    public Clustering(Data data) {
        this.data = data;
        this.data.generateDataPoints(NUM_POINTS, SEED);
    }

    public ArrayList<ArrayList<Point>> getClusters() { return clusters; }
    public void setClusters(ArrayList<ArrayList<Point>> clusters) { this.clusters = clusters; }

    public ArrayList<ArrayList<Point>> simulatedAnnealing() {
        ArrayList<ArrayList<Point>> bestSolution;

        // initialize temperature
        double temperature = 10000;
        double coolingRate = 0.003;

        // First, generate a random solution
        ArrayList<ArrayList<Point>> curSolution = generateRandomClusters(data, NUM_CLUSTERS, SEED);

        // Calculate its cost using some cost function you've defined
        int curCost = calculateCost(curSolution);

        // Repeat:
        do {
            ArrayList<ArrayList<Point>> newSolution = generateRandomClusters(data, NUM_CLUSTERS, SEED);
            int newCost = calculateCost(newSolution);

            if (newCost < curCost) bestSolution = newSolution;
            else if (acceptance(curCost, newCost, temperature) > Math.random()) bestSolution = newSolution;
            else bestSolution = curSolution;

            temperature *= (1 - coolingRate);
        } while (temperature > 1);

        return bestSolution;
    }

    // !!! Assumes centroid is located at index 0 !!!
    private static int calculateCost(ArrayList<ArrayList<Point>> clusters) {
        int cost = 0;

        for (ArrayList<Point> cluster : clusters) {
            Point centroid = cluster.get(0);
            cost += SSE(cluster, centroid);
        }

        return cost;
    }

    private static int SSE(ArrayList<Point> cluster, Point centroid) {
        int SSE = 0;

        for (Point p : cluster) {
			SSE += Math.pow(euclideanDistance(centroid, p), 2);
        }

        return SSE;
    }

    private ArrayList<ArrayList<Point>> generateRandomClusters(Data data, int K, int seed) {
        Random rand = new Random(seed);
        ArrayList<ArrayList<Point>> clusters = new ArrayList<>();
        Data dataCopy = new Data();

        for (Point p : data.getDataPoints()) dataCopy.getDataPoints().add(p);

        // create centroids
        for (int i = 0; i < K; i++) {
            int index = rand.nextInt(dataCopy.getDataPoints().size());
            ArrayList<Point> curCluster = new ArrayList<>();
            curCluster.add(dataCopy.getDataPoints().get(index));
            dataCopy.getDataPoints().remove(index);
            clusters.add(curCluster);
        }

        // Assign all points to the closest centroid
        for (Point dataPoint : dataCopy.getDataPoints()) {
            int smallestDistanceIndex = 0;
            for (int j = 0; j < clusters.size(); j++) {
                if (euclideanDistance(clusters.get(j).get(0), dataPoint) < euclideanDistance(clusters.get
                        (smallestDistanceIndex).get(0), dataPoint)) {
                    smallestDistanceIndex = j;
                }
            }
            clusters.get(smallestDistanceIndex).add(dataPoint);
        }

        return clusters;
    }

    private static double euclideanDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(Math.abs(p1.x - p2.x), 2) +
                Math.pow(Math.abs(p1.y - p2.y), 2));
    }

    private double acceptance(double solutionEnergy, double neighborEnergy, double temperature) {
        if (neighborEnergy > solutionEnergy) return 1.0;
        else return Math.exp((solutionEnergy - neighborEnergy) / temperature);
    }
}

// inter-cluster distance -> minimized
// intra-cluster distance -> maximized
