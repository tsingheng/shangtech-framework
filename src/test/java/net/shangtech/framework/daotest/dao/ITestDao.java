package net.shangtech.framework.daotest.dao;

import net.shangtech.framework.dao.IBaseDao;

public interface ITestDao extends IBaseDao<Object> {
	
	void test1();
	
	void test2();
	
	void save(Object obj);
}
