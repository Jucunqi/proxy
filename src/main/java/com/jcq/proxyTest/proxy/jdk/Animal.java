package com.jcq.proxyTest.proxy.jdk;

/**
 * @Description: jdk被代理对象，需要实现接口
 * @Author: jucunqi
 * @Date 2025/1/7
 */
public class Animal implements AnimalInterface{

    @Override
    public void jump() {
        System.out.println("jump");
    }
}
