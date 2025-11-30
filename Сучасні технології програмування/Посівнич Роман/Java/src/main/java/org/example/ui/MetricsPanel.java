package org.example.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MetricsPanel extends JPanel {
    private Map<Integer, Double> silScores;
    private Map<Integer, Double> chScores;
    private int currentK;

    public MetricsPanel() {
        setLayout(new GridLayout(2, 1, 10, 10));
    }

    public void updateMetrics(Map<Integer, Double> silScores, Map<Integer, Double> chScores, int currentK) {
        this.silScores = silScores;
        this.chScores = chScores;
        this.currentK = currentK;
        removeAll();
        add(new MetricGraphPanel("Оцінка Силуету", silScores, currentK, false));
        add(new MetricGraphPanel("Оцінка Калінскі-Харабаш", chScores, currentK, true));
        revalidate();
        repaint();
    }

    private static class MetricGraphPanel extends JPanel {
        private final String title;
        private final Map<Integer, Double> scores;
        private final int currentK;
        private final boolean isCH;

        public MetricGraphPanel(String title, Map<Integer, Double> scores, int currentK, boolean isCH) {
            this.title = title; this.scores = scores; this.currentK = currentK; this.isCH = isCH;
            setBackground(Color.WHITE);
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (scores == null || scores.isEmpty()) return;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int PADDING = 40;
            int w = getWidth(), h = getHeight();
            int plotW = w - 2 * PADDING, plotH = h - 2 * PADDING;

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString(title, PADDING, PADDING - 5);
            g2d.drawRect(PADDING, PADDING, plotW, plotH);

            double max = scores.values().stream().mapToDouble(d -> d).max().orElse(1);
            double min = isCH ? 0 : scores.values().stream().mapToDouble(d -> d).min().orElse(-1);
            double range = (max - min == 0) ? 1 : max - min;

            int minK = scores.keySet().stream().min(Integer::compareTo).orElse(2);
            int maxK = scores.keySet().stream().max(Integer::compareTo).orElse(7);
            int stepX = plotW / (maxK - minK + 1);

            int prevX = -1, prevY = -1;
            for (int k = minK; k <= maxK; k++) {
                if (!scores.containsKey(k)) continue;
                int x = PADDING + (k - minK) * stepX + stepX / 2;
                int y = PADDING + plotH - (int) ((scores.get(k) - min) / range * plotH);

                if (prevX != -1) {
                    g2d.setColor(Color.BLUE.darker());
                    g2d.drawLine(prevX, prevY, x, y);
                }
                g2d.setColor(k == currentK ? Color.RED : Color.BLACK);
                g2d.fillOval(x - 3, y - 3, 6, 6);
                g2d.drawString(String.valueOf(k), x - 3, PADDING + plotH + 15);
                prevX = x; prevY = y;
            }
        }
    }
}