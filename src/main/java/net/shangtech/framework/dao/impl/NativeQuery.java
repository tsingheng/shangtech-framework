package net.shangtech.framework.dao.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NativeQuery {
	
	String value() default "";
	
	Class<?> model() default DEFAULT.class;
	
	public static final class DEFAULT {}
}
