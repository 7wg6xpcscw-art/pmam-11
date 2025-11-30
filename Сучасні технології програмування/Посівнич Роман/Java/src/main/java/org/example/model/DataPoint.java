package org.example.model;

public class DataPoint {
    private double[] features;
    private int clusterId = -1;

    public DataPoint(double... features) {
        this.features = features;
    }

    public double[] getFeatures() { return features; }

    public void setFeatures(double[] features) { this.features = features; } // Для оновлення центроїдів

    public int getClusterId() { return clusterId; }

    public void setClusterId(int clusterId) { this.clusterId = clusterId; }

    public double distanceTo(DataPoint other) {
        double sum = 0;
        for (int i = 0; i < features.length; i++) {
            sum += Math.pow(features[i] - other.features[i], 2);
        }
        return Math.sqrt(sum);
    }
}