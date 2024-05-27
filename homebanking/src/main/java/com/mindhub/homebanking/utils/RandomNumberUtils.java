package com.mindhub.homebanking.utils;

public class RandomNumberUtils {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
