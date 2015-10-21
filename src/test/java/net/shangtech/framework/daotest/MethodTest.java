package net.shangtech.framework.daotest;

import java.lang.reflect.Method;

import org.junit.Test;

import net.shangtech.framework.dao.Pagination;
import net.shangtech.framework.dao.impl.BaseDao;
import net.shangtech.framework.util.MapHolder;

public class MethodTest {

	@Test
	public void test() throws Exception {
		Class<?> clazz = BaseDao.class;
		Method[] methods = clazz.getDeclaredMethods();
		for(Method m : methods){
			System.out.println(m.getName());
			for(Class<?> p : m.getParameterTypes()){
				System.out.println("\t" + p.getName());
			}
		}
		Method method = clazz.getMethod("queryPage", Class.class, String.class, Pagination.class, MapHolder.class);
		System.out.println(method.getName());
	}
}
