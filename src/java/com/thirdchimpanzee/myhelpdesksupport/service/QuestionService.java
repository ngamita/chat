package com.thirdchimpanzee.myhelpdesksupport.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskOrganization;
import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskQuestion;
import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskQuestionReply;
import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskUser;

public class QuestionService {
	public static final Logger _logger = Logger.getLogger(QuestionService.class.toString());

	private static QuestionService _self = null;

	private QuestionService() {
	}

	public static QuestionService getInstance() {
		if (_self == null) {
			_self = new QuestionService();
			ObjectifyService.register(HelpDeskQuestion.class);
			ObjectifyService.register(HelpDeskQuestionReply.class);
		}
		return _self;
	}

	public String addQuestion(HelpDeskQuestion _question) throws Exception {
		Objectify obj = ObjectifyService.begin();
		HelpDeskQuestion _record = new HelpDeskQuestion();
		_record.setQuestion(_question.getQuestion());
		_record.setAskedBy(_question.getAskedBy());
		_record.setAskedDateTime(new Date());
		_record.setEmailAddress(_question.getEmailAddress());
		_record.setOrgId(_question.getOrgId());
		_record.setStatus("NEW");
		_record.setRating(5);
		_record.setUserId(_question.getUserId());
		obj.put(_record);
		return "success";
	}
		
	public String addReply(HelpDeskQuestionReply _reply) throws Exception {
		Objectify obj = ObjectifyService.begin();
		HelpDeskQuestionReply _record = new HelpDeskQuestionReply();
		_record.setQuestionId(_reply.getQuestionId());
		_record.setReply(_reply.getReply());
		_record.setReplyBy(_reply.getReplyBy());
		_record.setAgent(_reply.isAgent());
		_record.setOrgId(_reply.getOrgId());
		_record.setReplyByUserId(_reply.getReplyByUserId());
		_record.setReplyDateTime(_reply.getReplyDateTime());
		obj.put(_record);
		return "success";
	}

	public HelpDeskQuestion findHelpDeskQuestionById(String id) {
		try {
			Objectify obj = ObjectifyService.begin();
			HelpDeskQuestion r = obj.query(HelpDeskQuestion.class).filter("id",Long.parseLong(id)).get();
			if (r != null)
				return r;
			return null;
		} catch (Exception ex) {
			return null;
		}
	}

	public List<HelpDeskQuestion> getAllHelpDeskQuestionsByOrgAndUser(String orgId, String userId) throws Exception {
		List<HelpDeskQuestion> _results = new ArrayList<HelpDeskQuestion>();
		Objectify obj = ObjectifyService.begin();
		_results = obj.query(HelpDeskQuestion.class).filter("orgId",Long.parseLong(orgId)).filter("userId",Long.parseLong(userId)).list();
		return _results;
	}
	
	public List<HelpDeskQuestion> getAllHelpDeskQuestionsByOrgAndAgent(String orgId, String agentId) throws Exception {
		List<HelpDeskQuestion> _results = new ArrayList<HelpDeskQuestion>();
		Objectify obj = ObjectifyService.begin();
		_results = obj.query(HelpDeskQuestion.class).filter("orgId",Long.parseLong(orgId)).filter("agentId",Long.parseLong(agentId)).list();
		return _results;
	}

	public List<HelpDeskQuestion> getAllHelpDeskQuestionsByOrgAndAgentAndStatus(String orgId, String agentId,String status) throws Exception {
		List<HelpDeskQuestion> _results = new ArrayList<HelpDeskQuestion>();
		Objectify obj = ObjectifyService.begin();
		_results = obj.query(HelpDeskQuestion.class).filter("orgId",Long.parseLong(orgId)).filter("agentId",Long.parseLong(agentId)).filter("status",status).list();
		return _results;
	}

	public List<HelpDeskQuestion> getAllHelpDeskQuestionsByOrgAndUserAndStatus(String orgId, String userId,String status) throws Exception {
		List<HelpDeskQuestion> _results = new ArrayList<HelpDeskQuestion>();
		Objectify obj = ObjectifyService.begin();
		_results = obj.query(HelpDeskQuestion.class).filter("orgId",Long.parseLong(orgId)).filter("userId",Long.parseLong(userId)).filter("status",status).list();
		return _results;
	}

	public List<HelpDeskQuestion> getAllHelpDeskQuestionsByOrg(String orgId) throws Exception {
		List<HelpDeskQuestion> _results = new ArrayList<HelpDeskQuestion>();
		Objectify obj = ObjectifyService.begin();
		_results = obj.query(HelpDeskQuestion.class).filter("orgId",Long.parseLong(orgId)).list();
		return _results;
	}
	
