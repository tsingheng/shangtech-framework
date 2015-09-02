package net.shangtech.framework.dao;

import net.shangtech.framework.util.MapHolder;

public interface QueryProvider {
	String getSqlById(String id, MapHolder<String> holder);
	String getCountById(String id, MapHolder<String> holder);
}
