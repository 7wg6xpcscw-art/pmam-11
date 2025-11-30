package org.example.ui;

import org.example.model.ClusterResult;
import org.example.model.DataPoint;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClusteringPanel extends JPanel {
    private final int PADDING = 40;
    private final int DOT_SIZE = 8;
    private final List<String> featureNames;
    private ClusterResult currentResult;
    private int currentK;
    private int visFeatureXIndex = 0;
    private int visFeatureYIndex = 1;

    public static final Color[] CLUSTER_COLORS = {
            new Color(255, 99, 132), new Color(54, 162, 235), new Color(75, 192, 192),
            new Color(255, 205, 86), new Color(153, 102, 255), new Color(255, 159, 64),
            new Color(199, 199, 199)
    };

    public ClusteringPanel(List<String> featureNames) {
        this.featureNames = featureNames;
        setLayout(new BorderLayout());
    }

    public void updateData(ClusterResult result, int k, int xIndex, int yIndex) {
        this.currentResult = result;
        this.currentK = k;
        this.visFeatureXIndex = xIndex;
        this.visFeatureYIndex = yIndex;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (currentResult == null) return;
        int plotWidth = getWidth() - 2 * PADDING;
        int plotHeight = getHeight() - 2 * PADDING;
        int plotXStart = PADDING;
        int plotYStart = PADDING;

        g2d.drawRect(plotXStart, plotYStart, plotWidth, plotHeight);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("2D Візуалізація Кластерів (K=" + currentK + ")", plotXStart, plotYStart - 10);
        g2d.drawString(featureNames.get(visFeatureXIndex) + " (Норм.)", plotXStart + plotWidth / 2 - 80, plotYStart + plotHeight + 30);

        // Малюємо Y вісь (повернута)
        g2d.rotate(-Math.PI / 2);
        g2d.drawString(featureNames.get(visFeatureYIndex) + " (Норм.)", -(plotYStart + plotHeight / 2 + 80), plotXStart - 25);
        g2d.rotate(Math.PI / 2);

        for (DataPoint point : currentResult.getClusteredPoints()) {
            double x = point.getFeatures()[visFeatureXIndex];
            double y = point.getFeatures()[visFeatureYIndex];
            int screenX = plotXStart + (int) (x * plotWidth);
            int screenY = plotYStart + plotHeight - (int) (y * plotHeight);
            int cid = point.getClusterId();
            g2d.setColor((cid >= 0 && cid < CLUSTER_COLORS.length) ? CLUSTER_COLORS[cid] : Color.LIGHT_GRAY);
            g2d.fillOval(screenX - DOT_SIZE / 2, screenY - DOT_SIZE / 2, DOT_SIZE, DOT_SIZE);
        }

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        for (DataPoint centroid : currentResult.getCentroids()) {
            double x = centroid.getFeatures()[visFeatureXIndex];
            double y = centroid.getFeatures()[visFeatureYIndex];
            int screenX = plotXStart + (int) (x * plotWidth);
            int screenY = plotYStart + plotHeight - (int) (y * plotHeight);
            g2d.drawRect(screenX - DOT_SIZE, screenY - DOT_SIZE, DOT_SIZE * 2, DOT_SIZE * 2);
        }
    }
}