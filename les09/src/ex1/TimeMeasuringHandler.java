package ex1;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.ArrayList;

public class TimeMeasuringHandler implements InvocationHandler {

    private final Sorter sorter;
    private final HashMap<String, ArrayList<Long>> methodTimes = new HashMap<>();

    public TimeMeasuringHandler(Sorter sorter) {
        this.sorter = sorter;
        for (Method method : sorter.getClass().getMethods()) {
            methodTimes.put(method.getName(), new ArrayList<>());
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = method.invoke(sorter, args);
        long time = System.currentTimeMillis() - startTime;
        methodTimes.computeIfAbsent(method.getName(), s -> new ArrayList<>()).add(time);
        return result;
    }

    public void dumpResults(Writer writer) throws IOException {
        for (HashMap.Entry<String, ArrayList<Long>> entry : methodTimes.entrySet()) {
            String methodName = entry.getKey();
            ArrayList<Long> times = entry.getValue();
            if (!times.isEmpty()) {
                double avg = times.stream().mapToLong(Long::valueOf).average().getAsDouble();
                writer.write(methodName + ": avg. time " + avg + " ms\n");
            }
        }
    }

    public void clearResults() {
        methodTimes.clear();
    }
}
