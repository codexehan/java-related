package codexe.han.spring.annotation.aopannotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExampleAspect {
   /* @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        System.out.println(joinPoint.getSignature() + " executed in "+executionTime +"ms");
        return proceed;
    }*/
    @Before("@annotation(LogExecutionTime)")
    public void logExecutionTime() throws Throwable {
        long start = System.currentTimeMillis();

        System.out.println(start +"ms");
    }
}

