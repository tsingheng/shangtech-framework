package net.shangtech.framework.orm.dao.support;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import net.shangtech.framework.orm.dao.converter.Converter;
import net.shangtech.framework.orm.dao.converter.Object2IntegerConverter;
import net.shangtech.framework.orm.dao.converter.Object2LongConverter;
import net.shangtech.framework.orm.dao.converter.Object2ObjectConverter;
import net.shangtech.framework.orm.dao.converter.Object2StringConverter;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.ResultTransformer;

public class AnnotationBeanResultTransformer implements ResultTransformer, Serializable {

    private static final long serialVersionUID = -3159482502418290635L;

    private final Class<?> resultClass;
	private boolean isInitialized;
	private String[] aliases;
	private Setter[] setters;
	private Class<?>[] classes;
	private Converter<?>[] converters;
	private Class<?> entityClass;

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
			else {
				check( aliases );
			}
			
			result = resultClass.newInstance();

			for ( int i = 0; i < aliases.length; i++ ) {
				if ( setters[i] != null ) {
					Converter<?> converter = converters[i];
					if(converter == null){
						//Class<?> valueClass = tuple[i].getClass();
						Class<?> requiredClass = classes[i];
						if(!requiredClass.isInstance(tuple[i])){
							if (requiredClass == Long.class) {
								converter = new Object2LongConverter();
							} else if (requiredClass == Integer.class) {
								converter = new Object2IntegerConverter();
							} else if (requiredClass == String.class) {
								converter = new Object2StringConverter();
							}
						}else{
							converter = new Object2ObjectConverter();
						}
						converters[i] = converter;
					}
					Object value = tuple[i];
					if(converter != null){
						value = converter.convert(tuple[i]);
					}
					setters[i].set( result, value, null );
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
		PropertyAccessor propertyAccessor = new ChainedPropertyAccessor(
				new PropertyAccessor[] {
						PropertyAccessorFactory.getPropertyAccessor( resultClass, null ),
						PropertyAccessorFactory.getPropertyAccessor( "field" )
				}
		);
		this.aliases = new String[ aliases.length ];
		setters = new Setter[ aliases.length ];
		classes = new Class<?>[ aliases.length ];
		converters = new Converter<?>[ aliases.length ];
		Map<String, Field> map = new HashMap<String, Field>();
		
		List<Class<?>> classChain = new LinkedList<Class<?>>();
		classChain.add(resultClass);
		while(!classChain.isEmpty()){
			Class<?> clazz = classChain.remove(0);
			Class<?> superClass = clazz.getSuperclass();
			if(superClass != null){
				classChain.add(superClass);
			}
			if(BaseEntity.class.equals(superClass)){
				entityClass = clazz;
			}
			Field[] fields = clazz.getDeclaredFields();
			for(Field field : fields){
				Column column = field.getAnnotation(Column.class);
				if(column == null){
					continue;
				}
				String name = column.name();
				if(StringUtils.isBlank(name)){
					name = field.getName();
				}
				if(map.get(name) != null){
					continue;
					//throw new IllegalStateException("dumplicated alias [" + name + "] for [" + resultClass + "]");
				}
				map.put(name, field);
			}
		}
		
		for ( int i = 0; i < aliases.length; i++ ) {
			String alias = aliases[ i ];
			if ( alias != null ) {
				this.aliases[ i ] = alias;
				Field field = map.get(alias);
				if(field == null){
					throw new PropertyNotFoundException("Could not find setter for " + alias + " on " + resultClass);
				}
				String propertyName = field.getName();
				classes[ i ] = field.getType();
				//如果是id，属性类型是Object
				if(Object.class.equals(field.getType()) && field.getDeclaringClass().equals(BaseEntity.class) && entityClass != null){
					classes[ i ] = (Class<?>) ((ParameterizedType) (entityClass.getGenericSuperclass())).getActualTypeArguments()[0];
				}
				setters[ i ] = propertyAccessor.getSetter(resultClass, propertyName);
			}
		}
		isInitialized = true;
	}

	private void check(String[] aliases) {
		if ( ! Arrays.equals( aliases, this.aliases ) ) {
			throw new IllegalStateException(
					"aliases are different from what is cached; aliases=" + Arrays.asList( aliases ) +
							" cached=" + Arrays.asList( this.aliases ) );
		}
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
		if ( ! Arrays.equals( aliases, that.aliases ) ) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result = resultClass.hashCode();
		result = 31 * result + ( aliases != null ? Arrays.hashCode( aliases ) : 0 );
		return result;
	}
}
