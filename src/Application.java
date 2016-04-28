import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;

public class Application extends JPanel {
    private static Random rand;
    private static int maxDataValue = 100;
    private static int minDataValue = 1;
    private Color clusterOneColor = Color.RED;
    private Color clusterTwoColor = Color.BLUE;
    private Color clusterThreeColor = Color.GREEN;
    private Color clusterFourColor = Color.ORANGE;
    private Stroke graphLine = new BasicStroke(3f);
    private static ArrayList<ArrayList<Point>> clusters = new ArrayList<>();

    private Application() {
        ArrayList<Point> data = generateData(getSeed());
        clusters = bisectingKmeans(data, 3);

        double sum = 0;
        for (int i = 0; i < clusters.size(); i++) {
            sum += intraClusterDistance(clusters.get(i));
            System.out.println("intra-cluster distance for cluster " + (i + 1) + ": " +
                    intraClusterDistance(clusters.get(i)));
        }
        System.out.println("sum of intra-cluster distances: " + sum);
        System.out.println("inter-cluster distance: " + interClusterDistance(clusters));
        System.out.println("minimum distance between clusters: " + minDistanceBetweenClusters(clusters));
        System.out.println("maximum distance between clusters: " + maxDistanceBetweenClusters(clusters));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // create x and y axes
        int padding = 30;
        g2.drawLine(padding, getHeight() - padding, padding, padding);
        g2.drawLine(padding, getHeight() - padding, getWidth() - padding, getHeight() - padding);

        // create hatch marks for y axis.
        int dataPointSize = 12;
        int numHatchesY = 10;
        for (int i = 0; i < numHatchesY; i++) {
            int x1 = dataPointSize + padding;
            int y0 = getHeight() - (((i + 1) * (getHeight() - padding * 2)) / numHatchesY + padding);
            g2.drawLine(padding, y0, x1, y0);
        }

        // and for x axis
        int numHatchesX = 10;
        for (int i = 0; i < numHatchesX; i++) {
            int x0 = (i + 1) * (getWidth() - padding * 2) / numHatchesX + padding;
            int y0 = getHeight() - padding;
            int y1 = y0 - dataPointSize;
            g2.drawLine(x0, y0, x0, y1);
        }

        g2.setStroke(graphLine);

        for (int i = 0; i < clusters.size(); i++) {
            if (i == 0) g2.setColor(clusterOneColor);
            else if (i == 1) g2.setColor(clusterTwoColor);
            else if (i == 2) g2.setColor(clusterThreeColor);
            else if (i == 3) g2.setColor(clusterFourColor);

            for (int j = 0; j < clusters.get(i).size(); j++) {
                int x = clusters.get(i).get(j).x * (getWidth() - 2 * padding) / (maxDataValue - minDataValue) + padding;
                int y = clusters.get(i).get(j).y * (getHeight() - 2 * padding) / (maxDataValue - minDataValue) +
                        padding;


                g2.fillOval(x, y, dataPointSize, dataPointSize);
                if (j == 0) g2.fillRect(x, y, dataPointSize, dataPointSize);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }

    private static void createAndShowGui() {
        Application mainPanel = new Application();

        JFrame frame = new JFrame("Bisecting K-means");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private static int getSeed() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a positive integer seed value");
        System.out.print("> ");
        int seed = scanner.nextInt();

        scanner.close();

        return seed;
    }

    private static ArrayList<ArrayList<Point>> kMeans(ArrayList<Point> data, int K) {
        ArrayList<ArrayList<Point>> clusters = new ArrayList<>();
        ArrayList<Point> dataPoints = new ArrayList<>(data);
        boolean centroidsChanged;
        int index;

        // Select K points as the initial centroids
        for (int i = 0; i < K; i++) {
            index = rand.nextInt(dataPoints.size());
            ArrayList<Point> currentCluster = new ArrayList<>();
            currentCluster.add(dataPoints.get(index));
            dataPoints.remove(index);
            clusters.add(currentCluster);
        }

        do {
            centroidsChanged = false;

            // Assign all points to the closest centroid
            for (Point dataPoint : dataPoints) {
                int smallestDistanceIndex = 0;
                for (int j = 0; j < clusters.size(); j++) {
                    if (euclideanDistance(clusters.get(j).get(0), dataPoint) < euclideanDistance(clusters.get
                            (smallestDistanceIndex).get(0), dataPoint)) {
                        smallestDistanceIndex = j;
                    }
                    if (manhattanDistance(clusters.get(j).get(0), dataPoint) < manhattanDistance(clusters.get
                            (smallestDistanceIndex).get(0), dataPoint)) {
                        smallestDistanceIndex = j;
                    }
                }
                clusters.get(smallestDistanceIndex).add(dataPoint);
            }

            // Recompute the centroid of each cluster
            for (ArrayList<Point> cluster : clusters) {
                dataPoints.add(cluster.get(0));
                for (int j = 0; j < cluster.size(); j++) {
                    if (sumSquaredError(cluster, cluster.get(j)) < sumSquaredError(cluster, cluster.get(0))) {
                        Collections.swap(cluster, 0, j);
                        centroidsChanged = true;
                    }
                }
            }
        } while (centroidsChanged);

        return clusters;
    }

    private static int sumSquaredError(ArrayList<Point> data, Point p) {
        int SSE = 0;

        for (Point aData : data) {
//			SSE += Math.pow(euclideanDistance(p, data.get(i)), 2);
            SSE += Math.pow(manhattanDistance(p, aData), 2);
        }

        return SSE;
    }

    private static ArrayList<Point> generateData(int seed) {
        rand = new Random(seed);
        ArrayList<Point> data = new ArrayList<>();
        int x;
        int y;

        int numDataPoints = 20;
        for (int i = 0; i < numDataPoints; i++) {
            x = rand.nextInt(maxDataValue) + minDataValue;
            y = rand.nextInt(maxDataValue) + minDataValue;
            data.add(new Point(x, y));
        }

        return data;
    }

    private static ArrayList<ArrayList<Point>> bisectingKmeans(ArrayList<Point> data, int K) {
        // Initialize the list of clusters
        ArrayList<ArrayList<Point>> clusters = new ArrayList<>();
        ArrayList<Point> dataCopy = new ArrayList<>(data);
        clusters.add(dataCopy);

        for (int i = 0; i < K; i++) {
            // Pick a cluster to split
            ArrayList<Point> selectedCluster = clusters.get(rand.nextInt(clusters.size()));
            ArrayList<ArrayList<Point>> tempClusters;
            ArrayList<ArrayList<Point>> subClusters = new ArrayList<>();

            int currentSSE;
            int lowestSSE = Integer.MAX_VALUE;

            for (int j = 0; j < 5; j++) {
                // find two sub-clusters of the selected array
                tempClusters = kMeans(selectedCluster, 2);

                currentSSE = sumSquaredError(tempClusters.get(0), tempClusters.get(0).get(0)) + sumSquaredError
                        (tempClusters.get(1), tempClusters.get(1).get(0));

                if (currentSSE < lowestSSE) subClusters = tempClusters;
            }

            clusters.remove(selectedCluster);
            clusters.addAll(subClusters);
        }

        return clusters;
    }

    private static double euclideanDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(Math.abs(p1.x - p2.x), 2) +
                Math.pow(Math.abs(p1.y - p2.y), 2));
    }

    private static int manhattanDistance(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::createAndShowGui);
    }
}