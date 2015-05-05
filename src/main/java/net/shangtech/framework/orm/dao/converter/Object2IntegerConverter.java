package net.shangtech.framework.orm.dao.converter;

public class Object2IntegerConverter implements Converter<Integer> {

	@Override
	public Integer convert(Object object) {
		if(object != null){
			return Integer.parseInt(object.toString());
		}
		return null;
	}

}
