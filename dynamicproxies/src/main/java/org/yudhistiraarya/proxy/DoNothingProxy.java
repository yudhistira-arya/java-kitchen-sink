package org.yudhistiraarya.proxy;

import java.lang.reflect.Proxy;
import java.util.Set;

public class DoNothingProxy {

    @SuppressWarnings("unchecked")
    public static <T> Set<T> getSetProxy(final Set<T> originalSet) {
        return (Set<T>) Proxy.newProxyInstance(
                originalSet.getClass().getClassLoader(),
                new Class[]{Set.class},
                (proxy, method, args) -> method.invoke(originalSet, args)
        );
    }
}
