import json
from abc import ABC, abstractmethod
from typing import List, Dict

from .operation import Operation
from ...image import Image, ThresholdType


class Node(ABC):
    DISCRIMINATOR = "Undefined"

    @abstractmethod
    def calculate(self, image: Image) -> float:
        pass


class IfNode(Node):
    DISCRIMINATOR = "IfNode"

    def __init__(self, condition_node: Node, condition: str, on_true: Node, on_false: Node):
        self.condition_node = condition_node
        self.condition = condition
        self.on_true = on_true
        self.on_false = on_false

    def calculate(self, image: Image) -> float:
        if eval(f'{self.condition_node.calculate(image)} {self.condition}'):
            return self.on_true.calculate(image)

        return self.on_false.calculate(image)


class ForNode(Node):
    DISCRIMINATOR = "ForNode"

    def __init__(self, children: List[Node], operation: Operation):
        if not children:
            raise ValueError("Children list in a ForNode should be not empty")

        self.operation = operation
        self.children = children

    def calculate(self, image: Image) -> float:
        return self.operation.execute(image)


class AtomNode(Node):
    DISCRIMINATOR = "AtomNode"

    def __init__(self, threshold_type: ThresholdType):
        self.threshold_type = threshold_type

    def calculate(self, image: Image) -> float:
        return image.threshold(self.threshold_type)


class Tree:
    def __init__(self, root: Node, **kwargs):
        self.root = root
        if "performance" in kwargs:
            self.performance = kwargs["performance"]
        else:
            self.performance = None

    def evaluate(self, images):
        thresholds = [self.root.calculate(image) for image in images]
        f_measures = [image.f_measure(threshold) for image, threshold in zip(images, thresholds)]

        return sum(f_measures) / len(f_measures)

    def stringify(self) -> str:
        return stringify_tree(self)

    @staticmethod
    def parse(source: str):
        return parse_tree(source)


def parse_tree(source: str):
    values = json.loads(source)

    performance = values["performance"]
    root = parse_node(values["root"])

    return Tree(root, performance=performance)


def parse_node(source: Dict):
    node_type = source["type"]

    if node_type == "IfNode":
        return parse_if(source)
    if node_type == "ForNode":
        return parse_for(source)
    if node_type == "AtomNode":
        return parse_atom(source)

    raise ValueError()


def parse_atom(source: Dict):
    threshold_type = [x for x in ThresholdType.values() if x.name == source["threshold_type"]][0]

    return AtomNode(threshold_type)


def parse_if(source: Dict):
    condition_node = parse_node(source["condition_node"])
    condition = source["condition"]
    on_true = parse_node(source["on_true"])
    on_false = parse_node(source["on_false"])

    return IfNode(condition_node, condition, on_true, on_false)


def parse_for(source: Dict):
    children = [parse_node(x) for x in source["children"]]
    operation = parse_operation(source["operation"])

    return ForNode(children, operation)


def parse_operation(source):
    operations = Operation.all()

    return [x.get_instance() for x in operations if x.DISCRIMINATOR == source][0]


def stringify_tree(source):
    root = preprocess_node(source.root)
    values = {
        "root": root,
        "performance": source.performance
    }

    return json.dumps(values)


def preprocess_node(source):
    if source.DISCRIMINATOR == AtomNode.DISCRIMINATOR:
        return preprocess_atom(source)
    if source.DISCRIMINATOR == IfNode.DISCRIMINATOR:
        return preprocess_if(source)
    if source.DISCRIMINATOR == ForNode.DISCRIMINATOR:
        return preprocess_for(source)

    raise ValueError()


def preprocess_atom(source):
    return {
        "type": "AtomNode",
        "threshold_type": source.threshold_type.name
    }


def preprocess_if(source):
    return {
        "type": "IfNode",
        "condition_node": preprocess_node(source.condition_node),
        "condition": source.condition,
        "on_true": preprocess_node(source.on_true),
        "on_false": preprocess_node(source.on_false)
    }


def preprocess_for(source):
    return {
        "type": "ForNode",
        "children": [preprocess_node(x) for x in source.children],
        "operation": preprocess_operation(source.operation)
    }


def preprocess_operation(source):
    return source.DISCRIMINATOR
