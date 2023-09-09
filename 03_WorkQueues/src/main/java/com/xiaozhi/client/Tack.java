package com.xiaozhi.client;

/**
 * @author xiaozhi
 */
public class Tack {

    public static void main(String[] args) {
        ThreadConsumer threadConsumer = new ThreadConsumer();
        new Thread(threadConsumer, "线程一").start();
        new Thread(threadConsumer, "线程二").start();
        new Thread(threadConsumer, "线程三").start();
    }
}
