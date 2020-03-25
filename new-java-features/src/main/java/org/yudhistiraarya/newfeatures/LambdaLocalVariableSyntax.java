package org.yudhistiraarya.newfeatures;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.Predicate;

class LambdaLocalVariableSyntax {

    static void main(String[] args) {
        final var numbers = Arrays.asList(1, 2, 3, 4 ,5);
        // benefit: you can use annotation with lambda parameter
        final Predicate<Integer> predicate = (@Nonnull var number) -> number < 5;
        numbers.stream()
                .filter(predicate)
                .forEach(System.out::println);

    }
}
