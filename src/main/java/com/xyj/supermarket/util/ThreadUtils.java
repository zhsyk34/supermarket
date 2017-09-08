package com.xyj.supermarket.util;

public abstract class ThreadUtils {

    public static void await(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
