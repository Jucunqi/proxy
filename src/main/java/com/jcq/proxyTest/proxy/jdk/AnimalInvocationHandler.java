package com.jcq.proxyTest.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Description: 基于jdk动态代理实现代理逻辑
 * @Author: jucunqi
 * @Date 2025/1/7
 */
public class AnimalInvocationHandler implements InvocationHandler {

    private Object target;

    public AnimalInvocationHandler(Object target) {
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("jdk  start");
        Object instance = method.invoke(target, args);
        System.out.println("jdk  end");
        return instance;
    }
}
