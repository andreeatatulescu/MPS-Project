package io.mps.project.image;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Image {
    private List<Double> thresholds;
    private List<Double> fMeasures;
    private Double idealThreshold;

    public double threshold(ThresholdType thresholdType) {
        return thresholds.get(thresholdType.getValue());
    }

    public double measure(double threshold) {
        return fMeasures.get((int)(threshold * 255));
    }
}
