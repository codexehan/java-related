package codexe.han.spring.aop.dynamicproxy.jdkdynamic;

import codexe.han.spring.aop.staticproxy.RrealStar;
import codexe.han.spring.aop.staticproxy.Star;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyHandler2 implements InvocationHandler {
    private Object target;

    public DynamicProxyHandler2(Object target){
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("第二层代理");
        Object object = method.invoke(target, args);
        System.out.println("第二层代理");
        return object;
    }
}

class Client1{
    public static void main(String[] args) {
        RrealStar rrealStar = new RrealStar();

        Star star = (Star) Proxy.newProxyInstance(rrealStar.getClass().getClassLoader(),rrealStar.getClass().getInterfaces(), new DynamicProxyHandler(rrealStar));
        star.sing();

        System.out.println("----------------------");

        Star star1 = (Star) Proxy.newProxyInstance(star.getClass().getClassLoader(), star.getClass().getInterfaces(), new DynamicProxyHandler2(star));
        star1.sing();

    }
}