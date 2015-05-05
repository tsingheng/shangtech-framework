package net.shangtech.framework.orm.dao.converter;

public class Object2LongConverter implements Converter<Long> {

	@Override
	public Long convert(Object object) {
		if(object != null){
			return Long.parseLong(object.toString());
		}
		return null;
	}

}
