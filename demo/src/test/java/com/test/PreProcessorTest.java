package com.test;

import com.example.CSVtoArrayList;
import com.example.PreProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PreProcessorTest {
    private final Double DELTA=0.1;
    private PreProcessor preProcessor = new PreProcessor();
    @Test
    @DisplayName("Replacing Null With Mode")
    void replaceNullWithMode() {
        List<List<Object>> data = createTestData();
//        data=data.subList(1,data.size());
        preProcessor.replaceNullWithMode(data,1);
        assertEquals(12.0, (double) data.get(1).get(1),DELTA);
    }

    @Test
    @DisplayName("Normalizing the column")
    void normalizeColumn() {
        List<List<Object>> data = createTestData();
//        data=data.subList(1,data.size());
        preProcessor.normalizeColumn(data,2);

        assertEquals(0.66, (Double) data.get(1).get(2), DELTA);
        assertEquals(0.0, (Double) data.get(2).get(2), DELTA);

    }

    @Test
    void labelEncodeColumn() {
        List<List<Object>> data = createTestData();
        preProcessor.labelEncodeColumn(data,0);
        assertEquals(1.0, (Double) data.get(1).get(0), DELTA);
        assertEquals(2.0, (Double) data.get(2).get(0), DELTA);

    }

    @Test
    void splitData() {
        List<List<Object>> data = createTestData();

    }

    private List<List<Object>> createTestData() {
        List<List<Object>> testData = new ArrayList<>();
        // Add two rows of mock data
        testData.add(Arrays.asList("Monday", 15.0, 65, 10, "Clear"));
        testData.add(Arrays.asList("Tuesday",null, 70, 15, "Partly Cloudy"));
        testData.add(Arrays.asList("Wednesday", 12.0, 60, 12, "Rainy"));
        testData.add(Arrays.asList("Thursday", 12.0, 75, 18, "Clear"));
        testData.add(Arrays.asList("Wednesday", 14.0, 60, 12, "Rainy"));

//        testData.add(Arrays.asList("Friday", 15.0, 75, 18, "Clear"));


        return testData;
    }
}