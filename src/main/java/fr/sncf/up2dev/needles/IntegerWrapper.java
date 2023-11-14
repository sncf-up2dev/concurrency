package fr.sncf.up2dev.needles;

/**
 * Simple integer wrapper.
 */
class IntegerWrapper {
    /**
     * Wrapped value.
     */
    private int value = 0;

    /**
     * Increment wrapped value by one.
     */
    public void increment() {
        this.value++;
    }

    /**
     * Read wrapped value.
     *
     * @return wrapped value
     */
    public int getValue() {
        return this.value;
    }
}
