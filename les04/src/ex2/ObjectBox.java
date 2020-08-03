package ex2;

import java.util.HashSet;

public class ObjectBox {
    private final HashSet<Object> objects = new HashSet<>();

    public void addObject(Object o) {
        objects.add(o);
    }

    public void deleteObject(Object o) {
        objects.remove(o);
    }

    public String dump() {
        return objects.toString();
    }
}
