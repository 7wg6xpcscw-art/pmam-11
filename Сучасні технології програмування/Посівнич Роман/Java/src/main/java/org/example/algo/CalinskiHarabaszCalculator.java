package org.example.algo;

import org.example.model.DataPoint;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CalinskiHarabaszCalculator {
    public double calculateCH(List<DataPoint> points, List<DataPoint> centroids) {
        if (points == null || points.isEmpty()) return 0.0;
        int N = points.size();
        int K = centroids.size();
        if (K <= 1 || N <= K) return 0.0;

        int numFeatures = points.get(0).getFeatures().length;
        double[] globalMean = new double[numFeatures];
        for (DataPoint p : points) {
            for (int i = 0; i < numFeatures; i++) globalMean[i] += p.getFeatures()[i];
        }
        for (int i = 0; i < numFeatures; i++) globalMean[i] /= N;
        DataPoint globalCenter = new DataPoint(globalMean);

        Map<Integer, List<DataPoint>> clusters = points.stream().collect(Collectors.groupingBy(DataPoint::getClusterId));
        double Tr_B = 0.0, Tr_W = 0.0;

        for (int i = 0; i < K; i++) {
            DataPoint centroid = centroids.get(i);
            int n_i = clusters.getOrDefault(i, List.of()).size();
            double dist = centroid.distanceTo(globalCenter);
            Tr_B += n_i * dist * dist;
            for (DataPoint p : clusters.getOrDefault(i, List.of())) {
                double d = p.distanceTo(centroid);
                Tr_W += d * d;
            }
        }
        return (Tr_W == 0) ? 0.0 : (Tr_B / (K - 1)) / (Tr_W / (N - K));
    }
}