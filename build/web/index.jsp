<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>

<%@page import="com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskUser"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="description" content="My HelpDesk Bot is a Google Chat Bot that sends allows you to setup a support community for your organization." />
	<meta name="keywords" content="Bot,Support,Help, Agent,Automation,Google Talk, Google, Google Chat,IM, Instant Message" />
	<meta name="author" content="Romin Irani" />
	<link rel="stylesheet" type="text/css" href="basic-minimal.css" title="Basic Minimal" media="all" />
	<title>My HelpDesk Support Bot</title>
	<script type="text/javascript">
	function check(){	
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
					/*if (status == "success")
					{						 					 
						message = "<ul>"
							+ "<li><a href=\"index.jsp\" class=\"current\">Home</a></li>"
							+ "<li><a href=\"showUserList.jsp\">User List</a></li>"
							+ "<li><a href=\"userguide.jsp\">User Guide</a></li>"			
							+ "<li><a href=\"about.jsp\">About</a></li>"
							+ "</ul>";
				    }else{
					     message = "<ul>"
							+ "<li><a href=\"index.jsp\" class=\"current\">Home</a></li>"
							+ "<li><a href=\"userguide.jsp\">User Guide</a></li>"			
							+ "<li><a href=\"about.jsp\">About</a></li>"
							+ "</ul>";
				    }
				    					    
					document.getElementById('sidebar').innerHTML = message;
					*/
		  		}
		  	}
			var params = "action=check";		
			xmlhttp.open("GET","/data?"+params,true);
			xmlhttp.send(params);		
	}
	</script>
</head>

<body class="light" onLoad="check()">
<div id="wrap">
	<div id="header">
		<h1><a href="index.jsp">My Helpdesk Support</a></h1>
		<p class="slogan">Virtual Helpdesk System powered by Google Talk</p>
	
	</div>
	
	<div id="sidebar">
		<ul>
	    	<%	UserService userService1 = UserServiceFactory.getUserService();
    		User user1 = userService1.getCurrentUser();
    		
			
    		if (user1 != null) {
			%>	
			<li>Welcome <%=userService1.getCurrentUser().getEmail()%></li>
			<li><a href="index.jsp" class="current">Home</a></li>
			<% 
			com.thirdchimpanzee.myhelpdesksupport.service.UserService usrService = com.thirdchimpanzee.myhelpdesksupport.service.UserService.getInstance();
			HelpDeskUser usr = usrService.findUserByEmailId(user1.getEmail());
			if (usr.isAdmin()) { %>
			<li><a href="showUserList.jsp">User List</a></li>
			<% } %>
			<li><a href="userguide.jsp">User Guide</a></li>			
			<li><a href="about.jsp" >About</a></li>
			<li><a href="<%= userService1.createLogoutURL(request.getRequestURI()) %>">Logout</a></li>
			<%
    			}
    		else {
    			%>
			<li><a href="index.jsp" class="current">Home</a></li>
			<li><a href="userguide.jsp">User Guide</a></li>			
			<li><a href="about.jsp" >About</a></li>
    			
    			<%
    		}
	    		%>
			
	    </ul>
	</div>

	<div id="content">
		<h2>Setup a Support System in minutes....</h2>
		<h3>			<%
	    		UserService userService = UserServiceFactory.getUserService();
	    		User user = userService.getCurrentUser();
	    		if (user == null) {
			%>
		<table width="100%">
	<tr width="100%">
		<td colspan="2" width="100%">
		<a class="button" href="/login"><span>Login</span></a> <a class="button" href="/signup"><span>Sign Up</span></a>
		</td>
	</tr>
</table>
		
		<%
    }
%>
		</h3>
		<h4>My Helpdesk Support Bot</b> application lets you setup a virtual Helpdesk Support all powered by <a href="http://www.google.com/talk">Google Talk</a>.</h4>
		<p><img src="images/myhelpdesksupport.png" width="400"></img></p>
		<p>You can setup an organization with users and agents. Users raise questions for which they need help. Agents are those folks in your organization who can receive questions and answer them. And best of all, all through the Google Talk interface without the need to setup anything else.</p>
		<p>Features include:</p>
		<ul>
		<li>Setup your Organization. </li>
		<li>Define your Users and Agents.</li>
		<li>Users and Agents add <b>myhelpdesksupport@appspot.com</b> as their friend in GTalk</li>
		<li>Users ask questions. Agents receive their questions and reply back. They can engage in multiple replies.</li>
		<li>Users can rate Agents.</li>
		</ul>
		<p>To setup your virtual Helpdesk system today, <b><a href="http://myhelpdesksupport.appspot.com/signup">Sign up</a></b> now and follow the <b><a href="userguide.html">User Guide</a></b> for getting started immediately.</p>
		<p>Any queries, please contact me via email (romin DOT k DOT irani AT gmail.com).</p>
		<h3 align="center">m y h e l p d e s k s u p p o r t @ a p p s p o t . c o m </h3> 
	</div>
	
	<div id="footer">
		<p>&copy; 2011 Romin Irani | Template design by <a href="http://andreasviklund.com/">Andreas Viklund</a></p>
	</div>
</div>
</body>
</html>