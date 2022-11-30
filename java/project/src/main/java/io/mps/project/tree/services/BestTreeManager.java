package io.mps.project.tree.services;

import io.mps.project.tree.models.Node;
import io.mps.project.tree.models.PerformanceTree;

public class BestTreeManager {
    private static final String FILENAME = "best_tree.json";

    private final TreeService treeService = TreeService.getInstance();
    private final TreeEvaluator treeEvaluator = TreeEvaluator.getInstance();

    private Double bestTreePerformance;

    public synchronized void changeIfBetter(Node tree) {
        var performance = treeEvaluator.test(tree);
        System.out.println("Performance on test set " + performance);

        if (performance > 50) {
            performance = treeEvaluator.validate(tree);
            System.out.println("Performance on validation set " + performance);

            if (bestTreePerformance == null || performance > bestTreePerformance) {
                var performanceTree = new PerformanceTree();
                performanceTree.setTree(tree);
                performanceTree.setPerformance(performance);

                treeService.save(performanceTree, FILENAME);
                bestTreePerformance = performance;
                System.out.println("Best tree is changed. Now best performance is " + bestTreePerformance);
            }
        }
    }

    private BestTreeManager() {
        treeService.find(FILENAME, PerformanceTree.class)
                .ifPresent(tree -> bestTreePerformance = tree.getPerformance());
    }

    private static final BestTreeManager INSTANCE = new BestTreeManager();

    public static BestTreeManager getInstance() {
        return INSTANCE;
    }
}
