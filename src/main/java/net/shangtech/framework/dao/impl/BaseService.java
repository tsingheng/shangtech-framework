package net.shangtech.framework.dao.impl;

import java.util.List;
import java.util.Map;

import net.shangtech.framework.dao.IBaseService;
import net.shangtech.framework.dao.Pagination;
import net.shangtech.framework.dao.Sort;

public abstract class BaseService<T> implements IBaseService<T> {

	@Override
    public void save(T obj) {
	    getDao().save(obj);
    }

	@Override
    public T find(Long id) {
	    return getDao().find(id);
    }

	@Override
    public void delete(T obj) {
	    getDao().delete(obj);
    }

	@Override
    public void delete(Long id) {
	    getDao().delete(getDao().find(id));
    }

	@Override
    public List<T> findAll(Sort... sorts) {
	    return getDao().findAll(sorts);
    }

	@Override
    public void findAll(Pagination<T> pagination, Sort... sorts) {
	    getDao().findAll(pagination, sorts);
    }

	@Override
	public <E> void query(String sqlId, Class<E> clazz, Pagination<E> pagination, Map<String, Object> params, Sort...sorts){
		getDao().query(sqlId, clazz, pagination, params, sorts);
	}
	
	@Override
	public <E> List<E> query(String sqlId, Class<E> clazz, Map<String, Object> params, Sort... sorts) {
		return getDao().query(sqlId, clazz, params, sorts);
	}
}
