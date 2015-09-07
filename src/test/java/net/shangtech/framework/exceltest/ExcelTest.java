package net.shangtech.framework.exceltest;

import java.io.FileInputStream;
import java.io.InputStream;

import net.shangtech.framework.excel.WorkbookReader;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class ExcelTest {

	@Test
	public void test() {
		try{
			InputStream is = new FileInputStream("D:\\0831商品价格异常.xls");
			WorkbookReader<ExcelBean> reader = new WorkbookReader<ExcelBean>(ExcelBean.class);
			reader.read(is);
			System.out.println(JSON.toJSONString(reader.getResult()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
