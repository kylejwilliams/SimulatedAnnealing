import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

class Data {
    private ArrayList<Point> dataPoints;

    ArrayList<Point> getDataPoints() { return dataPoints; }

    private void setDataPoints(ArrayList<Point> dataPoints) {
        this.dataPoints = dataPoints;
    }

    void generateDataPoints(int numPoints, int seed) {
        Random rand = new Random(seed);
        ArrayList<Point> tmpData = new ArrayList<>();

        for (int i = 0; i < numPoints; i++) {
            // 1 <= x, y <= 100
            int posX = 1 + rand.nextInt(100);
            int posY = 1 + rand.nextInt(100);

            tmpData.add(i, new Point(posX, posY));
        }

        setDataPoints(tmpData);
    }
}
