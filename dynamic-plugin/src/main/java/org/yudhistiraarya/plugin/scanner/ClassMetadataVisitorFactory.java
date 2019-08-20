package org.yudhistiraarya.plugin.scanner;

import java.nio.file.Path;

public interface ClassMetadataVisitorFactory {
    ClassMetadataVisitor getClassMetadataVisitor(final Path location);
}
