package io.mps.project.image;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ImageService {
    private static final String DIRECTORY = "src/main/resources/images";

    private List<Image> fullSet;
    private List<Image> testSet;
    private List<Image> validationSet;

    public synchronized List<Image> getAllFiles() {
        if (fullSet == null) {
            File directory = new File(DIRECTORY);
            fullSet = Arrays.stream(directory.listFiles()).map(this::load).toList();
        }

        return fullSet;
    }

    public synchronized List<Image> getTestFiles() {
        if (testSet == null) {
            testSet = getAllFiles().subList(0, (int) (fullSet.size() * 0.7));
        }

        return testSet;
    }

    public synchronized List<Image> getValidationSet() {
        if (validationSet == null) {
            validationSet = getAllFiles().subList((int) (fullSet.size() * 0.7), (int) (fullSet.size() * 0.95));
        }

        return validationSet;
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

    private ImageService() {}

    private static final ImageService INSTANCE = new ImageService();

    public static ImageService getInstance() {
        return INSTANCE;
    }
}
