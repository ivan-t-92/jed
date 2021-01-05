package ex2;

import java.io.PrintStream;

public class MyLogger {
    private final static MyLogger instance = new MyLogger();

    private final PrintStream os;

    private MyLogger() {
        os = System.out;
    }

    public static MyLogger getInstance() {
        return instance;
    }

    public void log(String s) {
        os.println(s);
    }
}
