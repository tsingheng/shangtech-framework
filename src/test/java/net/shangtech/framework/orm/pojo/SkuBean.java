package net.shangtech.framework.orm.pojo;


import java.util.Date;

import javax.persistence.Column;

import net.shangtech.framework.orm.entity.Sku;

public class SkuBean extends Sku {

	private static final long serialVersionUID = 1L;

	@Column(name = "create_time")
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
