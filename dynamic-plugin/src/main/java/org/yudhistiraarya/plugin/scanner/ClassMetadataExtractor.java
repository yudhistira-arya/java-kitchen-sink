package org.yudhistiraarya.plugin.scanner;

import java.nio.file.Path;
import java.util.Collection;

public interface ClassMetadataExtractor {
    Collection<ClassMetadata> extractClassesInformation(Path path);
}
