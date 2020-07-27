package ex2;

import java.util.Random;

public class RandomSqrt {
    public static void main(String[] args) throws Exception {
        int n = 1000;
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int randomInt = random.nextInt();
            if (randomInt < 0) {
                throw new Exception("negative random int");
            }
            int sqrt = (int)Math.sqrt(randomInt);
            if (sqrt * sqrt == randomInt) {
                System.out.println(randomInt);
            }
        }
    }
}
