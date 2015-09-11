package net.shangtech.framework.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.shangtech.framework.dao.IBaseDao;
import net.shangtech.framework.dao.Pagination;
import net.shangtech.framework.dao.QueryProvider;
import net.shangtech.framework.dao.Sort;
import net.shangtech.framework.util.MapHolder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

@SuppressWarnings("unchecked")
final public class BaseDao<T> implements IBaseDao<T> {

	private Class<T> entityClass;
	
	@Autowired(required = false)
	private HibernateTemplate hibernateTemplate;
	
	@Autowired(required = false)
	private QueryProvider queryProvider;
	
	@Override
	public void save(T obj) {
		hibernateTemplate.saveOrUpdate(obj);
	}

	@Override
	public T find(Long id) {
		return hibernateTemplate.get(entityClass, id);
	}

	@Override
	public void delete(T obj) {
		hibernateTemplate.delete(obj);
	}

	@Override
	public List<T> findAll(Sort... sorts) {
		return findByProperties(null, sorts);
	}

	@Override
	public void findAll(final Pagination<T> pagination, Sort... sorts) {
		findByProperties(pagination, null, sorts);
	}

	@Override
	public List<T> findByProperty(String propertyName, Object value, Sort...sorts) {
		return findByProperties(MapHolder.instance(propertyName, value), sorts);
	}

