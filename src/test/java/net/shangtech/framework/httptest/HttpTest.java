package net.shangtech.framework.httptest;

import java.util.HashMap;
import java.util.Map;

import net.shangtech.framework.util.HttpUtils;

import org.junit.Test;

public class HttpTest {

	@Test
	public void test1(){
		String url = "http://restapi.amap.com/v3/place/text";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("s", "rsv3");
		params.put("key", "d6cb1312d3f167ed6f00d94417771eab");
		params.put("keywords", "汽车维修");
		params.put("city", "泉州市");
		
		String result = HttpUtils.get(url, params);
		System.out.println(result);
	}
}
