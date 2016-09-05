package net.shangtech.framework.dao;

import java.util.List;
import java.util.Map;

public interface IBaseService<T> {

	void save(T obj);
	
	T find(Long id);
	
	void delete(T obj);
	
	void delete(Long id);
	
	List<T> findAll(Sort...sorts);
	
	void findAll(Pagination<T> pagination, Sort...sorts);
	
	IBaseDao<T> getDao();
	
	<E> void query(String sqlId, Class<E> clazz, Pagination<E> pagination, Map<String, Object> params, Sort...sorts);
	
	<E> List<E> query(String sqlId, Class<E> clazz, Map<String, Object> params, Sort...sorts);
}