	@Override
	public T findOneByProperty(String propertyName, Object value) {
		List<T> list = findByProperty(propertyName, value);
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void findByProperty(Pagination<T> pagination, String propertyName, Object value, Sort...sorts) {
		findByProperties(pagination, MapHolder.instance(propertyName, value), sorts);
	}

	@Override
	public List<T> findByProperties(final MapHolder<String> holder, final Sort... sorts) {
		final StringBuilder queryString = new StringBuilder("select o from " + entityClass.getSimpleName() + " o where 1=1 ");
		if(holder != null){
			for(String key : holder.getMap().keySet()){
				queryString.append(" and " + key + "=:" + key);
			}
		}
		if(ArrayUtils.isNotEmpty(sorts)){
			queryString.append(" order by ").append(StringUtils.join(sorts, ","));
		}
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(queryString.toString());
				if(holder != null){
					for(Entry<String, Object> entry : holder.getMap().entrySet()){
						query.setParameter(entry.getKey(), entry.getValue());
					}
				}
				return query.list();
			}
		});
	}

	@Override
	public T findOneByProperties(MapHolder<String> holder) {
		List<T> list = findByProperties(holder);
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void findByProperties(final Pagination<T> pagination, final MapHolder<String> holder, Sort... sorts) {
		final StringBuilder queryString = new StringBuilder("select o from " + entityClass.getSimpleName() + " o ");
		final StringBuilder where = new StringBuilder(" where 1=1 ");
		if(holder != null){
			for(String key : holder.getMap().keySet()){
				where.append(" and ").append(key).append("=:").append(key);
			}
		}
		queryString.append(where);
		if(ArrayUtils.isNotEmpty(sorts)){
			queryString.append(" order by ").append(StringUtils.join(sorts, ","));
		}
		List<T> list = hibernateTemplate.executeWithNativeSession(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException,SQLException {
				Query query = session.createQuery(queryString.toString());
				if(holder != null){
					for(Entry<String, Object> entry : holder.getMap().entrySet()){
						query.setParameter(entry.getKey(), entry.getValue());
					}
				}
				query.setFirstResult(pagination.getStart());
				query.setMaxResults(pagination.getLimit());
				return query.list();
			}
		});
		pagination.setItems(list);
		if(pagination.getIsFirst() && list.size() < pagination.getLimit()){
			pagination.setTotalCount(list.size());
		}else{
			//如果第一页都没排满,说明总共就这么一页了,没必要再去count一次
			Long totalCount = hibernateTemplate.executeWithNativeSession(new HibernateCallback<Long>() {
				@Override
				public Long doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.createQuery("select count(o) from " + entityClass.getSimpleName() + " o " + where.toString());
					return (Long) query.uniqueResult();
				}
			});
			pagination.setTotalCount(totalCount.intValue());
		}
	}
	
	protected int batchUpdateBySql(String sqlId, final MapHolder<String> holder){
		final String sql = queryProvider.getSqlById(sqlId, holder);
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				if(holder != null){
					query.setProperties(holder.getMap());
				}
				return query.executeUpdate();
			}
		});
	}
	
	protected <E> E queryOne(Class<E> clazz, String sqlId, String propertyName, Object value){
		List<E> list = queryList(clazz, sqlId, propertyName, value);
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	
	protected <E> List<E> queryList(Class<E> clazz, String sqlId, String propertyName, Object value, Sort...sorts){
		return queryList(clazz, sqlId, MapHolder.instance(propertyName, value), sorts);
	}
	
	protected <E> void queryPage(Class<E> clazz, String sqlId, Pagination<E> pagination, String propertyName, Object value, Sort...sorts){
		queryPage(clazz, sqlId, pagination, MapHolder.instance(propertyName, value), sorts);
	}
	
	protected <E> E queryOne(Class<E> clazz, String sqlId, MapHolder<String> holder){
		List<E> list = queryList(clazz, sqlId, holder);
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	
	protected <E> List<E> queryList(final Class<E> clazz, String sqlId, final MapHolder<String> holder, Sort...sorts){
		final StringBuilder sql = new StringBuilder(queryProvider.getSqlById(sqlId, holder));
		if(ArrayUtils.isNotEmpty(sorts)){
			if(StringUtils.containsIgnoreCase(sql.toString(), " order by ")){
				throw new IllegalArgumentException("dumplicated order by of [" + sqlId + "]");
			}
			sql.append(" order by ").append(StringUtils.join(sorts, ","));
		}
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<List<E>>() {
			@Override
			public List<E> doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				if(holder != null){
					query.setProperties(holder.getMap());
				}
				if(clazz.isAssignableFrom(Map.class)){
					query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				}else if(!clazz.equals(String.class) && !ClassUtils.isPrimitiveOrWrapper(clazz)){
					query.setResultTransformer(AnnotationBeanResultTransformer.get(clazz));
				}
				return query.list();
			}
		});
	}
	
	protected <E> void queryPage(final Class<E> clazz, String sqlId, final Pagination<E> pagination, final MapHolder<String> holder, Sort...sorts){
		final StringBuilder sql = new StringBuilder(queryProvider.getSqlById(sqlId, holder));
		if(ArrayUtils.isNotEmpty(sorts)){
			if(StringUtils.containsIgnoreCase(sql.toString(), " order by ")){
				throw new IllegalArgumentException("dumplicated order by of [" + sqlId + "]");
			}
			sql.append(" order by ").append(StringUtils.join(sorts, ","));
		}
		List<E> list = hibernateTemplate.executeWithNativeSession(new HibernateCallback<List<E>>() {
			@Override
			public List<E> doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				if(holder != null){
					query.setProperties(holder.getMap());
				}
				query.setFirstResult(pagination.getStart());
				query.setMaxResults(pagination.getLimit());
				if(clazz.isAssignableFrom(Map.class)){
					query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				}else if(!clazz.equals(String.class) && !ClassUtils.isPrimitiveOrWrapper(clazz)){
					query.setResultTransformer(AnnotationBeanResultTransformer.get(clazz));
				}
				return query.list();
			}
		});
		pagination.setItems(list);
		if(pagination.getIsFirst() && list.size() < pagination.getLimit()){
			pagination.setTotalCount(list.size());
		}else{
			//如果第一页都没排满,说明总共就这么一页了,没必要再去count一次
			final String countSql = queryProvider.getCountById(sqlId, holder);
			Long totalCount = hibernateTemplate.executeWithNativeSession(new HibernateCallback<Long>() {
				@Override
				public Long doInHibernate(Session session) throws HibernateException, SQLException {
					SQLQuery query = session.createSQLQuery(countSql);
					return (Long) query.uniqueResult();
				}
			});
			pagination.setTotalCount(totalCount.intValue());
		}
	}
	
	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

}
