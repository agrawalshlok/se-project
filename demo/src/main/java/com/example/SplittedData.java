package com.example;

import java.util.ArrayList;
import java.util.List;

public class SplittedData {
    List<List<Double>> x_train = new ArrayList<>();
    List<List<Double>> x_test = new ArrayList<>();
    List<Double> y_train = new ArrayList<>();
    List<Double> y_test = new ArrayList<>();

    public SplittedData (List<List<Double>> x_train, List<List<Double>> x_test,
    List<Double> y_train, List<Double> y_test) {
        this.x_train = x_train;
        this.x_test = x_test;
        this.y_train = y_train;
        this.y_test = y_test;
    }

    public List<List<Double>> get_x_train() {
        return x_train;
    }

    public List<List<Double>> get_x_test() {
        return x_test;
    }

    public List<Double> get_y_train() {
        return y_train;
    }

    public List<Double> get_y_test() {
        return y_test;
    }
}

