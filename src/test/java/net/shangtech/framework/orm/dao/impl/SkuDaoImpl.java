package net.shangtech.framework.orm.dao.impl;

import org.springframework.stereotype.Repository;

import net.shangtech.framework.orm.dao.SkuDao;
import net.shangtech.framework.orm.dao.hibernate.BaseDao;
import net.shangtech.framework.orm.dao.support.MapHolder;
import net.shangtech.framework.orm.dao.support.Pagination;
import net.shangtech.framework.orm.entity.Sku;
import net.shangtech.framework.orm.pojo.SkuBean;

@Repository
public class SkuDaoImpl extends BaseDao<Sku> implements SkuDao {

	@Override
	public Pagination<SkuBean> findSkuPage(Pagination<SkuBean> pagination) {
		MapHolder<String> holder = new MapHolder<String>();
		holder.put("sort", "category_id");
		holder.put("asc", "desc");
		return super.findBySql("SkuBean.findSkuPage", holder, pagination, SkuBean.class);
	}
	
}
