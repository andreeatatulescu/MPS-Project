package local;

import utils.BestTreeManager;
import utils.Utils;
import utils.Tree;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MainL {
    private static final List<String> operations = Arrays.asList("CubicMean", "SquareMean", "ArithmeticMean",
            "GeometricMean", "HarmonicMean");
    public static Tree train() throws IOException {
        // Obtaining the list of files from the data folder and randomizing it for different outputs.

        List<Tree> bestTrees = new ArrayList<>();
        float scorePerSet;

        List<List<List<Float>>> filePixels = new ArrayList<>();
        List<List<List<Float>>> computedWindows = new ArrayList<>();
        List<List<Float>> thresholds = new ArrayList<>();

        // Reading the pixels from the first 70% of the files and computing their windows.
        ThreadUtils.runnerRead(FileSet.trainSet, filePixels);
        Utils.compWindows(filePixels, computedWindows);

        System.out.println("The scores for the training set are:");
        // Choosing the best 5 trees with a score higher than 54 from the first 70% of files.
        while (bestTrees.size() < 5) {
            thresholds.clear();
            Tree tree = generateRandomTree();
            ThreadUtils.runnerThresholds(computedWindows, operations, tree, thresholds);
            scorePerSet = Utils.getScore(filePixels, thresholds);
            System.out.println(scorePerSet * 100);
            if (scorePerSet * 100 > 54) {
                System.out.println("Found a score higher than 54: " + scorePerSet * 100);
                bestTrees.add(tree);
            }
        }

        float max = 0;
        int idx = 0;
        System.out.println("The scores for the validation set are:");

        /* Keeping the best tree out of the five after being used on the validation set, based on the score obtained
        on the next 25% of the files. */
        filePixels.clear();
        computedWindows.clear();
        ThreadUtils.runnerRead(FileSet.validationSet, filePixels);
        Utils.compWindows(filePixels, computedWindows);

        for (Tree tre : bestTrees) {
            thresholds.clear();
            ThreadUtils.runnerThresholds(computedWindows, operations, tre, thresholds);
            scorePerSet = Utils.getScore(filePixels, thresholds);
            if (scorePerSet > max) {
                max = scorePerSet;
                idx = bestTrees.indexOf(tre);
            }
            System.out.println(scorePerSet * 100);
        }
        System.out.println("The best score was obtained by tree number " + (idx + 1) + " and it was " + max * 100 + ".");
        // Testing the best tree on the last 5% of the files;
        filePixels.clear();
        computedWindows.clear();
        ThreadUtils.runnerRead(FileSet.trainSet, filePixels);
        Utils.compWindows(filePixels, computedWindows);

        thresholds.clear();
        ThreadUtils.runnerThresholds(computedWindows, operations, bestTrees.get(idx), thresholds);
        scorePerSet = Utils.getScore(filePixels, thresholds);
        System.out.println("The score obtained on the test set is " + scorePerSet * 100 + ".");

        // Writing the best tree to the Local Binarization Output file.
        PrintWriter writer = new PrintWriter("LocalBinarization.txt", StandardCharsets.UTF_8);
        writer.println("The indexes of the thresholds and operations, starting from leaves to root, are:\n");
        int maxWidth = operations.size();
        int depth = 0;
        boolean second = false;
        while (maxWidth >= 1) {
            depth = Utils.getDepthAndPrint(operations, bestTrees, idx, writer, maxWidth, depth);
            if (maxWidth == operations.size()) {
                maxWidth = 1;
                second = true;
            } else if (maxWidth == 1 && second){
                maxWidth *= 2;
                second = false;
            } else {
                maxWidth /= 2;
            }
        }
        writer.close();

        BestTreeManager.LOCAL.changeIfBetter(bestTrees.get(idx), scorePerSet);

        return bestTrees.get(idx);
    }

    public static void test() {
        var tree = BestTreeManager.LOCAL.getTree();

        if (tree == null) {
            System.out.println("Tree is not generated. Train tree!");
            return;
        }
        var scorePerSet = testTreePerformance(tree, FileSet.testSet);
        System.out.println("The score obtained on the test set is " + scorePerSet + ".");
    }

    public static float testTreePerformance(Tree tree, List<File> files) {
        List<List<List<Float>>> filePixels = new ArrayList<>();
        List<List<List<Float>>> computedWindows = new ArrayList<>();
        List<List<Float>> thresholds = new ArrayList<>();

        ThreadUtils.runnerRead(files, filePixels);
        Utils.compWindows(filePixels, computedWindows);

        ThreadUtils.runnerThresholds(computedWindows, operations, tree, thresholds);

        return Utils.getScore(filePixels, thresholds);
    }

    public static Tree generateRandomTree() {
        Tree tree = new Tree();
        tree.ourShuffle(operations.size(), operations.size() * 2);
        tree.ourShuffle(operations.size(), operations.size());
        tree.ourShuffle(operations.size(), operations.size() - 1);
        tree.ourShuffle(operations.size(), (operations.size() - 1) / 2);

        return tree;
    }

    public static void main(String[] args) throws IOException {
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
