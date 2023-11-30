package com.example;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import java.util.HashMap;

public class knnClassifier implements MlModel{

    private List<List<Integer>> trainingData; // Change the type of trainingData to List<List<Double>>
    private List<Integer> labels;
    private int k;

    public knnClassifier(int k) {
        this.k = k;
        this.trainingData = new ArrayList<>();
        this.labels = new ArrayList<>();
    }

    public void fit(List<List<Integer>> X_train, List<Integer> y_train) {
        this.trainingData = X_train;
        this.labels = y_train;
    }

    private Integer _predict(List<Integer> instance) {
        List<Neighbor> neighbors = new ArrayList<>();

        // Calculate distances and store in neighbors list
        for (int i = 0; i < trainingData.size(); i++) {
            List<Integer> dataPoint = trainingData.get(i);
            int label = labels.get(i);
            double distance = euclideanDistance(instance, dataPoint);
            neighbors.add(new Neighbor(distance, label));
        }

        // Sort neighbors by distance
        Collections.sort(neighbors, Comparator.comparingDouble(Neighbor::getDistance));

        // Count occurrences of each label in the k nearest neighbors
        Map<Integer, Integer> labelCount = new HashMap<>();
        for (int i = 0; i < k; i++) {
            int neighborLabel = neighbors.get(i).getLabel();
            labelCount.put(neighborLabel, labelCount.getOrDefault(neighborLabel, 0) + 1);
        }

        // Find the label with the highest count
        int predictedLabel = Collections.max(labelCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        return predictedLabel;
    }

    public List<Integer> predict(List<List<Integer>> X_test) {
        List<Integer> y_pred = new ArrayList<>();
        for (int i = 0; i < X_test.size(); i++) {
            int predictedLabel = _predict(X_test.get(i));
            y_pred.add(predictedLabel);
        }

        return y_pred;
    }

    private double euclideanDistance(List<Integer> instance1, List<Integer> instance2) {
        double sum = 0.0;
        for (int i = 0; i < instance1.size(); i++) {
            sum += Math.pow(instance1.get(i) - instance2.get(i), 2);
        }
        return Math.sqrt(sum);
    }

    public double calculateAccuracy(List<Integer> yTrue, List<Integer> yPred) {
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
        private int label;

        public Neighbor(double distance, int label) {
            this.distance = distance;
            this.label = label;
        }

        public double getDistance() {
            return distance;
        }

        public int getLabel() {
            return label;
        }
    }

    public static void main(String[] args) {
        // Example usage
        knnClassifier knn = new knnClassifier(3);

        // Sample training data
        List<List<Integer>> trainingData = new ArrayList<>();
        trainingData.add(List.of(1, 2));
        trainingData.add(List.of(2, 3));
        trainingData.add(List.of(3, 4));

        // Corresponding labels
        List<Integer> labels = new ArrayList<>();
        labels.add(1);
        labels.add(2);
        labels.add(1);

        // Train the model
        knn.fit(trainingData, labels);

        // Make predictions
        List<Integer> testInstance = List.of(2, 3);
        List<List<Integer>> y_test = new ArrayList<>();
        y_test.add(testInstance);
        List<Integer> y_pred = knn.predict(y_test);

        System.out.println("Predicted label: " + y_pred.get(0));
    }
}
