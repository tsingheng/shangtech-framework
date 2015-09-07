package net.shangtech.framework.daotest.dao;

import net.shangtech.framework.dao.IBaseDao;

public interface ITestDao extends IBaseDao<Long> {
	
	void test1();
	
	void test2();
	
	void save(Long obj);
}
