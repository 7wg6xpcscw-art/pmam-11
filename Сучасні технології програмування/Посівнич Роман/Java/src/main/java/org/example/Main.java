package org.example;

import org.example.algo.CalinskiHarabaszCalculator;
import org.example.algo.KMeansClusterer;
import org.example.algo.SilhouetteCalculator;
import org.example.model.ClusterResult;
import org.example.model.DataPoint;
import org.example.ui.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends JFrame {

    private static final int MAX_K = 7;
    private static final int MIN_K = 2;
    private int currentK = 5;

    private final List<DataPoint> initialData;
    private final List<double[]> rawData;
    private final List<String> ALL_FEATURE_NAMES = Arrays.asList(
            "Річний Дохід (тис. $)", "Оцінка Витрат (1-100)", "Вік (Роки)", "Кредитний Рейтинг (1-10)"
    );

    private ClusterResult currentResult;
    private final ClusteringPanel clusteringPanel;
    private final MetricsPanel metricsPanel;
    private final LegendPanel legendPanel;
    private final ResultFormPanel resultFormPanel;
    private final JComboBox<Integer> kSelector;
    private final JComboBox<String> featureXSelector;
    private final JComboBox<String> featureYSelector;
    private final JTabbedPane tabbedPane;
    private final JScrollPane tableScrollPane;

    public Main() {
        rawData = loadMallData();
        initialData = normalizeData(rawData);

        // UI Components Setup
        kSelector = new JComboBox<>(getKOptions());
        kSelector.setSelectedItem(currentK);
        String[] feats = ALL_FEATURE_NAMES.toArray(new String[0]);
        featureXSelector = new JComboBox<>(feats); featureXSelector.setSelectedIndex(0);
        featureYSelector = new JComboBox<>(feats); featureYSelector.setSelectedIndex(1);

        clusteringPanel = new ClusteringPanel(ALL_FEATURE_NAMES);
        metricsPanel = new MetricsPanel();
        legendPanel = new LegendPanel();
        resultFormPanel = new ResultFormPanel(ALL_FEATURE_NAMES);

        // Table setup
        tableScrollPane = new JScrollPane(new JTable());
        UIUtils.customizeScrollPane(tableScrollPane, new Color(54, 162, 235));

        tabbedPane = new JTabbedPane();
        setupTabs();

        setTitle("Кластерний Аналіз (Modular Version)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1400, 820);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        runClustering();
        setVisible(true);
    }

    private void setupTabs() {
        // Tab 1
        JPanel tab1 = new JPanel(new BorderLayout());
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScrollPane, resultFormPanel);
        split.setResizeWeight(0.7);
        tab1.add(split);
        tabbedPane.addTab("1. Дані та Результати", tab1);

        // Tab 2
        JPanel tab2 = new JPanel(new BorderLayout());
        tab2.add(clusteringPanel, BorderLayout.CENTER);
        tab2.add(legendPanel, BorderLayout.EAST);
        tabbedPane.addTab("2. Візуалізація", tab2);

        // Tab 3
        tabbedPane.addTab("3. Метрики", metricsPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIUtils.ACCENT_COLOR);
        header.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("Обробка Кластерного Аналізу");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ctrl.setOpaque(false);

        ctrl.add(makeLabel("K:"));
        ctrl.add(styleBox(kSelector));
        ctrl.add(makeLabel("X:"));
        ctrl.add(styleBox(featureXSelector));
        ctrl.add(makeLabel("Y:"));
        ctrl.add(styleBox(featureYSelector));

        JButton runBtn = new JButton("Оновити");
        runBtn.addActionListener(e -> runClustering());
        ctrl.add(runBtn);

        header.add(ctrl, BorderLayout.EAST);
        return header;
    }

    private JComponent styleBox(JComboBox<?> box) {
        box.setUI(new UIUtils.CustomComboBoxUI());
        box.setBackground(Color.WHITE);
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new LineBorder(Color.WHITE, 1, true));
        p.add(box);
        return p;
    }

    private JLabel makeLabel(String t) {
        JLabel l = new JLabel(t); l.setForeground(Color.WHITE); return l;
    }

    private JPanel createMainPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(tabbedPane);
        return p;
    }

    private void runClustering() {
        currentK = (Integer) kSelector.getSelectedItem();
        int xIdx = featureXSelector.getSelectedIndex();
        int yIdx = featureYSelector.getSelectedIndex();

        // 1. Run KMeans
        List<DataPoint> dataCopy = deepCopyData();
        KMeansClusterer kmeans = new KMeansClusterer(dataCopy, currentK, 100);
        List<DataPoint> clustered = kmeans.cluster();
        currentResult = new ClusterResult(clustered, kmeans.getCentroids());

        // 2. Update Table
        updateTable(clustered);

        // 3. Calc Metrics for graph
        Map<Integer, Double> sil = new HashMap<>();
        Map<Integer, Double> ch = new HashMap<>();
        SilhouetteCalculator silCalc = new SilhouetteCalculator();
        CalinskiHarabaszCalculator chCalc = new CalinskiHarabaszCalculator();

        for(int k=MIN_K; k<=MAX_K; k++) {
            List<DataPoint> temp = deepCopyData();
            KMeansClusterer tempKm = new KMeansClusterer(temp, k, 100);
            List<DataPoint> res = tempKm.cluster();
            sil.put(k, silCalc.calculateOverallSilhouette(res));
            ch.put(k, chCalc.calculateCH(res, tempKm.getCentroids()));
        }

        // 4. Update UI Panels
        clusteringPanel.updateData(currentResult, currentK, xIdx, yIdx);
        legendPanel.updateData(currentResult);
        resultFormPanel.updateResults(currentResult, currentK, sil, ch);
        metricsPanel.updateMetrics(sil, ch, currentK);
    }

    private void updateTable(List<DataPoint> clustered) {
        String[] cols = new String[ALL_FEATURE_NAMES.size()*2 + 1];
        for(int i=0; i<ALL_FEATURE_NAMES.size(); i++) {
            cols[i] = ALL_FEATURE_NAMES.get(i) + " (Orig)";
            cols[i+ALL_FEATURE_NAMES.size()] = ALL_FEATURE_NAMES.get(i) + " (Norm)";
        }
        cols[cols.length-1] = "Cluster";

        String[][] data = new String[rawData.size()][cols.length];
        for(int i=0; i<rawData.size(); i++) {
            for(int j=0; j<ALL_FEATURE_NAMES.size(); j++) {
                data[i][j] = String.format("%.2f", rawData.get(i)[j]);
                data[i][j+ALL_FEATURE_NAMES.size()] = String.format("%.3f", clustered.get(i).getFeatures()[j]);
            }
            data[i][cols.length-1] = String.valueOf(clustered.get(i).getClusterId());
        }
        JTable t = (JTable) tableScrollPane.getViewport().getView();
        t.setModel(new DefaultTableModel(data, cols));
    }

    private List<DataPoint> deepCopyData() {
        return initialData.stream().map(p -> new DataPoint(p.getFeatures())).collect(Collectors.toList());
    }

    private List<double[]> loadMallData() {
        List<double[]> data = new ArrayList<>();
        Random r = new Random();
        for(int i=0;i<200;i++) data.add(new double[]{r.nextDouble()*100, r.nextDouble()*100, 18+r.nextDouble()*50, r.nextDouble()*10});
        return data;
    }

    private List<DataPoint> normalizeData(List<double[]> raw) {
        if(raw.isEmpty()) return new ArrayList<>();
        int feats = raw.get(0).length;
        double[] min = new double[feats], max = new double[feats];
        for(int j=0; j<feats; j++) { min[j] = raw.get(0)[j]; max[j] = raw.get(0)[j]; }
        for(double[] row : raw) {
            for(int j=0; j<feats; j++) { min[j] = Math.min(min[j], row[j]); max[j] = Math.max(max[j], row[j]); }
        }
        List<DataPoint> res = new ArrayList<>();
        for(double[] row : raw) {
            double[] norm = new double[feats];
            for(int j=0; j<feats; j++) norm[j] = (row[j] - min[j]) / (max[j] - min[j]);
            res.add(new DataPoint(norm));
        }
        return res;
    }

    private Integer[] getKOptions() {
        List<Integer> list = new ArrayList<>();
        for(int i=MIN_K; i<=MAX_K; i++) list.add(i);
        return list.toArray(new Integer[0]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}