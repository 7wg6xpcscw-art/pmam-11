package org.example.model;

import java.util.List;

public class ClusterResult {
    private final List<DataPoint> clusteredPoints;
    private final List<DataPoint> centroids;

    public ClusterResult(List<DataPoint> clusteredPoints, List<DataPoint> centroids) {
        this.clusteredPoints = clusteredPoints;
        this.centroids = centroids;
    }

    public List<DataPoint> getClusteredPoints() { return clusteredPoints; }
    public List<DataPoint> getCentroids() { return centroids; }
}