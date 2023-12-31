package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PreProcessor {
    // Function to replace null values with the mode for a specific column and convert doubles to integers
    public void replaceNullWithMode(List<List<Object>> data, int columnIndex) {
        // Calculate the mode for the specified column
        Object mode = calculateMode(data, columnIndex);
        // Replace null values with the mode and convert doubles to integers
        for (List<Object> row : data) {
            Object value = row.get(columnIndex);
            if (value == null || value=="") {
                row.set(columnIndex, mode);
            } else if (value instanceof Double) {
                row.set(columnIndex, ((Double) value).intValue());
            }
        }
    }

    // Function to calculate the mode for a specific column
    private Object calculateMode(List<List<Object>> data, int columnIndex) {
        Map<Object, Integer> valueCount = new HashMap<>();

        for (List<Object> row : data) {
            Object value = row.get(columnIndex);
            if (value != null) {
                valueCount.put(value, valueCount.getOrDefault(value, 0) + 1);
            }
        }

        // Find the mode
        Object mode = null;
        int maxCount = 0;

        for (Map.Entry<Object, Integer> entry : valueCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mode = entry.getKey();
            }
        }

        return mode;
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

    public List<Object> getColumn(List<List<Object>> dataset, int index){
        List<Object> column = new ArrayList<>();
        if(!dataset.isEmpty() && index< dataset.get(0).size()){
            for(List<Object> row: dataset){
                column.add(row.get(index));
            }
        }
        return column;
    }

    public <T> List<List<T>> get2DListWithoutColumn(List<List<T>> dataset, int index){
        List<List<T>> withoutColumn = new ArrayList<>();
        if(!dataset.isEmpty() && index < dataset.get(0).size()){
            for (List<T> row : dataset) {
                List<T> updatedRow = new ArrayList<>(row.subList(0,index));
                updatedRow.addAll(row.subList(index+1,row.size()));
                withoutColumn.add(new ArrayList<>(updatedRow));
            }

            return withoutColumn;
        }
        return dataset;
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

        obj.replaceNullWithMode(data, columnIndexToProcess);
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
