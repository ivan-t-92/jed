package ex4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MyClassLoader extends ClassLoader {
    private final String myClassName;
    private final Path myClassPath;
    private Class<?> loadedClass;

    public MyClassLoader(String myClassName, Path myClassPath) {
        this.myClassName = myClassName;
        this.myClassPath = myClassPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.equals(myClassName)) {
            if (loadedClass == null) {
                byte[] classBytes;
                try {
                    classBytes = Files.readAllBytes(myClassPath);
                } catch (IOException e) {
                    throw new ClassNotFoundException("can not find my class: " + myClassName);
                }
                loadedClass = defineClass(myClassName, classBytes, 0, classBytes.length);
            }
            return loadedClass;
        }
        return super.findClass(name);
    }
}
