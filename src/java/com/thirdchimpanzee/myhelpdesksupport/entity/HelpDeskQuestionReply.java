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
public class HelpDeskQuestionReply {
	@Id private Long id;
	private long   questionId;
	private String replyBy;
	private Date   replyDateTime;
	private String reply;
	private Long   orgId;
	private Long   replyByUserId;
	private boolean isAgent;
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
	 * @return the questionId
	 */
	public long getQuestionId() {
		return questionId;
	}
	/**
	 * @param questionId the questionId to set
	 */
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}
	/**
	 * @return the replyBy
	 */
	public String getReplyBy() {
		return replyBy;
	}
	/**
	 * @param replyBy the replyBy to set
	 */
	public void setReplyBy(String replyBy) {
		this.replyBy = replyBy;
	}
	/**
	 * @return the replyDateTime
	 */
	public Date getReplyDateTime() {
		return replyDateTime;
	}
	/**
	 * @param replyDateTime the replyDateTime to set
	 */
	public void setReplyDateTime(Date replyDateTime) {
		this.replyDateTime = replyDateTime;
	}
	/**
	 * @return the reply
	 */
	public String getReply() {
		return reply;
	}
	/**
	 * @param reply the reply to set
	 */
	public void setReply(String reply) {
		this.reply = reply;
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
	 * @return the replyByUserId
	 */
	public Long getReplyByUserId() {
		return replyByUserId;
	}
	/**
	 * @param replyByUserId the replyByUserId to set
	 */
	public void setReplyByUserId(Long replyByUserId) {
		this.replyByUserId = replyByUserId;
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
	
	
}
