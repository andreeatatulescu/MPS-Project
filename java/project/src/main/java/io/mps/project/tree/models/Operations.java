package io.mps.project.tree.models;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Operations {

    public static Operation get(OperationType operationType) {
        return switch (operationType) {
            case ARITHMETIC_MEAN -> Operations::arithmeticMean;
            case GEOMETRIC_MEAN -> Operations::geometricMean;
            case HARMONIC_MEAN -> Operations::harmonicMean;
        };
    }


    public static double arithmeticMean(List<Double> doubles) {
        return doubles.stream().mapToDouble(e -> e).sum() / doubles.size();
    }

    public static double geometricMean(List<Double> doubles) {
        var product = doubles.stream().mapToDouble(e -> e).reduce(1, (x, y) -> x * y);
        return Math.pow(product, 1.0 / doubles.size());
    }

    public static double squaredMean(List<Double> doubles) {
        return doubles.stream().mapToDouble(e -> Math.pow(e, 2)).sum() / doubles.size();
    }

    public static double harmonicMean(List<Double> doubles) {
        return doubles.size() / doubles.stream().mapToDouble(e -> 1.0 / e).sum();
    }
}
