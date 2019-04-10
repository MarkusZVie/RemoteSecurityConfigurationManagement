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

<title>
	<%
	String thisPageName = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1,
			request.getRequestURI().lastIndexOf(".jsp"));
	out.println(thisPageName);
	%>
</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="../style.css">
<script type="text/javascript" src="../w3ContentLoader.js"></script>
</head>

<body>
	<%
		GlobalSettingsAndVariablesInterface gsav = GlobalSettingsAndVariables.getInstance();
		ApplicationContext ac = RequestContextUtils.findWebApplicationContext(request);
		JSPSupporterBean jsb = (JSPSupporterBean) ac.getBean("jspSupporterBean");
		RSCMClientRepository rcr = jsb.getRSCMClientRepository();
		String clientId = request.getParameter("clientId");
		boolean isInvalid = false;
		if (clientId == null || clientId.equals("")) {
			isInvalid = true;
		} else {
			if (!gsav.isInteger(clientId)) {
				isInvalid = true;
			}
		}
		if (isInvalid) {
			out.println(
					"<script>function linkback() { window.location=\"ClientManagement.jsp\"} linkback() </script>");
			throw new javax.servlet.jsp.SkipPageException();
		}

		Optional<RSCMClient> orc = rcr.findById(Integer.parseInt(clientId));
		if (!orc.isPresent()) {
			out.println(
					"<script>function linkback() { window.location=\"ClientManagement.jsp\"} linkback() </script>");
			throw new javax.servlet.jsp.SkipPageException();
		}
		RSCMClient rc = orc.get();
	%>
	<!-- Navbar (sit on top) -->
	<div class="w3-top">
		<div class="w3-bar navbar w3-padding w3-card">
			<a href="ClientDetail.jsp?clientId=<%out.println(clientId);%>"
				class="w3-bar-item w3-button <%if (thisPageName.equals("ClientDetail")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%> 
				w3circle">Client-Details</a>
			<a href="SSHConsole.jsp?clientId=<%out.println(clientId);%>"
				class="w3-bar-item w3-button <%if (thisPageName.equals("SSHConsole")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%>  w3circle w3-margin-left">SSH-Console</a>
			<a href="SSHScriptBox.jsp?clientId=<%out.println(clientId);%>"
				class="w3-bar-item w3-button <%if (thisPageName.equals("SSHScriptBox")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%>  w3circle w3-margin-left">SSH-ScriptBox</a>
			<div w3-include-html="../NavBarAdministrator.html"></div>
		</div>
	</div>


	<div w3-include-html="../header.html"></div>

	<!-- Page content -->
	<div class="w3-content content">
		<div class="w3-container w3-padding-64" id="maincontent">
		
		
		</div>
	</div>
	<!-- Footer -->
	<footer class="w3-center w3-content content w3-padding-16">
		<div w3-include-html="../footer.html"></div>
		<script>
			includeHTML();
		</script>
	</footer>

</body>
</html>

