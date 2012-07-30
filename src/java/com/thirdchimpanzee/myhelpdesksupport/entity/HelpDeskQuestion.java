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
public class HelpDeskQuestion {
	@Id private Long id;
	private String question;
	private String askedBy;
	private Date   askedDateTime;
	private Date   assignedDateTime;
	private String answeredBy;
	private Date   answeredByDateTime;
	private Long   agentId;
	private String answer;
	private Long   orgId;
	private Long   userId;
	private String emailAddress;
	private String status;
	private int    rating;
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
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	/**
	 * @return the askedBy
	 */
	public String getAskedBy() {
		return askedBy;
	}
	/**
	 * @param askedBy the askedBy to set
	 */
	public void setAskedBy(String askedBy) {
		this.askedBy = askedBy;
	}
	/**
	 * @return the askedDateTime
	 */
	public Date getAskedDateTime() {
		return askedDateTime;
	}
	/**
	 * @param askedDateTime the askedDateTime to set
	 */
	public void setAskedDateTime(Date askedDateTime) {
		this.askedDateTime = askedDateTime;
	}
	/**
	 * @return the answeredBy
	 */
	public String getAnsweredBy() {
		return answeredBy;
	}
	/**
	 * @param answeredBy the answeredBy to set
	 */
	public void setAnsweredBy(String answeredBy) {
		this.answeredBy = answeredBy;
	}
	/**
	 * @return the answeredByDateTime
	 */
	public Date getAnsweredByDateTime() {
		return answeredByDateTime;
	}
	/**
	 * @param answeredByDateTime the answeredByDateTime to set
	 */
	public void setAnsweredByDateTime(Date answeredByDateTime) {
		this.answeredByDateTime = answeredByDateTime;
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
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
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
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}
	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}
	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	/**
	 * @return the agentId
	 */
	public Long getAgentId() {
		return agentId;
	}
	/**
	 * @param agentId the agentId to set
	 */
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	/**
	 * @return the assignedDateTime
	 */
	public Date getAssignedDateTime() {
		return assignedDateTime;
	}
	/**
	 * @param assignedDateTime the assignedDateTime to set
	 */
	public void setAssignedDateTime(Date assignedDateTime) {
		this.assignedDateTime = assignedDateTime;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HelpDeskQuestion [" + "question=" + question + ", answer=" + answer + ", answeredByDateTime=" + answeredByDateTime + ", askedBy=" + askedBy + ", askedDateTime=" + askedDateTime  + ", id=" + id + ", orgId=" + orgId+ ", rating=" + rating + ", status="	+ status + "]";
	}
	
	
	
}
