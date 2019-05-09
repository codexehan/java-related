package codexe.han.spring.aop.staticproxy;

public class RrealStar implements Star {

    @Override
    public void sing() {
        System.out.println("明星本人开始唱歌...");
    }

    @Override
    public void test() {
        System.out.println("哈哈哈哈");
    }
}
