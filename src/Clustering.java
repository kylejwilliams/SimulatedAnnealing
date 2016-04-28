
// *********************************
// First, generate a random solution
// *********************************

// pick x starting locations for the cluster centroids
// assign each data point to its nearest centroid

// **********************************************************
// Calculate its cost using some cost function you've defined
// **********************************************************
// Repeat:
// Generate a random neighboring solution
// calculate the new solution's cost
// compare them:
// if c_new < c_old : move to the new solution
// if c_new > c_old : MAYBE move to the new solution
// until an acceptable solution is found or you reach some maximum number of iterations

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Clustering {
    private ArrayList<ArrayList<Point>> clusters = new ArrayList<>();
    private Data data;

    public ArrayList<ArrayList<Point>> getClusters() { return clusters; }

    public Clustering(Data data) {
        this.data = data;
    }

    public void generateRandomClusters(int K, int seed) {

        Random rand = new Random(seed);

        // create centroids
        for (int i = 0; i < K; i++) {
            int index = rand.nextInt(data.getDataPoints().size());
            ArrayList<Point> curCluster = new ArrayList<>();
            curCluster.add(data.getDataPoints().get(index));
            data.getDataPoints().remove(index);
            clusters.add(curCluster);
        }

        // Assign all points to the closest centroid
        for (Point dataPoint : data.getDataPoints()) {
            int smallestDistanceIndex = 0;
            for (int j = 0; j < clusters.size(); j++) {
                if (euclideanDistance(clusters.get(j).get(0), dataPoint) < euclideanDistance(clusters.get
                        (smallestDistanceIndex).get(0), dataPoint)) {
                    smallestDistanceIndex = j;
                }
            }
            clusters.get(smallestDistanceIndex).add(dataPoint);
        }
    }

//    private static ArrayList<ArrayList<Point>> kMeans(ArrayList<Point> data, int K) {
//        Random rand = new Random();
//        ArrayList<ArrayList<Point>> clusters = new ArrayList<>();
//        ArrayList<Point> dataPoints = new ArrayList<>(data);
//        boolean centroidsChanged;
//        int index;
//
//        // Select K points as the initial centroids
//        for (int i = 0; i < K; i++) {
//            index = rand.nextInt(dataPoints.size());
//            ArrayList<Point> currentCluster = new ArrayList<>();
//            currentCluster.add(dataPoints.get(index));
//            dataPoints.remove(index);
//            clusters.add(currentCluster);
//        }
//
//        // Assign all points to the closest centroid
//        for (Point dataPoint : dataPoints) {
//            int smallestDistanceIndex = 0;
//            for (int j = 0; j < clusters.size(); j++) {
//                if (euclideanDistance(clusters.get(j).get(0), dataPoint) < euclideanDistance(clusters.get
//                        (smallestDistanceIndex).get(0), dataPoint)) {
//                    smallestDistanceIndex = j;
//                }
//            }
//            clusters.get(smallestDistanceIndex).add(dataPoint);
//        }
//
//        do {
//            centroidsChanged = false;
//
//            // Assign all points to the closest centroid
//            for (Point dataPoint : dataPoints) {
//                int smallestDistanceIndex = 0;
//                for (int j = 0; j < clusters.size(); j++) {
//                    if (euclideanDistance(clusters.get(j).get(0), dataPoint) < euclideanDistance(clusters.get
//                            (smallestDistanceIndex).get(0), dataPoint)) {
//                        smallestDistanceIndex = j;
//                    }
//                }
//                clusters.get(smallestDistanceIndex).add(dataPoint);
//            }
//
//            // Recompute the centroid of each cluster
//            for (ArrayList<Point> cluster : clusters) {
//                dataPoints.add(cluster.get(0));
//                for (int j = 0; j < cluster.size(); j++) {
//                    if (sumSquaredError(cluster, cluster.get(j)) < sumSquaredError(cluster, cluster.get(0))) {
//                        Collections.swap(cluster, 0, j);
//                        centroidsChanged = true;
//                    }
//                }
//            }
//        } while (centroidsChanged);
//
//        return clusters;
//    }
//
//    private static int sumSquaredError(ArrayList<Point> data, Point p) {
//        int SSE = 0;
//
//        for (Point aData : data) {
//            SSE += Math.pow(euclideanDistance(p, aData), 2);
//        }
//
//        return SSE;
//    }

    private static double euclideanDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(Math.abs(p1.x - p2.x), 2) +
                Math.pow(Math.abs(p1.y - p2.y), 2));
    }
}
