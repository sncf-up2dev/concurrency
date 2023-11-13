package fr.sncf.up2dev.needles;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class IntegerWrapperTest {
    private final static int NUMBER_OF_ITERATIONS = 1_000_000;
    private final static int NUMBER_OF_THREADS = 2;

    @Test
    void increment() throws InterruptedException {
        IntegerWrapper wrapper = new IntegerWrapper();
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            wrapper.increment();
        }
        assertEquals(NUMBER_OF_ITERATIONS, wrapper.getValue());
    }

    @Test
    void incrementMultiThread() throws InterruptedException {
        IntegerWrapper wrapper = new IntegerWrapper();
        Runnable task = () -> {
            for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
                wrapper.increment();
            }
        };
        Thread[] threads = new Thread[NUMBER_OF_THREADS];
        Arrays.setAll(threads, ignore -> new Thread(task));
        for (Thread thread: threads) {
            thread.start();
        }
        for (Thread thread: threads) {
            thread.join();
        }
        int value = wrapper.getValue();
        int expected = NUMBER_OF_THREADS * NUMBER_OF_ITERATIONS;
        assertEquals(expected, value);
    }
}