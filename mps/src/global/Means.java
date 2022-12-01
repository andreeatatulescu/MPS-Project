package global;

import java.util.List;

public class Means {
    public static Float mean(Float a, Float b) {
        return (a + b) / 2;
    }

    public static Float geometric_mean(Float a, Float b) {
        return (float) Math.sqrt(a * b);
    }

    public static Float harmonic_mean(Float a, Float b) {
        return (2 * a * b) / (a + b);
    }

    public static Float square_mean(Float a, Float b) {
        return (float) Math.sqrt((Math.pow(a, 2) + Math.pow(b, 2)) / 2);
    }

    public static Float cubic_mean(Float a, Float b) {
        return (float) Math.cbrt((Math.pow(a, 3) + Math.pow(b, 3)) / 2);
    }

    public static Float square_mean_list(List<Float> scores) {
        float sum = (float) 0;
        for (Float i : scores) {
            sum += (float) Math.pow(i, 2);
        }
        return (float) Math.sqrt(sum/scores.size());
    }
}
