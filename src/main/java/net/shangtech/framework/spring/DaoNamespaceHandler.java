package net.shangtech.framework.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class DaoNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("dao-config", new GenericDaoConfigBeanDefinitionParser());
	}

}
