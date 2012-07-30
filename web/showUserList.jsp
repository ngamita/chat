<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@page import="com.thirdchimpanzee.myhelpdesksupport.entity.HelpDeskUser"%>

<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="description" content="My HelpDesk Bot is a Google Chat Bot that sends allows you to setup a support community for your organization." />
	<meta name="keywords" content="Bot,Support,Help, Agent,Automation,Google Talk, Google, Google Chat,IM, Instant Message" />
	<meta name="author" content="Romin Irani" />
<link rel="stylesheet" type="text/css" href="basic-minimal.css"
	title="Basic Minimal" media="all" />
	<title>My HelpDesk Support Bot</title>
<script type="text/javascript">
var userList;
function getUserList(){
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
							 message= "<table class='user' width=\"100%\"><tr width=\"100%\">"
								 	+ "<th width='10%'></th>"
								 	+ "<th width='20%'>Name</th>"
								 	+ "<th width='30%'>Email Address</th>"
								 	+ "<th width='20%'>Status</th>"
								 	+ "<th width='20%'>Agent</th>"
								 	+ "<th width='10%'>Report</th>"
									+"</tr>";
							userList = xmlhttp.responseXML.documentElement.getElementsByTagName("User");
							if (userList != null && userList.length > 0){
								for (var i = 0; i < userList.length; i++){							
									var user  = userList[i];
									message += "<tr><td><input type='checkbox' id='"+i+"'/></td>";
									message += "<td>"+user.getElementsByTagName("Name")[0].firstChild.nodeValue + "</td>";
									message += "<td>"+user.getElementsByTagName("EmailAddress")[0].firstChild.nodeValue + "</td>";
									message += "<td>"+user.getElementsByTagName("Status")[0].firstChild.nodeValue + "</td>";
									var isAgent = "User"								
									if (user.getElementsByTagName("isAgent")[0].firstChild.nodeValue == "true"){								
										isAgent = "Agent";
									}
									message += "<td>"+isAgent+"</td>";
									if (isAgent == "Agent") {
										message += "<td><a href=showUserReport.jsp?agentid="+user.getElementsByTagName("ID")[0].firstChild.nodeValue +">Report</a>";
									}
									message += "</tr>";
								}
							}else{
								message += "<tr><td colspan='5'>No Users or Agents are present. Please add them by clicking on the Add User button above.</td></tr>";
							}			
							message +=  "</table>";
							 					 
							 
					    }else if (status == "invalid"){
						    window.location.replace("../" + xmlhttp.responseXML.documentElement.getElementsByTagName("Description")[0].firstChild.nodeValue);
					    }
					    else{
						     message = "Failed";
					    }					    
						document.getElementById('userList').innerHTML = message;
			  		}
			  	}
				var params = "action=getUserList";		
				xmlhttp.open("GET","/data?"+params,true);
				xmlhttp.send(params);		
}
function showAddUser(){
	window.location.replace("../addUser.jsp");
}
function deleteUsers(){
	var ids = "";
	var isFirst = true;
	if (userList != null && userList.length > 0){
		for (var i = 0; i < userList.length; i++){
			var checked = document.getElementById(i).value;
			if(checked == "on"){							
				var user  = userList[i];
				if (isFirst){
					ids += user.getElementsByTagName("ID")[0].firstChild.nodeValue;
					isFirst = false;
				}else{
					ids += "," + user.getElementsByTagName("ID")[0].firstChild.nodeValue; 
				}
			}
		}
	}
	if (ids.length > 0 ){
	
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
					window.location.href = "../showUserList.jsp";	 
					 
			    }else{
				     message = "Failed";
			    }					    
				document.getElementById('message').innerHTML = message;
	  		}
	  	}	  	
		var params = "action=deleteUsers&id="+ids;		
		xmlhttp.open("GET","/data?"+params,true);
		xmlhttp.send(params);	
	}
}
</script>
</head>

<body class="light" onLoad="getUserList()">
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
    		if (user != null)  {
		%>
		<li>Welcome <i><%=userService.getCurrentUser().getEmail()%></i></li>
		<li><a href="index.jsp">Home</a></li>
		<% 
	   		com.thirdchimpanzee.myhelpdesksupport.service.UserService usrService = com.thirdchimpanzee.myhelpdesksupport.service.UserService.getInstance();
			HelpDeskUser usr = usrService.findUserByEmailId(user.getEmail());
			if (usr.isAdmin()) { 
		%>
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
		<li><a href="<%=userService.createLoginURL(request.getRequestURI())%>">Login</a></li>
		<%
			}
		%>
</ul>
</div>

<div id="content">
<div id="message"></div>
<input type="button" onClick="showAddUser()" value="Add User" /> <input
	type="button" onClick="deleteUsers()" value="Delete" />
<div id="userList"></div>
</div>

<hr />
<div id="footer">
<p>&copy;2011 Romin Irani | Design by <a href="http://andreasviklund.com/">Andreas Viklund</a></p>
</div>
</div>
</body>
</html>