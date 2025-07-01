package com.example.project_two.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    @Before("execution(* com.example.project_two.service..*(..))")
    public void logBefore(JoinPoint joinPoint){
        log.info("Entering method: " + joinPoint.getSignature());
    }

    @AfterReturning(pointcut = "execution(* com.example.project_two.service..*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result){
        log.info("Method returned " + joinPoint.getSignature() + " with result " + result);
    }

    @AfterThrowing(pointcut = "execution(* com.example.project_two.service..*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex){
        log.error("Exception in method: " + joinPoint.getSignature() + " with message: " + ex.getMessage());
    }
}
