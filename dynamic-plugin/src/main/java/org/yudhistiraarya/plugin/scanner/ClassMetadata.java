package org.yudhistiraarya.plugin.scanner;

import com.google.common.base.Strings;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class ClassMetadata {
    private final String name;
    private final String superName;
    private final Collection<String> interfaces;
    private final int access;
    private final Path location;

    public ClassMetadata(final String name,
                         final String superName,
                         final Collection<String> interfaces,
                         final int access,
                         final Path location) {

        this.name = Strings.nullToEmpty(name);
        this.superName = Strings.nullToEmpty(superName);
        this.interfaces = interfaces == null ? Collections.emptyList() : interfaces;
        this.access = access;
        this.location = Objects.requireNonNull(location);
    }

    @Nonnull
    public Collection<String> getInterfaces() {
        return interfaces;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public String getSuperName() {
        return superName;
    }

    public int getAccess() {
        return access;
    }

    @Nonnull
    public Path getLocation() {
        return location;
    }
}
