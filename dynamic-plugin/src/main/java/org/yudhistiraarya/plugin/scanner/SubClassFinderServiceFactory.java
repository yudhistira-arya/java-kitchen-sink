package org.yudhistiraarya.plugin.scanner;

public class SubClassFinderServiceFactory implements ClassFinderServiceFactory {

    @Override
    public ClassFinderService newClassFinderService(final Class<?> clazz) {
        final ClassFileParser classFileParser = new DefaultClassFileParser(new DefaultClassMetadataVisitorFactory());
        final ClassMetadataExtractor classExtractor = new JarClassMetadataExtractor(classFileParser);
        return new DefaultSubclassFinderService(classExtractor, clazz);
    }
}
