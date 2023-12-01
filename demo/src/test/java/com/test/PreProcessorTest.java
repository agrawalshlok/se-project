package com.test;

import com.example.CSVtoArrayList;
import com.example.PreProcessor;
import com.example.SplittedData;
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
        System.out.println(data);
        assertEquals(0.789, (Double) data.get(1).get(2), DELTA);
        assertEquals(0.0, (Double) data.get(2).get(2), DELTA);

    }

    @Test
    @DisplayName("Encoding Column")
    void labelEncodeColumn() {
        List<List<Object>> data = createTestData();
        preProcessor.labelEncodeColumn(data,0);
        assertEquals(1.0, (Double) data.get(1).get(0), DELTA);
        assertEquals(2.0, (Double) data.get(2).get(0), DELTA);
    }

    @Test
    @DisplayName("Getting Column from 2D List")
    void getColumn(){
        List<List<Object>> data = createTestData();
        List<Object> Ycolumn=preProcessor.getColumn(data,data.get(0).size()-1);
        assertEquals(5,Ycolumn.size());
        assertEquals("Partly Cloudy",Ycolumn.get(1));
    }

    @Test
    @DisplayName("Getting 2D List without a column")
    void get2DList(){
        List<List<Object>> data = createTestData();
        List<List<Object>> newList= preProcessor.get2DListWithoutColumn(data,1);
        assertEquals(5,newList.size());
        assertEquals(4,newList.get(0).size());
        assertEquals(67,newList.get(0).get(1));

        List<List<Object>> newdata = createTestData();
        List<List<Object>> secondList= preProcessor.get2DListWithoutColumn(newdata,4);

        assertEquals(4,secondList.get(0).size());
        assertEquals(15.0,secondList.get(0).get(1));

    }


    @Test
    @DisplayName("Getting Train-Test Split")
    void splitData() {
        List<List<Object>> data = createTestData();
        preProcessor.labelEncodeColumn(data,0);
        preProcessor.labelEncodeColumn(data,4);
        preProcessor.replaceNullWithMode(data,1);

        //dataset shouldn't have null values at this point and everything should be label encoded.
        SplittedData newData=preProcessor.splitData(data,0.8,4);

        //testing number of rows in train and test
        assertEquals(4,newData.get_x_train().size());
        assertEquals(4,newData.get_y_train().size());

        assertEquals(1,newData.get_x_test().size());
        assertEquals(1,newData.get_y_test().size());

        //testing number of columns
        assertEquals(data.get(0).size()-1,newData.get_x_train().get(0).size());
        assertEquals(data.get(0).size()-1,newData.get_x_test().get(0).size());

    }

    private List<List<Object>> createTestData() {
        List<List<Object>> testData = new ArrayList<>();
        // Add two rows of mock data
        testData.add(Arrays.asList("Monday", 15.0, 67, 10, "Clear"));
        testData.add(Arrays.asList("Tuesday",null, 75, 15, "Partly Cloudy"));
        testData.add(Arrays.asList("Wednesday", 12.0, 60, 12, "Rainy"));
        testData.add(Arrays.asList("Thursday", 12.0, 79, 18, "Clear"));
        testData.add(Arrays.asList("Wednesday", 14.0, 67, 12, "Rainy"));

//        testData.add(Arrays.asList("Friday", 15.0, 75, 18, "Clear"));
        return testData;
    }
}