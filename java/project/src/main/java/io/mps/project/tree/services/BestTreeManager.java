package io.mps.project.tree.services;

import io.mps.project.tree.models.Node;
import io.mps.project.tree.models.PerformanceTree;

import java.util.Date;
import java.util.Optional;

public class BestTreeManager {
    private static final String FILENAME = "best_tree.json";

    private final TreeIOService treeService = TreeIOService.getInstance();
    private final TreeEvaluator treeEvaluator = TreeEvaluator.getInstance();

    private PerformanceTree bestTree;

    public synchronized void changeIfBetter(Node tree) {
        var start = new Date().getTime();
        var performance = treeEvaluator.train(tree);
        var end = new Date().getTime();

        System.out.println("Performance on test set " + performance + " with complexity " + treeEvaluator.complexity(tree) + " executed in " + (end - start) / 1000.0);

        if (performance > 50) {
            performance = treeEvaluator.validate(tree);
            System.out.println("Performance on validation set " + performance);

            if (bestTree == null || performance > bestTree.getPerformance()) {
                var performanceTree = new PerformanceTree();
                performanceTree.setTree(tree);
                performanceTree.setPerformance(performance);

                treeService.save(performanceTree, FILENAME);
                bestTree = performanceTree;
                System.out.println("Best tree is changed. Now best performance is " + bestTree.getPerformance());
            }
        }
    }

    public synchronized Optional<Node> get() {
        return Optional.ofNullable(bestTree).map(PerformanceTree::getTree);
    }

    private BestTreeManager() {
        treeService.find(FILENAME, PerformanceTree.class)
                .ifPresent(tree -> bestTree = tree);
    }

    private static final BestTreeManager INSTANCE = new BestTreeManager();

    public static BestTreeManager getInstance() {
        return INSTANCE;
    }
}
