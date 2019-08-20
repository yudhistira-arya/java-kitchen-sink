package org.yudhistiraarya.plugin.scanner;

import org.apache.commons.lang3.tuple.Pair;
import org.objectweb.asm.Opcodes;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.util.stream.Collectors.toMap;

class DefaultSubclassFinderService implements ClassFinderService {
    private final ClassMetadataExtractor classMetadataExtractor;
    private final Class<?> parentClass;

    public DefaultSubclassFinderService(final ClassMetadataExtractor classMetadataExtractor,
                                        final Class<?> parentClass) {
        this.classMetadataExtractor = classMetadataExtractor;
        this.parentClass = parentClass;
    }

    @Override
    public Collection<ClassMetadata> findClasses(final Path path) {
        final Collection<ClassMetadata> metadatas = classMetadataExtractor.extractClassesInformation(path);
        final Map<String, ClassMetadata> indexedAllMetadata = metadatas.stream()
                .map(metadata -> new AbstractMap.SimpleEntry<>(metadata.getName(), metadata))
                .collect(toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

        return findAllSubclasses(indexedAllMetadata, parentClass);
    }

    private static Collection<ClassMetadata> findAllSubclasses(final Map<String, ClassMetadata> allMetadata,
                                                               final Class<?> parentClass) {

        final Deque<Pair<String, ClassMetadata>> stack = new ConcurrentLinkedDeque<>();
        final List<ClassMetadata> subClasses = new ArrayList<>();

        allMetadata.entrySet().stream()
                .filter(entry -> isPublicConcreteClass(entry.getValue()))
                .filter(entry -> !entry.getValue().getSuperName().isEmpty() || !entry.getValue().getInterfaces().isEmpty())
                .forEach(entry -> {
                    final ClassMetadata metadata = entry.getValue();
                    if (isDirectChild(metadata, parentClass)) {
                        subClasses.add(metadata);
                        return;
                    }

                    putSuperClassIntoStack(stack, allMetadata, metadata.getName(), metadata);
                    putInterfaceIntoStack(stack, allMetadata, metadata.getName(), metadata);
                });

        while(!stack.isEmpty()) {
            final Pair<String, ClassMetadata> pair = stack.pop();
            final String concreteSubClassName = pair.getKey();
            final ClassMetadata metadata = pair.getValue();

            if (metadata.getInterfaces().isEmpty() && metadata.getSuperName().isEmpty()) {
                continue;
            }

            if (isDirectChild(metadata, parentClass)) {
               subClasses.add(allMetadata.get(concreteSubClassName));
               continue;
            }

            putSuperClassIntoStack(stack, allMetadata, concreteSubClassName, metadata);
            putInterfaceIntoStack(stack, allMetadata, concreteSubClassName, metadata);
        }
        return subClasses;
    }

    private static void putInterfaceIntoStack(final Deque<Pair<String, ClassMetadata>> stack,
                                              final Map<String, ClassMetadata> allMetadata,
                                              final String concreteSubClassName,
                                              final ClassMetadata metadata) {

        metadata.getInterfaces().stream()
                .filter(allMetadata::containsKey)
                .forEach(iface -> stack.push(Pair.of(concreteSubClassName, allMetadata.get(iface))));
    }

    private static void putSuperClassIntoStack(final Deque<Pair<String, ClassMetadata>> stack,
                                               final Map<String, ClassMetadata> allMetadata,
                                               final String concreteSubClassName,
                                               final ClassMetadata metadata) {

        final String superClassName = metadata.getSuperName();
        if (allMetadata.containsKey(superClassName)) {
            stack.push(Pair.of(concreteSubClassName, allMetadata.get(superClassName)));
        }
    }

    private static boolean isDirectChild(final ClassMetadata classMetadata, final Class<?> parentClassOrInterface) {
        boolean match = classMetadata
                .getSuperName()
                .replaceAll("/", ".")
                .equals(parentClassOrInterface.getName());

        if (match) {
           return true;
        } else {
            return classMetadata.getInterfaces().stream()
                    .map(interfaceName -> interfaceName.replace("/", "."))
                    .anyMatch(interfaceName -> interfaceName.equals(parentClassOrInterface.getName()));
        }
    }

    private static boolean isPublicConcreteClass(final ClassMetadata classMetadata) {
        final int access = classMetadata.getAccess();
        return ((access & Opcodes.ACC_INTERFACE) == 0)
                && ((access & Opcodes.ACC_PUBLIC) == 1)
                && ((access & Opcodes.ACC_ABSTRACT) == 0);
    }
}
