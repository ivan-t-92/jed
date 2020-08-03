package ex3;

import java.util.HashSet;

public class ObjectBox<E> {
    protected HashSet<E> objectSet = new HashSet<>();

    public void addObject(E o) {
        objectSet.add(o);
    }

    public void deleteObject(E o) {
        objectSet.remove(o);
    }

    public String dump() {
        return objectSet.toString();
    }

    @Override
    public int hashCode() {
        return objectSet.hashCode();
    }

    @Override
    public String toString() {
        return objectSet.toString();
    }
}
