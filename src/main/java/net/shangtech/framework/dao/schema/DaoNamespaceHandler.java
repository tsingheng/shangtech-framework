package net.shangtech.framework.dao.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class DaoNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("dao-config", new DaoConfigBeanDefinitionParser());
	}

}
