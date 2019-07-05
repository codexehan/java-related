package codexe.han.spring.annotation.aopannotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestCustomAspect {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        annotationConfigApplicationContext.register(AspectConfig.class);
        annotationConfigApplicationContext.refresh();
        Service injectClass = annotationConfigApplicationContext.getBean(Service.class);
        try {
            injectClass.serve();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
