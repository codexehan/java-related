package codexe.han.spring.annotation.aopannotation;

import codexe.han.spring.annotation.componentannotation.CustomizeScanTest;
import codexe.han.spring.annotation.componentannotation.ScanClass1;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Service {
    @LogExecutionTime
    public void serve() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("serve 2 seconds");
    }


}