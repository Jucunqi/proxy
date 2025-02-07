package com.jcq.proxyTest.proxy.asmProxy;

public class ByteArrayClassLoader extends ClassLoader{

    public ByteArrayClassLoader(ClassLoader parent) {
        super(parent);
    }

    // 根据类名和字节数组定义类
    public Class<?> defineClassFromByteArray(String name, byte[] byteCode) {
        return defineClass(name, byteCode, 0, byteCode.length);
    }
}
