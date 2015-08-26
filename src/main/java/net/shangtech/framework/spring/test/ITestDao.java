package net.shangtech.framework.spring.test;

import net.shangtech.framework.orm.dao.IBaseDao;

public interface ITestDao extends IBaseDao<Integer> {

	void test();
	
}
