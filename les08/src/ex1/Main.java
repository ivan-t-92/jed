package ex1;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Available processors: " + availableProcessors);
        int[] arr = {20, 1, 5, 10, 4, 2, 3, 15, 0};
        BigInteger[] factArr = factorials(arr, 1);
        System.out.println(Arrays.toString(arr) + "! -> " + Arrays.toString(factArr));

        Random r = new Random(0);
        arr = new int[100];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = r.nextInt(50000);
        }

        System.out.println("Benchmark...");
        for (int i = 1; i <= availableProcessors; i++) {
            factorials(arr, i);
        }
        for (int i = 1; i <= availableProcessors; i++) {
            runAndPrintTime(arr, i);
        }
    }

    private static void runAndPrintTime(int[] arr, int nThreads) {
        long startTime = System.currentTimeMillis();
        BigInteger[] factArr = factorials(arr, nThreads);
        long time = System.currentTimeMillis() - startTime;
        System.out.println("nThreads=" + nThreads + ", time: " + time + " ms");
    }

    private static BigInteger[] factorials(int[] arr, int nThreads) {
        // Идея: для отсортированного массива n[i]! = n[i-1]! * ((n[i-1]+1)*(n[i-1]+2)*...*n[i])

        int size = arr.length;
        ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(nThreads);

        Integer[] index = new Integer[arr.length];
        for (int i = 0; i < size; i++) {
            index[i] = i;
        }
        Arrays.sort(index, Comparator.comparingInt(o -> arr[o]));

        Future<BigInteger>[] futureFactArr = new Future[size];
        futureFactArr[index[0]] = executorService.submit(() -> factorial(arr[index[0]]));
        for (int i = 1; i < size; i++) {
            if (arr[index[i - 1]] != arr[index[i]]) {
                final int n1 = arr[index[i - 1]] + 1;
                final int n2 = arr[index[i]];
                futureFactArr[index[i]] = executorService.submit(() -> factorial(n1, n2));
            }
        }

        BigInteger[] factArr = new BigInteger[size];
        try {
            factArr[index[0]] = futureFactArr[index[0]].get();

            for (int i = 1; i < size; i++) {
                if (futureFactArr[index[i]] != null) {
                    factArr[index[i]] = factArr[index[i - 1]].multiply(futureFactArr[index[i]].get());
                } else {
                    factArr[index[i]] = factArr[index[i - 1]];
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        } finally {
            executorService.shutdown();
        }

        return factArr;
    }

    private static BigInteger factorial(int n) {
        return factorial(1, n);
    }

    private static BigInteger factorial(int n1, int n2) {
        BigInteger result = BigInteger.valueOf(n1);
        for (int i = n1 + 1; i <= n2; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
}
