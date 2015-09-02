package net.shangtech.framework.dao.impl;

import java.lang.reflect.Method;

import net.shangtech.framework.dao.IBaseDao;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class QueryInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method m = invocation.getMethod();
		if(m.getDeclaringClass().equals(IBaseDao.class)){
			return invocation.proceed();
		}
		
		BaseDao<?> dao = (BaseDao<?>) invocation.getThis();
		return null;
	}

}
