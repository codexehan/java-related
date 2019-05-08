package codexe.han.spring.aop.staticproxy;

/**
 * 代理类需要做一些明星唱歌的准备和收尾工作，但是唱歌还是需要明星本人实现
 * 所以代理类肯定要将真实的对象传进来
 */
public class ProxyStar implements Star {

    private Star star;

    public ProxyStar(Star star){
        this.star = star;
    }

    @Override
    public void sing() {
        System.out.println("代理先进行谈判...");
        this.star.sing();
        System.out.println("演出完毕，代理收拾");
    }
}
