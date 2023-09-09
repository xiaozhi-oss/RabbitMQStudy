package com.xaiozhi.util;

/**
 * @author xiaozhi
 */
public class SleepUtil {

    public static void sleep(long time) {
        try {
            Thread.sleep(1000 * time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
