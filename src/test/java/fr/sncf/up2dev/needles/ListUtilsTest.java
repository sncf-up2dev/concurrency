package fr.sncf.up2dev.needles;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListUtilsTest {

    @Test
    void copyUnordered() {
        List<String> origin = new ArrayList<>();
        for (int i = 0; i < 1_000_000; i++) {
            origin.add(UUID.randomUUID().toString());
        }
        List<String> copy = ListUtils.copyUnordered(origin);
        assertEquals(origin.size(), copy.size());
        assertEquals(new HashSet<>(origin), new HashSet<>(copy));
    }
}