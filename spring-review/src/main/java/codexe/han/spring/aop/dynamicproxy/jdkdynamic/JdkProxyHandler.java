package codexe.han.spring.aop.dynamicproxy.jdkdynamic;

import codexe.han.spring.aop.staticproxy.RrealStar;
import codexe.han.spring.aop.staticproxy.Star;

import java.lang.reflect.Proxy;

/**
 * JDK动态代理只能代理实现了接口的类
 * Proxy.newProxyInstance()方法，接收三个参数：
 * 第一个参数指定当前目标对象使用的类加载器
 * 第二个参数指定目标对象实现的接口的类型
 * 第三个参数指定动态处理器，执行
 */
public class JdkProxyHandler {

    /**
     * 用来接收真实明星对象
     */
    private Object realStar;

    public JdkProxyHandler(Star star){
        super();
        this.realStar = star;
    }

    /**
     * @return
     */
    public Object getProxyInstance(){
        return Proxy.newProxyInstance(realStar.getClass().getClassLoader(),
                realStar.getClass().getInterfaces(),
                (proxy, method, args)->{
                    System.out.println("代理先进行谈判...");
                    //唱歌需要明星自己来唱
                    Object object = method.invoke(realStar, args);

                    System.out.println("演出完，代理垫后");
                    return object;
                });
    }
}

class Client{

    public static void main(String[] args) {
        Star realStar = new RrealStar();

        Star proxy = (Star) new JdkProxyHandler(realStar).getProxyInstance();

        proxy.sing();

        proxy.test();
    }

}