package io.mps.project;

import io.mps.project.tree.services.BestTreeManager;
import io.mps.project.tree.services.TreeEvaluator;
import io.mps.project.tree.services.TreeTrainer;

import java.util.Scanner;

public class Runner {

    private static final TreeTrainer treeTrainer = TreeTrainer.getInstance();
    private static final TreeEvaluator treeEvaluator = TreeEvaluator.getInstance();
    private static final BestTreeManager bestTreeManager = BestTreeManager.getInstance();

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Select command: \"EXIT\", \"TRAIN\", \"TEST\"");

                String cmd = scanner.next();
                if ("EXIT".equalsIgnoreCase(cmd)) {
                    break;
                } else if ("TRAIN".equalsIgnoreCase(cmd)) {
                    treeTrainer.train(10);
                } else if ("TEST".equalsIgnoreCase(cmd)) {
                    bestTreeManager.get().ifPresentOrElse(
                            tree -> System.out.println("Performance of best tree: " + treeEvaluator.test(tree)),
                            () -> System.out.println("No best tree found. Please train tree")
                    );
                }
            }
        }
    }
}
