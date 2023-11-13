package fr.sncf.up2dev.needles;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static <T> List<T> copyUnordered(List<T> source) {
        List<T> copy = new ArrayList<>();
        source.stream()
                .parallel()
                .forEach(item -> copy.add(item));
        return copy;
    }
}
