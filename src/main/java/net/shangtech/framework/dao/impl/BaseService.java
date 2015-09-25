package net.shangtech.framework.dao.impl;

import java.util.List;

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

}
