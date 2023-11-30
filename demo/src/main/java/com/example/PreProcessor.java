package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PreProcessor {
    public void replaceNullWithMean(List<List<Object>> data, int columnIndex) {
        // Calculate the mean for the specified column
        double sum = 0;
        int count = 0;

        for (List<Object> row : data) {
            Object value = row.get(columnIndex);
            if (value != null) {
                sum += Double.parseDouble(value.toString());
                count++;
            }
        }

        double mean = sum / count;

        // Replace null values with the mean
        for (List<Object> row : data) {
            Object value = row.get(columnIndex);
            if (value == null) {
                row.set(columnIndex, mean);
            }
        }
    }

    // Function to normalize a specific column
    public void normalizeColumn(List<List<Object>> data, int columnIndex) {
        // Find the min and max values for the specified column
        Double min = Double.MAX_VALUE;
        Double max = Double.MIN_VALUE;

        for (List<Object> row : data) {
            Double value = Double.parseDouble(row.get(columnIndex).toString());
            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        // Normalize the values in the specified column
        for (List<Object> row : data) {
            Double value = Double.parseDouble(row.get(columnIndex).toString());
            Double normalizedValue = (value - min) / (max - min);
            row.set(columnIndex, normalizedValue);
        }
    }

    // Function to label encode a specific column
    public void labelEncodeColumn(List<List<Object>> data, int columnIndex) {
        // Create a mapping of unique values to their corresponding labels
        Map<Object, Double> valueToLabel = new HashMap<>();
        double labelCounter = 0;

        for (List<Object> row : data) {
            Object value = row.get(columnIndex);
            if (!valueToLabel.containsKey(value)) {
                valueToLabel.put(value, labelCounter);
                labelCounter++;
            }
        }

        // Label encode the values in the specified column
        for (List<Object> row : data) {
            Object value = row.get(columnIndex);
            double label = valueToLabel.get(value);
            row.set(columnIndex, label);
        }
    }

    private SplittedData xysplit (List<List<Object>> trainingData, List<List<Object>> testingData, int y_col) {
        List<List<Double>> x_train = new ArrayList<>();
        List<List<Double>> x_test = new ArrayList<>();
        List<Double> y_train = new ArrayList<>();
        List<Double> y_test = new ArrayList<>();

        for (List<Object> row: trainingData) {
            List<Double> temp = new ArrayList<>();
            for (int i = 0; i < row.size(); i++) {
                if (i != y_col) {
                    double value = Double.parseDouble(row.get(i).toString());
                    temp.add(value);
                } else {
                    double value = Double.parseDouble(row.get(i).toString());
                    y_train.add(value);
                }
            }

            x_train.add(temp);
        }

        for (List<Object> row: testingData) {
            List<Double> temp = new ArrayList<>();
            for (int i = 0; i < row.size(); i++) {
                if (i != y_col) {
                    double value = Double.parseDouble(row.get(i).toString());
                    temp.add(value);
                } else {
                    double value = Double.parseDouble(row.get(i).toString());
                    y_test.add(value);
                }
            }

            x_test.add(temp);
        }

        SplittedData ret = new SplittedData(x_train, x_test, y_train, y_test);
        return ret;
    }

    public SplittedData splitData(List<List<Object>> data, double splitRatio, int y_col) {
        if (splitRatio < 0 || splitRatio > 1) {
            throw new IllegalArgumentException("Invalid split ratio. It must be between 0 and 1.");
        }

        List<List<Object>> trainingData = new ArrayList<>();
        List<List<Object>> testingData = new ArrayList<>();

        Random random = new Random();
        int cnt = (int)(splitRatio * ((double)(data.size())));
        ArrayList<Double> used = new ArrayList<Double>();
        for (int i = 0; i < data.size(); i++)
            used.add(0.0);

        int curr = 0;
        while (curr < cnt) {
            int randomValue = random.nextInt(data.size());
            if (used.get(randomValue) == 1.0)
                continue;
            
            used.set(randomValue, 1.0);
            curr++;
        }


        int i = 0;
        for (List<Object> row : data) {
            if (used.get(i) == 1.0) {
                trainingData.add(row);
            } else {
                testingData.add(row);
            }
        
            i++;
        }

        SplittedData ret = xysplit(trainingData, testingData, y_col);
        return ret; 
    }

    public static void main(String[] args) {
        // Example usage
        PreProcessor obj = new PreProcessor();
        List<List<Object>> data = new ArrayList<>();

        // Populating the data with sample values
        data.add(Arrays.asList(1.0, "A", 10));
        data.add(Arrays.asList(2.3, "B", 20));
        data.add(Arrays.asList(null, "A", 30));
        data.add(Arrays.asList(4.4, "C", 40));
        data.add(Arrays.asList(5.6, "B", 50));

        System.out.println("Original Data:");
        printData(data);

        int columnIndexToProcess = 0;

        obj.replaceNullWithMean(data, columnIndexToProcess);
        obj.normalizeColumn(data, columnIndexToProcess);
        obj.labelEncodeColumn(data,1);

        System.out.println("\nProcessed Data:");
        printData(data);

        double splitRatio = 0.8; // 80% for training, 20% for testing
        SplittedData splitResult = obj.splitData(data, splitRatio, 1);

        List<List<Double>> x_train = splitResult.get_x_train();
        List<List<Double>> x_test = splitResult.get_x_test();
        List<Double> y_test = splitResult.get_y_test();
        List<Double> y_train = splitResult.get_y_train();

        System.out.println("\nTraining Data x:");
        for (List<Double> row: x_train)
            System.out.println(row);
        
        System.out.println("\nTraining Data y:");
        for (Double val: y_train)
            System.out.println(val);

        System.out.println("\nTesting Data x:");
        for (List<Double> row: x_test)
            System.out.println(row);
        
        System.out.println("\nTesting Data y:");
        for (Double val: y_test)
            System.out.println(val);
    }

    private static void printData(List<List<Object>> data) {
        for (List<Object> row : data) {
            System.out.println(row);
        }
    }
}
