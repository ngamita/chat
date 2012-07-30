package com.thirdchimpanzee.myhelpdesksupport.xmpp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskQuestion;
import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskQuestionReply;
import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskUser;
import com.thirdchimpanzee.myhelpdesksupport.service.OrganizationService;
import com.thirdchimpanzee.myhelpdesksupport.service.QuestionService;
import com.thirdchimpanzee.myhelpdesksupport.service.UserService;

/**
 * This is the Chat Interface to the Application. All interactions between the Google Talk User and the application is routed through this.
 * 
 * It is easy to follow the code. 
 * 1. The main message pump is the doGet method below from which we first use some XMPP semantics to retrieve out who is sending us the message
 * and the text of the message.
 * 
 * 2. Once we have the message, we need to interpret it and compare it against the commands that we understand. If we understand the command i.e. help,
 * about, remove then we can process them otherwise we need to send back a message saying that we do not understand the command. 
 * 
 * @author irani_r
 * @version 1.0
 * 
 */


@SuppressWarnings("serial")
public class XMPPMessageServlet extends HttpServlet {
	public static final Logger _log = Logger.getLogger(XMPPMessageServlet.class.getName());
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String strCallResult="";
		String strStatus="";
		resp.setContentType("text/plain");
		XMPPService xmpp = null;
		JID fromJid = null;
		try {
	
			HelpDeskUser _User = null;
			//STEP 1 - Extract out the message and the Jabber Id of the user sending us the message via the Google Talk client
			xmpp = XMPPServiceFactory.getXMPPService();
			Message msg = xmpp.parseMessage(req);

			fromJid = msg.getFromJid();
			String body = msg.getBody();
			
			_log.info("Received a message from " + fromJid.getId() + " and body = " + body);
			String emailId = fromJid.getId().substring(0,fromJid.getId().indexOf("/"));
			String userId = fromJid.getId();
			//String emailId = fromJid.getId();
			_log.info("Email Id : " + userId);
			
			//String strWord = req.getParameter("command");
			String strCommand = body;
			
			//Do validations here. Only basic ones i.e. cannot be null/empty
			if (strCommand == null) throw new Exception("You must give a command.");
			
			//Trim the stuff
			strCommand = strCommand.trim();
			if (strCommand.length() == 0) throw new Exception("You must give a command.");
			
			/**
			 * STEP 2 : Now that we have something, compare it against the commands that understand and process them accordingly.
			 * 
			 * We currently support only 2 commands that are not single word commands. 
			 * 1. remove [ID] : This removes a particular specified ACTIVE Reminder in the system. ACTIVE Reminders are those reminders that have not yet 
			 * got triggered. The reminder is specified by an ID, which are retrieved by the list command
			 * 2. 
			 */
			
			//Find the user first
			try {
				_User = UserService.getInstance().findUserByEmailId(emailId);
				//if (_User == null) throw new Exception("You are not registered in the Support System. No commands will be honoured.");
				if (_User.getStatus().equals("INACTIVE")) throw new Exception("You are current set to INACTIVE in the Support System. Please contact your Organization Administrator.");
			}
			catch (Exception ex) {
				_log.log(Level.SEVERE,"Could not identify the User in the System. Reason : " + ex.getMessage());
				throw new Exception(ex.getMessage());
			}

			
			String[] words = strCommand.split(" ");
			if (words.length >= 2) {
				try {
					//Determine what the command is
					if (words[0].equalsIgnoreCase("/reply")) {
						//if (!_User.isAgent()) throw new Exception("Error: Only an Agent can use this command.");
						String data = strCommand.replaceFirst("/reply", "").trim();
						if (data.length() == 0) throw new Exception("You must provide the reply text message.");
						
						if (_User.isAgent()) {
							//Retrieve the Question that the Agent is currently answering
							List<HelpDeskQuestion> _questions = QuestionService.getInstance().getAllHelpDeskQuestionsByOrgAndAgentAndStatus(String.valueOf(_User.getOrgId()), String.valueOf(_User.getId()), "ANSWERSENT");
							for (HelpDeskQuestion helpDeskQuestion : _questions) {
								//Create the Reply and Add That.
								HelpDeskQuestionReply _Reply = new HelpDeskQuestionReply();
								_Reply.setOrgId(_User.getOrgId());
								_Reply.setQuestionId(helpDeskQuestion.getId());
								_Reply.setReply(data);
								_Reply.setReplyBy(_User.getUsername());
								_Reply.setReplyDateTime(new Date());
								String strResult = QuestionService.getInstance().addReply(_Reply);
								if (strResult.equalsIgnoreCase("success")) {
									strCallResult = "Thank You for your reply.";
								}
								else {
									strCallResult = "Sorry! There was an error saving your reply. Please try again.";
								}
							}
						}
						else {
							//Retrieve the Question that the user got the ANSWERSENT for
							//Retrieve the Question that the Agent is currently answering
							List<HelpDeskQuestion> _questions = QuestionService.getInstance().getAllHelpDeskQuestionsByOrgAndUserAndStatus(String.valueOf(_User.getOrgId()), String.valueOf(_User.getId()), "ANSWERSENT");
							for (HelpDeskQuestion helpDeskQuestion : _questions) {
								//Create the Reply and Add That.
								HelpDeskQuestionReply _Reply = new HelpDeskQuestionReply();
								_Reply.setOrgId(_User.getOrgId());
								_Reply.setQuestionId(helpDeskQuestion.getId());
								_Reply.setReply(data);
								_Reply.setReplyBy(_User.getUsername());
								_Reply.setReplyDateTime(new Date());
								_Reply.setReplyByUserId(_User.getId());
								String strResult = QuestionService.getInstance().addReply(_Reply);
								if (strResult.equalsIgnoreCase("success")) {
									strCallResult = "Thank You for your reply.";
								}
								else {
									strCallResult = "Sorry! There was an error saving your reply. Please try again.";
								}
							}
						}
						
						//strCallResult = "/reply -- Not Yet Implemented.";
					}
					else if (words[0].equalsIgnoreCase("/support")) {
						//Check if the role is User
						//if (_User.isAgent()) throw new Exception("Error: Only a User can use this command.");
						String data = strCommand.replaceFirst("/support", "").trim();
						if (data.length() == 0) throw new Exception("You must provide question that you want to get help on.");
						//Create the Record in the database
						HelpDeskQuestion _question = new HelpDeskQuestion();
						_question.setOrgId(_User.getOrgId());
						_question.setAskedBy(_User.getUsername());
						_question.setUserId(_User.getId());
						_question.setEmailAddress(_User.getEmailAddress());
						_question.setQuestion(data);
						String strResult = QuestionService.getInstance().addQuestion(_question);
						if (strResult.equals("success")) {
							//Create a Task to assign till an Agent is available.
							strCallResult = "Thank You for the message. Your query has been registered and will be assigned soon to one of our available agents, who will respond to you.";
						}
						else {
							strCallResult = "There was a problem adding your question in the Support System. Please try again or contact the Administrator.";
						}
					}
					else if (words[0].equalsIgnoreCase("/answer")) {
						//Check if role is Agent
						if (!_User.isAgent()) throw new Exception("Error: Only an Agent can use this command.");
						String data = strCommand.replaceFirst("/answer", "").trim();
						if (data.length() == 0) throw new Exception("You must provide the answer text message.");
						List<HelpDeskQuestion> _questions = QuestionService.getInstance().getAllHelpDeskQuestionsByOrgAndAgentAndStatus(String.valueOf(_User.getOrgId()),String.valueOf(_User.getId()),"ASSIGNED");
						for (HelpDeskQuestion helpDeskQuestion : _questions) {
							//SET the STATUS to ANSWERED and other attributes like Answer, AnswerDateTime, etc
							String strResult = QuestionService.getInstance().setHelpDeskQuestionAnswer(helpDeskQuestion.getId(), _User.getId(), _User.getUsername(), data);
							//Send the reply back to the user
							if (strResult.equals("success")) {
								strCallResult = "Thank You for the answer. We will forward the same to the user.";
							}
							else {
								strCallResult = "There was a problem adding your answer in the Support System. Please try again or contact the Administrator.";
							}
						}
					}
					else if (words[0].equalsIgnoreCase("/rate")) {
						//Check if the role is User
						if (_User.isAgent()) throw new Exception("Error: Only an User can use this command.");
						String data = strCommand.replaceFirst("/rate", "").trim();
						if (data.length() == 0) throw new Exception("You must provide a rating.");
						try {
								int rating = Integer.parseInt(data);
								if ((rating< 0) || (rating>5)) {
									throw new Exception("Please provide a rating between 1 and 5.");
								}
								List<HelpDeskQuestion> _questions = QuestionService.getInstance().getAllHelpDeskQuestionsByOrgAndUserAndStatus(String.valueOf(_User.getOrgId()),String.valueOf(_User.getId()),"CLOSED");
								for (HelpDeskQuestion helpDeskQuestion : _questions) {
									//String strResult = QuestionService.getInstance().setHelpDeskQuestionStatus(String.valueOf(helpDeskQuestion.getId()), "CLOSED");
									String strResult = QuestionService.getInstance().setHelpDeskQuestionRating(String.valueOf(helpDeskQuestion.getId()), rating);
									if (strResult.equals("success")) {
										strCallResult = "Thank You for giving us feedback via your rating.";
									}
									else {
										strCallResult = "There was a problem recording your /rate. Please try again";
									}
									break;
								}
								
						}
						catch (Exception ex) {
							throw new Exception("Please provide a rating between 1 and 5.");
						}
					}
				}
				catch (Exception ex) {
					strCallResult = ex.getMessage();
				}
			}			
			/**
			 * THESE are single word commands that we understand. Currently we understands help, about and list
			 * Refer to the help section below for the complete list of commands and what they do. 
			 */
			else if (words.length == 1) {
				if ((words[0].equalsIgnoreCase("/help")) || (words[0].equalsIgnoreCase("help"))) {
					//Print out help
					StringBuffer SB = new StringBuffer();
					SB.append("***** Welcome to GASP Support Bot *****");
					SB.append("\r\nI understand the following commands:");
					SB.append("\r\n1. Type /help to get the list of commands or type /about to get information on XMPP Support Bot.");
					SB.append("\r\n2. If you are a User, you can give the following commands: ");
					SB.append("\r\n   i) /support [Question] to ask a question.");
					SB.append("\r\n  ii) /thankyou to thank the agent for doing a super job in answering your question.");
					SB.append("\r\n iii) /rate [Rating] to rate an answer. The value for [Rating] should be between [1-5] with 1 as the lowest rating and 5 as the highest rating.");
					SB.append("\r\n  iv) /reply [Question] to continue the original thread if a particular reply is not satisfactory and if you have followup queries.");
					SB.append("\r\n\r\n3. If you are an Agent, you can give the following commands: ");
					SB.append("\r\n   i) /answer to answer the question raised by an user and which the XMPP Support Bot has assigned to you.");
					SB.append("\r\n  ii) /reply to reply back to the question that you are currently answering in case of further query from the user.");
					SB.append("\r\n iii) /askme to ask the XMPP Support Bot to provide you a question to answer if one is available.");
					SB.append("\r\n  iv) /status to find out how you are doing as an Agent, your ratings, etc.");
					strCallResult = SB.toString();
					
				}
				else if (words[0].equalsIgnoreCase("/who")) {
					strCallResult = "Hello! I am the The GASP Support Bot version 1.0"+"\r\n"+"Developer: Romin Irani"+"\r\n"+"(http://myhelpdesksupport.appspot.com)";
				}
				else if (words[0].equalsIgnoreCase("/askme")) {
					if (!_User.isAgent()) throw new Exception("Error: Only an Agent can use this command.");
					//Find a question that is not yet answered and provide it to this Agent
					//If not -- display a message saying that there are no open questions available.
					strCallResult = QuestionService.getInstance().assignAgent(_User.getOrgId(), _User.getId());
					//strCallResult = "Not Yet Implemented.";
				}
				else if (words[0].equalsIgnoreCase("/thankyou")) {
					if (_User.isAgent()) throw new Exception("Error: Only an User can use this command.");
					//Determine the current question that was raised by User, set the status to CLOSED and send across the message to Rate
					List<HelpDeskQuestion> _questions = QuestionService.getInstance().getAllHelpDeskQuestionsByOrgAndUserAndStatus(String.valueOf(_User.getOrgId()),String.valueOf(_User.getId()),"ANSWERSENT");
					for (HelpDeskQuestion helpDeskQuestion : _questions) {
						String strResult = QuestionService.getInstance().setHelpDeskQuestionStatus(String.valueOf(helpDeskQuestion.getId()), "CLOSED");
						if (strResult.equals("success")) {
							strCallResult = "Thank You for the message. I am happy that your query has been satisfactorily answered. Please take a few moments and rate your experience by typing in /rate [rating (1-5)]. For example, to rate your experience as 5, simply type /rate 5.";
						}
						else {
							strCallResult = "There was a problem recording your /thankyou. Please try again";
						}
						break;
					}
				}
				else if (words[0].equalsIgnoreCase("/status")) {
					if (!_User.isAgent()) throw new Exception("Error: Only an Agent can use this command.");
					//Get the statistics for the Agent
					//Get a summary of the results for the Agent
					//strCallResult = "Not Yet Implemented.";
					List<HelpDeskQuestion> _results = QuestionService.getInstance().getAllHelpDeskQuestionsByOrgAndAgent(String.valueOf(_User.getOrgId()), String.valueOf(_User.getId()));
					for (HelpDeskQuestion helpDeskQuestion : _results) {
						strCallResult+= helpDeskQuestion.toString();
					}
				}
				else if (words[0].equalsIgnoreCase("/resetorg")) {
					if (!_User.isAdmin()) throw new Exception("Error: Only an Admin can use this command.");
					//First Remove the users
					//Then remove the organization
					List<HelpDeskUser> _results = UserService.getInstance().getAllUsers(String.valueOf(_User.getOrgId()));
					for (HelpDeskUser helpDeskUser : _results) {
						UserService.getInstance().removeUserByID(String.valueOf(helpDeskUser.getId()));
					}
					OrganizationService.getInstance().removeOrganizationByID(String.valueOf(_User.getOrgId()));
					strCallResult = "Your Organization and Users have been removed successfully. Please Sign up again.";
				}
				else {
					strCallResult = "Sorry! Could not understand your command. Please type /help to get a list of commands that I can understand.";
				}
			}
			else {
				strCallResult = "Sorry! Could not understand your command. Please type /help to get a list of commands that I can understand.";
			}
			
			//Send out the Response message on the same XMPP channel. This will be delivered to the user via the Google Talk client.
	        Message replyMessage = new MessageBuilder().withRecipientJids(fromJid).withBody(strCallResult).build();
                
	        boolean messageSent = false;
	        //if (xmpp.getPresence(fromJid).isAvailable()) {
	        SendResponse status = xmpp.sendMessage(replyMessage);
	        messageSent = (status.getStatusMap().get(fromJid) == SendResponse.Status.SUCCESS);
	        //}
	        if (messageSent) {
	        	strStatus = "Message has been sent successfully";
	        }
	        else {
	        	strStatus = "Message could not be sent";
	        }
	        _log.info(strStatus);
		}
		catch (Exception ex) {
			
			//If there is an exception then we send back a generic message to the client i.e. MyReminderBot could not understand your command. Please
			//try again. We log the exception internally.
			_log.info("Something went wrong. Please try again!" + ex.getMessage());
	        Message replyMessage = new MessageBuilder()
            .withRecipientJids(fromJid)
            .withBody("Something went wrong. Please try again. Error Message : " + ex.getMessage())
            .build();
                
	        boolean messageSent = false;
	        //The condition is commented out so that it can work over non Google Talk XMPP providers also.
	        //if (xmpp.getPresence(fromJid).isAvailable()) {
	        SendResponse status = xmpp.sendMessage(replyMessage);
	        messageSent = (status.getStatusMap().get(fromJid) == SendResponse.Status.SUCCESS);
	        //}
	        if (messageSent) {
	        	strStatus = "Message has been sent successfully";
	        }
	        else {
	        	strStatus = "Message could not be sent";
	        }
	        _log.info(strStatus);
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		 doGet(req, resp);
	}
}
