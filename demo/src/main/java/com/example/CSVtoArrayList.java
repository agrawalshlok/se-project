package com.example;

import java.io.*;
import java.util.*;

public class CSVtoArrayList {

    public static List<List<String>> readCSV(String filePath) {
        List<List<String>> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> record = new ArrayList<>();
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
        String filePath = "CWC2023.csv+";
        List<List<String>> csvData = readCSV(filePath);

        // Printing the content of the ArrayList of ArrayLists
        for (List<String> record : csvData) {
            System.out.println(record);
        }
    }
}
