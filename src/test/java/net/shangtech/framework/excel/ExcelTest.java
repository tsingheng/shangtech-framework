package net.shangtech.framework.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class ExcelTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelTest.class);

	@Test
	public void test() throws FileNotFoundException{
		InputStream is = new FileInputStream("E:\\git\\shangtech-framework\\test-files\\excel-test-1.xls");
		WorkbookReader<TestObject> reader = new WorkbookReader<TestObject>(TestObject.class);
		reader.read(is);
		List<TestObject> list = reader.getAllSuccessList();
		logger.info(JSONObject.toJSONString(list));
	}
	
}
