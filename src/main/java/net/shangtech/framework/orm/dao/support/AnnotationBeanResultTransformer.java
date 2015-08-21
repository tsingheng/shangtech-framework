package net.shangtech.framework.orm.dao.support;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.HibernateException;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

public class AnnotationBeanResultTransformer implements ResultTransformer, Serializable {

    private static final long serialVersionUID = -3159482502418290635L;

    private final Class<?> resultClass;
	private boolean isInitialized;
	private Map<String, PropertyDescriptor> mappedFields;

	public AnnotationBeanResultTransformer(Class<?> resultClass) {
		if ( resultClass == null ) {
			throw new IllegalArgumentException( "resultClass cannot be null" );
		}
		isInitialized = false;
		this.resultClass = resultClass;
	}

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
				PropertyDescriptor pd = mappedFields.get(column.toLowerCase());
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
				mappedFields.put(pd.getName().toLowerCase(), pd);
				Field field = FieldUtils.getDeclaredField(resultClass, pd.getName());
				Column column = field.getAnnotation(Column.class);
				if(column != null){
					mappedFields.put(column.name(), pd);
				}
			}
		}
		isInitialized = true;
	}

	@SuppressWarnings("rawtypes")
    public List transformList(List collection) {
		return collection;
	}

	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		AnnotationBeanResultTransformer that = ( AnnotationBeanResultTransformer ) o;

		if ( ! resultClass.equals( that.resultClass ) ) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result = resultClass.hashCode();
		return result;
	}
}
