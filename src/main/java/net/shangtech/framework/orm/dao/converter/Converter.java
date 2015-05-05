package net.shangtech.framework.orm.dao.converter;


public interface Converter<T> {
	public T convert(Object object);
}
