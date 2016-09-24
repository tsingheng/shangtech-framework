package net.shangtech.framework.schema;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

import net.shangtech.framework.dao.IBaseDao;

import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AssignableTypeFilter;

public class DaoComponentScanner extends ClassPathBeanDefinitionScanner {
	
	private BeanDefinition baseDaoDefinition;

	public DaoComponentScanner(BeanDefinition baseDaoDefinition, BeanDefinitionRegistry registry) {
		super(registry);
		addIncludeFilter(new AssignableTypeFilter(IBaseDao.class));
		this.baseDaoDefinition = baseDaoDefinition;
	}

	public DaoComponentScanner(BeanDefinition baseDaoDefinition, BeanDefinitionRegistry registry, boolean useDefaultFilters) {
		super(registry, useDefaultFilters);
		addIncludeFilter(new AssignableTypeFilter(IBaseDao.class));
		this.baseDaoDefinition = baseDaoDefinition;
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		try {
			Class<?> clazz = ClassUtils.getClass(beanDefinition.getBeanClassName());
			if(clazz.isInterface() && ClassUtils.isAssignable(clazz, IBaseDao.class)){
				return true;
			}
		} catch (ClassNotFoundException e) {
			
		}
		return false;
	}
	
	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		return super.doScan(basePackages);
	}
	
	@Override
	protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
		try {
			Class<?> clazz = ClassUtils.getClass(definitionHolder.getBeanDefinition().getBeanClassName());
			BeanDefinition definition = new GenericBeanDefinition();
			definition.setParentName("parentGenericDaoProxy");
			definition.setAttribute("className", clazz.getName());
			definition.getPropertyValues().addPropertyValue("proxyInterfaces", new Class<?>[]{clazz});
			definition.getPropertyValues().addPropertyValue("target", baseDaoDefinition);
			
			for(Type type : clazz.getGenericInterfaces()){
				if(type instanceof ParameterizedType){
					Class<?> entityClass = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
					definition.getPropertyValues().addPropertyValue("entityClass", entityClass);
				}
			}
			
			BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, definitionHolder.getBeanName());
			BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
