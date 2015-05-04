package net.shangtech.framework.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.shangtech.framework.orm.dao.support.BaseEntity;

@Entity
@Table(name = "t_test")
public class TestEntity extends BaseEntity<Long> {

	/**
	 * 
	 */
    private static final long serialVersionUID = 1L;
	
    @Column
    private String testName;

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}
    
}
