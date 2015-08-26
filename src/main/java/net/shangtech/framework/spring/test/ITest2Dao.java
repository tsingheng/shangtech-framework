package net.shangtech.framework.spring.test;

import net.shangtech.framework.orm.dao.IBaseDao;
import net.shangtech.framework.orm.entity.Sku;

public interface ITest2Dao extends IBaseDao<Sku> {
	
	void test1();
	
	void test2();
}
