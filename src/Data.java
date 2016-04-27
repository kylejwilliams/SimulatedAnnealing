import java.awt.*;
import java.util.Random;

/**
 * Created by Kyle on 4/25/2016.
 */
public class Data {
    private Point[] dataPoints;

    public Point[] getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(Point[] dataPoints) {
        this.dataPoints = dataPoints;
    }


    public void generateDataPoints(int numPoints, int seed) {
        Random rand = new Random(seed);
        Point[] tmpData = new Point[numPoints];

        for (int i = 0; i < numPoints; i++) {
            int posX = 1 + rand.nextInt(100);
            int posY = 1 + rand.nextInt(100);

            tmpData[i] = new Point(posX, posY);
        }

        setDataPoints(tmpData);
    }

    public void printDataPoints() {
        for (int i = 0; i < getDataPoints().length; i++) {
            System.out.println(i + ": " + getDataPoints()[i]);
        }
    }
}
