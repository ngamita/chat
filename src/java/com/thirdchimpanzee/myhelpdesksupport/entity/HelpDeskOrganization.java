/**
 * My Help Desk Support 
 * Copyright (c) 2011 by Romin Irani. All Rights Reserved.
 */
package com.thirdchimpanzee.myhelpdesksupport.entity;

import javax.persistence.Id;

/**
 * Entity class for HelpDeskOrganization object
 * 
 */
public class HelpDeskOrganization {
	@Id private Long id;
	private String orgName;
	private String domainName;
	private String orgType;
	private String status;
	private String field1;
	private String field2;
	private String field3;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	/**
	 * @return the domainName
	 */
	public String getDomainName() {
		return domainName;
	}
	/**
	 * @param domainName the domainName to set
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	/**
	 * @return the orgType
	 */
	public String getOrgType() {
		return orgType;
	}
	/**
	 * @param orgType the orgType to set
	 */
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	/**
	 * @return the field1
	 */
	public String getField1() {
		return field1;
	}
	/**
	 * @param field1 the field1 to set
	 */
	public void setField1(String field1) {
		this.field1 = field1;
	}
	/**
	 * @return the field2
	 */
	public String getField2() {
		return field2;
	}
	/**
	 * @param field2 the field2 to set
	 */
	public void setField2(String field2) {
		this.field2 = field2;
	}
	/**
	 * @return the field3
	 */
	public String getField3() {
		return field3;
	}
	/**
	 * @param field3 the field3 to set
	 */
	public void setField3(String field3) {
		this.field3 = field3;
	}
	
}
