package global;

import utils.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class ThreadUtils {

    private static final int numberOfThreads = 16;
    private static final Thread[] threads = new Thread[numberOfThreads];
    private static final Semaphore semaphore = new Semaphore(1);

    /**
     * This is a parent method for runThread to start the calculations on threads and
     * obtain the result we want on all the files.
     */
    public static void runner(List<File> filesList, Tree tree, List<Float> result) {
        for (int t = 0; t < numberOfThreads; t++) {
            int thread = t;
            threads[t] = new Thread(() -> {
                try {
                    ThreadUtils.runThread(filesList, thread, tree, result);
                } catch (Exception ignored) {}
            });
        }
        for (Thread t1 : threads) {
            t1.start();
        }
        for (Thread t2 : threads) {
            try {
                t2.join();
            } catch (Exception ignored) {}
        }
    }

    /**
     * Method to obtain the scores of the files based on the thread.
     * @param filesList the files on which we apply the tree.
     * @param thread the current thread
     * @param tree the tree we apply on the files
     * @param result the scores
     */
    public static void runThread(List<File> filesList, int thread, Tree tree, List<Float> result) throws Exception {
        // Obtaining the start and the end for each thread to divide the files between each other.
        List<Integer> range = Utils.getThreadRange(filesList.size(), numberOfThreads, thread);
        List<File> inFiles = filesList.subList(range.get(0), range.get(1));

        List<Float> tempThresh = new ArrayList<>();
        List<Float> tempResult = new ArrayList<>();

        for (File file : inFiles) {
            // Opening each file and obtaining the threshold & F measures.
            List<List<Float>> rez = CsvParser.parse(file);
            List<Float> thresholds = rez.get(0);
            List<Float> fMeasures = rez.get(1);
            // Saving the ideal threshold and removing it from the thresholds list.
            Float idealThreshold = thresholds.get(0);
            thresholds.remove(0);
            // This is the maximum width of the operations at each level.
            int maxWidth = ALL_OPERATIONS.size();
            /* Considering we start from the leaves and go to root, in the index list we have the indexes stored
            the same way. */
            int depth = 0;
            while (maxWidth >= 1) {
                for (int i = 0; i < maxWidth; i++) {
                    Float thr1 = thresholds.get(tree.getThreshIdx().get(depth).get(i * 2));
                    Float thr2 = thresholds.get(tree.getThreshIdx().get(depth).get(i * 2 + 1));
                    String operation = ALL_OPERATIONS.get(tree.getOperIdx().get(depth).get(i));
                    if (Objects.equals(operation, "CubicMean")) {
                        tempThresh.add(Means.cubic_mean(thr1, thr2));
                    } else if (Objects.equals(operation, "SquareMean")) {
                        tempThresh.add(Means.square_mean(thr1, thr2));
                    } else if (Objects.equals(operation, "ArithmeticMean")) {
                        tempThresh.add(Means.mean(thr1, thr2));
                    } else if (Objects.equals(operation, "HarmonicMean")) {
                        tempThresh.add(Means.harmonic_mean(thr1, thr2));
                    } else if (Objects.equals(operation, "GeometricMean")) {
                        tempThresh.add(Means.geometric_mean(thr1, thr2));
                    } else if (Objects.equals(operation, "Minimum")) {
                        tempThresh.add(Math.min(thr1, thr2));
                    } else if (Objects.equals(operation, "Maximum")) {
                        tempThresh.add(Math.max(thr1, thr2));
                    }
                }
                depth++;
                // Since there is one threshold that remains the first time, we must use it in the next level.
                if (maxWidth == ALL_OPERATIONS.size()) {
                    tempThresh.add(thresholds.get(thresholds.size() - 1));
                    maxWidth = (maxWidth + 1) / 2;
                } else {
                    maxWidth /= 2;
                }
                thresholds.clear();
                thresholds.addAll(tempThresh);
                tempThresh.clear();
            }
            // Scaling the result based on the fMeasure of the ideal threshold.
            tempResult.add(fMeasures.get((int)(thresholds.get(0) * 255)) / fMeasures.get((int)(idealThreshold*255))*100);
        }
        semaphore.acquire();
        result.addAll(tempResult);
        semaphore.release();
    }

    public static final List<String> ALL_OPERATIONS =
            Arrays.asList("CubicMean", "SquareMean", "ArithmeticMean", "GeometricMean", "HarmonicMean", "Minimum", "Maximum");
}
