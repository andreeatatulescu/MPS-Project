package io.mps.project.tree.models;

import io.mps.project.image.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForNode implements Node {

    private List<Node> children;
    private OperationType operationType;

    @Override
    public double calculate(Image image) {
        var thresholds = children.stream().map(node -> node.calculate(image)).toList();
        return Operations.get(operationType).apply(thresholds);
    }
}
