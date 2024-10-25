package com.glatinis.basecraft.utils;

public class MathUtils {

    public static boolean chance(double probability) {
        return Math.random() < probability / 100;
    }
}
