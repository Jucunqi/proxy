package com.jcq.proxyTest.proxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Description: 基于cglib动态代理实现的代理逻辑拦截器
 * @Author: jucunqi
 * @Date 2025/1/7
 */
public class PersonInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("cglib before");
        Object instance = methodProxy.invokeSuper(o, objects);
        System.out.println("cglib after");
        return instance;
    }
}
