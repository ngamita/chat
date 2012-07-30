package com.thirdchimpanzee.myhelpdesksupport.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskOrganization;
import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskUser;

public class UserService {
	public static final Logger _logger = Logger.getLogger(UserService.class.toString());

	private static UserService _self = null;

	private UserService() {
	}

	public static UserService getInstance() {
		if (_self == null) {
			_self = new UserService();
			ObjectifyService.register(HelpDeskUser.class);
		}
		return _self;
	}

	public String addUser(String username, String emailAddress, boolean isAdmin, boolean isAgent, String orgId) throws Exception {
		Objectify obj = ObjectifyService.begin();
		HelpDeskUser _record = new HelpDeskUser();
		_record.setAdmin(isAdmin);
		_record.setAgent(isAgent);
		_record.setEmailAddress(emailAddress);
		_record.setOrgId(Long.parseLong(orgId));
		_record.setXMPPStatus("");
		_record.setUsername(username);
		_record.setStatus("AVAILABLE");
		obj.put(_record);
		return "success";
	}
	
	public HelpDeskUser findUserByEmailId(String emailid) {
		try {
			Objectify obj = ObjectifyService.begin();
			HelpDeskUser r = obj.query(HelpDeskUser.class).filter("emailAddress",emailid).get();
			if (r != null)
				return r;
			return null;
		} catch (Exception ex) {
			return null;
		}
	}
	
	public HelpDeskUser findUserById(String id) {
		try {
			Objectify obj = ObjectifyService.begin();
			HelpDeskUser r = obj.query(HelpDeskUser.class).filter("id",Long.parseLong(id)).get();
			if (r != null)
				return r;
			return null;
		} catch (Exception ex) {
			return null;
		}
	}

	public List<HelpDeskUser> getAllUsers(String orgId, String status) throws Exception {
		List<HelpDeskUser> _results = new ArrayList<HelpDeskUser>();
		Objectify obj = ObjectifyService.begin();
		_results = obj.query(HelpDeskUser.class).filter("orgId",Long.parseLong(orgId)).filter("status",status).list();
		return _results;
	}
	
	public List<HelpDeskUser> getAllUsers(String orgId) throws Exception {
		List<HelpDeskUser> _results = new ArrayList<HelpDeskUser>();
		Objectify obj = ObjectifyService.begin();
		_results = obj.query(HelpDeskUser.class).filter("orgId",Long.parseLong(orgId)).list();
		return _results;
	}
	
	public List<HelpDeskUser> getAllAgentsByOrgIdStatus(String orgId, String status) throws Exception {
		List<HelpDeskUser> _results = new ArrayList<HelpDeskUser>();
		Objectify obj = ObjectifyService.begin();
		_results = obj.query(HelpDeskUser.class).filter("orgId",Long.parseLong(orgId)).filter("status",status).filter("isAgent",true).list();
		return _results;
	}
	
	public List<HelpDeskUser> getAllAgentsByOrgId(String orgId) throws Exception {
		List<HelpDeskUser> _results = new ArrayList<HelpDeskUser>();
		Objectify obj = ObjectifyService.begin();
		_results = obj.query(HelpDeskUser.class).filter("orgId",Long.parseLong(orgId)).filter("isAgent",true).list();
		return _results;
	}

	public List<HelpDeskUser> getAllAgentsByStatus(String status) throws Exception {
		List<HelpDeskUser> _results = new ArrayList<HelpDeskUser>();
		Objectify obj = ObjectifyService.begin();
		_results = obj.query(HelpDeskUser.class).filter("status",status).filter("isAgent",true).list();
		return _results;
	}
	
	public List<HelpDeskUser> getAllAgents() throws Exception {
		List<HelpDeskUser> _results = new ArrayList<HelpDeskUser>();
		Objectify obj = ObjectifyService.begin();
		_results = obj.query(HelpDeskUser.class).filter("isAgent",true).list();
		return _results;
	}

	public String removeUserByID(String userId) throws Exception {
		Objectify obj = ObjectifyService.begin();
		try {
			HelpDeskUser r = obj.query(HelpDeskUser.class).filter("id",Long.parseLong(userId)).get();
			if (r != null) {
				obj.delete(r);
				return "success";
			}
			else {
				return "fail";
			}
		}
		catch (Exception ex) {
			throw new Exception("Could not remove the User. Please try again.");
		}
	}
		
		public String setUserStatus(Long userId, String status) throws Exception {
			Objectify obj = ObjectifyService.begin();
			try {
				HelpDeskUser r = obj.query(HelpDeskUser.class).filter("id",userId).get();
				if (r != null) {
					r.setStatus(status);
					obj.put(r);
					return "success";
				}
				else {
					return "fail";
				}
			}
			catch (Exception ex) {
				throw new Exception("Could not set the User status. Please try again.");
			}		
		}

		public String setUserXMPPStatus(String emailId, String XMPPStatus) throws Exception {
			Objectify obj = ObjectifyService.begin();
			try {
				HelpDeskUser r = obj.query(HelpDeskUser.class).filter("emailAddress",emailId).get();
				if (r != null) {
					r.setXMPPStatus(XMPPStatus);
					obj.put(r);
					return "success";
				}
				else {
					return "fail";
				}
			}
			catch (Exception ex) {
				throw new Exception("Could not set the User XMPP status. Please try again.");
			}		
		}
}
