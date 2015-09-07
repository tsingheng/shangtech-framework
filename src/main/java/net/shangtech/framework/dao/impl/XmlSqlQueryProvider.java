package net.shangtech.framework.dao.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.shangtech.framework.dao.QueryProvider;
import net.shangtech.framework.util.MapHolder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlSqlQueryProvider implements QueryProvider, InitializingBean {
	
	private static final Logger logger = LoggerFactory.getLogger(XmlSqlQueryProvider.class);

	private Map<String, SqlHolder> sqlMap = new HashMap<String, SqlHolder>();
	
	private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
	
	private Set<String> resolvedResources = new HashSet<String>();
	
	private DocumentBuilder db;
	
	private String[] definitions;
	
	VelocityEngine ve;
	
	public XmlSqlQueryProvider() throws ParserConfigurationException{
		ve = new VelocityEngine();
		ve.init();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
	}
	
	@Override
	public String getSqlById(String id, MapHolder<String> mapHolder) {
		SqlHolder holder = sqlMap.get(id);
		if(holder == null){
			throw new IllegalArgumentException("can not find query id [" + id + "]");
		}
		String sql = holder.getSql();
		if(BooleanUtils.isTrue(holder.getTemplate()) && mapHolder != null){
			Map<String, Object> params = mapHolder.getMap();
			//模板解析
			VelocityContext context = new VelocityContext();
			for(Entry<String, Object> entry : params.entrySet()){
				context.put(entry.getKey(), entry.getValue());
			}
			StringWriter writer = new StringWriter();
			ve.evaluate(context, writer, "XmlSqlQueryProvider.parseSql", sql);
			sql = writer.getBuffer().toString();
			IOUtils.closeQuietly(writer);
		}
		return sql;
	}
	
	@Override
	public String getCountById(String id, MapHolder<String> mapHolder) {
		SqlHolder holder = sqlMap.get(id);
		if(holder == null || StringUtils.isBlank(holder.getCountSql())){
			throw new IllegalArgumentException("can not find count query id [" + id + "]");
		}
		String sql = holder.getCountSql();
		if(BooleanUtils.isTrue(holder.getTemplate()) && mapHolder != null){
			Map<String, Object> params = mapHolder.getMap();
			//模板解析
			VelocityContext context = new VelocityContext();
			for(Entry<String, Object> entry : params.entrySet()){
				context.put(entry.getKey(), entry.getValue());
			}
			StringWriter writer = new StringWriter();
			ve.evaluate(context, writer, "XmlSqlQueryProvider.parseSql", sql);
			sql = writer.getBuffer().toString();
			IOUtils.closeQuietly(writer);
		}
		return sql;
	}

	public String[] getDefinitions() {
		return definitions;
	}

	public void setDefinitions(String[] definitions) {
		this.definitions = definitions;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(definitions != null){
			for(String definition : definitions){
				Resource[] resources = resourceResolver.getResources(definition);
				if(resources != null){
					for(Resource resource : resources){
						processConfig(resource);
					}
				}
			}
		}
	}
	
	private void processConfig(Resource resource) throws IOException, SAXException{
		String url = resource.getURL().getPath();
		if(resolvedResources.contains(url)){
			return;
		}
		resolvedResources.add(url);
		Document document = db.parse(resource.getInputStream());
		NodeList sqlNodeList = document.getElementsByTagName("sql");
		for(int i = 0; i < sqlNodeList.getLength(); i++){
			Node sqlNode = sqlNodeList.item(i);
			if(sqlNode.getNodeType() != Node.ELEMENT_NODE){
				continue;
			}
			Element ele = (Element) sqlNode;
			String id = ele.getAttribute("id");
			if(StringUtils.isBlank(id)){
				throw new RuntimeException("id must be defined, Offending resource: URL [" + url + "]");
			}
			if(sqlMap.get(id) != null){
				throw new RuntimeException("dumplicated sql id [" + id + "]");
			}
			Boolean template = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(ele.getAttribute("template")));
			NodeList queryNodes = ele.getElementsByTagName("query");
			if(queryNodes.getLength() == 0){
				throw new RuntimeException("missing query node of sql [" + id + "]");
			}
			SqlHolder holder = new SqlHolder();
			String sql = queryNodes.item(0).getTextContent();
			holder.setSql(sql);
			NodeList countNodes = ele.getElementsByTagName("count");
			if(countNodes.getLength() > 0){
				String count = countNodes.item(0).getTextContent();
				holder.setCountSql(count);
			}
			holder.setTemplate(template);
			sqlMap.put(id, holder);
		}
		logger.info("find {} sql definitions in [{}]", sqlNodeList.getLength(), url);
	}
}