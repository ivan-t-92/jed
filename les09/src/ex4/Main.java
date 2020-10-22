package ex4;

import javax.tools.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    private static final String className = "SomeClass";

    public static void main(String[] args) {
        Path sourceFilePath = Path.of(System.getProperty("java.io.tmpdir"), className + ".java");
        Path classFilePath = Path.of(System.getProperty("java.io.tmpdir"), className + ".class");

        System.out.println("Input code here:");
        ArrayList<String> lines = readCode();
        writeJavaSourceFile(lines, sourceFilePath);
        compile(sourceFilePath);
        Worker worker = constructWorker(classFilePath);
        System.out.println("Your code output:");
        worker.doWork();

        try {
            Files.delete(sourceFilePath);
            Files.delete(classFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Worker constructWorker(Path classFilePath) {
        ClassLoader classLoader = new MyClassLoader(className, classFilePath);
        Class<?> workerClass;
        try {
            workerClass = classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            return (Worker) workerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static void compile(Path path) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        File location = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        try {
            fileManager.setLocation(StandardLocation.CLASS_PATH, Collections.singletonList(location));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(path);
        compiler.getTask(null, null, null, null, null, compilationUnits).call();
        try {
            fileManager.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeJavaSourceFile(ArrayList<String> lines, Path path) {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            bufferedWriter.write("import ex4.Worker;\n");
            bufferedWriter.write("public class " + className + " implements Worker {\n");
            bufferedWriter.write("public void doWork() {\n");

            for (String s : lines) {
                bufferedWriter.write(s);
            }

            bufferedWriter.write("}\n");
            bufferedWriter.write("}\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<String> readCode() {
        ArrayList<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        String line;
        while (!(line = scanner.nextLine()).isEmpty()) {
            lines.add(line);
        }
        return lines;
    }
}
