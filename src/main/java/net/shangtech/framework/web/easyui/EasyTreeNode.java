package net.shangtech.framework.web.easyui;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class EasyTreeNode implements Serializable {

	private static final long serialVersionUID = -6709306627303480700L;
	
	public static final String STATE_OPEN = "open";
	public static final String STATE_CLOSED = "closed";

	private Long id;
	
	private String text;
	
	private String state;
	
	private Long parentId;
	
	private List<EasyTreeNode> children;
	
	private Map<String, Object> attribute;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<EasyTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<EasyTreeNode> children) {
		this.children = children;
	}

	public Map<String, Object> getAttribute() {
		return attribute;
	}

	public void setAttribute(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
}
