package com.example;

import java.io.*;
import java.util.*;

public class CSVtoArrayList {

    public static List<List<Object>> readCSV(String filePath) {
        List<List<Object>> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<Object> record = new ArrayList<>();
                String[] values = line.split(",");
                
                for (String value : values) {
                    record.add(value);
                }
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }

    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        String filePath = System.getProperty("user.dir")+"/src/main/java/com/example/dataset.csv";
        List<List<Object>> csvData = readCSV(filePath);

        // Printing the content of the ArrayList of ArrayLists
        for (List<Object> record : csvData) {
            System.out.println(record);
        }
    }
}
