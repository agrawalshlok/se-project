package com.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        //Dataset Loading
        String filePath = System.getProperty("user.dir")+"/src/main/java/com/example/dataset.csv";
        List<List<Object>> records=CSVtoArrayList.readCSV(filePath);

        //Data Preprocessing

        PreProcessor preProcessor = new PreProcessor();
        preProcessor.replaceNullWithMode(records,1);
        preProcessor.replaceNullWithMode(records,2);

        records=records.subList(1,records.size());
        preProcessor.labelEncodeColumn(records,0);
        preProcessor.labelEncodeColumn(records,4);

        preProcessor.normalizeColumn(records,1);
        preProcessor.normalizeColumn(records,3);

        System.out.println(records);

        //Splitting Train and Test Data

        SplittedData splittedData= preProcessor.splitData(records,0.8,4);

        //Training on knn model
        knnClassifier knn= new knnClassifier(8);
        knn.fit(splittedData.x_train,splittedData.y_train);

        //Training on Naive Bayes Classifier
        NaiveBayesClassifier naiveBayesClassifier = new NaiveBayesClassifier();
        naiveBayesClassifier.fit(splittedData.x_train,splittedData.y_train);

        //Making Predictions
        List<Double> yPredKnn=knn.predict(splittedData.x_test);
        Double accuracyScoreknn=knn.calculateAccuracy(splittedData.y_test,yPredKnn);

        List<Double> yPredNaive= naiveBayesClassifier.predict(splittedData.x_test);
        Double accuracyScoreNaive= naiveBayesClassifier.calculateAccuracy(splittedData.y_test,yPredNaive);

        System.out.println("Accuracy Score of Knn: " + accuracyScoreknn);
        System.out.println("Accuracy Score of Naive Bayes : " + accuracyScoreNaive);
    }
}