package org.yudhistiraarya.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ASM4;

public class AddFieldAdapter extends ClassVisitor {
    private final String fieldName;
    private String fieldDefault;
    private int access = ACC_PUBLIC;
    private boolean isFieldPresent;


    public AddFieldAdapter(final String fieldName, final int fieldAccess, final ClassVisitor cv) {
        super(ASM4, cv);
        this.cv = cv;
        this.fieldName = fieldName;
        this.access = fieldAccess;
    }

    @Override
    public FieldVisitor visitField(final int access,
                                   final String name,
                                   final String desc,
                                   final String signature,
                                   final Object value) {
        synchronized (this) {
            if (name.equals(fieldName)) {
                isFieldPresent = true;
            }
        }
        return cv.visitField(access, name, desc, signature, value);
    }

    /**
     * Since this is the last method called, it's recommended to do insertion here.
     */
    @Override
    public void visitEnd() {
        if (!isFieldPresent) {
            final FieldVisitor fv = cv.visitField(access, fieldName, Type.BOOLEAN_TYPE.toString(), null, null);
            if (fv != null) {
                fv.visitEnd();
            }
        }
        super.visitEnd();
    }
}
