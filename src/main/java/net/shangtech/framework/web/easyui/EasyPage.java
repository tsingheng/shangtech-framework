package net.shangtech.framework.web.easyui;

import java.io.Serializable;
import java.util.List;

import net.shangtech.framework.dao.Pagination;

public class EasyPage<T> implements Serializable {

	private static final long serialVersionUID = 2026686164481108854L;
	
	private Integer total;
	
	private List<T> rows;
	
	private List<T> footer;
	
	public static <T> EasyPage<T> convert(Pagination<T> pagination){
		EasyPage<T> page = new EasyPage<T>();
		page.setTotal(pagination.getTotalCount());
		page.setRows(pagination.getItems());
		return page;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public List<T> getFooter() {
		return footer;
	}

	public void setFooter(List<T> footer) {
		this.footer = footer;
	}
	
}
