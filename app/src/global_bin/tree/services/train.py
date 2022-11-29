from concurrent.futures import ThreadPoolExecutor
from random import randrange, random

from .best_tree_manager import BestTreeManager
from ..common import *
from ...image.io import find_train_set, find_validation_set

best_tree_manager = BestTreeManager.get_instance()
task_executor = ThreadPoolExecutor(max_workers=100)


def random_tree(depth=None) -> Tree:
    if depth is None:
        depth = randrange(10)

    return Tree(random_node_async(depth).result())


def random_node_async(depth):
    if depth <= 5:
        return task_executor.submit(random_node, depth)

    node_types = [IfNode, ForNode]
    node_type = node_types[randrange(len(node_types))]

    if node_type == IfNode:
        return random_if_async(depth)

    return random_for_async(depth)


def random_node(depth) -> Node:
    if depth == 0:
        return random_atom()

    node_types = [IfNode, ForNode]
    node_type = node_types[randrange(len(node_types))]

    if node_type == IfNode:
        return random_if(depth)

    return random_for(depth)


def random_atom() -> AtomNode:
    return AtomNode(ThresholdType.random())


def random_if_async(depth):
    value = random()
    conditions = ['<', '>', '==', '!=']
    condition = f'{conditions[randrange(len(conditions))]} {value}'

    condition_node = random_atom()
    if_true = random_node_async(depth - 1)
    if_false = random_node_async(depth - 1)

    return task_executor.submit(lambda: IfNode(condition_node, condition, if_true.result(), if_false.result()))


def random_if(depth) -> IfNode:
    if depth <= 0:
        raise ValueError("IfNode accepts only depth >= 1")

    conditions = ['<', '<=', '>', '>=', '==', '!=']
    condition = conditions[randrange(len(conditions))]
    value = random()

    condition_node = random_atom()
    if_true = random_node(depth - 1)
    if_false = random_node(depth - 1)

    return IfNode(condition_node, f'{condition} {value}', if_true, if_false)


def random_for_async(depth):
    children_size = 15
    children = [random_node_async(randrange(depth)) for _ in range(children_size)]
    operation = random_operation()

    return task_executor.submit(lambda: ForNode([x.result() for x in children], operation))


def random_for(depth) -> ForNode:
    children_size = ThresholdType.count()
    children = [random_node(depth - 1) for _ in range(children_size)]
    operation = random_operation()

    return ForNode(children, operation)


def random_operation() -> Operation:
    operations = Operation.all()
    return operations[randrange(len(operations))].get_instance()


def train(n=10):

    train_set = find_train_set()

    while True:
        tree = random_tree()
        performance = tree.evaluate(train_set)
        # if performance > 0.5:
        if performance > 50:
            tree.performance = tree.evaluate(find_validation_set())
            print(f'{performance} - {tree.performance}')
            best_tree_manager.change_if_better(tree)
