package net.shangtech.framework.orm.dao;

import net.shangtech.framework.orm.dao.support.Pagination;
import net.shangtech.framework.orm.entity.Sku;
import net.shangtech.framework.orm.pojo.SkuBean;

public interface SkuDao extends IBaseDao<Sku> {
	
	Pagination<SkuBean> findSkuPage(Pagination<SkuBean> pagination);
	
}
