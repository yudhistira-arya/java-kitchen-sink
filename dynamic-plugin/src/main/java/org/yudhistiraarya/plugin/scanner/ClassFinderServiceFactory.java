package org.yudhistiraarya.plugin.scanner;

public interface ClassFinderServiceFactory {
    ClassFinderService newClassFinderService(Class<?> clazz);
}
