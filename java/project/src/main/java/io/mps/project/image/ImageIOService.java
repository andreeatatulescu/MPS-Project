package io.mps.project.image;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ImageIOService {
    private static final String DIRECTORY = "src/main/resources/images";

    private List<Image> fullSet;
    private List<Image> trainSet;
    private List<Image> validationSet;
    private List<Image> testSet;


    public synchronized List<Image> getAllFiles() {
        if (fullSet == null) {
            File directory = new File(DIRECTORY);
            fullSet = Arrays.stream(directory.listFiles()).map(this::load).collect(Collectors.toList());
        }

        return fullSet;
    }

    public synchronized List<Image> getTrainSet() {
        if (trainSet == null) {
            trainSet = getAllFiles().subList(0, (int) (fullSet.size() * 0.7));
        }

        return trainSet;
    }

    public synchronized List<Image> getValidationSet() {
        if (validationSet == null) {
            validationSet = getAllFiles().subList((int) (fullSet.size() * 0.7), (int) (fullSet.size() * 0.95));
        }

        return validationSet;
    }

    public synchronized List<Image> getTestSet() {
        if (testSet == null) {
            testSet = getAllFiles().subList((int) (fullSet.size() * 0.95), fullSet.size());
        }

        return testSet;
    }

    public Image load(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
               var allThresholds = parseDoubleLine(reader.readLine());
               var fMeasures = parseDoubleLine(reader.readLine());

               var idealThreshold = allThresholds.get(0);
               var thresholds = allThresholds.subList(1, allThresholds.size());

               return new Image(thresholds, fMeasures, idealThreshold);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Double> parseDoubleLine(String doubles) {
        return Arrays.stream(doubles.split(","))
                .map(Double::parseDouble)
                .toList();
    }

    private ImageIOService() {}

    private static final ImageIOService INSTANCE = new ImageIOService();

    public static ImageIOService getInstance() {
        return INSTANCE;
    }
}
