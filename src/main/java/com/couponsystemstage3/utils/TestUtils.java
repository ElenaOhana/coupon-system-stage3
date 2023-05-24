package com.couponsystemstage3.utils;

public class TestUtils {
    private static int count = 1;

    public static void printTest(String content) {
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("                    Test # " + (count++) + " Content: " + content );
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
    }
}
