<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskUser"%>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="description" content="" />
<meta name="keywords" content="" />
<meta name="author" content="" />
<link rel="stylesheet" type="text/css" href="basic-minimal.css"
	title="Basic Minimal" media="all" />
<title></title>
<script type="text/javascript">
	function trim(stringToTrim) {
		return stringToTrim.replace(/^\s+|\s+$/g,"");
	}

	function addOrg(){
		//Validate if form fields are provided.
		var orgname="";
		var orgdomain="";
		var orgtype="Organization";
		orgname = trim(document.getElementById('orgname').value);
		orgdomain = trim(document.getElementById('domainname').value);
		if ((orgname.length == 0) || (orgdomain.length == 0)) {
			alert("You need to enter Organization Name and Domain Name.");
			return;
		}
		
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
						 //message= "Succefully added Organization.";
						 
						 document.getElementById('orgname').value=""
						 document.getElementById('domainname').value=""
						 //document.getElementById('orgtype').value="";

						 window.location.href = "../showUserList.jsp";
						 
				    }else{
					     message = "Failed: " + xmlhttp.responseXML.documentElement.getElementsByTagName("Description")[0].firstChild.nodeValue;
				    }					    
					document.getElementById('message').innerHTML = message;
		  		}
		  	}
			var params = "orgname="+orgname+"&domainname="+orgdomain+"&orgtype="+orgtype+"&action=addorg";
						
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
		<li><a href="userguide.html">User Guide</a></li>
		<li><a href="about.html">About</a></li>
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
	if (usr == null) {
	%>
	<tr width="100%">
		<td colspan="2">
		<h3>Add Organization</h3>
		</td>
	</tr>
	<tr>
		<td>Organization Name *</td>
		<td><input type="text" id="orgname" required autofocus placeholder="Example: Cloudspokes" /></td>
	</tr>
	<tr>
		<td>Domain Name *</td>
		<td><input type="text" id="domainname" required placeholder="Example: gmail.com" /></td>
	</tr>
	<!--<tr>
		<td align="right">Organization Type</td>
		<td align="right"><input type="text" id="orgtype" value="Organization" readonly /></td>
	</tr>
	-->
	<tr>
	<td colspan="2">		<p>If you are going to test out this system using <b>Gmail</b> accounts, then please enter the domain name as gmail.com</p>
	</td></tr>
	<tr>
		<td colspan="2">
			<!-- input type="hidden" name="action" value="addorg"/-->
			<input type="button" value="Add" onClick="addOrg()"/>
		</td>
	</tr>
	<%} else { %>
<tr>
		<td colspan="2"><p>You have already setup an Organization or are part of an Organization.</p>
		</td>
	</tr>	
	<% } %>
</table>
</div>
<hr />
<div id="footer">
<p>&copy;2011 Romin Irani | Design by <a href="http://andreasviklund.com/">Andreas Viklund</a></p>
</div>
</div>
</body>
</html>