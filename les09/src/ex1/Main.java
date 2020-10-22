package ex1;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;

public class Main {
    private static final Path path = Path.of(System.getProperty("java.io.tmpdir"), "time.txt");

    private static final MySorter mySorter = new MySorter();
    private static final TimeMeasuringHandler handler = new TimeMeasuringHandler(mySorter);
    private static final Sorter sorterProxy = (Sorter) Proxy.newProxyInstance(
            Main.class.getClassLoader(),
            mySorter.getClass().getInterfaces(),
            handler);

    public static void main(String[] args) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            measureSortingTime(1000, writer);
            measureSortingTime(10000, writer);
            measureSortingTime(100000, writer);
            Desktop.getDesktop().open(new File(path.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void measureSortingTime(int nElements, Writer writer) throws IOException {
        writer.write(nElements + " elements\n");

        Random random = new Random(nElements);
        int[] ints = new int[nElements];
        for (int i = 0; i < nElements; i++) {
            ints[i] = random.nextInt();
        }
        for (int i = 0; i < 10; i++) {
            System.out.println("n=" + nElements + ", run " + (i + 1) + "/10");
            sorterProxy.bubbleSort(Arrays.copyOf(ints, nElements));
            sorterProxy.standardSort(Arrays.copyOf(ints, nElements));
        }

        handler.dumpResults(writer);
        handler.clearResults();
        writer.write("\n");
    }
}
