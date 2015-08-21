package net.shangtech.framework.orm.test;

import java.util.List;

import net.shangtech.framework.BaseSpringTest;
import net.shangtech.framework.orm.dao.support.AnnotationBeanResultTransformer;
import net.shangtech.framework.orm.pojo.SkuDTO;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

public class JDBCTest extends BaseSpringTest {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Test
	public void test(){
		try{
			Session session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("select * from t_sku");
//			query.setResultTransformer(new AliasToBeanResultTransformer(SkuDTO.class));
//			query.addEntity(Sku.class);
			query.setResultTransformer(new AnnotationBeanResultTransformer(SkuDTO.class));
			List<SkuDTO> list = query.list();
			System.out.println(JSON.toJSONString(list));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
