package net.shangtech.framework.dao;

import java.util.List;

public interface IBaseService<T> {

	void save(T obj);
	
	T find(Long id);
	
	void delete(T obj);
	
	void delete(Long id);
	
	List<T> findAll(Sort...sorts);
	
	void findAll(Pagination<T> pagination, Sort...sorts);
	
	IBaseDao<T> getDao();
}
