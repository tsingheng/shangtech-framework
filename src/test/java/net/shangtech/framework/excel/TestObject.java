package net.shangtech.framework.excel;

import java.util.Date;

import net.shangtech.framework.excel.converter.String2DateConverter;
import net.shangtech.framework.excel.support.Column;

public class TestObject {
	
	private Long id;
	
	private String name;
	
	private Date birthday;

	public Long getId() {
		return id;
	}

	@Column(name = "编号")
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@Column(name = "姓名")
	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	@Column(name = "生日", converter = String2DateConverter.class, pattern = "yyyy-MM-dd")
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	
}
