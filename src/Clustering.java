
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Clustering {
    private ArrayList<ArrayList<Point>> clusters = new ArrayList<>();
    private Data data;
    private final int SEED = 333;
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
        ArrayList<ArrayList<Point>> curSolution = generateRandomClusters(data, NUM_CLUSTERS);

        // print some stats
        displayStats(curSolution);

        // set as best solution
        bestSolution = curSolution;

        // Repeat:
        while (temperature > 1) {
            // generate random neighboring solution
            ArrayList<ArrayList<Point>> newSolution = generateNewSolution(curSolution);

            // calculate costs
            int newCost = calculateCost(newSolution);
            int curCost = calculateCost(curSolution);
            int bestCost = calculateCost(bestSolution);

            // randomly accept a worse cost
            if (acceptance(curCost, newCost, temperature) > Math.random()) curSolution = newSolution;
            else bestSolution = curSolution;

            // check if we found a better solution
            if (curCost < bestCost) bestSolution = curSolution;

            temperature *= (1 - coolingRate);
        }

        // display resulting stats
        displayStats(bestSolution);
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

    private ArrayList<ArrayList<Point>> generateRandomClusters(Data data, int K) {
        Random rand = new Random(SEED);
        ArrayList<ArrayList<Point>> clusters = new ArrayList<>();
        Data dataCopy = new Data(new ArrayList<>(data.getDataPoints()));

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

    private ArrayList<ArrayList<Point>> generateNewSolution(ArrayList<ArrayList<Point>> solution) {
        Random rand = new Random(SEED);
        ArrayList<Point> centroids = new ArrayList<>();
        ArrayList<Point> dataPoints = new ArrayList<>();
        ArrayList<ArrayList<Point>> newSolution = new ArrayList<>();


        // separate centroids into one array and all other points into another
        for (ArrayList<Point> cluster : solution) {
            for (Point p : cluster) {
                if (cluster.indexOf(p) == 0 && !centroids.contains(p)) {
                    centroids.add(p);
                }
                else dataPoints.add(p);
            }
        }

        int randCentroidIndex = rand.nextInt(centroids.size());
        int randDataPointIndex = rand.nextInt(centroids.size());

        // select one centroid at random and exchange it with a random other point from the second array
        Point tmpCentroid = new Point(centroids.get(randCentroidIndex));
        Point tmpDataPoint = new Point(dataPoints.get(randDataPointIndex));

        centroids.set(randCentroidIndex, tmpDataPoint);
        dataPoints.set(randDataPointIndex, tmpCentroid);

        ArrayList<Point> tmp = new ArrayList<>();
        for (Point p : centroids) {
            tmp.add(p);
            newSolution.add(tmp);
            tmp = new ArrayList<>();
        }

        // assign each point to it's nearest centroid
        for (Point p : dataPoints) {
            int smallestDistanceIndex = 0;
            for (ArrayList<Point> c : newSolution) {
                if (euclideanDistance(p, c.get(0)) <
                        euclideanDistance(p, newSolution.get(smallestDistanceIndex).get(0)))
                    smallestDistanceIndex = newSolution.indexOf(c);
            }
            newSolution.get(smallestDistanceIndex).add(p);
        }

        return newSolution;
    }

    private static double euclideanDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(Math.abs(p1.x - p2.x), 2) +
                Math.pow(Math.abs(p1.y - p2.y), 2));
    }

    private double acceptance(double solutionEnergy, double neighborEnergy, double temperature) {
        if (neighborEnergy < solutionEnergy) return 1.0;
        else return Math.exp((solutionEnergy - neighborEnergy) / temperature);
    }

    private static double intraClusterDistance(ArrayList<Point> data) {
        double sum = 0;

        for (Point aData : data) {
            sum += euclideanDistance(aData, data.get(0));
        }

        return sum;
    }

    private static double interClusterDistance(ArrayList<ArrayList<Point>> clusters) {
        ArrayList<ArrayList<Point>> tmpClusters = new ArrayList<>(clusters);
        double sum = 0;

        while (tmpClusters.size() > 0) {
            for (ArrayList<Point> cluster : tmpClusters) {
                sum += Math.pow(euclideanDistance(cluster.get(0), tmpClusters.get(0).get(0)), 2);
            }
            tmpClusters.remove(0);
        }

        return sum;
    }

    private static void displayStats(ArrayList<ArrayList<Point>> clusters) {
        // print some stats
        System.out.println("Inter-cluster distance: " + interClusterDistance(clusters));
        for (int i = 0; i < clusters.size(); i++) {
            System.out.println("Cluster " + i + " intra-cluster distance: " + intraClusterDistance(clusters.get(i)));
        }

        System.out.println();
    }

    private static double minDistanceBetweenClusters(ArrayList<ArrayList<Point>> clusters) {
        ArrayList<ArrayList<Point>> tmpClusters = new ArrayList<>(clusters);
        double min = Integer.MAX_VALUE;
        while (tmpClusters.size() > 0) {
            for (ArrayList<Point> cluster : tmpClusters) {
                if (euclideanDistance(cluster.get(0), tmpClusters.get(0).get(0)) < min &&
                        euclideanDistance(cluster.get(0), tmpClusters.get(0).get(0)) != 0) {
                    min = euclideanDistance(cluster.get(0), tmpClusters.get(0).get(0));
                }
            }
            tmpClusters.remove(0);
        }

        return min;
    }

    private static double maxDistanceBetweenClusters(ArrayList<ArrayList<Point>> clusters) {
        ArrayList<ArrayList<Point>> tmpClusters = new ArrayList<>(clusters);
        double max = Integer.MIN_VALUE;
        while (tmpClusters.size() > 0) {
            for (ArrayList<Point> cluster : tmpClusters) {
                if (euclideanDistance(cluster.get(0), tmpClusters.get(0).get(0)) > max) {
                    max = euclideanDistance(cluster.get(0), tmpClusters.get(0).get(0));
                }
            }
            tmpClusters.remove(0);
        }

        return max;
    }
}

// inter-cluster distance -> minimized
// intra-cluster distance -> maximized
