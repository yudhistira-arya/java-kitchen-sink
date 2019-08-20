package org.yudhistiraarya.plugin.scanner;

import java.nio.file.Path;

class DefaultClassMetadataVisitorFactory implements ClassMetadataVisitorFactory {
    @Override
    public ClassMetadataVisitor getClassMetadataVisitor(final Path location) {
        return new DefaultClassMetadataVisitor(location);
    }
}
