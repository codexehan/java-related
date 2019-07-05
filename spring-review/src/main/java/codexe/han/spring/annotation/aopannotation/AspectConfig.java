package codexe.han.spring.annotation.aopannotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "codexe.han.spring.annotation.aopannotation")
public class AspectConfig {
}
