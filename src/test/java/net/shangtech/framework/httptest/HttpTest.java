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
		params.put("city", "丰泽区");
		
		String result = HttpUtils.get(url, params);
		System.out.println(result);
	}
	
	@Test
	public void testWeixin(){
//		String url = "http://m.jd.com/market/dl.action?from=Jd_index";
		String url = "http://union.m.jd.com/download/go.action?unionId=Jd_index&subunionId=PCDownLoad&to=http%3A%2F%2Fstorage.jd.com%2Fjdmobile%2FJd_index_1453791583251.apk%3FtimeStamp%3D1454056364266&key=b88904a03342d2b07423cc8ce6ac73b6";
		Map<String, String> headers = buildWeixinHeaders();
		String result = HttpUtils.get(url, null, headers);
		System.out.println(result);
	}
	
	private Map<String, String> buildWeixinHeaders(){
		Map<String, String> headers = new HashMap<>();
		headers.put("accept-language", "zh-CN");
		headers.put("user-agent", "Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; HM NOTE 1S Build/KTU84P) AppleWebKit/533.1 (KHTML, like Gecko)Version/4.0 MQQBrowser/5.4 TBS/025489 Mobile Safari/533.1 MicroMessenger/6.3.11.49_rc8fa1c5.720 NetType/WIFI Language/zh_CN");
		headers.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headers.put("accept-charset", "utf-8, iso-8859-1, utf-16, *;q=0.7");
		headers.put("accept-encoding", "gzip");
		headers.put("connection", "keep-alive");
		headers.put("host", "union.m.jd.com");
		return headers;
	}
}
