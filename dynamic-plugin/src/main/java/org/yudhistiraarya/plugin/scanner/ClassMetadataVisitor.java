package org.yudhistiraarya.plugin.scanner;

import org.objectweb.asm.ClassVisitor;

import java.util.Optional;

abstract class ClassMetadataVisitor extends ClassVisitor {

    ClassMetadataVisitor(int api) {
        super(api);
    }

    abstract Optional<ClassMetadata> getClassMetadata();
}
