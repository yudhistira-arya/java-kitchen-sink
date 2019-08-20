package org.yudhistiraarya.plugin.scanner;

import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

class DefaultClassFileParser implements ClassFileParser {

    private final ClassMetadataVisitorFactory classVisitorFactory;
    private static final int ASM_CR_ACCEPT_CRITERIA = 0;


    DefaultClassFileParser(final ClassMetadataVisitorFactory classVisitorFactory) {
        this.classVisitorFactory = classVisitorFactory;
    }

    @Override
    public ClassMetadata extract(final InputStream entry, final Path location) {
        try {
            final ClassReader classReader = new ClassReader(entry);
            final ClassMetadataVisitor classVisitor = classVisitorFactory.getClassMetadataVisitor(location);
            classReader.accept(classVisitor, ASM_CR_ACCEPT_CRITERIA);
            return classVisitor.getClassMetadata().orElseThrow(() -> new RuntimeException("Unable to extract class metadata"));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
