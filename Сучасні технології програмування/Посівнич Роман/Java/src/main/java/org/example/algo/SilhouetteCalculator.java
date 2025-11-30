package org.example.algo;

import org.example.model.DataPoint;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SilhouetteCalculator {
    public double calculateOverallSilhouette(List<DataPoint> points) {
        if (points == null || points.size() <= 1) return 0.0;
        Map<Integer, List<DataPoint>> clusters = points.stream()
                .collect(Collectors.groupingBy(DataPoint::getClusterId));
        if (clusters.size() <= 1) return 0.0;

        double totalSilhouette = 0.0;
        for (DataPoint point : points) {
            int currentClusterId = point.getClusterId();
            List<DataPoint> currentCluster = clusters.get(currentClusterId);
            double a_i = 0.0;
            if (currentCluster.size() > 1) {
                double sumDistances = currentCluster.stream()
                        .filter(other -> other != point)
                        .mapToDouble(point::distanceTo).sum();
                a_i = sumDistances / (currentCluster.size() - 1);
            }
            double b_i = Double.MAX_VALUE;
            for (Map.Entry<Integer, List<DataPoint>> entry : clusters.entrySet()) {
                if (entry.getKey() != currentClusterId) {
                    double avgDistance = entry.getValue().stream()
                            .mapToDouble(point::distanceTo).average().orElse(Double.MAX_VALUE);
                    b_i = Math.min(b_i, avgDistance);
                }
            }
            totalSilhouette += (b_i != Double.MAX_VALUE) ? (b_i - a_i) / Math.max(a_i, b_i) : 0.0;
        }
        return totalSilhouette / points.size();
    }
}