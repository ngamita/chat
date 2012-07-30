/**
 * My Help Desk Support 
 * Copyright (c) 2011 by Romin Irani. All Rights Reserved.
 */
package com.thirdchimpanzee.myhelpdesksupport.entity;

import javax.persistence.Id;

/**
 * Entity class for HelpDeskUser object
 * 
 */
public class HelpDeskUser {
	@Id private Long id;
	private String username;
	private String emailAddress;
	private boolean isAdmin;
	private boolean isAgent;
	private String status;
	private Long orgId;
	private String XMPPStatus;
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
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
	 * @return the isAdmin
	 */
	public boolean isAdmin() {
		return isAdmin;
	}
	/**
	 * @param isAdmin the isAdmin to set
	 */
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	/**
	 * @return the isAgent
	 */
	public boolean isAgent() {
		return isAgent;
	}
	/**
	 * @param isAgent the isAgent to set
	 */
	public void setAgent(boolean isAgent) {
		this.isAgent = isAgent;
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
	 * @return the xMPPStatus
	 */
	public String getXMPPStatus() {
		return XMPPStatus;
	}
	/**
	 * @param xMPPStatus the xMPPStatus to set
	 */
	public void setXMPPStatus(String xMPPStatus) {
		XMPPStatus = xMPPStatus;
	}
}
