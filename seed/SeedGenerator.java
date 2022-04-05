package com.shourya.customvillage.seed;

import java.util.Random;

public class SeedGenerator {
    public static final int SEED_LENGTH = 10;
    public static final Random random = new Random();

    public static int[] getRandomSeedInt() {
        int[] seed = new int[SEED_LENGTH];
        for (int i = 0; i < seed.length; i ++)
            seed[i] = random.nextInt(10);
        return seed;
    }

    public static String getRandomSeedString() {
        String seed = "";
        for (int i = 0; i < SEED_LENGTH; i ++)
            seed += String.valueOf(random.nextInt(10));
        return seed;
    }

}