	public String removeHelpDeskQuestionByID(String id) throws Exception {
		Objectify obj = ObjectifyService.begin();
		try {
			HelpDeskQuestion r = obj.query(HelpDeskQuestion.class).filter("id",Long.parseLong(id)).get();
			if (r != null) {
				obj.delete(r);
				return "success";
			}
			else {
				return "fail";
			}
		}
		catch (Exception ex) {
			throw new Exception("Could not remove the HelpDeskQuestion. Please try again.");
		}
	}
		
		public String setHelpDeskQuestionStatus(String id, String status) throws Exception {
			Objectify obj = ObjectifyService.begin();
			try {
				HelpDeskQuestion r = obj.query(HelpDeskQuestion.class).filter("id",Long.parseLong(id)).get();
				if (r != null) {
					r.setStatus(status);
					obj.put(r);
					if (status.equals("CLOSED")) {
						//Make the Agent available
						UserService.getInstance().setUserStatus(r.getAgentId(), "AVAILABLE");
					}
					return "success";
				}
				else {
					return "fail";
				}
			}
			catch (Exception ex) {
				throw new Exception("Could not set the HelpDeskQuestion status. Please try again.");
			}		
		}
		
		public String setHelpDeskQuestionRating(String id, int rating) throws Exception {
			Objectify obj = ObjectifyService.begin();
			try {
				HelpDeskQuestion r = obj.query(HelpDeskQuestion.class).filter("id",Long.parseLong(id)).get();
				if (r != null) {
					r.setRating(rating);
					r.setStatus("CLOSED_RATED");
					obj.put(r);
					return "success";
				}
				else {
					return "fail";
				}
			}
			catch (Exception ex) {
				throw new Exception("Could not set the HelpDeskQuestion rating. Please try again.");
			}		
		}

		
		public String assignHelpDeskQuestionAgent(Long id, Long agentId, String agentName) throws Exception {
			Objectify obj = ObjectifyService.begin();
			try {
				HelpDeskQuestion r = obj.query(HelpDeskQuestion.class).filter("id",id).get();
				if (r != null) {
					r.setAgentId(agentId);
					r.setAnsweredBy(agentName);
					r.setAssignedDateTime(new Date());
					r.setStatus("ASSIGNED");
					obj.put(r);
					return "success";
				}
				else {
					return "fail";
				}
			}
			catch (Exception ex) {
				throw new Exception("Could not set assign the question to the HelpDeskQuestion Agent. Please try again.");
			}		
		}

		public String setHelpDeskQuestionAnswer(Long id, Long agentId, String agentName, String answer) throws Exception {
			Objectify obj = ObjectifyService.begin();
			try {
				HelpDeskQuestion r = obj.query(HelpDeskQuestion.class).filter("id",id).get();
				if (r != null) {
					r.setAgentId(agentId);
					r.setAnsweredBy(agentName);
					r.setAnsweredByDateTime(new Date());
					r.setAnswer(answer);
					r.setStatus("ANSWERED");
					obj.put(r);
					return "success";
				}
				else {
					return "fail";
				}
			}
			catch (Exception ex) {
				throw new Exception("Could not set the HelpDeskQuestion answer. Please try again.");
			}		
		}
		
		public String assignAgent(Long OrgId, long AgentId) {
			try {
				Objectify obj = ObjectifyService.begin();
				HelpDeskUser _User = UserService.getInstance().findUserById(String.valueOf(AgentId));
				if (_User == null) throw new Exception("No such user exists in the System.");
				List<HelpDeskQuestion> _results = new ArrayList<HelpDeskQuestion>();
				_results = obj.query(HelpDeskQuestion.class).filter("status","NEW").filter("orgId",OrgId).list();
				if (_results.size() == 0) {
					return "Sorry! There are no Open questions in the system for your Organization.";
				}
				for (HelpDeskQuestion helpDeskQuestion : _results) {
						//Check if Agent is still AVAILABLE
						
						if (_User.getStatus().equals("AVAILABLE")) {
							//Can be assigned
							String strResult = "";
							//Set the Agent status to BUSY
							strResult = UserService.getInstance().setUserStatus(AgentId, "BUSY");
							if (strResult.equals("success")) {
								//Update the Question Data i.e. Agent assigned in the Question
								strResult = QuestionService.getInstance().assignHelpDeskQuestionAgent(helpDeskQuestion.getId(), _User.getId(), _User.getUsername());
								if (strResult.equals("success")) {
									//Send XMPP Message
									return helpDeskQuestion.getAskedBy() + " is in need of support, perhaps you can answer the question: " + helpDeskQuestion.getQuestion();
								}
								else {
									return "There was an error in assigning you a support question. Please try again.";
								}
							}
							else {
								//Question is assigned
								return "There was an error in assigning you a Support Question. Please answer that first.";
							}
						}
						else {
							return "A Support Question has already been assigned to you. Please answer that first.";
						}
				}
				return "Sorry! There are no Open questions in the system for your Organization.";
			}
			catch (Exception ex) {
				_logger.log(Level.SEVERE,"There was an error in assigning you a Support Question. Please answer that first." + ex.getMessage());
				return null;
			}
		}

