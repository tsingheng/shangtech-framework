package net.shangtech.framework.orm.dao.support;

public class SqlHolder {
	/** sql内容 */
	private String sql;
	
	/** 是否使用模板解析sql */
	private Boolean template;
	
	public String getSql() {
		return sql;
	}
	
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public Boolean getTemplate() {
		return template;
	}
	
	public void setTemplate(Boolean template) {
		this.template = template;
	}
	
}
