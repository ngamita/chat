/**
 * My Help Desk Support 
 * Copyright (c) 2011 by Romin Irani. All Rights Reserved.
 */
package com.thirdchimpanzee.myhelpdesksupport.entity;

import java.util.Date;

import javax.persistence.Id;

/**
 * Entity class for HelpDeskQuestion object
 * 
 */
public class OrganizationResetTask {
	@Id private Long id;
	private Long orgId;
	private Long orgAdminUserId;
	private String emailAddress;
	private String status;
	private Date resetTaskDateTime;
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
	 * @return the orgId
	 */
	public Long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the orgAdminUserId
	 */
	public Long getOrgAdminUserId() {
		return orgAdminUserId;
	}
	/**
	 * @param orgAdminUserId the orgAdminUserId to set
	 */
	public void setOrgAdminUserId(Long orgAdminUserId) {
		this.orgAdminUserId = orgAdminUserId;
	}
	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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
	 * @return the resetTaskDateTime
	 */
	public Date getResetTaskDateTime() {
		return resetTaskDateTime;
	}
	/**
	 * @param resetTaskDateTime the resetTaskDateTime to set
	 */
	public void setResetTaskDateTime(Date resetTaskDateTime) {
		this.resetTaskDateTime = resetTaskDateTime;
	}
}
