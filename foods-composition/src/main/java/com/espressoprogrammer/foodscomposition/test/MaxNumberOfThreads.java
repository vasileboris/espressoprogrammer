package com.espressoprogrammer.foodscomposition.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * It creates threads until operating system limits are reached
 */
public class MaxNumberOfThreads {
    private static final Logger logger = LoggerFactory.getLogger(MaxNumberOfThreads.class);

    public static void main(String... args) {
        long count = 0;
        while(true){
            logger.info("Thread count {}", ++count);
            new Thread(() -> {
                try {
                    Thread.sleep(10000000);
                } catch(InterruptedException e) { }
            }).start();
        }
    }

}
