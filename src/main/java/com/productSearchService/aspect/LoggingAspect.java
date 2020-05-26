package com.productSearchService.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

    @Aspect
    @Configuration
    @Slf4j
    public class LoggingAspect {
        @Around("execution(* com.productSearchService.controller.ProductController.*(..)) || execution(* com.productSearchService.service.ProductServiceImpl.*(..)) || execution(* com.productSearchService.dao.LuceneDocs.*(..)) || execution(* com.productSearchService.dao.ProductRepositoryImpl.*(..))")
        public Object around(ProceedingJoinPoint jointpoint) throws Throwable {
            log.info("Entering into {}.{}", jointpoint.getTarget().getClass().getSimpleName(), jointpoint.getSignature().getName());
            long startTime = System.currentTimeMillis();
            Object response = jointpoint.proceed();
            long timeTaken = System.currentTimeMillis() - startTime;
            log.info("Existing from {}.{} with return value {} and total time: {}", jointpoint.getTarget().getClass().getSimpleName(), jointpoint.getSignature().getName(), response, timeTaken);
            return response;
        }
}
