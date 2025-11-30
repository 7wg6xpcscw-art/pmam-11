package org.example.algo;

import org.example.model.DataPoint;
import java.util.*;
import java.util.stream.Collectors;

public class KMeansClusterer {
    private final List<DataPoint> dataPoints;
    private final int k;
    private final int maxIterations;
    private final List<DataPoint> centroids = new ArrayList<>();

    public KMeansClusterer(List<DataPoint> dataPoints, int k, int maxIterations) {
        this.dataPoints = dataPoints;
        this.k = k;
        this.maxIterations = maxIterations;
    }

    public List<DataPoint> getCentroids() { return centroids; }

    private void initializeCentroids() {
        Random random = new Random();
        Set<Integer> initialIndices = new HashSet<>();
        while (initialIndices.size() < k && initialIndices.size() < dataPoints.size()) {
            initialIndices.add(random.nextInt(dataPoints.size()));
        }
        for (int index : initialIndices) {
            centroids.add(new DataPoint(dataPoints.get(index).getFeatures()));
        }
    }

    private void assignPointsToClusters() {
        for (DataPoint point : dataPoints) {
            double minDistance = Double.MAX_VALUE;
            int closestCluster = -1;
            for (int i = 0; i < centroids.size(); i++) {
                double distance = point.distanceTo(centroids.get(i));
                if (distance < minDistance) {
                    minDistance = distance;
                    closestCluster = i;
                }
            }
            point.setClusterId(closestCluster);
        }
    }

    private boolean updateCentroids() {
        boolean changed = false;
        for (int i = 0; i < k; i++) {
            final int clusterIndex = i;
            List<DataPoint> pointsInCluster = dataPoints.stream()
                    .filter(p -> p.getClusterId() == clusterIndex)
                    .collect(Collectors.toList());

            if (pointsInCluster.isEmpty()) continue;

            double[] sumFeatures = new double[dataPoints.get(0).getFeatures().length];
            for (DataPoint point : pointsInCluster) {
                for (int j = 0; j < sumFeatures.length; j++) {
                    sumFeatures[j] += point.getFeatures()[j];
                }
            }
            double[] newFeatures = new double[sumFeatures.length];
            for (int j = 0; j < sumFeatures.length; j++) {
                newFeatures[j] = sumFeatures[j] / pointsInCluster.size();
            }
            DataPoint newCentroid = new DataPoint(newFeatures);
            if (newCentroid.distanceTo(centroids.get(i)) > 1e-6) changed = true;
            centroids.get(i).setFeatures(newFeatures);
        }
        return changed;
    }

    public List<DataPoint> cluster() {
        centroids.clear();
        initializeCentroids();
        for (int i = 0; i < maxIterations; i++) {
            assignPointsToClusters();
            if (!updateCentroids()) break;
        }
        assignPointsToClusters();
        return dataPoints;
    }
}