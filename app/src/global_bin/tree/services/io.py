from math import floor
from time import time

from ..common import Tree

__DIRECTORY = "../files/tree/"


def save(tree: Tree, filename):
    with open(f'{__DIRECTORY}{filename}', "w+") as file:
        file.write(tree.stringify())


def save_with_timestamp(tree: Tree):
    filename = f'{floor(tree.performance)}-{time()}.json'
    save(tree, filename)


def save_best(tree: Tree):
    filename = "best_tree.json"
    save(tree, filename)


def find(filename) -> Tree:
    with open(f'{__DIRECTORY}{filename}') as file:
        content = file.read()
        return Tree.parse(content)


def find_best() -> Tree:
    return find("best_tree.json")
