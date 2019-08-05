package org.yudhistiraarya.generic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DynamicCastingTest {

    @Test
    void shouldDoDynamicCastOnList() {
        final String expected1 = "Banana";
        final String expected2 = "Toast";

        final List noGenericList = new ArrayList();
        noGenericList.add(expected1);
        noGenericList.add(10);
        noGenericList.add(expected2);

        final List<String> result = DynamicCasting.dynamicCastingCollection(String.class, noGenericList);
        assertThat(result).hasSize(2).containsOnly(expected1, expected2);
    }
}