package net.shangtech.framework.daotest;

import net.shangtech.framework.BaseSpringTest;
import net.shangtech.framework.daotest.dao.ITestDao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DaoTest extends BaseSpringTest {

	@Autowired private ITestDao testDao;
	
	@Test
	public void test(){
		testDao.test1();
		testDao.find(0L);
//		testDao.save(null);
		System.out.println(testDao.getEntityClass());
	}
	
}
