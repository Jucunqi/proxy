package com.jcq.proxyTest.proxy.asmTest;

import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

// 自定义类加载器
class ByteArrayClassLoader extends ClassLoader {
    public ByteArrayClassLoader(ClassLoader parent) {
        super(parent);
    }

    // 根据字节数组定义类
    public Class<?> defineClassFromByteArray(String name, byte[] byteCode) {
        return defineClass(name, byteCode, 0, byteCode.length);
    }
}

// 使用 ASM 生成类的字节码
class ClassGenerator {
    public static byte[] generateClass() throws Exception {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        // 定义类
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "GeneratedClass", null, "java/lang/Object", null);

        // 添加默认构造函数
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        // 添加一个简单的方法
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "hello", "()V", null, null);
        mv.visitCode();
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("Hello from generated class!");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();

        cw.visitEnd();

        byte[] byteArray = cw.toByteArray();
        // 将生成的字节码写入新的 class 文件
        try (FileOutputStream fos = new FileOutputStream("Generate.class")) {
            fos.write(byteArray);
        }
        return byteArray;
    }
}

public class InMemoryClassExecution {
    public static void main(String[] args) throws Exception {
        // 生成类的字节码
        byte[] classBytes = ClassGenerator.generateClass();

        // 创建自定义类加载器
        ByteArrayClassLoader classLoader = new ByteArrayClassLoader(InMemoryClassExecution.class.getClassLoader());

        // 加载类
        Class<?> loadedClass = classLoader.defineClassFromByteArray("GeneratedClass", classBytes);

        // 创建类的实例
        Object instance = loadedClass.getDeclaredConstructor().newInstance();

        // 获取要调用的方法
        Method method = loadedClass.getMethod("hello");

        // 调用方法
        method.invoke(instance);
    }
}