package net.shangtech.framework.orm;

import javax.persistence.Column;

public class TestBean extends TestEntity {

	/**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Column
    private String beanName;

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
    
}
