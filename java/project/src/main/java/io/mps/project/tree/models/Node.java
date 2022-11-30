package io.mps.project.tree.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.mps.project.image.Image;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AtomNode.class, name = "ATOM_NODE"),
        @JsonSubTypes.Type(value = IfNode.class, name = "IF_NODE"),
        @JsonSubTypes.Type(value = ForNode.class, name = "FOR_NODE"),
})
public interface Node {

    double calculate(Image image);
}
