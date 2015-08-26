package net.shangtech.framework.spring.test;

import net.shangtech.framework.orm.dao.hibernate.BaseDao;
import net.shangtech.framework.orm.entity.Sku;

public abstract class Test2Dao extends BaseDao<Sku> implements ITest2Dao {

	@Override
	public void test1() {
		System.out.println("test1  success !!!!!");
	}

}
