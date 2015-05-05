package net.shangtech.framework.orm.dao.converter;

public class Object2StringConverter implements Converter<String> {

	@Override
	public String convert(Object object) {
		if(object != null){
			return object.toString();
		}
		return null;
	}

}
