package net.shangtech.framework.dao.impl;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.HibernateException;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

public class AnnotationBeanResultTransformer implements ResultTransformer, Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean isInitialized;
	
	private final Class<?> resultClass;
	
	private Map<String, PropertyDescriptor> mappedFields;
	
	private static final Map<Class<?>, AnnotationBeanResultTransformer> transformers = new HashMap<Class<?>, AnnotationBeanResultTransformer>();
	
	public static AnnotationBeanResultTransformer get(Class<?> clazz){
		AnnotationBeanResultTransformer transformer = transformers.get(clazz);
		if(transformer == null){
			transformer = new AnnotationBeanResultTransformer(clazz);
			transformers.put(clazz, transformer);
		}
		return transformer;
	}
	
	private AnnotationBeanResultTransformer(Class<?> resultClass) {
		if ( resultClass == null ) {
			throw new IllegalArgumentException( "resultClass cannot be null" );
		}
		isInitialized = false;
		this.resultClass = resultClass;
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Object result;

		try {
			if ( ! isInitialized ) {
				initialize( aliases );
			}
			
			result = resultClass.newInstance();
			BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(result);

			for ( int i = 0; i < aliases.length; i++ ) {
				String column = aliases[i];
				PropertyDescriptor pd = mappedFields.get(column);
				if(pd != null){
					wrapper.setPropertyValue(pd.getName(), tuple[i]);
				}
			}
		}
		catch ( InstantiationException e ) {
			throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
		}
		catch ( IllegalAccessException e ) {
			throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
		}

		return result;
	}
	
	private void initialize(String[] aliases) {
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(resultClass);
		for(PropertyDescriptor pd : pds){
			if(pd.getWriteMethod() != null){
				mappedFields.put(pd.getName(), pd);
				Field field = FieldUtils.getDeclaredField(resultClass, pd.getName());
				Column column = field.getAnnotation(Column.class);
				if(column != null && StringUtils.isNotBlank(column.name())){
					mappedFields.put(column.name(), pd);
				}
			}
		}
		isInitialized = true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List transformList(List collection) {
		return null;
	}

}
