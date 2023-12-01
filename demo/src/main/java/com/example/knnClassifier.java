package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import java.util.HashMap;

public class knnClassifier implements MlModel {

    private List<List<Double>> trainingData; // Change the type of trainingData to List<List<Double>>
    private List<Double> labels;
    private int k;

    public knnClassifier(int k) {
        this.k = k;
        this.trainingData = new ArrayList<>();
        this.labels = new ArrayList<>();
    }

    public void fit(List<List<Double>> X_train, List<Double> y_train) {
        this.trainingData = X_train;
        this.labels = y_train;
    }

    public List<List<Double>> getTrainingData() {
        return trainingData;
    }

    public List<Double> getLabels() {
        return labels;
    }

    private Double _predict(List<Double> instance) {
        List<Neighbor> neighbors = new ArrayList<>();

        // Calculate distances and store in neighbors list
        for (int i = 0; i < trainingData.size(); i++) {
            List<Double> dataPoint = trainingData.get(i);
            double label = labels.get(i);
            double distance = euclideanDistance(instance, dataPoint);
            neighbors.add(new Neighbor(distance, label));
        }

        // Sort neighbors by distance
        Collections.sort(neighbors, Comparator.comparingDouble(Neighbor::getDistance));

        // Count occurrences of each label in the k nearest neighbors
        Map<Double, Integer> labelCount = new HashMap<>();
        for (int i = 0; i < k; i++) {
            double neighborLabel = neighbors.get(i).getLabel();
            labelCount.put(neighborLabel, labelCount.getOrDefault(neighborLabel, 0) + 1);
        }

        // Find the label with the highest count
        double predictedLabel = Collections.max(labelCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        return predictedLabel;
    }

    public List<Double> predict(List<List<Double>> X_test) {
        List<Double> y_pred = new ArrayList<>();
        for (int i = 0; i < X_test.size(); i++) {
            double predictedLabel = _predict(X_test.get(i));
            y_pred.add(predictedLabel);
        }

        return y_pred;
    }

    private double euclideanDistance(List<Double> instance1, List<Double> instance2) {
        double sum = 0.0;
        for (int i = 0; i < instance1.size(); i++) {
            sum += Math.pow(instance1.get(i) - instance2.get(i), 2);
        }
        return Math.sqrt(sum);
    }

    public double calculateAccuracy(List<Double> yTrue, List<Double> yPred) {
        int correct = 0;
        for (int i = 0; i < yTrue.size(); i++) {
            if (yTrue.get(i).equals(yPred.get(i))) {
                correct++;
            }
        }
        return (double) correct / yTrue.size();
    }

    private static class Neighbor {
        private double distance;
        private double label;

        public Neighbor(double distance, double label) {
            this.distance = distance;
            this.label = label;
        }

        public double getDistance() {
            return distance;
        }

        public double getLabel() {
            return label;
        }
    }

    public static void main(String[] args) {
        // Example usage
        knnClassifier knn = new knnClassifier(3);

        // Sample training data
        List<List<Double>> trainingData = new ArrayList<>();
        trainingData.add(List.of(1.0, 2.0));
        trainingData.add(List.of(2.0, 3.0));
        trainingData.add(List.of(3.0, 4.0));
        trainingData.add(List.of(5.0, 5.0));
        trainingData.add(List.of(1.5, 2.5));
        trainingData.add(List.of(4.0, 3.0));

        // Corresponding labels for training data
        List<Double> labels = new ArrayList<>();
        labels.add(0.0);
        labels.add(1.0);
        labels.add(0.0);
        labels.add(1.0);
        labels.add(0.0);
        labels.add(1.0);

        // Train the model
        knn.fit(trainingData, labels);

        // Test data
        List<List<Double>> testData = new ArrayList<>();
        testData.add(List.of(2.5, 3.5));
        testData.add(List.of(4.5, 4.5));
        testData.add(List.of(1.0, 2.0));

        // Corresponding labels for test data (ground truth)
        List<Double> testLabels = List.of(1.0, 1.0, 0.0);

        // Make predictions
        List<Double> predictions = knn.predict(testData);

        // Calculate accuracy
        double accuracy = knn.calculateAccuracy(testLabels, predictions);

        // Print results
        for (int i = 0; i < testData.size(); i++) {
            System.out.println("Test instance: " + testData.get(i) +
                    " | Predicted label: " + predictions.get(i) +
                    " | True label: " + testLabels.get(i));
        }

        System.out.println("Accuracy: " + accuracy);

    }
}
