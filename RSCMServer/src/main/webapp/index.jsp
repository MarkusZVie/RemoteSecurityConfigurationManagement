
<%@page import="org.springframework.security.core.userdetails.UserDetails"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Optional"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="java.lang.reflect.Field"%>
<%@page
	import="at.ac.univie.rscm.spring.api.controller.JSPSupporterBean"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.data.domain.Sort"%>
<%@page import="at.ac.univie.rscm.model.RSCMClient"%>
<%@page import="java.util.List"%>
<%@page import="org.springframework.beans.factory.annotation.Autowired"%>
<%@page
	import="at.ac.univie.rscm.spring.api.repository.RSCMClientRepository"%>
<%@page
	import="at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface"%>
<%@page
	import="at.ac.univie.rscm.application.global.GlobalSettingsAndVariables"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>

<title>Remote Security Configuration Management <%
	String thisPageName = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1,
			request.getRequestURI().lastIndexOf(".jsp"));
%>
</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css"
	href="WebressourcenImport/style.css">
<script type="text/javascript"
	src="WebressourcenImport/w3ContentLoader.js"></script>
</head>

<body>

	<!-- Navbar (sit on top) -->
	<div class="w3-top">
		<div class="w3-bar navbar w3-padding w3-card">
			<a href="RegisterAccount.jsp"	class="w3-bar-item w3-button w3-teal w3-margin-right">Register</a> 
			
			<%
			//https://dzone.com/articles/how-to-get-current-logged-in-username-in-spring-se
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (principal instanceof UserDetails) {
				out.println("<a href=\"logout\" class=\"w3-bar-item w3-button w3-teal w3-margin-right\">Logout</a>");
			} else {
				out.println("<a href=\"login\" class=\"w3-bar-item w3-button w3-teal w3-margin-right\">Login</a>");
			}
			
			%>
			
			
			
<div class="w3-right w3-hide-small">
			<a href="/WebPage/Downloads.jsp"class="w3-bar-item w3-button w3-blue-grey w3circle w3-margin-left">Downloads</a>
			<a href="/Administration/ClientManagement.jsp"class="w3-bar-item w3-button w3-blue-grey w3circle w3-margin-left">Administration</a>
			<a href="/Application/ManageRoles.jsp"class="w3-bar-item w3-button w3-blue-grey w3circle w3-margin-left">Application</a>
			<a href="/ScriptExecution/ExecuteOnApplicants.jsp"class="w3-bar-item w3-button w3-blue-grey w3circle w3-margin-left">ScriptExecution</a>
			<a href="/index.jsp" class="w3-button w3-circle w3-teal w3-margin-left"><i class="fa fa-home"></i></a> 
		</div>
		</div>
		
		<!-- Right-sided navbar links. Hide them on small screens -->
		
		
	</div>

	<div w3-include-html="WebressourcenImport/header.html"></div>

	<!-- Page content -->
	<div class="w3-content content">
		<div class="w3-container w3-padding-64" id="maincontent">
			<h1>Welcome to Remote Security Configuration Management</h1>
			<br>
			<table>
				<tr>
					<td><a style="width:150px;" href="RegisterAccount.jsp" class="w3-bar-item w3-button w3-teal w3-margin-right">Register</a></td>
					<td><p>Here you can sign up yourself to use the RSCM Application</p></td>
				</tr>
				<tr>
					<%
					//https://dzone.com/articles/how-to-get-current-logged-in-username-in-spring-se
					if (principal instanceof UserDetails) {
						out.println("<td><a href=\"logout\" class=\"w3-bar-item w3-button w3-teal w3-margin-right\">Logout</a></td>");
						out.println("<td><p>You can logout here</p></td>");
					} else {
						out.println("<td><a href=\"login\" class=\"w3-bar-item w3-button w3-teal w3-margin-right\">Login</a></td>");
						out.println("<td><p>If you have already an account you can sign-in here</p></td>");
					}
					
					%>
				</tr>
				<tr >
					<td style ="border-top: 1px solid black;"><a style="width:150px;" href="/WebPage/Downloads.jsp" class="w3-bar-item w3-button w3-teal w3-margin-right">Downloads</a></td>
					<td style ="border-top: 1px solid black;"><p>Here are all available downloads (for instance RSCM-Client-Installer)</p></td>
				</tr>
				<tr>
					<td><a style="width:150px;" href="/Administration/ClientManagement.jsp" class="w3-bar-item w3-button w3-teal w3-margin-right">Administration</a></td>
					<td><p>Here just for Administration purposes</p></td>
				</tr>
				<tr>
					<td><a style="width:150px;" href="/Application/ManageRoles.jsp" class="w3-bar-item w3-button w3-teal w3-margin-right">Application</a></td>
					<td><p>You can add and modify applicationdata here</p></td>
				</tr>
				<tr>
					<td><a style="width:150px;" href="/ScriptExecution/ExecuteOnApplicants.jsp" class="w3-bar-item w3-button w3-teal w3-margin-right">Script Execution</a></td>
					<td><p>You can define the script execution here</p></td>
				</tr>
				

			</table>



		</div>
	</div>
	<!-- Footer -->
	<footer class="w3-center w3-content content w3-padding-16">
		<div w3-include-html="WebressourcenImport/footer.html"></div>
		<script>
			includeHTML();
		</script>
	</footer>

</body>
</html>

