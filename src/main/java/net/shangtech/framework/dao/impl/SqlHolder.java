package net.shangtech.framework.dao.impl;

public class SqlHolder {
	
	private String sql;
	
	private String countSql;
	
	private Boolean template;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getCountSql() {
		return countSql;
	}

	public void setCountSql(String countSql) {
		this.countSql = countSql;
	}

	public Boolean getTemplate() {
		return template;
	}

	public void setTemplate(Boolean template) {
		this.template = template;
	}
	
}
