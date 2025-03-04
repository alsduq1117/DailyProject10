package com.example.project10.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.example.project10.service.PostService.*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("[메서드 실행] 메서드 이름 : {} , 메서드 인자 : {}", methodName, args);

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long finish = System.currentTimeMillis() - start;
        log.info("[메서드 종료] 메서드 이름 : {} , 걸린 시간 : {}ms , 반환 결과 : {}", methodName, finish, result);

        return result;
    }
}
