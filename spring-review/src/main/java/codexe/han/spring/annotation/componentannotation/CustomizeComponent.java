package codexe.han.spring.annotation.componentannotation;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomizeComponent {
    String value() default "";
}
