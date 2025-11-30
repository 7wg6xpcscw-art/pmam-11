package org.example.ui;

import org.example.model.ClusterResult;
import org.example.model.DataPoint;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ResultFormPanel extends JPanel {
    private final JLabel silhouetteLabel;
    private final JLabel calinskiLabel;
    private final JTextArea centroidArea;
    private final List<String> featureNames;

    public ResultFormPanel(List<String> featureNames) {
        this.featureNames = featureNames;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Результати"));

        JPanel metricsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        silhouetteLabel = new JLabel("Силует: N/A");
        calinskiLabel = new JLabel("Кал.-Харабаш: N/A");
        metricsPanel.add(silhouetteLabel);
        metricsPanel.add(calinskiLabel);
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(metricsPanel, BorderLayout.NORTH);

        centroidArea = new JTextArea();
        centroidArea.setEditable(false);
        centroidArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(centroidArea);
        UIUtils.customizeScrollPane(scroll, new Color(54, 162, 235));
        add(scroll, BorderLayout.CENTER);
    }

    public void updateResults(ClusterResult result, int k, Map<Integer, Double> silScores, Map<Integer, Double> chScores) {
        ((javax.swing.border.TitledBorder) getBorder()).setTitle("Числові Результати (K=" + k + ")");
        silhouetteLabel.setText(String.format("Силует: %.4f", silScores.getOrDefault(k, 0.0)));
        calinskiLabel.setText(String.format("Кал.-Харабаш: %.4f", chScores.getOrDefault(k, 0.0)));

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Центроїди (K=%d):\n------------------\n", k));
        int c = 0;
        for (DataPoint centroid : result.getCentroids()) {
            sb.append(String.format("C%d: ", c + 1));
            for (int i = 0; i < featureNames.size(); i++) {
                sb.append(String.format("%s: %.2f, ", featureNames.get(i), centroid.getFeatures()[i]));
            }
            sb.setLength(sb.length() - 2);
            sb.append("\n\n");
            c++;
        }
        centroidArea.setText(sb.toString());
    }
}