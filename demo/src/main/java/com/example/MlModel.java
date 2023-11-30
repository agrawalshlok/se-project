package com.example;
import java.util.List;

public interface MlModel {
    void fit(List<List<Double>> X_train, List<Double> y_train);
    List<Double> predict(List<List<Double>> X_test);
    public double calculateAccuracy(List<Double> yTrue, List<Double> yPred);
}
