from threading import Lock

from .io import save_best, find_best


class BestTreeManager:
    INSTANCE = None

    @staticmethod
    def get_instance():
        if BestTreeManager.INSTANCE is None:
            BestTreeManager.INSTANCE = BestTreeManager()

        return BestTreeManager.INSTANCE

    def __init__(self):
        self.lock = Lock()
        self.tree = find_best()

    def change_if_better(self, tree):
        with self.lock:
            if self.tree is None or self.tree.performance < tree.performance:
                self.tree = tree
                save_best(tree)

    def change(self, tree):
        with self.lock:
            self.tree = tree
            save_best(tree)
