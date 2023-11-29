import java.util.List;

public interface MlModel {
    void fit(List<List<Double>> X_train, List<Integer> y_train);
    List<Integer> predict(List<List<Double>> X_test); 
    public double calculateAccuracy(List<Integer> yTrue, List<Integer> yPred);
}
