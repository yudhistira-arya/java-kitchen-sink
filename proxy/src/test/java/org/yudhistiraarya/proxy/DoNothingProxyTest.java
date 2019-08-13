package org.yudhistiraarya.proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.LongAdder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DoNothingProxyTest {

    private Set<String> proxiedSet;
    private LinkedHashSet<String> originalSet;

    @BeforeEach
    void beforeEach() {
        this.originalSet = new LinkedHashSet<>(Arrays.asList("One", "Two", "Three"));
        this.proxiedSet = DoNothingProxy.getSetProxy(originalSet);
    }

    @Test
    public void shouldHaveTheSameContentWithOriginalSet() {
        final LongAdder adder = new LongAdder();
        final List<String> copyList = new ArrayList<>(this.originalSet);

        this.proxiedSet.forEach(value -> {
            assertThat(value).isEqualTo(copyList.get(adder.intValue()));
            adder.increment();
        });
    }

    @Test
    public void shouldNotBeAbleToCastBackToImplementationClassFromProxy() {
        assertThatThrownBy(() -> {
            final LinkedHashSet<String> original = (LinkedHashSet<String>) this.proxiedSet;
        }).isInstanceOf(ClassCastException.class);
    }
}