package io.mps.project.tree.models;

import io.mps.project.image.Image;
import io.mps.project.image.ThresholdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtomNode implements Node {
    public ThresholdType thresholdType;

    @Override
    public double calculate(Image image) {
        return image.threshold(thresholdType);
    }
}
