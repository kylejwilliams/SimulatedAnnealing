import javax.swing.*;
import java.awt.*;
import java.util.*;

class Graph extends JPanel {
    private static final int MAX_SCORE = 100;
    private static final int BORDER_GAP = 30;
    //private static final Color GRAPH_POINT_COLOR = new Color(150, 50, 50, 180);
    private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
    private static final int GRAPH_POINT_WIDTH = 12;
    private static final int Y_HATCH_CNT = 10;
    private static final int X_HATCH_CNT = 10;
    private static final int PREFERRED_WIDTH = 800;
    private static final int PREFERRED_HEIGHT = 800;

    private ArrayList<ArrayList<Point>> clusters = new ArrayList<>();
    private ArrayList<ArrayList<Point>> graphPointClusters = new ArrayList<>();

    Graph(ArrayList<ArrayList<Point>> c) {
        clusters = c;
    }

    private void createGraphPoints() {
        int scale = (getWidth() - (2 * BORDER_GAP)) / MAX_SCORE;

        for (ArrayList<Point> cluster : clusters) {
            ArrayList<Point> graphPoints = new ArrayList<>();

            for (Point p : cluster) {
                int x = p.x * scale + BORDER_GAP;
                int y = p.y * scale + BORDER_GAP;
                graphPoints.add(new Point(x, y));
            }
            graphPointClusters.add(graphPoints);
        }
    }

    private void createAxes(Graphics2D g2) {
        // create y-axis
        g2.drawLine(BORDER_GAP, BORDER_GAP, BORDER_GAP, getHeight() - BORDER_GAP);

        // create x-axis
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

        // create hatch marks
        for (int i = 0; i < Y_HATCH_CNT; i++) {

            // y-axis
            int y = (i * ((getHeight() - BORDER_GAP) - BORDER_GAP) / Y_HATCH_CNT) + BORDER_GAP;
            int x0 = BORDER_GAP;
            int x1 = BORDER_GAP + GRAPH_POINT_WIDTH;
            g2.drawLine(x0, y, x1, y);

            // x-axis
            int x = (i * ((getWidth() - BORDER_GAP) - BORDER_GAP) / X_HATCH_CNT) + BORDER_GAP;
            int y0 = getHeight() - BORDER_GAP;
            int y1 = getHeight() - BORDER_GAP - GRAPH_POINT_WIDTH;
            g2.drawLine(x, y0, x, y1);
        }
    }

    private void plotGraphPoints(Graphics2D g2) {
        Random rand = new Random();
        g2.setStroke(GRAPH_STROKE);
        for (ArrayList<Point> cluster : graphPointClusters) {
            rand = new Random(graphPointClusters.indexOf(cluster));
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            g2.setColor(new Color(r, g, b));

            for (Point p : cluster) {
                int x = p.x - GRAPH_POINT_WIDTH / 2;
                int y = p.y - GRAPH_POINT_WIDTH / 2;
                int ovalW = GRAPH_POINT_WIDTH;
                int ovalH = GRAPH_POINT_WIDTH;
                g2.fillOval(x, y, ovalW, ovalH);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        createAxes(g2);
        createGraphPoints();
        plotGraphPoints(g2);
    }

    void displayGraph() {
        Graph mainPanel = new Graph(clusters);
        mainPanel.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        JFrame frame = new JFrame("Simulated Annealing");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
