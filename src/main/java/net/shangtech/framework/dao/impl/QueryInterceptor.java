package net.shangtech.framework.dao.impl;

import java.lang.reflect.Method;
import java.util.Collection;

import javax.enterprise.inject.Default;

import net.shangtech.framework.dao.IBaseDao;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

public class QueryInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method m = invocation.getMethod();
		if(m.getDeclaringClass().equals(IBaseDao.class)){
			return invocation.proceed();
		}
		
		IBaseDao<?> dao = (IBaseDao<?>) invocation.getThis();
		NativeQuery query = m.getAnnotation(NativeQuery.class);
		String sqlId = query.value();
		if(StringUtils.isBlank(sqlId)){
			sqlId = dao.getEntityClass().getSimpleName() + "." + m.getName();
		}
		Class<?> model = query.model();
		String method = null;
		Object[] arguments = ArrayUtils.add(invocation.getArguments(), 0, sqlId);
		if(!Default.class.equals(model)){
			arguments = ArrayUtils.add(arguments, 0, model);
		}
		if(Default.class.equals(model)){
			method = "batchUpdateBySql";
		}else if(m.getReturnType().equals(Void.class)){
			//分页查询
			method = "queryPage";
		}else if(m.getReturnType().isAssignableFrom(Collection.class)){
			//列表查询
			method = "queryList";
		}else{
			method = "queryOne";
		}
		return MethodUtils.invokeMethod(dao, method, arguments);
	}

}
