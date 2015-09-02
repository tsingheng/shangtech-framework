package net.shangtech.framework.daotest;

import org.apache.commons.lang3.ClassUtils;

public class ClassTest {
	public static void main(String[] args){
		Integer a = 0;
		int b = 1;
		String c = "sss";
		System.out.println(a.getClass().isPrimitive());
//		System.out.println(b.getClass().isPrimitive());
		System.out.println(c.getClass().isPrimitive());
		System.out.println(ClassUtils.isPrimitiveOrWrapper(Integer.class));
		System.out.println(ClassUtils.isPrimitiveOrWrapper(String.class));
	}
}
