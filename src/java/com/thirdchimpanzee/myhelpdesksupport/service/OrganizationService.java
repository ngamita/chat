package com.thirdchimpanzee.myhelpdesksupport.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.appengine.api.datastore.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskOrganization;
import com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskUser;
import com.thirdchimpanzee.myhelpdesksupport.entity.OrganizationResetTask;

public class OrganizationService {
	public static final Logger _logger = Logger.getLogger(OrganizationService.class.toString());

	private static OrganizationService _self = null;

	private OrganizationService() {
	}

	public static OrganizationService getInstance() {
		if (_self == null) {
			_self = new OrganizationService();
			ObjectifyService.register(HelpDeskOrganization.class);
			ObjectifyService.register(OrganizationResetTask.class);
		}
		return _self;
	}

	public String addOrganization(String domainName, String orgName, String orgType) throws Exception {
		Objectify obj = ObjectifyService.begin();
		HelpDeskOrganization _record = new HelpDeskOrganization();
		_record.setOrgName(orgName);
		_record.setOrgType(orgType);
		_record.setDomainName(domainName);
		_record.setStatus("ACTIVE");
		obj.put(_record);
		return "success";
	}
	
	public HelpDeskOrganization findOrganizationByID(String orgId) {
		try {
			Objectify obj = ObjectifyService.begin();
			HelpDeskOrganization r = obj.query(HelpDeskOrganization.class).filter("id",Long.parseLong(orgId)).get();
			if (r != null)
				return r;
			return null;
		} catch (Exception ex) {
			return null;
		}
	}
	
	public HelpDeskOrganization findOrganizationByName(String orgName) {
		try {
			Objectify obj = ObjectifyService.begin();
			HelpDeskOrganization r = obj.query(HelpDeskOrganization.class).filter("orgName",orgName).get();
			if (r != null)
				return r;
			return null;
			
		}
		catch (Exception ex) {
			return null;
		}
	}
	
	public HelpDeskOrganization findOrganizationByDomain(String domainName) {
		try {
			Objectify obj = ObjectifyService.begin();
			HelpDeskOrganization r = obj.query(HelpDeskOrganization.class).filter("domainName",domainName).get();
			if (r != null)
				return r;
			return null;
			
		}
		catch (Exception ex) {
			return null;
		}
	}

	public List<HelpDeskOrganization> getAllOrganizations(String status) throws Exception {
		List<HelpDeskOrganization> _results = new ArrayList<HelpDeskOrganization>();
		Objectify obj = ObjectifyService.begin();
		_results = obj.query(HelpDeskOrganization.class).filter("status",status).list();
		return _results;
	}
	
	public List<HelpDeskOrganization> getAllOrganizations() throws Exception {
		List<HelpDeskOrganization> _results = new ArrayList<HelpDeskOrganization>();
		Objectify obj = ObjectifyService.begin();
		_results = obj.query(HelpDeskOrganization.class).list();
		return _results;
	}
	
	public String removeOrganizationByID(String orgId) throws Exception {
		Objectify obj = ObjectifyService.begin();
		try {
			HelpDeskOrganization r = obj.query(HelpDeskOrganization.class).filter("id",Long.parseLong(orgId)).get();
			if (r != null) {
				obj.delete(r);
				return "success";
			}
			else {
				return "fail";
			}
		}
		catch (Exception ex) {
			throw new Exception("Could not remove the Organization. Please try again.");
		}
	}
		
		public String setOrganizationStatus(String orgId, String status) throws Exception {
			Objectify obj = ObjectifyService.begin();
			try {
				HelpDeskOrganization r = obj.query(HelpDeskOrganization.class).filter("id",Long.parseLong(orgId)).get();
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
				throw new Exception("Could not set the Organization status. Please try again.");
			}		
		}
		
		public String resetOrganization(String emailAddress) {
			//Create a Task in the system and send an email with a link
			Objectify obj = ObjectifyService.begin();
			try {
				//Validate if user exists
				//Validate if user is an Admin
				//Get his organization
				HelpDeskUser _User = UserService.getInstance().findUserByEmailId(emailAddress);
				if (_User == null) throw new Exception("Could not initiate the Task to Reset the Organization. Please try again.");
				if (!_User.isAdmin()) throw new Exception("You are not an Administrator in any Organization defined in the Support System.");
				
				OrganizationResetTask ORT = new OrganizationResetTask();
				ORT.setEmailAddress(emailAddress);
				ORT.setOrgAdminUserId(_User.getId());
				ORT.setOrgId(_User.getOrgId());
				ORT.setResetTaskDateTime(new Date());
				ORT.setStatus("NEW");
				obj.put(ORT);
				Long id = ORT.getId();
				//Send an Email Out
				//Call the GAEJ Email Service
				Properties props = new Properties();
				Session session = Session.getDefaultInstance(props, null);
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress("admin@myhelpdesksupport.appspot.com"));
				msg.addRecipient(Message.RecipientType.TO,
				new InternetAddress(emailAddress));
				msg.setSubject("MyHelpDesk Support : Organization Reset Task");
				msg.setText("You have chosen to reset your Organization and Users in it. Please click the link in this email to complete the task: http://myhelpdesksupport.appspot.com/admin?action=orgreset&id="+id);
				Transport.send(msg);
				return "success";
			}
			catch (Exception ex) {
				return ex.getMessage();
			}		
		}
}
