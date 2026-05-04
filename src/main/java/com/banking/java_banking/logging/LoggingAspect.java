package com.banking.java_banking.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Order(1)
@Slf4j
public class LoggingAspect {

    @Pointcut("within(com.banking.java_banking.controller..*)")
    public void controllerMethods() {}

    @Pointcut("within(com.banking.java_banking.service.impl..*)")
    public void serviceMethods() {}

    @Around("controllerMethods()")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info(">>> [CONTROLLER] {}.{}() called with args: {}", className, methodName, Arrays.toString(args));

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            log.info("<<< [CONTROLLER] {}.{}() returned in {}ms with result: {}", className, methodName, duration, result);
            return result;
        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - start;
            log.error("!!! [CONTROLLER] {}.{}() threw {} after {}ms: {}", className, methodName, ex.getClass().getSimpleName(), duration, ex.getMessage());
            throw ex;
        }
    }

    @Around("serviceMethods()")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info(">>> [SERVICE] {}.{}() called", className, methodName);

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            log.info("<<< [SERVICE] {}.{}() completed in {}ms", className, methodName, duration);
            return result;
        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - start;
            log.error("!!! [SERVICE] {}.{}() threw {} after {}ms: {}", className, methodName, ex.getClass().getSimpleName(), duration, ex.getMessage());
            throw ex;
        }
    }

    @AfterThrowing(pointcut = "controllerMethods() || serviceMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        log.error("[EXCEPTION] {}.{}() - {}: {}", className, methodName, ex.getClass().getName(), ex.getMessage(), ex);
    }
}
