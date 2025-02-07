package com.jcq.proxyTest.proxy.asmProxy;

import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class PersonClassVisitor extends ClassVisitor {


    public PersonClassVisitor(int i,ClassVisitor classVisitor) {
        super(i,classVisitor);
    }
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        // 去掉包名，只保留类名
        String simpleName = name.substring(name.lastIndexOf('/') + 1);
        super.visit(version, access, simpleName, signature, superName, interfaces);
    }

    private String removePackage(String fullName) {
        return fullName.substring(fullName.lastIndexOf('/') + 1);
    }
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {

        // 处理方法引用，去掉包名
        String newName = removePackage(name);
        MethodVisitor mv = super.visitMethod(access, newName, descriptor, signature, exceptions);
        if ("run".equals(name)) {
            // 对 run 方法进行增强
            return new PersonMethodVisitor(api, mv);
        }
        return mv;
    }
}


// 自定义 MethodVisitor 用于修改方法字节码
class PersonMethodVisitor extends MethodVisitor {
    public PersonMethodVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public void visitCode() {
        // 在方法开始处添加打印语句
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("Before running...");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        super.visitCode();
    }

    @Override
    public void visitInsn(int opcode) {
        if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
            // 在方法返回或抛出异常前添加打印语句
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("After running...");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
        super.visitInsn(opcode);
    }
}