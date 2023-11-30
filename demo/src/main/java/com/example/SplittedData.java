package com.example;

import java.util.ArrayList;
import java.util.List;

public class SplittedData {
    List<List<Integer>> x_train = new ArrayList<>();
    List<List<Integer>> x_test = new ArrayList<>();
    List<Integer> y_train = new ArrayList<>();
    List<Integer> y_test = new ArrayList<>();

    public SplittedData (List<List<Integer>> x_train, List<List<Integer>> x_test,
    List<Integer> y_train, List<Integer> y_test) {
        this.x_train = x_train;
        this.x_test = x_test;
        this.y_train = y_train;
        this.y_test = y_test;
    }

    public List<List<Integer>> get_x_train() {
        return x_train;
    }

    public List<List<Integer>> get_x_test() {
        return x_test;
    }

    public List<Integer> get_y_train() {
        return y_train;
    }

    public List<Integer> get_y_test() {
        return y_test;
    }
}
