package com.test;

import com.example.NaiveBayesClassifier;
import com.example.PreProcessor;
import com.example.SplittedData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NaiveBayesClassifierTest {

    private final Double DELTA=0.01;
    private PreProcessor preProcessor = new PreProcessor();
    private NaiveBayesClassifier naiveBayesClassifier= new NaiveBayesClassifier();
    private SplittedData splittedData ;

    @Test
    void calculateAccuracy() {
        List<Double> YPred= Arrays.asList(0.23,0.45,0.21,0.22);
        List<Double> YTrue= Arrays.asList(0.33,0.45,0.21,0.22);

        assertEquals(0.75,naiveBayesClassifier.calculateAccuracy(YTrue,YPred),DELTA);
    }

    @Test
    void calculateClassProbability() {

        List<List<Object>> data=createTestData();
        List<Object> yColumn=preProcessor.getColumn(data,4);
        List<Double> getList=convertToDoubleList(yColumn);

        double prob= naiveBayesClassifier.calculateClassProbability(1.0,getList);
        double prob1= naiveBayesClassifier.calculateClassProbability(2.0,getList);

        assertEquals(0.5,prob,DELTA);
        assertEquals(0.33,prob1,DELTA);
    }

    @Test
    void calculateFeatureProbability() {
        List<List<Object>> data=createTestData();
        List<List<Object>> X=preProcessor.get2DListWithoutColumn(data,4);
        List<Object> Y = preProcessor.getColumn(data,4);

        List<Double> YNew= convertToDoubleList(Y);
        List<List<Double>> XNew=new ArrayList<>();
        for (List<Object> row: X){
            XNew.add(convertToDoubleList(row));
        }

        naiveBayesClassifier.fit(XNew,YNew);

        assertEquals(0.375,naiveBayesClassifier.calculateFeatureProbability(1,15.0,1.0,XNew,YNew),DELTA);
        assertEquals(0.25,naiveBayesClassifier.calculateFeatureProbability(2,65.0,1.0,XNew,YNew),DELTA);
        assertEquals(0.142,naiveBayesClassifier.calculateFeatureProbability(0,1.0,2.0,XNew,YNew),DELTA);


    }

    @Test
    void getUniqueValues() {
        List<Double> data=Arrays.asList(1.0,2.0,3.0,4.0,3.0,4.0,5.0,2.0,2.0,2.0);
        List<Double> data1=Arrays.asList(1.0,1.0,1.0);

        List<Double> newData=naiveBayesClassifier.getUniqueValues(data);
        List<Double> newData1=naiveBayesClassifier.getUniqueValues(data1);

        assertEquals(5,newData.size());
        assertEquals(1,newData1.size());
    }

    @Test
    void getUnique2DValues() {
        List<Double> data=Arrays.asList(1.0,2.0,3.0);
        List<Double> data1=Arrays.asList(1.0,1.0,1.0);
        List<Double> data2=Arrays.asList(2.0,2.0,2.0);
        List<Double> data3=Arrays.asList(4.0,3.0,4.0);

        List<List<Double>> NewList= new ArrayList<>();
        NewList.add(data);
        NewList.add(data1);
        NewList.add(data2);
        NewList.add(data3);


        List<List<Double>> UniqueList=naiveBayesClassifier.getUnique2DValues(NewList);
        assertEquals(3,UniqueList.size());
        assertEquals(3,UniqueList.get(0).size());
        assertEquals(4,UniqueList.get(2).size());

    }

    private List<List<Object>> createTestData() {
        List<List<Object>> testData = new ArrayList<>();
        // Add two rows of mock data
        testData.add(Arrays.asList(1.0, 15.0, 67.0, 15.0, 1.0));
        testData.add(Arrays.asList(1.0,14.0, 75.0, 15.0, 3.0));
        testData.add(Arrays.asList(2.0, 12.0, 65.0, 12.0, 2.0));
        testData.add(Arrays.asList(3.0, 12.0, 65.0, 18.0, 1.0));
        testData.add(Arrays.asList(2.0, 14.0, 67.0, 12.0, 2.0));
        testData.add(Arrays.asList(3.0, 15.0, 75.0, 18.0, 1.0));

        return testData;
    }

    private static List<Double> convertToDoubleList(List<Object> originalList) {
        // Optional: Filter out non-finite values
        return originalList.stream()
                .map(NaiveBayesClassifierTest::convertToDouble)
                .filter(Double::isFinite)
                .toList();
    }

    private static Double convertToDouble(Object obj) {
        try {
            return Double.parseDouble(obj.toString());
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
}