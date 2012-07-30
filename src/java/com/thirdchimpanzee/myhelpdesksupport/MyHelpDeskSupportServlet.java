package com.thirdchimpanzee.myhelpdesksupport;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskOrganization;
import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskUser;
import com.thirdchimpanzee.myhelpdesksupport.service.OrganizationService;

@SuppressWarnings("serial")
public class MyHelpDeskSupportServlet extends HttpServlet {	

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		String strResponseMessage = "";

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();		
		if (user != null) {
			try{	
				if (req.getRequestURI().indexOf("/signup") > -1) {
					ServletContext context = getServletContext();
					RequestDispatcher reqDispatcher = context
					.getRequestDispatcher("/addOrg.jsp");
					reqDispatcher.forward(req, resp);
				} else if (req.getRequestURI().indexOf("/login") > -1) {
					com.thirdchimpanzee.myhelpdesksupport.service.UserService usrService = com.thirdchimpanzee.myhelpdesksupport.service.UserService.getInstance();
					HelpDeskUser usr = usrService.findUserByEmailId(user.getEmail());					
					if (usr != null && usr.isAdmin()){						
						ServletContext context = getServletContext();
						RequestDispatcher reqDispatcher = context
						.getRequestDispatcher("/showUserList.jsp");
						reqDispatcher.forward(req, resp);
					}else if (usr == null){
						ServletContext context = getServletContext();
						RequestDispatcher reqDispatcher = context
						.getRequestDispatcher("/error.jsp");
						reqDispatcher.forward(req, resp);
					}else if (!usr.isAdmin()){
						ServletContext context = getServletContext();
						RequestDispatcher reqDispatcher = context
						.getRequestDispatcher("/index.jsp");
						reqDispatcher.forward(req, resp);
					}

				} else if (req.getRequestURI().indexOf("/data") > -1){
					try {
						String strAction = req.getParameter("action");
						if (strAction == null)
							throw new Exception("Missing param:action");
						if (strAction.trim().length() == 0)
							throw new Exception("Missing param:action");

						com.thirdchimpanzee.myhelpdesksupport.service.UserService usrService = com.thirdchimpanzee.myhelpdesksupport.service.UserService.getInstance();
						HelpDeskUser usr = usrService.findUserByEmailId(user.getEmail());
						
						if (strAction.equalsIgnoreCase("check")) {
							if (usr != null && usr.isAdmin()){
								strResponseMessage = buildSuccessResponse("");
							}else{
								strResponseMessage = buildFailureResponse("");
							}
						}	

						if (strAction.equalsIgnoreCase("addorg")) {
							if (usr != null){
								throw new Exception("You are already signed up as an Administrator in the System. Simply go ahead and &lt;a href=login&gt;login&lt;/a&gt; using your Google Account.");
							}
							strResponseMessage = addOrg(req, user);
						}
						else {
							
							if ((usr != null) && (usr.isAdmin())){							
								if (strAction.equalsIgnoreCase("getUserList")) {
									strResponseMessage = getUserList(user);
								}
								else if(strAction.equalsIgnoreCase("addUser")) {							
									strResponseMessage = addUser(req, user);
								}
								else if(strAction.equalsIgnoreCase("deleteUsers")) {
									String id = req.getParameter("id");
									if (id == null || id.trim().length() == 0){
										throw new Exception("Please select Users to be deleted.");
									}
									strResponseMessage = deleteUsers(id.split(","));
								}
							}else{
								strResponseMessage = buildInvalidResponse();
							}
						}

					} catch (Exception ex) {
						ex.printStackTrace();
						strResponseMessage = buildFailureResponse(ex.getMessage());
						/*req.setAttribute("status", "failed");
						req.setAttribute("message", "unknown error");*/
					}					
					resp.setContentType("text/xml");
					System.out.println(strResponseMessage.toString());
					resp.getWriter().write(strResponseMessage.toString());
					resp.flushBuffer();
				
			} else {
				resp.setContentType("text/xml");
				resp.getWriter().write(
						"User " + user.getEmail() + " non admin");
				resp.flushBuffer();
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	else {
		resp.sendRedirect(userService.createLoginURL(req
				.getRequestURI()));
	}

}

private String addOrg(HttpServletRequest req, User user) throws Exception{	
	String strResponseMessage = "";
	String domainname = req.getParameter("domainname");
	if (domainname == null)
		throw new Exception("Missing Domain Name");
	if (domainname.trim().length() == 0)
		throw new Exception("Missing Domain Name");
	//Extract out the orgname
	String orgname = req.getParameter("orgname");
	if (orgname == null)
		throw new Exception("Missing Organization Name");
	if (orgname.trim().length() == 0)
		throw new Exception("Missing Organization Name");
	//Extract out the orgtype
	String orgtype = req.getParameter("orgtype");
	if (orgtype == null)
		throw new Exception("Missing Organization Type");
	if (orgtype.trim().length() == 0)
		throw new Exception("Missing Organization Type");




	HelpDeskOrganization _Org = OrganizationService.getInstance().findOrganizationByName(orgname);

	if (_Org == null){
		String strResult = OrganizationService.getInstance()
		.addOrganization(domainname.trim(), orgname.trim(),
				orgtype.trim());
		if (strResult.equals("success")) {
			_Org = OrganizationService.getInstance().findOrganizationByName(orgname);					
			String strResult1 = com.thirdchimpanzee.myhelpdesksupport.service.UserService.getInstance().addUser("Admin",user.getEmail(),Boolean.parseBoolean("true"),Boolean.parseBoolean("false"),String.valueOf(_Org.getId()));
			if (strResult1.equals("success")) {						
				strResponseMessage = buildSuccessResponse("");
			}else{
				strResponseMessage = buildFailureResponse("Unknown Error");						
			}
		}
		else {
			strResponseMessage = buildFailureResponse("Unknown Error");

		}
	}else {
		strResponseMessage = buildFailureResponse("Organization " + orgname + " already exist");
	}

	return strResponseMessage;
}

private String getUserList(User user) throws Exception{		
	String strResponseMessage = "";		
	com.thirdchimpanzee.myhelpdesksupport.service.UserService usrService = com.thirdchimpanzee.myhelpdesksupport.service.UserService.getInstance();
	HelpDeskUser usr = usrService.findUserByEmailId(user.getEmail());
	String orgid = String.valueOf(usr.getOrgId());
	if (orgid == null) throw new Exception("Invalid Organization");
	if (orgid.trim().length() == 0) throw new Exception("Invalid Organization");

	List<HelpDeskUser> _UserList = usrService.getAllUsers(orgid);
	StringBuilder result = new StringBuilder();
	result.append("<UserList>");
	for (HelpDeskUser _User:_UserList) {
		if(_User.isAdmin()){
			continue;
		}
		result.append("<User>");
		result.append("<ID>" + _User.getId() + "</ID>");
		result.append("<Name>" + _User.getUsername() + "</Name>");
		result.append("<EmailAddress>" + _User.getEmailAddress() + "</EmailAddress>");
		result.append("<Status>" + _User.getStatus() + "</Status>");
		result.append("<isAdmin>" + _User.isAdmin() + "</isAdmin>");
		result.append("<isAgent>" + _User.isAgent() + "</isAgent>");
		result.append("</User>");
	}
	result.append("</UserList>");
	strResponseMessage = buildSuccessResponse(result.toString());
	return strResponseMessage;
}

private String addUser(HttpServletRequest req, User user) throws Exception{
	com.thirdchimpanzee.myhelpdesksupport.service.UserService usrService = com.thirdchimpanzee.myhelpdesksupport.service.UserService.getInstance();
	HelpDeskUser usr = usrService.findUserByEmailId(user.getEmail());


	String strResponseMessage = "";		
	String username = req.getParameter("username");
	if (username == null) throw new Exception("Username is mandatory");
	if (username.trim().length() == 0) throw new Exception("Username is mandatory");
	//Extract out the emailaddress
	String emailaddress = req.getParameter("emailaddress");
	if (emailaddress == null) throw new Exception("Email Address is mandatory");
	if (emailaddress.trim().length() == 0) throw new Exception("Email Address is mandatory");
	if (usrService.findUserByEmailId(emailaddress) != null){
		throw new Exception("User already registered");
	}
	//Extract out the isadmin,
	/*String isadmin = req.getParameter("isadmin");
		if (isadmin == null) throw new Exception("Missing param:isadmin");
		if (isadmin.trim().length() == 0) throw new Exception("Missing param:isadmin");*/
	//Extract out the isagent
	String isagent = req.getParameter("isagent");
	if ("on".equalsIgnoreCase(isagent)){
		isagent = "true";
	}
	System.out.println("agent :" + isagent);
	/*if (isagent == null) throw new Exception("Agent is mandatory");
		if (isagent.trim().length() == 0) throw new Exception("Agent is mandatory");*/
	//Extract out the orgid
	/*String orgid = req.getParameter("orgid");
		if (orgid == null) throw new Exception("Missing param:orgid");
		if (orgid.trim().length() == 0) throw new Exception("Missing param:orgid");*/
	String strResult = com.thirdchimpanzee.myhelpdesksupport.service.UserService.getInstance().addUser(username.trim(),emailaddress.trim(),Boolean.parseBoolean("false"),Boolean.parseBoolean(isagent.trim()),String.valueOf(usr.getOrgId()));
	if (strResult.equals("success")) {
		strResponseMessage = buildSuccessResponse("");
	}
	else {
		strResponseMessage = buildFailureResponse("Unknown Error");
	}
	return strResponseMessage; 
}

private String deleteUsers(String[] userIds){
	String strResponseMessage = "";		

	try {
		for (String UserId : userIds) {
			String strResult = com.thirdchimpanzee.myhelpdesksupport.service.UserService
			.getInstance().removeUserByID(UserId);
			if (strResult.equals("success")) {
				strResponseMessage = buildSuccessResponse("");
			} else {
				strResponseMessage = buildFailureResponse("Unknown Error");
			}
		}
	} catch (Exception e) {
		// TODO: handle exception
		strResponseMessage = buildFailureResponse("Unknown Error");
	}
	return strResponseMessage;
}

private String buildSuccessResponse(String strResponseData) {
	StringBuilder SB = new StringBuilder();
	SB.append("<Result>");
	SB.append("<Status>success</Status>");
	SB.append("<Description/>");
	SB.append("<ResultData>");
	SB.append(strResponseData);
	SB.append("</ResultData>");
	SB.append("</Result>");
	return SB.toString();
}
private String buildFailureResponse(String strErrorMessage) {
	StringBuilder SB = new StringBuilder();
	SB.append("<Result>");
	SB.append("<Status>fail</Status>");
	SB.append("<Description>" + strErrorMessage.trim()+"</Description>");
	SB.append("<ResultData/>");
	SB.append("</Result>");
	return SB.toString();
}
private String buildInvalidResponse() {
	StringBuilder SB = new StringBuilder();
	SB.append("<Result>");
	SB.append("<Status>invalid</Status>");
	SB.append("<Description>error.jsp</Description>");
	SB.append("<ResultData/>");
	SB.append("</Result>");
	return SB.toString();
}
}

