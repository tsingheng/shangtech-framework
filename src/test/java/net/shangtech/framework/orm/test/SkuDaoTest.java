package net.shangtech.framework.orm.test;

import java.util.Date;

import net.shangtech.framework.BaseSpringTest;
import net.shangtech.framework.orm.dao.SkuDao;
import net.shangtech.framework.orm.dao.support.Pagination;
import net.shangtech.framework.orm.pojo.SkuBean;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SkuDaoTest extends BaseSpringTest {

	private static final Logger logger = LoggerFactory.getLogger(SkuDaoTest.class);
	
	@Autowired private SkuDao skuDao;
	
	@Test
	public void testFindSkuPage(){
		try{
			Pagination<SkuBean> pagination = new Pagination<SkuBean>();
			skuDao.findSkuPage(pagination);
			for(SkuBean bean : pagination.getItems()){
				Long id = bean.getId();
				Long categoryId = bean.getCategoryId();
				Date createTime = bean.getCreateTime();
				Double weight = bean.getWeight();
				logger.info("id:" + id + ",categoryId:" + categoryId + ",createTime:{},weight:{}", createTime, weight);
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}
	
	@Test
	public void testQuery(){
		skuDao.findSku();
	}
	
}
