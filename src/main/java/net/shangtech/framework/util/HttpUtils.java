package net.shangtech.framework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	
	public static String get(String url, Map<String, String> params) {
		CloseableHttpClient client = HttpClients.createDefault();
		return get(client, url, params, null);
	}
	
	public static String get(String url, Map<String, String> params, Map<String, String> headers) {
		CloseableHttpClient client = HttpClients.createDefault();
		return get(client, url, params, headers);
	}
	
	public static String get(CloseableHttpClient client, String url, Map<String, String> params, Map<String, String> headers) {
		HttpGet get = new HttpGet(buildUrl(url, params));
		
		if(headers != null){
			for(Entry<String, String> entry : headers.entrySet()){
				get.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
			}
		}
		
		CloseableHttpResponse response = null;
		try{
			response = client.execute(get);
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK || response.getEntity() == null){
				throw new HttpRequestException();
			}
			return EntityUtils.toString(response.getEntity());
		}catch (Exception e) {
			throw new HttpRequestException(e);
		}finally{
			IOUtils.closeQuietly(response);
		}
	}
	
	public static String post(CloseableHttpClient client, String url, Map<String, String> params) {
		HttpPost post = new HttpPost(url);
		List<NameValuePair> vnps = new ArrayList<NameValuePair>();
		for(Entry<String, String> entry : params.entrySet()){
			NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
			vnps.add(pair);
		}
		CloseableHttpResponse response = null;
		try{
			HttpEntity entity = new UrlEncodedFormEntity(vnps, "UTF-8");
			post.setEntity(entity);
			response = client.execute(post);
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK || response.getEntity() == null){
				throw new HttpRequestException();
			}
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
			throw new HttpRequestException(e);
		}finally{
			IOUtils.closeQuietly(response);
		}
	}
	
	public static String buildUrl(String url, Map<String, String> params){
		if(params == null){
			return url;
		}
		StringBuilder sb = new StringBuilder(url);
		if(!url.contains("?")){
			sb.append("?_=&");
		}
		for(Entry<String, String> param : params.entrySet()){
			sb.append(param.getKey()).append("=").append(param.getValue()).append("&");
		}
		return sb.toString();
	}
}
class HttpRequestException extends RuntimeException {

	private static final long serialVersionUID = 4432785354455074459L;
	
	public HttpRequestException(String message){
		super(message);
	}
	
	public HttpRequestException(){
		super();
	}
	
	public HttpRequestException(Exception e){
		super(e);
	}
}
