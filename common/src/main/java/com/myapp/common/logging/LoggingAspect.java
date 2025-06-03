package com.myapp.common.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    // Pointcut for all methods in ContactService
    @Pointcut("execution(* com.example.demo.phonebook.service.*.*(..))")
    public void contactServiceMethods() {}

    // Before advice: Log method entry
    @Before("contactServiceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println("Entering " + methodName + " with args: " + java.util.Arrays.toString(args));
    }

    // AfterReturning advice: Log successful return
    @AfterReturning(pointcut = "contactServiceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Exiting " + methodName + " with result: " + result);
    }

    // AfterThrowing advice: Log exceptions
    @AfterThrowing(pointcut = "contactServiceMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println(methodName + " threw exception: " + exception.getMessage());
    }

    // Around advice: Measure execution time
    @Around("contactServiceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        long start = System.nanoTime();
        try {
            Object result = joinPoint.proceed(); // Execute the method
            long end = System.nanoTime();
            System.out.println(methodName + " executed in " + (end - start) + " ns");
            return result;
        } catch (Throwable t) {
            long end = System.nanoTime();
            System.out.println(methodName + " failed in " + (end - start) + " ns");
            throw t;
        }
    }
}