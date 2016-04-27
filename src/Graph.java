import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

class Graph extends JPanel {
    private static final int MAX_SCORE = 100;
    private static final int BORDER_GAP = 30;
    private static final Color GRAPH_POINT_COLOR = new Color(150, 50, 50, 180);
    private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
    private static final int GRAPH_POINT_WIDTH = 12;
    private static final int Y_HATCH_CNT = 10;
    private static final int PREFERRED_WIDTH = 800;
    private static final int PREFERRED_HEIGHT = 600;

    private Data data;
    private List<Point> graphPoints = new ArrayList<>();

    Graph(Data d) {
        data = d;
    }

    private void createGraphPoints() {
        double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (data.getDataSize() - 1);
        double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);

        for (int i = 0; i < data.getDataSize(); i++) {
            int x1 = (int) (i * xScale + BORDER_GAP);
            int y1 = (int) ((MAX_SCORE - data.getDataPoints()[i].getY() * yScale + BORDER_GAP));
            graphPoints.add(new Point(x1, y1));
        }
    }

    private void createAxes(Graphics2D g2) {
        // create x and y axes
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

        // create hatch marks for y axis.
        for (int i = 0; i < Y_HATCH_CNT; i++) {
            int x0 = BORDER_GAP;
            int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
            int y = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
            g2.drawLine(x0, y, x1, y);
        }

        // and for x axis
        for (int i = 0; i < data.getDataSize() - 1; i++) {
            int x = (i + 1) * (getWidth() - BORDER_GAP * 2) / (data.getDataSize() - 1) + BORDER_GAP;
            int y0 = getHeight() - BORDER_GAP;
            int y1 = y0 - GRAPH_POINT_WIDTH;
            g2.drawLine(x, y0, x, y1);
        }
    }

    private void plotGraphPoints(Graphics2D g2) {
        g2.setStroke(GRAPH_STROKE);
        g2.setColor(GRAPH_POINT_COLOR);
        for (Point g : graphPoints) {
            int x = g.x - GRAPH_POINT_WIDTH / 2;
            int y = g.y - GRAPH_POINT_WIDTH / 2;
            int ovalW = GRAPH_POINT_WIDTH;
            int ovalH = GRAPH_POINT_WIDTH;
            g2.fillOval(x, y, ovalW, ovalH);
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
        Graph mainPanel = new Graph(data);
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
