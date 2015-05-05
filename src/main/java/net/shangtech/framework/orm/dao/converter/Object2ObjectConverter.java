package net.shangtech.framework.orm.dao.converter;

public class Object2ObjectConverter implements Converter<Object> {

	@Override
	public Object convert(Object object) {
		return object;
	}

}
