package io.mps.project.tree.models;

import io.mps.project.image.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IfNode implements Node {

    private Condition condition;
    private Node ifTrue;
    private Node ifFalse;


    @Override
    public double calculate(Image image) {
        if (condition.verify(image)) {
            return ifTrue.calculate(image);
        }

        return ifFalse.calculate(image);
    }
}
