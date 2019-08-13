package org.yudhistiraarya.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Child-first, parent-last classloader.
 */
public class PluginClassLoader extends URLClassLoader {

    private static final Logger LOG = LoggerFactory.getLogger(PluginClassLoader.class);
    private static final String JAVA_PACKAGE_PREFIX = "java.";
    private static final String JAVAX_PACKAGE_PREFIX = "javax.";
    private static final String SUN_PACKAGE_PREFIX = "sun";
    private static final String JNA_PREFIX = "com.sun.jna";
    private final List<Class<?>> pluginInterfaces;

    public PluginClassLoader(final URL[] urls,
                             final ClassLoader parent,
                             final List<Class<?>> pluginInterfaces) {
        super(urls, parent);
        this.pluginInterfaces = pluginInterfaces;
    }

    public PluginClassLoader(final File file,
                             final ClassLoader parent,
                             final List<Class<?>> pluginInterfaces) throws MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, parent);
        this.pluginInterfaces = pluginInterfaces;
    }

    public PluginClassLoader(final Path path,
                             final ClassLoader parent,
                             final List<Class<?>> pluginInterfaces) throws MalformedURLException {
        this(path.toFile(), parent, pluginInterfaces);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    public void addFile(File file) {
        try {
            addURL(file.getCanonicalFile().toURI().toURL());
        } catch (IOException e) {
            throw new org.yudhistiraarya.plugin.PluginException(e);
        }
    }

    /**
     * Load class using based on the child-first mechanism except classes that are inside the prefix list.
     *
     * @param fullyQualifiedClassName fully qualified class name of the class to load
     * @param resolve                 resolve flag.
     * @return loaded class
     * @throws ClassNotFoundException if can't find the class, even after delegating to parent.
     * @since 1.5.0
     */
    @Override
    public Class<?> loadClass(final String fullyQualifiedClassName, final boolean resolve) throws ClassNotFoundException {
        if (fullyQualifiedClassName.startsWith(JAVA_PACKAGE_PREFIX)
                || fullyQualifiedClassName.startsWith(JAVAX_PACKAGE_PREFIX)
                || fullyQualifiedClassName.startsWith(SUN_PACKAGE_PREFIX)
                || fullyQualifiedClassName.startsWith(JNA_PREFIX)) {
            return findSystemClass(fullyQualifiedClassName);
        }

        if (pluginInterfaces.stream()
                .anyMatch(pluginInterface -> pluginInterface.getName().equals(fullyQualifiedClassName))) {
            LOG.debug("Load using parent classloader: {}", fullyQualifiedClassName);
            return loadClass(fullyQualifiedClassName, resolve);
        }

        LOG.debug("Start loading: {}", fullyQualifiedClassName);
        final Class<?> loadedClass = findLoadedClass(fullyQualifiedClassName);
        if (loadedClass != null) {
            LOG.debug("Class already loaded: {}", fullyQualifiedClassName);
            if (resolve) {
                resolveClass(loadedClass);
            }
            return loadedClass;
        }

        try {
            final Class<?> classInCustomStepClasspath = findClass(fullyQualifiedClassName);
            LOG.debug("Found class in the Custom Step Classpath");
            if (resolve) {
                resolveClass(classInCustomStepClasspath);
            }
            return classInCustomStepClasspath;
        } catch (final Exception e) {
            LOG.debug("Unable to find class in the custom step classpath. Delegating to parent classloader.");
        }

        return super.loadClass(fullyQualifiedClassName, resolve);
    }

    /**
     * Retrieve resource, child first.
     *
     * @param name resource name
     * @return URL
     */
    @Override
    public URL getResource(final String name) {
        final URL url = findResource(name);
        if (url != null) {
            LOG.debug("Resource found in custom step classpath: {}", name);
            return url;
        }

        LOG.debug("Unable to find resource in custom step classpath. Delegating to parent");
        return super.getResource(name);
    }

    /**
     * Retrieve resources, child-first.
     *
     * @param name name of the resource.
     * @return Enumeration of URL
     * @throws IOException io exception
     */
    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        final Enumeration<URL> resources = findResources(name);
        Enumeration<URL> parentUrls = null;
        if (getParent() != null) {
            parentUrls = getParent().getResources(name);
        }

        final List<URL> urls = new ArrayList<URL>();

        if (resources != null) {
            while (resources.hasMoreElements()) {
                urls.add(resources.nextElement());
            }
        }
        if (parentUrls != null) {
            while (parentUrls.hasMoreElements()) {
                urls.add(parentUrls.nextElement());
            }
        }
        LOG.debug(urls.toString());
        return new Enumeration<URL>() {
            Iterator<URL> iter = urls.iterator();

            public boolean hasMoreElements() {
                return iter.hasNext();
            }

            public URL nextElement() {
                return iter.next();
            }
        };
    }
}
