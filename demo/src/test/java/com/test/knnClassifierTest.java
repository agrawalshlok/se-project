package com.test;

import org.junit.jupiter.api.Test;
import com.example.knnClassifier;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class knnClassifierTest {

    private knnClassifier knn1 = new knnClassifier(3);
    @Test
    void fit() {
        List<List<Double>> trainingData = createTrainFeatures();
        List<Double> labels = createTrainLabels();

        knn1.fit(trainingData, labels);

        assertEquals(6, knn1.getTrainingData().size());
        assertEquals(6, knn1.getLabels().size());
        assertEquals(0.0, knn1.getLabels().get(0));
    }

    @Test
    void predict() {

        List<List<Double>> trainingData = createTrainFeatures();
        List<Double> labels = createTrainLabels();

        knn1.fit(trainingData, labels);
        List<List<Double>> testData = createTestFeatures();

        List<Double> y_pred = knn1.predict(testData);

        assertEquals(3, y_pred.size());
        assertEquals(0.0, y_pred.get(0));
        assertEquals(1.0, y_pred.get(1));
        assertEquals(0.0, y_pred.get(2));
    }

    @Test
    void calculateAccuracy() {
        List<List<Double>> trainingData = createTrainFeatures();
        List<Double> labels = createTrainLabels();

        knn1.fit(trainingData, labels);

        List<List<Double>> testData = createTestFeatures();
        List<Double> y_true = createTestLabels();

        List<Double> y_pred = knn1.predict(testData);

        Double accuracy  = knn1.calculateAccuracy(y_true, y_pred);
        assertEquals(accuracy, 0.66, 0.01);
    }

    private List<List<Double>> createTrainFeatures() {
        List<List<Double>> trainingData = new ArrayList<>();
        trainingData.add(List.of(1.0, 2.0));
        trainingData.add(List.of(2.0, 3.0));
        trainingData.add(List.of(3.0, 4.0));
        trainingData.add(List.of(5.0, 5.0));
        trainingData.add(List.of(1.5, 2.5));
        trainingData.add(List.of(4.0, 3.0));

        return trainingData;
    }

    private List<Double> createTrainLabels()
    {
        List<Double> labels = new ArrayList<>();
        labels.add(0.0);
        labels.add(1.0);
        labels.add(0.0);
        labels.add(1.0);
        labels.add(0.0);
        labels.add(1.0);

        return labels;
    }
    private List<List<Double>> createTestFeatures() {
        List<List<Double>> testData = new ArrayList<>();

        testData.add(List.of(2.5, 3.5));
        testData.add(List.of(4.5, 4.5));
        testData.add(List.of(1.0, 2.0));

        return testData;
    }

    private List<Double> createTestLabels()
    {
        List<Double> testLabels = new ArrayList<>();
        testLabels.add(1.0);
        testLabels.add(1.0);
        testLabels.add(0.0);
        return testLabels;
    }

}