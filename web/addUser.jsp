<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskUser"%>

<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="description" content="My HelpDesk Bot is a Google Chat Bot that sends allows you to setup a support community for your organization." />
	<meta name="keywords" content="Bot,Support,Help, Agent,Automation,Google Talk, Google, Google Chat,IM, Instant Message" />
	<meta name="author" content="Romin Irani" />
	<link rel="stylesheet" type="text/css" href="basic-minimal.css" title="Basic Minimal" media="all" />
	<title>My HelpDesk Support Bot</title>
<script type="text/javascript">
	function addUser(){
		var xmlhttp;
		if (window.XMLHttpRequest)
		  {// code for IE7+, Firefox, Chrome, Opera, Safari
			  xmlhttp=new XMLHttpRequest();
		  }else
		  {// code for IE6, IE5
		  	xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		  }
			xmlhttp.onreadystatechange=function()
		  	{
		  		if (xmlhttp.readyState==4 && xmlhttp.status==200)
		    	{
		  			var status = xmlhttp.responseXML.documentElement.getElementsByTagName("Status")[0].firstChild.nodeValue;
					var message = "";
					if (status == "success")
					{
						 message= "";
						 
						 document.getElementById('username').value=""
						 document.getElementById('emailaddress').value=""
						 document.getElementById('isagent').value="";

						 window.location.href = "../showUserList.jsp";
						 
				    }else if (status == "invalid"){
					    window.location.replace("../" + xmlhttp.responseXML.documentElement.getElementsByTagName("Description")[0].firstChild.nodeValue);
				    }else{
				    	 message = "Failed: " + xmlhttp.responseXML.documentElement.getElementsByTagName("Description")[0].firstChild.nodeValue;
				    }					    
					document.getElementById('message').innerHTML = message;
		  		}
		  	}
			var params = "username="+document.getElementById('username').value+"&emailaddress="+document.getElementById('emailaddress').value+"&isagent="+document.getElementById('isagent').value+"&action=addUser";			
			xmlhttp.open("GET","/data?"+params,true);
			xmlhttp.send(params);		
}
</script>
</head>

<body class="light">
<div id="wrap">
<div id="header">
<h1><a href="index.jsp">My Helpdesk Support</a></h1>
<p class="slogan">Virtual Helpdesk System powered by Google Talk</p>
</div>

<div id="sidebar">
<ul>
	    <%
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
    		if (user != null) {
		%>
		<li>Welcome <i><%=userService.getCurrentUser().getEmail()%></i></li>
		<li><a href="index.jsp">Home</a></li>
					<% 
			com.thirdchimpanzee.myhelpdesksupport.service.UserService usrService = com.thirdchimpanzee.myhelpdesksupport.service.UserService.getInstance();
			HelpDeskUser usr = usrService.findUserByEmailId(user.getEmail());
			if (usr.isAdmin()) { %>
		
    	<li><a href="showUserList.jsp" class="current">User List</a></li>
    	<% } %>
		<li><a href="userguide.jsp">User Guide</a></li>
		<li><a href="about.jsp">About</a></li>
		<li><a href="<%=userService.createLogoutURL("/index.jsp")%>">Logout</a>
		</li>
		<%
			} else {
		%>
		<li><a href="index.jsp">Home</a></li>
		<li><a href="<%=userService.createLoginURL(request.getRequestURI())%>">login</a></li>
		<%
			}
		%>
</ul>
</div>

<div id="content">
<table width="100%">
	<tr>
		<td colspan="2">		
			<div id="message"/>
		</td>
	</tr>
	<%
	com.thirdchimpanzee.myhelpdesksupport.service.UserService usrService = com.thirdchimpanzee.myhelpdesksupport.service.UserService.getInstance();
	HelpDeskUser usr = usrService.findUserByEmailId(user.getEmail());
	if (usr.isAdmin()) {
	%>
	<tr width="100%">
		<td colspan="2">
		<h3>Add User</h3>
		</td>
	</tr>
	<tr>
		<td align="left">User Name</td>
		<td align="left"><input type="text" id="username" placeholder="First & Last Name" /></td>
	</tr>
	<tr>
		<td align="left">Email Address</td>
		<td align="left"><input type="text" id="emailaddress" placeholder="Ex: admin@test.com" /></td>
	</tr>
	<tr>
		<td align="left">Agent (Select if Agent. If User, leave it unchecked)</td>
		<td align="left"><input type="checkbox" id="isagent" /></td>
	</tr>
	<tr>
		<td>			
		</td>
		<td align="left">
			<!-- input type="hidden" name="action" value="addorg"/-->
			<input type="button" value="Add" onClick="addUser()"/>
		</td>
	</tr>
	<%} else { %>
<tr>
		<td colspan="2"><p>You are not authorized to access this page.</p>
		</td>
	</tr>	
	<% } %>
</table>
</div>
<hr />
<div id="footer">
<p>&copy;2011 Romin Irani | Design by <a
	href="http://andreasviklund.com/">Andreas Viklund</a></p>
</div>
</div>
</body>
</html>