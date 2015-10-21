package net.shangtech.framework.dao.impl;

import java.lang.reflect.Method;
import java.util.Collection;


import net.shangtech.framework.dao.IBaseDao;
import net.shangtech.framework.dao.impl.NativeQuery.DEFAULT;

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
		String sqlId = null;
		Class<?> model = null;
		if(query != null){
			sqlId = query.value();
			model = query.model();
		}
		if(StringUtils.isBlank(sqlId)){
			sqlId = dao.getEntityClass().getSimpleName() + "." + m.getName();
		}
		if(model == null){
			model = dao.getEntityClass();
		}
		String method = null;
		Object[] arguments = ArrayUtils.add(invocation.getArguments(), 0, sqlId);
		if(!DEFAULT.class.equals(model)){
			arguments = ArrayUtils.add(arguments, 0, model);
		}
		if(DEFAULT.class.equals(model)){
			method = "batchUpdateBySql";
		}else if(m.getReturnType().getName().equals("void")){
			//分页查询
			method = "queryPage";
//			arguments = ArrayUtils.add(arguments, null);
		}else if(Collection.class.isAssignableFrom(m.getReturnType())){
			//列表查询
			method = "queryList";
//			arguments = ArrayUtils.add(arguments, null);
		}else{
			method = "queryOne";
		}
		return MethodUtils.invokeMethod(dao, method, arguments);
	}

}
