package net.shangtech.framework.exceltest;

import java.util.Date;

import net.shangtech.framework.excel.Column;
import net.shangtech.framework.excel.WorkSheet;

@WorkSheet(start = 2)
public class ExcelBean {
	
	@Column(col = "A")
	private Integer id;
	
	@Column(col = "B")
	private Date createTime;
	
	@Column(col = "C")
	private Double price;
	
	@Column(col = "D")
	private Date version;
	
	@Column(col = "E")
	private Long groupId;
	
	@Column(col = "G")
	private Long skuId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	
	
}