		public void assignAgents() {
			//For each NEW Question, see if any agents are free. If yes, assign to them 
			//and send the message to the Agent.
			try {
				List<HelpDeskUser> _agents = UserService.getInstance().getAllAgentsByStatus("AVAILABLE");
				List<HelpDeskQuestion> _results = new ArrayList<HelpDeskQuestion>();
				Objectify obj = ObjectifyService.begin();
				_results = obj.query(HelpDeskQuestion.class).filter("status","NEW").list();
				for (HelpDeskQuestion helpDeskQuestion : _results) {
					//Find Agents that are available and XMPP STATUS = AVAILABLE
					for (HelpDeskUser agent : _agents) {
						if (agent.getOrgId().equals(helpDeskQuestion.getOrgId())) {
							if ((agent.getStatus().equals("AVAILABLE")) && (agent.getXMPPStatus().equals("AVAILABLE"))) {
								//Can be assigned
								String strResult = "";
								//Set the Agent status to BUSY
								strResult = UserService.getInstance().setUserStatus(agent.getId(), "BUSY");
								if (strResult.equals("success")) {
									//Update the Question Data i.e. Agent assigned in the Question
									strResult = QuestionService.getInstance().assignHelpDeskQuestionAgent(helpDeskQuestion.getId(), agent.getId(), agent.getUsername());
									if (strResult.equals("success")) {
										//Send XMPP Message
										sendIM(agent.getEmailAddress(), helpDeskQuestion.getAskedBy() + " is in need of support, perhaps you can answer the question: " + helpDeskQuestion.getQuestion());
									}
								}
							}
						}
					}
				}
			}
			catch (Exception ex) {
			  _logger.log(Level.SEVERE,"Error in assigning question to Agents : " + ex.getMessage());	
			}
			
		}

		
		public void sendAgentReplies() {
			//For each NEW Question, see if any agents are free. If yes, assign to them 
			//and send the message to the Agent.
			try {
				List<HelpDeskQuestion> _results = new ArrayList<HelpDeskQuestion>();
				Objectify obj = ObjectifyService.begin();
				_results = obj.query(HelpDeskQuestion.class).filter("status","ANSWERED").list();
				for (HelpDeskQuestion helpDeskQuestion : _results) {
					//Find the User for this Question and see if XMPP STATUS = AVAILABLE
					HelpDeskUser _User = UserService.getInstance().findUserById(String.valueOf(helpDeskQuestion.getUserId()));
					if (_User != null) {
						if ((_User.getStatus().equals("AVAILABLE")) && (_User.getXMPPStatus().equals("AVAILABLE"))) {
							sendIM(_User.getEmailAddress(),helpDeskQuestion.getAnsweredBy() + "  suggests, " + helpDeskQuestion.getAnswer());
							String strResult = "";
							strResult = QuestionService.getInstance().setHelpDeskQuestionStatus(String.valueOf(helpDeskQuestion.getId()), "ANSWERSENT");
						}
					}
				}
			}
			catch (Exception ex) {
			  _logger.log(Level.SEVERE,"Error in assigning question to Agents : " + ex.getMessage());	
			}
		}
		
		
		/**
		 * 
		 * @param JabberId The JabberId of the user to send out the XMPP message to
		 * @param msg The message i.e. the text that we need to send out in the XMPP message. 
		 * @throws Exception
		 */
		private void sendIM(String JabberId, String msg) throws Exception {
			XMPPService xmpp = null;
			JID fromJid = new JID(JabberId);
			xmpp = XMPPServiceFactory.getXMPPService();
			Message replyMessage = new MessageBuilder()
	        .withRecipientJids(fromJid)
	        .withBody(msg)
	        .build();
	        boolean messageSent = false;
	        //The condition is commented out so that it can work over non Google Talk XMPP providers also.
	        //if (xmpp.getPresence(fromJid).isAvailable()) {  
	        SendResponse status = xmpp.sendMessage(replyMessage);
	        messageSent = (status.getStatusMap().get(fromJid) == SendResponse.Status.SUCCESS);
	        //}
	        if (messageSent) {
	        	_logger.info("Message has been sent successfully");
	        }
	        else {
	        	_logger.info("Message could not be sent");
	        }
		}
}
