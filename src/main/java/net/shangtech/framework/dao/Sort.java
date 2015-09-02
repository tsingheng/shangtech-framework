package net.shangtech.framework.dao;

public class Sort {
	
	private String field;
	
	private String type;
	
	private Sort(String field, String type){
		this.field = field;
		this.type = type;
	}
	
	private static final String ASC 	= "asc";
	private static final String DESC 	= "desc";
	
	public static Sort desc(String field){
		return new Sort(field, DESC);
	}
	
	public static Sort asc(String field){
		return new Sort(field, ASC);
	}

	public String getField() {
		return field;
	}

	public String getType() {
		return type;
	}
	
	public String toString(){
		return field + " " + type;
	}
	
}
