package org.example.ui;

import org.example.model.ClusterResult;
import javax.swing.*;
import java.awt.*;

public class LegendPanel extends JPanel {
    private ClusterResult currentResult;

    public LegendPanel() {
        setPreferredSize(new Dimension(200, 0));
        setBackground(new Color(250, 250, 250));
    }

    public void updateData(ClusterResult result) {
        this.currentResult = result;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentResult == null) return;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("Легенда Кластерів", 15, 30);

        int startY = 60;
        int colorBoxSize = 15;
        Color[] colors = ClusteringPanel.CLUSTER_COLORS;
        int count = currentResult.getCentroids().size();

        for (int i = 0; i < count; i++) {
            g2d.setColor(colors[i % colors.length]);
            g2d.fillRect(15, startY + i * 30, colorBoxSize, colorBoxSize);
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString("Кластер " + (i + 1), 15 + colorBoxSize + 10, startY + i * 30 + 13);
        }
    }
}