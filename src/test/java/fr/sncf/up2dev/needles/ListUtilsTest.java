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
        // Original list
        List<String> origin = new ArrayList<>();
        // Populate the list with 1_000_000 random UUIDs
        for (int i = 0; i < 1_000_000; i++) {
            origin.add(UUID.randomUUID().toString());
        }
        // Copy the list
        List<String> copy = ListUtils.copyUnordered(origin);
        // First filter, size should be the same
        assertEquals(origin.size(), copy.size());
        // Unordered content should be the same
        assertEquals(new HashSet<>(origin), new HashSet<>(copy));
    }
}