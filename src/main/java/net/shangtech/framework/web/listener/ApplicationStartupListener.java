package net.shangtech.framework.web.listener;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;

import net.shangtech.framework.util.SpringUtils;

public class ApplicationStartupListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("ctx", sce.getServletContext().getContextPath());
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		SpringUtils.setApplicationContext(ctx);
		
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("classpath:application.properties");
		if(resource.exists()){
			Properties props = new Properties();
			try {
				props.load(resource.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(Entry<Object, Object> entry : props.entrySet()){
				sce.getServletContext().setAttribute(entry.getKey().toString(), entry.getValue());
			}
		}
	}
}
