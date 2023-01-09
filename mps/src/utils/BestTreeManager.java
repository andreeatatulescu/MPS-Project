package utils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BestTreeManager {

    public static final BestTreeManager LOCAL = new BestTreeManager("localBestTree.txt");
    public static final BestTreeManager GLOBAL = new BestTreeManager("globalBestTree.txt");

    private String fileName;
    private Tree bestTree;
    private double bestPerformance;

    private BestTreeManager(String fileName) {
        this.fileName = fileName;
        init();
    }

    public void changeIfBetter(Tree tree, double performance) {
        if (bestTree == null || performance > bestPerformance) {
            bestTree = tree;
            bestPerformance = performance;

            saveTree(tree, performance);
        }
    }

    public Tree getTree() {
        return bestTree;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;

        init();
    }

    public void reset() {
        if (bestTree != null) {
            bestTree = null;
            bestPerformance = 0;
            new File(fileName).delete();
        }
    }

    public void init() {
        try (var scanner = new Scanner(new FileReader(fileName))) {
            List<List<Integer>> threshIdx = parseList(scanner.nextLine());
            List<List<Integer>> operIdx = parseList(scanner.nextLine());
            bestTree = new Tree(threshIdx, operIdx);
            bestPerformance = scanner.nextFloat();
        } catch (FileNotFoundException e){
            bestTree = null;
            bestPerformance = 0;
        }
    }

    private void saveTree(Tree tree, double performance) {
        try (var writer = new FileWriter(fileName)) {
            writer.append(stringify(tree.getThreshIdx()))
                .append("\n")
                .append(stringify(tree.getOperIdx()))
                .append("\n")
                .append(String.valueOf(performance))
                .flush();
        } catch (IOException e) {
        }
    }

    private static List<List<Integer>> parseList(String line) {
        return Arrays.stream(line.split(";"))
                .map(list -> Arrays.stream(list.split(",")).map(Integer::parseInt).toList())
                .collect(Collectors.toList());
    }

    private static String stringify(List<List<Integer>> list) {
        return list.stream()
                .map(l -> l.stream().map(Object::toString).collect(Collectors.joining(",")))
                .collect(Collectors.joining(";"));
    }
}
