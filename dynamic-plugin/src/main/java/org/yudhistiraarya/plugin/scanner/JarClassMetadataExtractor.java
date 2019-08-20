package org.yudhistiraarya.plugin.scanner;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import static java.util.stream.Collectors.toList;

class JarClassMetadataExtractor implements ClassMetadataExtractor {
    private static final String CLASSPATH = "Class-Path";

    private final ClassFileParser classFileParser;

    JarClassMetadataExtractor(@Nonnull ClassFileParser classFileParser) {
        this.classFileParser = Objects.requireNonNull(classFileParser);
    }

    private Set<Path> populatePathToSearch(final Path path) {
        final Set<Path> placesToSearch = new HashSet<>();
        final File file = Objects.requireNonNull(path).toFile();

        if (!file.exists()) {
            throw new IllegalArgumentException("File doesnt exists: "  + file);
        }

        if (isJar(path)) {
            placesToSearch.add(path);
            loadClassPathFromManifest(path, placesToSearch);
        }
        return placesToSearch;
    }

    @Override
    public Collection<ClassMetadata> extractClassesInformation(@Nonnull final Path path) {
        final Set<Path> placesToSearch = populatePathToSearch(path);
        return placesToSearch.stream()
                .flatMap(searchPath -> processJar(searchPath).stream())
                .collect(toList());
    }

    private List<ClassMetadata> processJar(final Path path) {
        try (final JarFile jarFile = new JarFile(path.toFile())) {
            final List<ClassMetadata> someMetadata = new ArrayList<>();
            final Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    final ClassMetadata classMetadata = classFileParser.extract(jarFile.getInputStream(entry), path);
                    someMetadata.add(classMetadata);
                }
            }
            return someMetadata;
        } catch (final IOException e) {
            // TODO: use more specific/custom runtime exception
            throw new RuntimeException(e);
        }
    }

    private void loadClassPathFromManifest(final Path path, Set<Path> placesToSearch) {
        try (final JarFile jarFile = new JarFile(path.toFile())) {
            final Manifest manifest = jarFile.getManifest();

            if (manifest == null) {
                return;
            }

            final Path parentDir = path.getParent();
            final Attributes attributes = manifest.getMainAttributes();
            attributes.entrySet().stream()
                    .filter(attribute -> String.valueOf(attribute.getKey()).equals(CLASSPATH))
                    .filter(attribute -> attribute.getValue() != null && !String.valueOf(attribute.getValue()).isEmpty())
                    .flatMap(attribute -> {
                        final String value = (String) attribute.getValue();
                        return Arrays.stream(value.split("\\s"))
                                .map(parentDir::resolve)
                                .collect(toList()).stream();
                    })
                    .forEach(this::populatePathToSearch);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isJar(final Path path) {
        return path.toFile().getName().endsWith(".jar");
    }
}
