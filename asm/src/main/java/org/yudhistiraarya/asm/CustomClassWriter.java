package org.yudhistiraarya.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class CustomClassWriter {

    private final static Logger LOG = LoggerFactory.getLogger(CustomClassWriter.class);
    private final static String CLASSNAME = "org.yudhistiraarya.common.NasiLemak";
    private final ClassReader classReader;
    private final ClassWriter classWriter;
    AddFieldAdapter addFieldAdapter;

    public CustomClassWriter() {
        try {
            classReader = new ClassReader(this.getClass().getResourceAsStream("simple.jar"));
            classWriter = new ClassWriter(classReader,0);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] addField() {
        addFieldAdapter = new AddFieldAdapter("aNewBooleanField", Opcodes.ACC_PUBLIC, classWriter);
        classReader.accept(addFieldAdapter, 0);
        return classWriter.toByteArray();
    }

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, NoSuchFieldException {
        final CustomClassWriter writer = new CustomClassWriter();
        writer.addField();
        final URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("asm/src/test/resources/simple.jar").toURI().toURL()});
        final Class<?> klazz = classLoader.loadClass(CLASSNAME);
        klazz.getField("aNewBooleanField");

    }
}
