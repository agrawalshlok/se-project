package com.test;

import com.example.CSVtoArrayList;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class CSVtoArrayListTest {

    @Test
    void readCSV() {

        String filePath = System.getProperty("user.dir")+"/src/main/java/com/example/dataset.csv";
        List<List<Object>> records=CSVtoArrayList.readCSV(filePath);
        assertEquals(31, records.size()); // Assuming 30 rows in the test CSV

        // Check values for a specific row (e.g., the first row)
        List<Object> firstRow = records.get(1);
        assertEquals("Monday", firstRow.get(0)); // Assuming the first column is "Day"
        assertEquals("15", firstRow.get(1));    // Assuming the second column is "Temperature (Celsius)"
        assertEquals("65", firstRow.get(2));    // Assuming the third column is "Humidity (%)"
        assertEquals("10", firstRow.get(3));    // Assuming the fourth column is "Wind Speed (km/h)"
        assertEquals("Clear", firstRow.get(4));
    }
}