package com.thirdchimpanzee.myhelpdesksupport.xmpp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.xmpp.Presence;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.thirdchimpanzee.myhelpdesksupport.service.UserService;


public class XMPPPresenceServlet extends HttpServlet {

  public static final Logger _log = Logger.getLogger(XMPPPresenceServlet.class.getName());  
  @Override
  protected void doPost(HttpServletRequest request,
                       HttpServletResponse response)
      throws ServletException, IOException {


    String action = request.getRequestURI()
        .replaceAll("/_ah/xmpp/presence/", "")
        .replaceAll("/", "");

    XMPPService xmppService = XMPPServiceFactory.getXMPPService();
    Presence presence =
        xmppService.parsePresence(request);
    String emailId = presence.getFromJid().getId().substring(0,presence.getFromJid().getId().indexOf("/"));
    _log.info("Presence with following : " + emailId  + " action = " + action);
    //String emailId = presence.getFromJid().getId();
	try {
		if ((action.equalsIgnoreCase("available")) || (action.equalsIgnoreCase("unavailable"))) 
		UserService.getInstance().setUserXMPPStatus(emailId,action.toUpperCase());
	}
	catch (Exception ex) {
		_log.log(Level.SEVERE,ex.getMessage());
	}
  }
}
