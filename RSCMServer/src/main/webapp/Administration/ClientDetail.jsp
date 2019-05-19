<%@page import="at.ac.univie.rscm.model.RSCMClientConnection"%>
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
<link rel="stylesheet" type="text/css" href="../WebressourcenImport/style.css">
<script type="text/javascript" src="../WebressourcenImport/w3ContentLoader.js"></script>
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
			<div w3-include-html="../WebressourcenImport/NavBarGeneral.html"></div>
		</div>
	</div>


	<div w3-include-html="../WebressourcenImport/header.html"></div>

	<!-- Page content -->
	<div class="w3-content content">
		<div class="w3-container w3-padding-64" id="maincontent"></div>

		<!-- Menu Section -->
		<div class="w3-row" id="menu">
			<div class="w3-col l6 w3-padding-large">
				<h2 class="w3-center">
					ClientInformation
					</h1>
					<br>
					<%
						Map<Integer, Date> openPorts = gsav.getPortScanner().getOpenPorts();

						Method[] methods = RSCMClient.class.getMethods();
						for (Method m : methods) {
							//check if method is a get method
							if (m.getName().startsWith("get") && m.getParameterTypes().length == 0
									&& !m.getName().equals("getClass") && !m.getName().equals("getRSCMClientConnections")) {
								out.println("<h4>" + m.getName().substring(3) + "</h4>");
								out.println("<p class=\"w3-text-grey\"><div style='width:100%; max-width:500px; overflow:auto'>" + m.invoke(rc, null) + "</div></p>");
								out.println("<br>");
							}
						}
						out.println("<h4> IsConnected </h4>");
						if (openPorts.containsKey(rc.getClientPort())) {
							out.println("<p class=\"w3-text-grey\">Online since: " + openPorts.get(rc.getClientPort()) + "</p>");
						} else {
							out.println("<p class=\"w3-text-grey\">no</p>");
						}
						out.println("<br>");
					%>
				
			</div>
			<div class="w3-col l6 w3-padding-large">
				<h2 class="w3-center">
					Client Connection History </h2>
					<table class='w3-table w3-striped w3-hoverable'>
						<%
							List<RSCMClientConnection> connectionList = rc.getRSCMClientConnections();
							boolean firstRow = true;
							Method[] methods2 = RSCMClientConnection.class.getMethods();
							
							for (RSCMClientConnection rcc : connectionList) {
								//add port activity to rc
								//if its first row than create header
								if (firstRow) {
									firstRow = false;
									out.println("<tr>");

									for (Method m : methods2) {
										//check if method is a get method
										if (m.getName().startsWith("get") && m.getParameterTypes().length == 0
												&& !m.getName().equals("getClass") && !m.getName().equals("getRscmClient")) {
											out.println("<th>" + m.getName().substring(3) + "</th>");
										}
									}

									out.println("</tr>");
								}

								out.println("<tr>");
								//print content of db select

								for (Method m : methods2) {
									if (m.getName().startsWith("get") && m.getParameterTypes().length == 0
											&& !m.getName().equals("getClass") && !m.getName().equals("getRscmClient")) {

										out.println("<td>" + m.invoke(rcc, null) + "</td>");

									}

								}

								out.println("</tr>");
							}
						%>
					</table>
					<br>
			</div>
		</div>
	</div>
	<!-- Footer -->
	<footer class="w3-center w3-content content w3-padding-16">
		<div w3-include-html="../WebressourcenImport/footer.html"></div>
		<script>
			includeHTML();
		</script>
	</footer>

</body>
</html>

