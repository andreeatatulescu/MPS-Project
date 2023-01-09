package local;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileSet {

    private static final List<File> fileList = Arrays.stream(Objects.requireNonNull(new File("src/local/MPS-Local").listFiles())).toList();

    public static final List<File> testSet = fileList.subList(fileList.size() * 95 / 100, fileList.size());
    public static final List<File> validationSet = fileList.subList(fileList.size() * 70 / 100, fileList.size() * 95 / 100);
    public static final List<File> trainSet = fileList.subList(0, fileList.size() * 70 / 100);
}
