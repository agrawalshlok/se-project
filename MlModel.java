import java.util.List;

public interface MlModel {
    void fit(List<List<Integer>> X, List<Integer> y);
    List<Integer> predict(List<List<Integer>> X); 
    public double calculateAccuracy(List<Integer> yTrue, List<Integer> yPred);
}
