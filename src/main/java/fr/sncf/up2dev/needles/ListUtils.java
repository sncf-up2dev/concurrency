package fr.sncf.up2dev.needles;

import java.util.ArrayList;
import java.util.List;

/**
 * List utilities.
 */
public class ListUtils {

    /**
     * Copy a list in an unordered way.
     *
     * @param source source list
     * @return unordered copy of source list
     * @param <T>   type of list items
     */
    public static <T> List<T> copyUnordered(List<T> source) {
        List<T> copy = new ArrayList<>();
        source.stream()
                .parallel()
                .forEach(item -> copy.add(item));
        return copy;
    }
}
