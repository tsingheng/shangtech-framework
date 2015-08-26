package net.shangtech.framework.spring;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryInterceptor implements MethodInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(QueryInterceptor.class);
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		logger.info("test success!!!");
		return null;
	}

}
