package io.mps.project.tree.models;

import io.mps.project.image.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Condition {
    private Node node;
    private Type type;
    private double reference;

    public boolean verify(Image image) {
        double threshold = node.calculate(image);

        return switch (type) {
            case LESS -> threshold < reference;
            case GREATER -> threshold > reference;
            case EQUAL -> threshold == reference;
        };
    }

    public enum Type { GREATER, LESS, EQUAL }
}
