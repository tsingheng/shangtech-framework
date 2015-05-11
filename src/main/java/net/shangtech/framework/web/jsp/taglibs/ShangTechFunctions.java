package net.shangtech.framework.web.jsp.taglibs;

import java.util.Collection;

public class ShangTechFunctions {
	
	public static int max(int a, int b){
		return Math.max(a, b);
	}
	
	public static int min(int a, int b){
		return Math.min(a, b);
	}
	
	public static double ceil(double a){
		return Math.ceil(a);
	}
	
	public static boolean contains(Collection<?> collection, Object object){
		if(collection == null || collection.isEmpty()){
			return false;
		}
		for(Object item : collection){
			if(item == null && object == null){
				return true;
			}
			if(item != null && item.equals(object)){
				return true;
			}
		}
		return false;
	}
}
