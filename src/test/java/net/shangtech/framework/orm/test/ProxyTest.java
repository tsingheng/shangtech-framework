package net.shangtech.framework.orm.test;

import net.shangtech.framework.BaseSpringTest;
import net.shangtech.framework.spring.test.ITestDao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProxyTest extends BaseSpringTest {

	@Autowired
	private ITestDao testDao;
	
	@Test
	public void test(){
		testDao.tt();
		testDao.test();
	}
	
}
