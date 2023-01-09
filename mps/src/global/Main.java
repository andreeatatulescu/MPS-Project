package global;

import utils.BestTreeManager;
import utils.Means;
import utils.Tree;
import utils.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static Tree train() throws IOException {
        // Obtaining the list of files from the data folder and randomizing it for different outputs.
        List<String> operations = ThreadUtils.ALL_OPERATIONS;

        List<Tree> bestTrees = new ArrayList<>();

        float scorePerSet;
        System.out.println("The scores for the training set are:");
        // Choosing the best 5 trees with a score higher than 86 from the first 70% of files.
        while (bestTrees.size() < 5) {
            Tree tree = generateRandomTree();

            scorePerSet = testTreePerformance(tree, FileSet.trainSet);
            System.out.println(scorePerSet);
            if (scorePerSet > 86) {
                System.out.println("Found a score higher than 86: " + scorePerSet);
                bestTrees.add(tree);
            }
        }
        /* Keeping the best tree out of the five after being used on the validation set, based on the score obtained
        on the next 25% of the files. */
        float max = 0;
        int idx = 0;
        System.out.println("The scores for the validation set are:");
        for (Tree tree : bestTrees) {
            scorePerSet = testTreePerformance(tree, FileSet.trainSet);
            if (scorePerSet > max) {
                max = scorePerSet;
                idx = bestTrees.indexOf(tree);
            }
            System.out.println(scorePerSet);
        }
        System.out.println("The best score was obtained by tree number " + (idx + 1) + " and it was " + max + ".");
        // Testing the best tree on the last 5% of the files;
        scorePerSet = testTreePerformance(bestTrees.get(idx), FileSet.testSet);
        System.out.println("The score obtained on the test set is " + scorePerSet + ".");

        BestTreeManager.GLOBAL.changeIfBetter(bestTrees.get(idx), scorePerSet);

        // Writing the best tree to the Global Binarization Output file.
        PrintWriter writer = new PrintWriter("GlobalBinarization.txt", StandardCharsets.UTF_8);
        writer.println("The indexes of the thresholds and operations, starting from leaves to root, are:\n");
        int maxWidth = operations.size();
        int depth = 0;
        while (maxWidth >= 1) {
            depth = Utils.getDepthAndPrint(operations, bestTrees, idx, writer, maxWidth, depth);
            if (maxWidth == operations.size()) {
                maxWidth = (maxWidth + 1) / 2;
            } else {
                maxWidth /= 2;
            }
        }
        writer.close();

        return bestTrees.get(idx);
    }

    public static void test() {
        var tree = BestTreeManager.GLOBAL.getTree();

        if (tree == null) {
            System.out.println("Tree is not generated. Train tree!");
            return;
        }
        var scorePerSet = testTreePerformance(tree, FileSet.testSet);
        System.out.println("The score obtained on the test set is " + scorePerSet + ".");
    }


    public static float testTreePerformance(Tree tree, List<File> filesList) {
        var result = new ArrayList<Float>();
        ThreadUtils.runner(filesList, tree, result);

        return Means.mean_score(result);
    }

    public static Tree generateRandomTree() {
        var operations = ThreadUtils.ALL_OPERATIONS;

        Tree tree = new Tree();
        tree.ourShuffle(operations.size(), operations.size() * 2 + 1);
        tree.ourShuffle(operations.size(), operations.size() + 1);
        tree.ourShuffle(operations.size(), (operations.size() + 1) / 2);
        tree.ourShuffle(operations.size(), (operations.size() + 1) / 4);

        return tree;
    }

    public static void main(String[] args) throws Exception {
        var scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Command? [train, test, exit]");

            String cmd = scanner.next();

            if ("exit".equalsIgnoreCase(cmd)) {
                break;
            } else if ("train".equalsIgnoreCase(cmd)) {
                train();
            } else if ("test".equalsIgnoreCase(cmd)) {
                test();
            } else {
                System.out.printf("Undefined command '%s'".formatted(cmd));
            }
        }
    }
}