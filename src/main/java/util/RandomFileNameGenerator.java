package util;

import java.util.Random;

public class RandomFileNameGenerator {
    private Random random;

    public RandomFileNameGenerator() {
        random = new Random(System.currentTimeMillis());
    }

    public String getRandomName(String fileResolution) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 20; i++) {
            builder.append((char)(random.nextInt(26) + 'a'));
        }
        return builder.append('.')
                .append(fileResolution)
                .toString();
    }
}
