package org.yudhistiraarya.plugin;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class PluginMain {
    // TODO: should scan directory instead of a single jar
    private static final Path SAMPLE_JAR_FILE = Paths.get(
            System.getProperty("user.dir"),
            "dynamic-plugin/sample-plugin/build/libs/sample-plugin.jar");
    private static final List<Class<?>> pluginInterfaces = Arrays.asList(
            PluginDefinition.class,
            Configuration.class,
            ConfigurationBuilder.class,
            Processing.class,
            ProcessingBuilder.class,
            ProcessingContext.class,
            ProcessingResult.class
    );

    public static void main(final String[] args) throws MalformedURLException {
        final ClassLoader pluginClassloader = new PluginClassLoader(
                SAMPLE_JAR_FILE,
                PluginMain.class.getClassLoader(),
                pluginInterfaces
        );
        // TODO: find plugin-api interface implementation
    }
}
