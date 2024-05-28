package com.mindhub.homebanking.utils;

import java.util.Random;

public class RandomNumberUtils {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static int generateCVV(){
        return getRandomNumber(100,999);
    }

    public static String generateCardNumber (){
        String part1 = String.format("%04d", getRandomNumber(0, 9999));
        String part2 = String.format("%04d", getRandomNumber(0, 9999));
        String part3 = String.format("%04d", getRandomNumber(0, 9999));
        String part4 = String.format("%04d", getRandomNumber(0, 9999));

        return part1 + "-" + part2 + "-" + part3 + "-" + part4;

    }
}