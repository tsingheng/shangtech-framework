package net.shangtech.framework.dao.impl;

public @interface Scalar {
	String column();
	
	Class<?> type();
}
