package com.celfocus.omnichannel.digital.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
	
	@Pointcut("execution(* com.celfocus.omnichannel.digital.*.*.*(..))")
	public void logging() {}

}
