<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="description" content="My HelpDesk Bot is a Google Chat Bot that sends allows you to setup a support community for your organization." />
	<meta name="keywords" content="Bot,Support,Help, Agent,Automation,Google Talk, Google, Google Chat,IM, Instant Message" />
	<meta name="author" content="Romin Irani" />
	<link rel="stylesheet" type="text/css" href="basic-minimal.css" title="Basic Minimal" media="all" />
	<title>My HelpDesk Support Bot</title>
</head>

<body class="light">
<div id="wrap">
	<div id="header">
		<h1><a href="index.jsp">My Helpdesk Support</a></h1>
		<p class="slogan">Virtual Helpdesk System powered by Google Talk</p>
	</div>
	
	<div id="sidebar">
	</div>

	<div id="content">
	<table width="100%">
	<tr width="100%">
		<td>
		</td>
		<td align="right" width="100%">
		<%
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			if (user != null) {
		%>
		<p><a href="<%=userService.createLogoutURL("/index.jsp")%>">Logout</a></p>
		<%
			} else {
		%>
		<p><a
			href="<%=userService.createLoginURL(request.getRequestURI())%>">Login</a></p>
		<%
			}
		%>
		</td>
	</tr>
</table>
	<p>Sorry. You cannot perform this operation. There could be various reasons for this:</p>
	<ul>
	<li>You are not registered with the system as an Administrator. Only an Administrator registered with the system can perform operations like Add User, Add Organization and view User Reports.</li>
	<li>registered as a User or Agent in this System by your Administrator.</li>
	</ul>  
	<p>In case you are an Administrator and wish to setup your organization with this Support System, <b><a href="signup">sign up</a></b> now.</p>
	</div>
	<div id="footer">
		<p>&copy; 2011 Romin Irani | Template design by <a href="http://andreasviklund.com/">Andreas Viklund</a></p>
	</div>
</div>
</body>
</html>