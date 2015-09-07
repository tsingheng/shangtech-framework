package net.shangtech.framework.schema;

import java.util.Set;

import net.shangtech.framework.dao.impl.BaseDao;
import net.shangtech.framework.dao.impl.QueryInterceptor;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScanBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class DaoConfigBeanDefinitionParser extends ComponentScanBeanDefinitionParser {
	
	private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

	private static final String RESOURCE_PATTERN_ATTRIBUTE = "resource-pattern";

	private static final String USE_DEFAULT_FILTERS_ATTRIBUTE = "use-default-filters";

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		
		registQueryInterceptor(parserContext);
		registerGenericDaoProxy(parserContext);
		
		String[] basePackages = StringUtils.tokenizeToStringArray(element.getAttribute(BASE_PACKAGE_ATTRIBUTE), ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

		// Actually scan for bean definitions and register them.
		DaoComponentScanner scanner = configureScanner(parserContext, element);
		Set<BeanDefinitionHolder> beanDefinitions = scanner.doScan(basePackages);
		registerComponents(parserContext.getReaderContext(), beanDefinitions, element);
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
	
	@Override
	protected DaoComponentScanner configureScanner(ParserContext parserContext, Element element) {
		XmlReaderContext readerContext = parserContext.getReaderContext();

		boolean useDefaultFilters = true;
		if (element.hasAttribute(USE_DEFAULT_FILTERS_ATTRIBUTE)) {
			useDefaultFilters = Boolean.valueOf(element.getAttribute(USE_DEFAULT_FILTERS_ATTRIBUTE));
		}

		// Delegate bean definition registration to scanner class.
		DaoComponentScanner scanner = createScanner(readerContext, useDefaultFilters);
		scanner.setResourceLoader(readerContext.getResourceLoader());
		scanner.setEnvironment(parserContext.getDelegate().getEnvironment());
		scanner.setBeanDefinitionDefaults(parserContext.getDelegate().getBeanDefinitionDefaults());
		scanner.setAutowireCandidatePatterns(parserContext.getDelegate().getAutowireCandidatePatterns());

		if (element.hasAttribute(RESOURCE_PATTERN_ATTRIBUTE)) {
			scanner.setResourcePattern(element.getAttribute(RESOURCE_PATTERN_ATTRIBUTE));
		}

		try {
			parseBeanNameGenerator(element, scanner);
		}
		catch (Exception ex) {
			readerContext.error(ex.getMessage(), readerContext.extractSource(element), ex.getCause());
		}

		try {
			parseScope(element, scanner);
		}
		catch (Exception ex) {
			readerContext.error(ex.getMessage(), readerContext.extractSource(element), ex.getCause());
		}

		parseTypeFilters(element, scanner, readerContext, parserContext);

		return scanner;
	}

	@Override
	protected DaoComponentScanner createScanner(XmlReaderContext readerContext, boolean useDefaultFilters) {
		BeanDefinition baseDaoDefinition = buildBaseDao();
		return new DaoComponentScanner(baseDaoDefinition, readerContext.getRegistry(), useDefaultFilters);
	}
}
