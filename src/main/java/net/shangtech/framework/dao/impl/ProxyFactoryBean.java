package net.shangtech.framework.dao.impl;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.InitializingBean;

public class ProxyFactoryBean extends org.springframework.aop.framework.ProxyFactoryBean implements InitializingBean {

	private static final long serialVersionUID = -2159972860906590451L;
	
	private Class<?> entityClass;

	@Override
	public void afterPropertiesSet() throws Exception {
		MethodUtils.invokeMethod(getObject(), "setEntityClass", entityClass);
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

}
