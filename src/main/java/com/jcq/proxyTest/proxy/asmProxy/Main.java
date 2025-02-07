package com.jcq.proxyTest.proxy.asmProxy;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 通过asm 在内存中动态生成代理对象的class，并执行新的方法
 *
 * @author : jucunqi
 * @since : 2025/2/7
 */
public class Main {

    public static void main(String[] args) {

        try (FileOutputStream fos = new FileOutputStream("Person.class")) {

            // 创建 ClassReader 读取 Person 类的字节码
            byte[] bytecode = getClassBytes();

            // 创建自定义类加载器
            ByteArrayClassLoader classLoader = new ByteArrayClassLoader(Main.class.getClassLoader());
            // 加载类
            Class<?> loadedClass = classLoader.defineClassFromByteArray("Person", bytecode);
            // 创建类的实例
            Object instance = loadedClass.getDeclaredConstructor().newInstance();
            // 获取要调用的方法
            Method method = loadedClass.getMethod("run");
            // 调用方法
            method.invoke(instance);

            // 将生成的字节码写入新的 class 文件
            fos.write(bytecode);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static byte[] getClassBytes() throws IOException {
        ClassReader classReader = new ClassReader(Person.class.getName());
        // 创建 ClassWriter 用于生成新的字节码
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        // 创建自定义的 ClassVisitor
        PersonClassVisitor personClassVisitor = new PersonClassVisitor(Opcodes.ASM5, classWriter);
        // 开始解析 Person 类的字节码并应用修改
        classReader.accept(personClassVisitor, 0);
        // 获取生成的新字节码
        return classWriter.toByteArray();
    }
}
