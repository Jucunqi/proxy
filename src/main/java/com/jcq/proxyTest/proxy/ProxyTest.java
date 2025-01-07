package com.jcq.proxyTest.proxy;

import com.jcq.proxyTest.proxy.cglib.Person;
import com.jcq.proxyTest.proxy.cglib.PersonInterceptor;
import com.jcq.proxyTest.proxy.jdk.Animal;
import com.jcq.proxyTest.proxy.jdk.AnimalInterface;
import com.jcq.proxyTest.proxy.jdk.AnimalInvocationHandler;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

/**
 * @Description: jdk/cglib动态代理测试
 * @Author: jucunqi
 * @Date 2025/1/7
 */
public class ProxyTest {

    public static void main(String[] args) {

        // cglib
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Person.class);
        enhancer.setCallback(new PersonInterceptor());
        Person instance = (Person) enhancer.create();
        instance.sayHello();

        // jdk
        AnimalInterface animal = new Animal();
        AnimalInvocationHandler animalInvocationHandler = new AnimalInvocationHandler(animal);
        AnimalInterface proxy = (AnimalInterface) Proxy.newProxyInstance(AnimalInterface.class.getClassLoader(), new Class[]{AnimalInterface.class}, animalInvocationHandler);
        proxy.jump();
    }
}
