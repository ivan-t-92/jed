package ex1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final String inputFileName = "C:\\Users\\ivan\\IdeaProjects\\jed\\les07\\src\\ex1\\text.txt";
    private static final String outputFileName = inputFileName + ".dict.txt";

    public static void main(String[] args) {
        Path inputPath = Path.of(inputFileName);
        String fileContent;
        try {
            fileContent = Files.readString(inputPath);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String[] words = fileContent.toLowerCase().split("[^\\wа-я]");
        List<String> dict = Arrays.stream(words).distinct().sorted().collect(Collectors.toList());

        Path outputPath = Path.of(outputFileName);
        try {
            Files.write(outputPath, dict);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
