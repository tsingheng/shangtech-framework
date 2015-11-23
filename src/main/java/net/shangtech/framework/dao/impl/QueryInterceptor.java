package net.shangtech.framework.dao.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.hibernate.type.Type;

import net.shangtech.framework.dao.IBaseDao;
import net.shangtech.framework.dao.Pagination;
import net.shangtech.framework.dao.Sort;
import net.shangtech.framework.dao.impl.NativeQuery.DEFAULT;
import net.shangtech.framework.util.MapHolder;

public class QueryInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method m = invocation.getMethod();
		if(m.getDeclaringClass().equals(IBaseDao.class)){
			return invocation.proceed();
		}
		
		Object[] arguments = invocation.getArguments();
		
		IBaseDao<?> dao = (IBaseDao<?>) invocation.getThis();
		NativeQuery query = m.getAnnotation(NativeQuery.class);
		//有NativeQuery注解的才使用SQL查询
		if(query != null){
			return invokeSql(m, arguments, dao);
		}else{
			return invokeHql(m, arguments, dao);
		}
	}
	
	private Object invokeHql(Method method, Object[] arguments, IBaseDao<?> dao) throws Throwable {
		List<Object> args = new LinkedList<>();
		
		boolean hasMapHolder = hasMapHolder(arguments);
		if(hasMapHolder){
			for(Object arg : arguments){
				args.add(arg);
			}
		}else{
			MapHolder<String> holder = new MapHolder<>();
			args.add(holder);
			Annotation[][] annotations = method.getParameterAnnotations();
			for(int i = 0; i < arguments.length; i++){
				Object arg = arguments[i];
				if(arg.getClass().equals(Pagination.class)){
					args.add(arg);
				}else if(arg.getClass().equals(Sort.class)){
					args.add(arg);
				}else{
					QueryParam queryParam = (QueryParam) getQueryParam(annotations[i]);
					if(queryParam != null){
						holder.put(queryParam.value(), arg);
					}
				}
			}
		}
		
		String methodName;
		if(method.getReturnType().getName().equals("void")){
			methodName = "findByProperties";
			if (args.size() == 1) {
				args.add(null);
			}
		}else if(Collection.class.isAssignableFrom(method.getReturnType())){
			methodName = "findByProperties";
			if (args.size() == 2) {
				args.add(null);
			}
		}else{
			methodName = "findOneByProperties";
		}
		return MethodUtils.invokeMethod(dao, methodName, args.toArray());
	}
	
	private Object invokeSql(Method method, Object[] arguments, IBaseDao<?> dao) throws Throwable {
		List<Object> args = new LinkedList<>();
		
		NativeQuery query = method.getAnnotation(NativeQuery.class);
		
		Class<?> model = query.model();
		if(model == null){
			model = dao.getEntityClass();
		}
		if(!DEFAULT.class.equals(model)){
			args.add(model);
		}
		
		String sqlId = query.value();
		if(StringUtils.isBlank(sqlId)){
			sqlId = dao.getEntityClass().getSimpleName() + "." + method.getName();
		}
		args.add(sqlId);
		
		boolean hasMapHolder = hasMapHolder(arguments);
		if(hasMapHolder){
			for(Object arg : arguments){
				args.add(arg);
			}
		}else{
			MapHolder<String> holder = new MapHolder<>();
			Annotation[][] annotations = method.getParameterAnnotations();
			for(int i = 0; i < arguments.length; i++){
				Object arg = arguments[i];
				if(arg.getClass().equals(Pagination.class)){
					args.add(arg);
				}else{
					QueryParam queryParam = (QueryParam) getQueryParam(annotations[i]);
					if(queryParam != null){
						holder.put(queryParam.value(), arg);
					}
				}
			}
			args.add(holder);
		}
		
		Scalar[] scalars = query.scalars();
		if(scalars != null && scalars.length > 0){
			Map<String, Type> scalarMap = new HashMap<>();
			for(Scalar scalar : scalars){
				Field field = FieldUtils.getDeclaredField(scalar.type(), "INSTANCE");
				
				Type type = (Type) field.get(null);
				scalarMap.put(scalar.column(), type);
			}
			for(Object arg : args){
				if(MapHolder.class.equals(arg.getClass())){
					@SuppressWarnings("unchecked")
					MapHolder<String> holder = (MapHolder<String>) arg;
					holder.put(BaseDao.SCALAR_KEY, scalarMap);
					break;
				}
			}
		}
		
		String methodName;
		if(DEFAULT.class.equals(model)){
			methodName = "batchUpdateBySql";
		}else if(method.getReturnType().getName().equals("void")){
			//分页查询
			methodName = "queryPage";
		}else if(Collection.class.isAssignableFrom(method.getReturnType())){
			//列表查询
			methodName = "queryList";
		}else{
			methodName = "queryOne";
		}
		return MethodUtils.invokeMethod(dao, methodName, args.toArray());
	}
	
	private Annotation getQueryParam(Annotation[] annotations){
		for(Annotation annotation : annotations){
			if(annotation.annotationType().equals(QueryParam.class)){
				return annotation;
			}
		}
		return null;
	}

	private boolean hasMapHolder(Object[] arguments){
		if(ArrayUtils.isEmpty(arguments)){
			return false;
		}else{
			for(Object arg : arguments){
				if(arg.getClass().equals(MapHolder.class)){
					return true;
				}
			}
		}
		return false;
	}
}
