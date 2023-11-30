package com.example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NaiveBayesClassifier{

    private Map<Integer, Double> classProbs;//class probabilities
    private Map<Integer, Map<Integer, Map<Integer,Double>> > featureProbs;
    private List<Integer> classes;//list of classes(number of unique y values)
    private List<List<Integer>> features;//maintains unique value of features.

    public NaiveBayesClassifier() {
        this.classProbs = new HashMap<>();
        this.featureProbs = new HashMap<>();
    }

    public void fit(List<List<Integer>> X, List<Integer> y) {
        // Get unique classes and features
        this.classes = getUniqueValues(y);
        this.features = getUnique2DValues(X);

        // Calculate class probabilities
        for (Integer c : this.classes) {

            double classProb = calculateClassProbability(c, y);
            classProbs.put(c, classProb);

            // Calculate feature probabilities for each class
            Map<Integer, Map<Integer,Double>> featureProbMap = new HashMap<>();

            for (int i=0;i<features.size();i++) {

                Map<Integer,Double> featureval=new HashMap<>();

                for(int j=0;j<features.get(i).size();j++){

                    Integer val=features.get(i).get(j);
                    double featureProb = calculateFeatureProbability(i, val, c, X, y);
                    featureval.put(val,featureProb);

                }

                featureProbMap.put(i, featureval);
            }

            featureProbs.put(c, featureProbMap);
        }
    }

    public List<Integer> predict(List<List<Integer>> X) {
        List<Integer> yPred = new ArrayList<>();
        for (List<Integer> sample : X) {
            // Calculate the posterior probability for each class
            //Here we take posterior is proportional to sum of log likelihoods and log of class probability.
            Map<Integer,Double> classProbabilites=new HashMap<>();

            for (Integer c : this.classes) {

                double classProb = Math.log(classProbs.get(c));
                double featureProbSum = 0.0;

                for (int i = 0; i < sample.size(); i++) {
                    int feature = sample.get(i);
                    double featureProb = Math.log(featureProbs.get(c).get(i).getOrDefault(feature, 1e-10));
                    featureProbSum += featureProb;
                }

                double posteriorProb = classProb + featureProbSum;

                classProbabilites.put(c, posteriorProb);
            }
            // Choose the class with the highest posterior probability
            int predClass = classProbabilites.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow(null)
                    .getKey();

            yPred.add(predClass);
        }

        return yPred;
    }

    public double calculateAccuracy(List<Integer> yTrue, List<Integer> yPred) { //calculation done based on number of correctly classification.
        int correct = 0;
        for (int i = 0; i < yTrue.size(); i++) {
            if (yTrue.get(i).equals(yPred.get(i))) {
                correct++;
            }
        }
        return (double) correct / yTrue.size();
    }

    private double calculateClassProbability(Integer targetClass, List<Integer> y) { //to calculate probability of a class
        int count = 0;
        for (Integer c : y) {
            if (c.equals(targetClass)) {
                count++;
            }
        }
        return (double) count / y.size();
    }

    private double calculateFeatureProbability(Integer column,Integer featureval, Integer targetClass, List<List<Integer>> X, List<Integer> y) { //finding P(w/y) with laplace smoothing

        //Formula for laplace smoothing : P(w/y)=(number of points with w and y + 1)/(number of points with y + number of features)

        int count = 1; //default value
        int total = 1; 

        for (int i = 0; i < X.size(); i++) {
            if (y.get(i).equals(targetClass)) {
                total+=1;
                if(featureval.equals(X.get(i).get(column))) count++;
            }
        }

        for (Integer c : this.classes) {
            if (c.equals(targetClass)) {
                total += X.get(0).size(); // To get number of features.
                break;
            }
        }

        return (double) count / total;
    }

    private List<Integer> getUniqueValues(List<Integer> list) {
        return list.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private List<List<Integer>> getUnique2DValues(List<List<Integer>> list) {
        List<List<Integer>> unique=new ArrayList<>();

        int x=list.size();
        int y=list.get(0).size();

        for(int j=0;j<y;j++){
            List<Integer> column=new ArrayList<>();
            for(int i=0;i<x;i++){
                column.add(list.get(i).get(j));
            }
            unique.add(getUniqueValues(column));
        }

        return unique;
    }

    public static void main(String[] args) {
        // Example usage with a larger dataset
        List<List<Integer>> X_train = new ArrayList<>();
        List<Integer> y_train = new ArrayList<>();

        // Generating a synthetic dataset
        for (int i = 0; i < 100; i++) {
            List<Integer> features = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                features.add((int) (Math.random() * 5));  // Random integer between 0 and 4
            }
            X_train.add(features);

            // Assigning a class label (0 to 3) based on some condition (for example, sum of features)
            int label = (features.stream().mapToInt(Integer::intValue).sum() > 7) ? 1 : 0;
            y_train.add(label);
        }

        // Create and fit the Naive Bayes classifier
        NaiveBayesClassifier nbClassifier = new NaiveBayesClassifier();
        nbClassifier.fit(X_train, y_train);

        // Testing data
        List<List<Integer>> X_test = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<Integer> testFeatures = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                testFeatures.add((int) (Math.random() * 5));  // Random integer between 0 and 4
            }
            X_test.add(testFeatures);
        }

        // Predict the labels for the test data
        List<Integer> y_pred = nbClassifier.predict(X_test);

        // Print the predicted labels
        System.out.println("Predicted Labels: " + y_pred);

    }
}

