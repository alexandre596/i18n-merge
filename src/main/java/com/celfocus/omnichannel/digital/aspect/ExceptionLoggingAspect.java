package com.celfocus.omnichannel.digital.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class ExceptionLoggingAspect {
	
    @Around("execution(* com.celfocus.omnichannel.digital.*.*.*(..))")
    public Object logError(ProceedingJoinPoint pj) throws Throwable {
    	try {
            return pj.proceed();
        } catch (Throwable e) {
        	Log log = LogFactory.getLog(pj.getTarget().getClass());
            log.error(e.getMessage(), e);
            throw e;
        }
    }
    
    
}