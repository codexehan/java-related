package codexe.han.spring.aop.dynamicproxy.cglibdynamic;

import codexe.han.spring.aop.staticproxy.RrealStar;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Cglib动态代理是针对类实现代理的
 * JDK动态代理只能代理实现了接口的类
 *
 * Cglib运行时动态生成了被代理累的子类拦截父类方法调用
 *
 * Spring如何选择两种代理模式的
 *
 * 如果目标对象实现了接口，默认才用JDK代理
 * 如果目标对象没有实现接口，则使用Cglib代理
 */
public class DynamicCglibProxy implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    public Object getProxyObj(Class clazz){
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        enhancer.setUseCache(false);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("cglib proxy 1 start");
        methodProxy.invokeSuper(o, objects);
        System.out.println("cglib proxy 2 start");
        return null;
    }
}

class TestCglibProxy{
    public static void main(String[] args) {
        DynamicCglibProxy dynamicCglibProxy = new DynamicCglibProxy();
        RrealStar rrealStar = (RrealStar) dynamicCglibProxy.getProxyObj(RrealStar.class);

        rrealStar.sing();
    }
}
