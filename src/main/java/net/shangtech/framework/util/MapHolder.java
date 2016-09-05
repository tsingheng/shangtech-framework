package net.shangtech.framework.util;

import java.util.HashMap;
import java.util.Map;

public class MapHolder<K> {
	
	private Map<K, Object> map = new HashMap<>();
	
	public static <K> MapHolder<K> instance(K key, Object value){
		MapHolder<K> holder = new MapHolder<>();
		holder.map.put(key, value);
		return holder;
	}
	
	public MapHolder(){
		super();
	}
	
	public MapHolder(Map<K, Object> map){
		super();
		this.map = map;
	}
	
	public MapHolder<K> put(K key, Object value){
		map.put(key, value);
		return this;
	}
	
	public Map<K, Object> getMap(){
		return map;
	}
}
