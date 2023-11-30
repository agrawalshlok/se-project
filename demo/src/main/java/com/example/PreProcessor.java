package com.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PreProcessor {

    // Function to replace null values with the mean for a specific column
    public static void replaceNullWithMean(List<List<Object>> data, int columnIndex) {
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
    public static void normalizeColumn(List<List<Object>> data, int columnIndex) {
        // Find the min and max values for the specified column
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (List<Object> row : data) {
            double value = Double.parseDouble(row.get(columnIndex).toString());
            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        // Normalize the values in the specified column
        for (List<Object> row : data) {
            double value = Double.parseDouble(row.get(columnIndex).toString());
            double normalizedValue = (value - min) / (max - min);
            row.set(columnIndex, normalizedValue);
        }
    }

    // Function to label encode a specific column
    public static void labelEncodeColumn(List<List<Object>> data, int columnIndex) {
        // Create a mapping of unique values to their corresponding labels
        Map<Object, Integer> valueToLabel = new HashMap<>();
        int labelCounter = 0;

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
            int label = valueToLabel.get(value);
            row.set(columnIndex, label);
        }
    }

    public static Map<String, List<List<Object>>> splitData(List<List<Object>> data, double splitRatio) {
        if (splitRatio < 0 || splitRatio > 1) {
            throw new IllegalArgumentException("Invalid split ratio. It must be between 0 and 1.");
        }

        List<List<Object>> trainingData = new ArrayList<>();
        List<List<Object>> testingData = new ArrayList<>();

        Random random = new Random();
        int cnt = (int)(splitRatio * ((double)(data.size())));
        ArrayList<Integer> used = new ArrayList<Integer>();
        for (int i = 0; i < data.size(); i++)
            used.add(0);

        int curr = 0;
        while (curr < cnt) {
            int randomValue = random.nextInt(data.size());
            if (used.get(randomValue) == 1)
                continue;
            
            used.set(randomValue, 1);
            curr++;
        }


        int i = 0;
        for (List<Object> row : data) {
            if (used.get(i) == 1) {
                trainingData.add(row);
            } else {
                testingData.add(row);
            }
        
            i++;
        }

        
        Map<String, List<List<Object>>> result = new HashMap<>();
        result.put("training", trainingData);
        result.put("testing", testingData);

        return result;
    }

    public static void main(String[] args) {
        // Example usage
        List<List<Object>> data = new ArrayList<>();

        // Populating the data with sample values
        data.add(Arrays.asList(1.0, "A", 10));
        data.add(Arrays.asList(2.0, "B", 20));
        data.add(Arrays.asList(null, "A", 30));
        data.add(Arrays.asList(4.0, "C", 40));
        data.add(Arrays.asList(5.0, "B", 50));

        System.out.println("Original Data:");
        printData(data);

        int columnIndexToProcess = 0;

        replaceNullWithMean(data, columnIndexToProcess);
        normalizeColumn(data, columnIndexToProcess);
        labelEncodeColumn(data,1);

        System.out.println("\nProcessed Data:");
        printData(data);

        double splitRatio = 0.8; // 80% for training, 20% for testing
        Map<String, List<List<Object>>> splitResult = splitData(data, splitRatio);

        List<List<Object>> trainingData = splitResult.get("training");
        List<List<Object>> testingData = splitResult.get("testing");

        System.out.println("\nTraining Data:");
        printData(trainingData);

        System.out.println("\nTesting Data:");
        printData(testingData);
    }

    private static void printData(List<List<Object>> data) {
        for (List<Object> row : data) {
            System.out.println(row);
        }
    }
}
