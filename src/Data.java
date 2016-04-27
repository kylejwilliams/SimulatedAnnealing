import java.awt.*;
import java.util.Random;

class Data {
    private Point[] dataPoints;

    Point[] getDataPoints() { return dataPoints; }
    int getDataSize() { return dataPoints.length; }

    private void setDataPoints(Point[] dataPoints) {
        this.dataPoints = dataPoints;
    }

    void generateDataPoints(int numPoints, int seed) {
        Random rand = new Random(seed);
        Point[] tmpData = new Point[numPoints];

        for (int i = 0; i < numPoints; i++) {
            int posX = 1 + rand.nextInt(100);
            int posY = 1 + rand.nextInt(100);

            tmpData[i] = new Point(posX, posY);
        }

        setDataPoints(tmpData);
    }
}
