package com.thirdchimpanzee.myhelpdesksupport.xmpp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.xmpp.Subscription;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.thirdchimpanzee.myhelpdesksupport.service.UserService;

public class XMPPSubscriptionServlet extends HttpServlet {
  public static final Logger _log = Logger.getLogger(XMPPSubscriptionServlet.class.getName());  
  @Override
  protected void doPost(HttpServletRequest request,
                       HttpServletResponse response)
      throws ServletException, IOException {


    String action = request.getRequestURI()
        .replaceAll("/_ah/xmpp/subscription/", "")
        .replaceAll("/", "");

    XMPPService xmppService = XMPPServiceFactory.getXMPPService();
    Subscription subscription = xmppService.parseSubscription(request);
    
    //String emailId = subscription.getFromJid().getId();
	//String userId = subscription.getFromJid().getId();
    String emailId = subscription.getFromJid().getId().substring(0,subscription.getFromJid().getId().indexOf("/"));
	try {
		UserService.getInstance().setUserXMPPStatus(emailId,action.toUpperCase());
	}
	catch (Exception ex) {
		_log.log(Level.SEVERE,ex.getMessage());
	}
    

  }
}
