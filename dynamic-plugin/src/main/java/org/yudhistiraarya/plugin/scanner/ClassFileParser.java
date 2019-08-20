package org.yudhistiraarya.plugin.scanner;

import java.io.InputStream;
import java.nio.file.Path;

public interface ClassFileParser {
    ClassMetadata extract(final InputStream entry, final Path location);
}
