package org.yudhistiraarya.generic;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

final class DynamicCasting {

    static <T> List<T> dynamicCastingCollection(Class<T> type, List<?> listOfObjects) {
        return listOfObjects.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(toList());
    }
}
