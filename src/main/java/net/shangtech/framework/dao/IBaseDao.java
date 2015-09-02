package net.shangtech.framework.dao;

import java.util.List;

import net.shangtech.framework.util.MapHolder;

public interface IBaseDao<T> {
	
	Class<T> getEntityClass();
	void setEntityClass(Class<T> entityClass);

	/**
	 * 插入或者更新数据
	 * @param obj
	 */
	void save(T obj);
	
	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
	T find(Long id);
	
	/**
	 * 删除实体
	 * @param obj
	 */
	void delete(T obj);
	
	/**
	 * 查询所有数据
	 * @param sorts
	 * @return
	 */
	List<T> findAll(Sort...sorts);
	
	/**
	 * 分页查询所有数据
	 * @param pagination
	 * @param sorts
	 */
	void findAll(Pagination<T> pagination, Sort...sorts);
	
	List<T> findByProperty(String propertyName, Object value, Sort...sorts);
	T findOneByProperty(String propertyName, Object value);
	void findByProperty(Pagination<T> pagination, String propertyName, Object value, Sort...sorts);
	
	List<T> findByProperties(MapHolder<String> holder, Sort...sorts);
	T findOneByProperties(MapHolder<String> holder);
	void findByProperties(Pagination<T> pagination, MapHolder<String> holder, Sort...sorts);
}
