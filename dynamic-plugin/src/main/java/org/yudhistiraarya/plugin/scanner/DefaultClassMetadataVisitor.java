package org.yudhistiraarya.plugin.scanner;

import org.objectweb.asm.Opcodes;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

class DefaultClassMetadataVisitor extends ClassMetadataVisitor {

    private final Path location;
    private ClassMetadata classMetadata;

    DefaultClassMetadataVisitor(final Path location) {
        super(Opcodes.ASM7);
        this.location = location;
    }

    /**
     * "Visit" a class. Required by ASM interface.
     *
     * @param version    class version
     * @param access     access modifiers
     * @param name       internal class name
     * @param signature  class signature
     * @param superName  super class name (note it will use / instead of .)
     * @param interfaces names of all implemented interfaces (note it will use / instead of .)
     */
    @Override
    public void visit(final int version,
                      final int access,
                      final String name,
                      final String signature,
                      final String superName,
                      final String[] interfaces) {
        this.classMetadata = new ClassMetadata(
                name,
                superName,
                new HashSet<>(Arrays.asList(interfaces)),
                access,
                location
        );
    }

    @Override
    Optional<ClassMetadata> getClassMetadata() {
        return Optional.of(classMetadata);
    }
}
