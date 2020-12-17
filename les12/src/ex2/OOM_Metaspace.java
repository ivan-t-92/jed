package ex2;

import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class OOM_Metaspace {

    public static void main(String[] args) {
        ArrayList<Object> proxyList = new ArrayList<>();
        int i = 0;

        try {
            while (true) {
                proxyList.add(Proxy.newProxyInstance(
                        new URLClassLoader(new URL[]{new URL("file:" + i + ".jar")}),
                        ArrayList.class.getInterfaces(),
                        (proxy, method, args1) -> null));
                i++;
            }
        } catch (Throwable any) {
            any.printStackTrace();
            System.out.println("i=" + i);
        }
    }
}

