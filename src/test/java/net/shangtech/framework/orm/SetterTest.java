package net.shangtech.framework.orm;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.junit.Test;

public class SetterTest {

	@Test
	public void test(){
		Class<?> clazz = TestBean.class;
		PropertyAccessor propertyAccessor = new ChainedPropertyAccessor(
				new PropertyAccessor[] {
						PropertyAccessorFactory.getPropertyAccessor( clazz, null ),
						PropertyAccessorFactory.getPropertyAccessor( "field" )
				}
		);
		
		List<Class<?>> clazzList = new LinkedList<Class<?>>();
		clazzList.add(clazz);
		
		Map<String, String> map = new HashMap<String, String>();
		while(!clazzList.isEmpty()){
			Class<?> c = clazzList.remove(0);
			if(c.getSuperclass() != null){
				clazzList.add(c.getSuperclass());
			}
			Field[] fields = c.getDeclaredFields();
			
			for(Field field : fields){
				System.out.println(field.getType() + ":" + field.getName());
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
					//throw new IllegalStateException("dumplicated alias [" + name + "] for [" + clazz + "]");
				}
				map.put(name, field.getName());
			}
		}
		
		for(Entry<String, String> entry : map.entrySet()){
			Setter setter = propertyAccessor.getSetter(clazz, entry.getValue());
			System.out.println(setter.getMethodName());
		}
		Setter setter = propertyAccessor.getSetter(clazz, "id");
		System.out.println(setter.getMethodName());
		BigInteger a = BigInteger.valueOf(1);
		TestBean testBean = new TestBean();
		setter.set(testBean, a, null);
		Long id = testBean.getId();
		System.out.println(id);
	}
}
