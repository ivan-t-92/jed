package ex1;

import java.util.Random;

public class DifficultToDelete {
    byte[] bytes = new byte[1024];
    @Override
    protected void finalize() {
        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            r.nextBytes(bytes);
        }
    }
}
