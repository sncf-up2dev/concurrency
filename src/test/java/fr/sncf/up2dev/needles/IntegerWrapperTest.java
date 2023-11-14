package fr.sncf.up2dev.needles;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class IntegerWrapperTest {
    private final static int NUMBER_OF_ITERATIONS = 1_000_000;
    private final static int NUMBER_OF_THREADS = 2;

    @Test
    void increment() {
        IntegerWrapper wrapper = new IntegerWrapper();
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            wrapper.increment();
        }
        assertEquals(NUMBER_OF_ITERATIONS, wrapper.getValue());
    }

    @Test
    void incrementMultiThread() throws InterruptedException {
        IntegerWrapper wrapper = new IntegerWrapper();
        // Task to be executed by each thread: increment the wrapper value by NUMBER_OF_ITERATIONS
        Runnable task = () -> {
            for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
                wrapper.increment();
            }
        };
        // Array of threads
        Thread[] threads = new Thread[NUMBER_OF_THREADS];
        // Initialize the array with threads, each executing the task
        Arrays.setAll(threads, ignore -> new Thread(task));
        // Start all threads
        for (Thread thread: threads) {
            thread.start();
        }
        // Join all threads
        for (Thread thread: threads) {
            thread.join();
        }
        int value = wrapper.getValue();
        // Expected value is number of threads times number of iterations per thread
        int expected = NUMBER_OF_THREADS * NUMBER_OF_ITERATIONS;
        assertEquals(expected, value);
    }
}