package com.celfocus.omnichannel.digital.aspect;

import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class LogAspect {
	
	@Before("Pointcuts.logging()")
    public void beforeMethod(JoinPoint pj) throws Throwable {
		Logger logger = LoggerFactory.getLogger(pj.getTarget().getClass());
    	
		if(logger.isTraceEnabled()) {
    	    Object[] signatureArgs = pj.getArgs();
    	    for (Object signatureArg : signatureArgs) {
    	        logger.trace(">>> Method: {}; Arguments: {}; Time: {}", pj.getSignature().getName(), Objects.toString(signatureArg, "[null]"), System.currentTimeMillis());
    	    }
		} else if(logger.isDebugEnabled()) {
    		logger.debug(">>> Method: {}#{}", pj.getTarget().getClass().getName(), pj.getSignature().getName());
    	} 
    }
	
	@AfterReturning(pointcut="Pointcuts.logging()", returning="retVal")
    public void afterReturnMethod(JoinPoint pj, Object retVal) throws Throwable {
		Logger logger = LoggerFactory.getLogger(pj.getTarget().getClass());
    	
		if(logger.isTraceEnabled()) {
    	    Object[] signatureArgs = pj.getArgs();
    	    for (Object signatureArg : signatureArgs) {
    	        logger.trace("<<< Method: {}; Arguments: {}; Return: {}; Time: {}", pj.getSignature().getName(), Objects.toString(signatureArg, "[null]") , retVal, System.currentTimeMillis());
    	    }
		} else if(logger.isDebugEnabled()) {
			logger.debug("<<< Method: {}#{}", pj.getTarget().getClass().getName(), pj.getSignature().getName());
    	} 
    }
    
	@AfterThrowing(pointcut="Pointcuts.logging()", throwing ="throwing")
    public void afterThrowMethod(JoinPoint pj, Object throwing) throws Throwable {
		Logger logger = LoggerFactory.getLogger(pj.getTarget().getClass());
    	
		if(logger.isTraceEnabled()) {
    	    Object[] signatureArgs = pj.getArgs();
    	    for (Object signatureArg : signatureArgs) {
    	        logger.trace("<<<! Method: {}; Arguments: {}; Throws: {}; Time: {}", pj.getSignature().getName(), Objects.toString(signatureArg, "[null]"), throwing, System.currentTimeMillis());
    	    }
		} else if(logger.isDebugEnabled()) {
    		logger.debug("<<< Method: {}", pj.getSignature().getName());
    	} 
    }
    
}