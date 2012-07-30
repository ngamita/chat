package com.thirdchimpanzee.myhelpdesksupport;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;

import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskOrganization;
import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskUser;
import com.thirdchimpanzee.myhelpdesksupport.service.OrganizationService;
import com.thirdchimpanzee.myhelpdesksupport.service.QuestionService;
import com.thirdchimpanzee.myhelpdesksupport.service.UserService;



@SuppressWarnings("serial")
	public class AdminServlet extends HttpServlet {
		public void doGet(HttpServletRequest req, HttpServletResponse resp)
				throws IOException {
		String strResponseMessage = "";	
		try {
			//Extract out the Action first
			String strAction = req.getParameter("action");
			if (strAction == null) throw new Exception("Missing param:action");
			if (strAction.trim().length() == 0) throw new Exception("Missing param:action");
			
			
			
			//ADD ORGANIZATION
			if (strAction.equalsIgnoreCase("addorg")) {
				//Extract out the domainname
				String domainname = req.getParameter("domainname");
				if (domainname == null) throw new Exception("Missing param:domainname");
				if (domainname.trim().length() == 0) throw new Exception("Missing param:domainname");
				//Extract out the orgname
				String orgname = req.getParameter("orgname");
				if (orgname == null) throw new Exception("Missing param:orgname");
				if (orgname.trim().length() == 0) throw new Exception("Missing param:orgname");
				//Extract out the orgtype
				String orgtype = req.getParameter("orgtype");
				if (orgtype == null) throw new Exception("Missing param:orgtype");
				if (orgtype.trim().length() == 0) throw new Exception("Missing param:orgtype");
				String strResult = OrganizationService.getInstance().addOrganization(domainname.trim(),orgname.trim(),orgtype.trim());
				if (strResult.equals("success")) {
					strResponseMessage = buildSuccessResponse("");
				}
				else {
					strResponseMessage = buildFailureResponse("Unknown Error");
				}
			}
			//GET USER LIST 
			else if (strAction.equalsIgnoreCase("getorglist")){
                List<HelpDeskOrganization> _OrgList = OrganizationService.getInstance().getAllOrganizations();
                StringBuilder result = new StringBuilder();
                result.append("<OrgList>");
                for (HelpDeskOrganization _Org:_OrgList) {
                	result.append("<Organization>");
                	result.append("<ID>" + _Org.getId() + "</ID>");
                	result.append("<Name>" + _Org.getOrgName() + "</Name>");
                	result.append("<DomainName>" + _Org.getDomainName() + "</DomainName>");
                	result.append("<Status>" + _Org.getStatus() + "</Status>");
                	result.append("<OrgType>" + _Org.getOrgType() + "</OrgType>");
                	result.append("</Organization>");
                }
                result.append("</OrgList>");
                strResponseMessage = buildSuccessResponse(result.toString());
			}
			//DELETE ORGANIZATION
			else if (strAction.equalsIgnoreCase("deleteorg")){
				String OrgId = req.getParameter("ID");
				if (OrgId == null) throw new Exception("Organization ID (ID)has to be provided.");
				if (OrgId.trim().length() == 0) throw new Exception("Organization ID (ID)has to be provided.");
				String strResult = OrganizationService.getInstance().removeOrganizationByID(OrgId);
				if (strResult.equals("success")) {
					strResponseMessage = buildSuccessResponse("");
				}
				else {
					strResponseMessage = buildFailureResponse("Unknown Error");
				}
			} 
			//GET STATUS OF ORGANIZATION
			else if (strAction.equalsIgnoreCase("getorgstatus")){
				String OrgId = req.getParameter("ID");
				if (OrgId == null) throw new Exception("Org ID has to be provided.");
				if (OrgId.trim().length() == 0) throw new Exception("OrgId has to be provided.");
				HelpDeskOrganization _Org = OrganizationService.getInstance().findOrganizationByID(OrgId);
				if (_Org == null) throw new Exception("Could not retrieve the Organization Details. Please try again.");
				StringBuilder result = new StringBuilder();
				result.append("<Organization>");
				result.append("<ID>" + _Org.getId() + "</ID>");
				result.append("<Name>" + _Org.getOrgName() + "</Name>");
				result.append("<Status>" + _Org.getStatus() + "</Status>");
				result.append("<DomainName>" + _Org.getDomainName() + "</DomainName>");
				result.append("<OrgType>" + _Org.getOrgType() + "</OrgType>");
				result.append("<Field1>" + _Org.getField1() + "</Field1>");
				result.append("<Field2>" + _Org.getField2() + "</Field2>");
				result.append("<Field3>" + _Org.getField3() + "</Field3>");
				result.append("</Organization>");
				strResponseMessage = buildSuccessResponse(result.toString());
			} 
			//SET STATUS OF ORGANIZATION
			else if (strAction.equalsIgnoreCase("setorgstatus")){
				String OrgId = req.getParameter("ID");
				if (OrgId == null) throw new Exception("Org ID has to be provided.");
				if (OrgId.trim().length() == 0) throw new Exception("OrgId has to be provided.");
				String status = req.getParameter("status");
				if (status == null) throw new Exception("status has to be provided.");
				if (status.trim().length() == 0) throw new Exception("status has to be provided.");
				HelpDeskOrganization _Org = OrganizationService.getInstance().findOrganizationByID(OrgId);
				if (_Org == null) throw new Exception("Could not retrieve the Organization Details. Please try again.");
				String strResult = OrganizationService.getInstance().setOrganizationStatus(OrgId, status);
				if (strResult.equals("success")) {
					strResponseMessage = buildSuccessResponse("");
				}
				else {
					strResponseMessage = buildFailureResponse("Unknown Error");
				}
			} 
			//ADD USER
			else 			
				if (strAction.equalsIgnoreCase("adduser")) {
					//Extract out the username
					String username = req.getParameter("username");
					if (username == null) throw new Exception("Missing param:username");
					if (username.trim().length() == 0) throw new Exception("Missing param:username");
					//Extract out the emailaddress
					String emailaddress = req.getParameter("emailaddress");
					if (emailaddress == null) throw new Exception("Missing param:emailaddress");
					if (emailaddress.trim().length() == 0) throw new Exception("Missing param:emailaddress");
					//Extract out the isadmin,
					String isadmin = req.getParameter("isadmin");
					if (isadmin == null) throw new Exception("Missing param:isadmin");
					if (isadmin.trim().length() == 0) throw new Exception("Missing param:isadmin");
					//Extract out the isagent
					String isagent = req.getParameter("isagent");
					if (isagent == null) throw new Exception("Missing param:isagent");
					if (isagent.trim().length() == 0) throw new Exception("Missing param:isagent");
					//Extract out the orgid
					String orgid = req.getParameter("orgid");
					if (orgid == null) throw new Exception("Missing param:orgid");
					if (orgid.trim().length() == 0) throw new Exception("Missing param:orgid");
					String strResult = UserService.getInstance().addUser(username.trim(),emailaddress.trim(),Boolean.parseBoolean(isadmin.trim()),Boolean.parseBoolean(isagent.trim()),orgid.trim());
					if (strResult.equals("success")) {
						strResponseMessage = buildSuccessResponse("");
					}
					else {
						strResponseMessage = buildFailureResponse("Unknown Error");
					}
				}
				//GET USER LIST 
				else if (strAction.equalsIgnoreCase("getuserlist")){
					//Extract out the orgid
					String orgid = req.getParameter("orgid");
					if (orgid == null) throw new Exception("Missing param:orgid");
					if (orgid.trim().length() == 0) throw new Exception("Missing param:orgid");

	                List<HelpDeskUser> _UserList = UserService.getInstance().getAllUsers(orgid);
	                StringBuilder result = new StringBuilder();
	                result.append("<UserList>");
	                for (HelpDeskUser _User:_UserList) {
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
				}
				//DELETE USER
				else if (strAction.equalsIgnoreCase("deleteuser")){
					String UserId = req.getParameter("ID");
					if (UserId == null) throw new Exception("User ID (ID)has to be provided.");
					if (UserId.trim().length() == 0) throw new Exception("User ID (ID)has to be provided.");
					String strResult = UserService.getInstance().removeUserByID(UserId);
					if (strResult.equals("success")) {
						strResponseMessage = buildSuccessResponse("");
					}
					else {
						strResponseMessage = buildFailureResponse("Unknown Error");
					}
				} 
				//GET STATUS OF USER
				else if (strAction.equalsIgnoreCase("getuserstatus")){
					String UserId = req.getParameter("ID");
					if (UserId == null) throw new Exception("UserId ID has to be provided.");
					if (UserId.trim().length() == 0) throw new Exception("UserId has to be provided.");
					HelpDeskUser _User = UserService.getInstance().findUserById(UserId);
					if ( _User == null) throw new Exception("Could not retrieve the User Details. Please try again.");
					StringBuilder result = new StringBuilder();
					result.append("<User>");
                	result.append("<ID>" + _User.getId() + "</ID>");
                	result.append("<Name>" + _User.getUsername() + "</Name>");
                	result.append("<EmailAddress>" + _User.getEmailAddress() + "</EmailAddress>");
                	result.append("<Status>" + _User.getStatus() + "</Status>");
                	result.append("<isAdmin>" + _User.isAdmin() + "</isAdmin>");
                	result.append("<isAgent>" + _User.isAgent() + "</isAgent>");
					result.append("</User>");
					strResponseMessage = buildSuccessResponse(result.toString());
				} 
				//SET STATUS OF USER
				else if (strAction.equalsIgnoreCase("setuserstatus")){
					String UserId = req.getParameter("ID");
					if (UserId == null) throw new Exception("UserId ID has to be provided.");
					if (UserId.trim().length() == 0) throw new Exception("UserId has to be provided.");
					String status = req.getParameter("status");
					if (status == null) throw new Exception("status has to be provided.");
					if (status.trim().length() == 0) throw new Exception("status has to be provided.");
					HelpDeskUser _User = UserService.getInstance().findUserById(UserId);
					if (_User == null) throw new Exception("Could not retrieve the User Details. Please try again.");
					String strResult = UserService.getInstance().setUserStatus(Long.parseLong(UserId),status);
					if (strResult.equals("success")) {
						strResponseMessage = buildSuccessResponse("");
					}
					else {
						strResponseMessage = buildFailureResponse("Unknown Error");
					}
				}
				else if (strAction.equalsIgnoreCase("assignagents")) {
					QuestionService.getInstance().assignAgents();
				}
				else if (strAction.equalsIgnoreCase("sendagentreplies")) {
					QuestionService.getInstance().sendAgentReplies();
				}
			else {
				throw new Exception("Unknown action");
			}
		}
		catch (Exception ex) {
			strResponseMessage = buildFailureResponse(ex.getMessage());
		}
		resp.setContentType("text/xml");
		resp.getWriter().write(strResponseMessage.toString());
		resp.flushBuffer();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		 doGet(req, resp);
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
}
