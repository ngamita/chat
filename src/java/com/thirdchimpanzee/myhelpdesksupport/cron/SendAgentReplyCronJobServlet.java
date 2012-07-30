/**
 * My Reminder Bot
 * Copyright (c) 2011 by Romin Irani. All Rights Reserved.
 */

package com.thirdchimpanzee.myhelpdesksupport.cron;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskQuestion;
import com.thirdchimpanzee.myhelpdesksupport.service.QuestionService;

/**
 * Google App Engine Assign Agents Cron Job Class
 * 
 * This is the only CRON Job currently in the system that is fired at an interval of 1 minute.
 * The cron job configuration is present in WEB-INF\cron.xml file. In the initial release we are making
 * this single CRON Job do all the work of assigning the agents to unaswered questions.
 * 
 * 
 * @author irani_r
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class SendAgentReplyCronJobServlet extends HttpServlet {
 private static final Logger _logger = Logger.getLogger(SendAgentReplyCronJobServlet.class.getName());
 public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	 try {
		 _logger.info("Cron Job has been executed");
		 QuestionService.getInstance().sendAgentReplies();
	 }
	 catch (Exception ex) {
		 //Log any exceptions in your Cron Job
		 _logger.info("Error in executing Cron Job : " + ex.getMessage());
	 }
 }

 @Override
 public void doPost(HttpServletRequest req, HttpServletResponse resp)
 throws ServletException, IOException {
 doGet(req, resp);
 }
}
