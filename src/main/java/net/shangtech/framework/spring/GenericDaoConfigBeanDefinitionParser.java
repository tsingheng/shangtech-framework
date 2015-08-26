package net.shangtech.framework.spring;

import net.shangtech.framework.orm.dao.hibernate.BaseDao;
import net.shangtech.framework.spring.test.ITestDao;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class GenericDaoConfigBeanDefinitionParser implements BeanDefinitionParser {

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		registQueryInterceptor(parserContext);
		registerGenericDaoProxy(parserContext);
		BeanDefinition baseDaoDefinition = buildBaseDao();
		
		BeanDefinition definition = new GenericBeanDefinition();
		definition.setParentName("parentGenericDaoProxy");
		definition.getPropertyValues().addPropertyValue("proxyInterfaces", new Class<?>[]{ITestDao.class});
		definition.getPropertyValues().addPropertyValue("target", baseDaoDefinition);
		
		BeanComponentDefinition component = new BeanComponentDefinition(definition, "testDao");
		parserContext.registerBeanComponent(component);
		
//		BeanDefinition definition1 = new GenericBeanDefinition();
//		definition1.setParentName("parentGenericDaoProxy");
//		definition1.getPropertyValues().addPropertyValue("proxyInterfaces", new Class<?>[]{ITest2Dao.class});
//		GenericBeanDefinition test2 = new GenericBeanDefinition();
//		test2.setBeanClass(Test2Dao.class);
//		test2.setAbstract(true);
//		definition1.getPropertyValues().addPropertyValue("target", test2);
//		
//		BeanComponentDefinition component1 = new BeanComponentDefinition(definition1, "test2Dao");
//		parserContext.registerBeanComponent(component1);
		
		return null;
	}
	
	private BeanDefinition buildBaseDao(){
		GenericBeanDefinition definition = new GenericBeanDefinition();
		definition.setBeanClass(BaseDao.class);
		definition.setAbstract(true);
		return definition;
	}
	
	private void registerGenericDaoProxy(ParserContext pc){
		GenericBeanDefinition definition = new GenericBeanDefinition();
		definition.setBeanClassName(ProxyFactoryBean.class.getName());
		definition.setAbstract(true);
		definition.getPropertyValues().add("interceptorNames", new String[]{"queryInterceptor"});
		BeanComponentDefinition component = new BeanComponentDefinition(definition, "parentGenericDaoProxy");
		pc.registerBeanComponent(component);
	}
	
	private void registQueryInterceptor(ParserContext pc){
		BeanDefinition definition = new GenericBeanDefinition();
		definition.setBeanClassName(QueryInterceptor.class.getName());
		BeanComponentDefinition component = new BeanComponentDefinition(definition, "queryInterceptor");
		pc.registerBeanComponent(component);
	}

}
