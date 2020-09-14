package ex2;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        RandomWordGenerator wordGen = new RandomWordGenerator(15);
        String[] words = new String[100];
        for (int i = 0; i < words.length; i++) {
            words[i] = wordGen.randomWord();
        }

        String dirName = "C:\\tmp";

        Path dir = Path.of(dirName);
        if (!Files.exists(dir)) {
            try {
                Files.createDirectory(dir);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        getFiles(dirName, 3, 10000, words);
    }

    private static void getFiles(String path, int n, int size, String[] words) {
        final int maxWords = 15;
        final int maxSentences = 20;
        final int maxParagraphs = 4;
        final char[] sentenceEnd = {'.', '!', '?'};

        Random random = new Random();
        int wordCount = random.nextInt(maxWords) + 1;
        int sentenceCount = random.nextInt(maxSentences) + 1;
        int paragraphCount = random.nextInt(maxParagraphs) + 1;

        for (int fileIndex = 0; fileIndex < n; fileIndex++) {
            StringBuilder sb = new StringBuilder();

            for (int paragraphIndex = 0; paragraphIndex < paragraphCount; paragraphIndex++) {
                for (int sentenceIndex = 0; sentenceIndex < sentenceCount; sentenceIndex++) {
                    String word = words[random.nextInt(words.length)];
                    sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1));
                    for (int wordIndex = 1; wordIndex < wordCount; wordIndex++) {
                        sb.append(' ').append(words[random.nextInt(words.length)]);
                    }
                    sb.append(sentenceEnd[random.nextInt(sentenceEnd.length)]).append(' ');
                }
                sb.append("\r\n");
            }

            String fileName = Path.of(path, "file" + fileIndex + ".txt").toString();
            try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
                raf.setLength(size);
                byte[] bytes = sb.toString().getBytes();
                raf.write(bytes, 0, Integer.min(bytes.length, size));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class RandomWordGenerator {
        private final Random random = new Random();
        private final int maxLetters;

        RandomWordGenerator(int maxLetters) {
            this.maxLetters = maxLetters;
        }

        String randomWord() {
            int letterCount = random.nextInt(maxLetters) + 1;
            StringBuilder sb = new StringBuilder(letterCount);
            for (int i = 0; i < letterCount; i++) {
                sb.append((char) ('a' + random.nextInt('z' - 'a')));
            }
            return sb.toString();
        }
    }
}
