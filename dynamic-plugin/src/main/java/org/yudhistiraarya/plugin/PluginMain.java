package org.yudhistiraarya.plugin;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.yudhistiraarya.plugin.scanner.ClassFinderService;
import org.yudhistiraarya.plugin.scanner.ClassFinderServiceFactory;
import org.yudhistiraarya.plugin.scanner.ClassMetadata;
import org.yudhistiraarya.plugin.scanner.SubClassFinderServiceFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class PluginMain {
    private static final List<Class<?>> pluginInterfaces = Arrays.asList(
            PluginDefinition.class,
            Configuration.class,
            ConfigurationBuilder.class,
            Processing.class,
            ProcessingBuilder.class,
            ProcessingContext.class,
            ProcessingResult.class
    );

    private static boolean isJar(final Path path) {
        return path.toFile().getName().endsWith(".jar");
    }

    public static void main(final String[] args) throws IOException, URISyntaxException {
        System.out.println(Object.class.isAssignableFrom(String.class));
        final ImmutableSet<ClassPath.ClassInfo> allClasses = ClassPath.from(ClassLoader.getSystemClassLoader()).getAllClasses();
        final long startinTime = System.currentTimeMillis();
        allClasses.stream()
                .filter(klz -> klz.getName().contains("yudhistira"))
                .forEach(klz -> System.out.println(klz.getName()));
        System.out.println("Time taken: " + (System.currentTimeMillis() - startinTime));

        // TODO: should scan directory instead of a single jar
//        final Path addonsPath = Paths.get(PluginMain.class.getClassLoader().getResource("addons").toURI());
//        final ClassFinderServiceFactory classFinderFactory = new SubClassFinderServiceFactory();
//        final ClassFinderService classFinder = classFinderFactory.newClassFinderService(PluginDefinition.class);
//
//        final List<ClassMetadata> allClassMetadatas = Files.walk(addonsPath)
//                .filter(PluginMain::isJar)
//                .flatMap(jarPath -> classFinder.findClasses(jarPath).stream())
//                .collect(toList());
//
//        allClassMetadatas.forEach(metadata -> System.out.println(metadata.getName()));

//        final ClassLoader pluginClassloader = new PluginClassLoader(
//                sampleJarFile,
//                PluginMain.class.getClassLoader(),
//                pluginInterfaces
//        );
        // TODO: find plugin-api interface implementation
    }
}
