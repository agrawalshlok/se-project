package com.example;
import java.util.*;
import java.util.stream.Collectors;

public class NaiveBayesClassifier implements MlModel{

    private Map<Double, Double> classProbs;//class probabilities
    private Map<Double, Map<Integer, Map<Double,Double>> > featureProbs;
    private List<Double> classes;//list of classes(number of unique y values)
    private List<List<Double>> features;//maintains unique value of features.

    public NaiveBayesClassifier() {
        this.classProbs = new HashMap<>();
        this.featureProbs = new HashMap<>();
    }

    public void fit(List<List<Double>> X, List<Double> y) {
        // Get unique classes and features
        this.classes = getUniqueValues(y);
        this.features = getUnique2DValues(X);

        // Calculate class probabilities
        for (Double c : this.classes) {

            double classProb = calculateClassProbability(c, y);
//            System.out.println(c+": "+classProb);
            classProbs.put(c, classProb);

            // Calculate feature probabilities for each class
            Map<Integer, Map<Double,Double>> featureProbMap = new HashMap<>();

            for (int i=0;i<features.size();i++) {

                Map<Double,Double> featureVal=new HashMap<>();

                for(int j=0;j<features.get(i).size();j++){

                    double val=features.get(i).get(j);
                    double featureProb = calculateFeatureProbability(i, val, c, X, y);
                    featureVal.put(val,featureProb);

                }

                featureProbMap.put(i, featureVal);
//                System.out.println(i+": "+featureProbMap);
            }

            featureProbs.put(c, featureProbMap);
        }
    }

    public List<Double> predict(List<List<Double>> X) {
        List<Double> yPred = new ArrayList<>();
        for (List<Double> sample : X) {
            // Calculate the posterior probability for each class
            //Here we take posterior is proportional to sum of log likelihoods and log of class probability.
            Map<Double,Double> classProbabilites=new HashMap<>();

            for (Double c : this.classes) {

                double classProb = Math.log(classProbs.get(c));
                double featureProbSum = 0.0;

                for (int i = 0; i < sample.size(); i++) {
                    double feature = sample.get(i);
                    double featureProb = Math.log(featureProbs.get(c).get(i).getOrDefault(feature, 1e-10));
                    featureProbSum += featureProb;
                }

                double posteriorProb = classProb + featureProbSum;

                classProbabilites.put(c, posteriorProb);
            }
            // Choose the class with the highest posterior probability
            double predClass = classProbabilites.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow(null)
                    .getKey();

            yPred.add(predClass);
        }

        return yPred;
    }

    public double calculateAccuracy(List<Double> yTrue, List<Double> yPred) { //calculation done based on number of correctly classification.
        int correct = 0;
        for (int i = 0; i < yTrue.size(); i++) {
            if (areDoublesEqual(yTrue.get(i), yPred.get(i), 0.0001)) {
                correct++;
            }
        }
        return (double) correct / yTrue.size();
    }

    private static boolean areDoublesEqual(double value1, double value2, double delta ) {
        return Math.abs(value1 - value2) < delta;
    }

    public double calculateClassProbability(Double targetClass, List<Double> y) { //to calculate probability of a class
        int count = 0;
        for (Double c : y) {
            if (areDoublesEqual(c,targetClass,0.0001)) {
                count++;
            }
        }
        return (double) count / y.size();
    }

    public double calculateFeatureProbability(Integer column,Double featureval, Double targetClass, List<List<Double>> X, List<Double> y) { //finding P(w/y) with laplace smoothing

        //Formula for laplace smoothing : P(w/y)=(number of points with w and y + 1)/(number of points with y + number of features)

        int count = 1; //default value
        int total = 1;

        for (int i = 0; i < X.size(); i++) {
            if (areDoublesEqual(y.get(i),targetClass,0.0001)) {
                total+=1;
                if(areDoublesEqual(featureval,X.get(i).get(column),0.0001)) count++;
            }
        }

        for (Double c : this.classes) {
            if (areDoublesEqual(c,targetClass,0.0001)) {
                total += X.get(0).size(); // To get number of features.
                break;
            }
        }

        return (double) count / total;
    }

    public List<Double> getUniqueValues(List<Double> list) {
        return list.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<List<Double>> getUnique2DValues(List<List<Double>> list) {
        List<List<Double>> unique=new ArrayList<>();

        int x=list.size();
        int y=list.get(0).size();

        for(int j=0;j<y;j++){
            List<Double> column=new ArrayList<>();
            for(int i=0;i<x;i++){
                column.add(list.get(i).get(j));
            }
            unique.add(getUniqueValues(column));
        }

        return unique;
    }

    public static void main(String[] args) {
        // Example usage with a larger dataset
        List<List<Double>> X_train = new ArrayList<>();
        List<Double> y_train = new ArrayList<>();

        X_train.add(Arrays.asList(1.2, 3.4, 2.1, 4.5, 0.8));
        X_train.add(Arrays.asList(0.5, 2.8, 3.2, 1.7, 4.3));
        X_train.add(Arrays.asList(2.0, 1.0, 4.5, 3.1, 2.2));
        X_train.add(Arrays.asList(3.0, 1.2, 2.4, 3.2, 4.2));
        X_train.add(Arrays.asList(4.3, 3.0, 1.2, 0.9, 2.5));

        y_train.add(0.0);
        y_train.add(1.0);
        y_train.add(0.0);
        y_train.add(1.0);
        y_train.add(1.0);
        // Create and fit the Naive Bayes classifier
        NaiveBayesClassifier nbClassifier = new NaiveBayesClassifier();
        nbClassifier.fit(X_train, y_train);

        // Testing data
        List<List<Double>> X_test = new ArrayList<>();
        X_test.add(Arrays.asList(1.2, 1.0, 2.1, 4.5, 0.8));
        X_test.add(Arrays.asList(3.0, 1.2, 4.5, 1.7, 4.2));
        X_test.add(Arrays.asList(4.3, 3.0, 1.2, 3.2, 0.8));

        // Predict the labels for the test data
        List<Double> y_pred = nbClassifier.predict(X_test);

        // Print the predicted labels
        System.out.println("Predicted Labels: " + y_pred);

    }
}

