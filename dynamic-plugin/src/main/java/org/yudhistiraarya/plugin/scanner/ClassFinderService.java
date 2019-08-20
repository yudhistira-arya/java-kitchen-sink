package org.yudhistiraarya.plugin.scanner;

import java.nio.file.Path;
import java.util.Collection;

public interface ClassFinderService {
    Collection<ClassMetadata> findClasses(Path path);
}
