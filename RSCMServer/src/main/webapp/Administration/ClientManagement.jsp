<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
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
	<!-- Navbar (sit on top) -->
	<div class="w3-top">
		<div class="w3-bar navbar w3-padding w3-card">
			<a href="ClientManagement.jsp" class="w3-bar-item w3-button w3-orange w3circle">Client-Management</a>
			<div w3-include-html="../WebressourcenImport/NavBarGeneral.html"></div>
			
		</div>
	</div>

	<div w3-include-html="../WebressourcenImport/header.html"></div>

	<!-- Page content -->
	<div class="w3-content content">
		<div class="w3-container w3-padding-64" id="maincontent">
			<h1>Clients</h1>
			<br>
			<table class='w3-table w3-striped w3-hoverable table'>

				<%
					GlobalSettingsAndVariablesInterface gsav = GlobalSettingsAndVariables.getInstance();
					ApplicationContext ac = RequestContextUtils.findWebApplicationContext(request);
					JSPSupporterBean jsb = (JSPSupporterBean) ac.getBean("jspSupporterBean");
					RSCMClientRepository rcr = jsb.getRSCMClientRepository();
					//Get parameter if some exists
					String sortFielName = request.getParameter("sortByField");
					String sortDirection = request.getParameter("sortDirection");
					List<RSCMClient> rcList;

					//when there are parameter sort by them
					if (!(sortFielName == null || sortDirection == null)) {
						if (!(sortFielName.equals("") || sortDirection.equals(""))) {
							rcList = rcr.findAll(new Sort(Sort.Direction.fromString(sortDirection), sortFielName));
						} else {
							rcList = rcr.findAll(new Sort(Sort.Direction.ASC, "RscmclientId"));
						}
					} else {
						rcList = rcr.findAll(new Sort(Sort.Direction.ASC, "RscmclientId"));
					}
					
					//create list of methots (get)
					Method[] methods = RSCMClient.class.getMethods();
					boolean firstRow = true;
					//go throw db list
					Map<Integer,Date> openPorts = gsav.getPortScanner().getOpenPorts();
					for (RSCMClient rc : rcList) {
						//add port activity to rc
						if(openPorts.containsKey(rc.getClientPort())){
							rc.setIsActive(true);
						}else{
							rc.setIsActive(false);
						}
						
						//if its first row than create header
						if (firstRow) {
							firstRow = false;
							out.println("<tr>");
							
							for (Method m : methods) {
								//check if method is a get method
								if (m.getName().startsWith("get") && m.getParameterTypes().length == 0
										&& !m.getName().equals("getClass")) {
									//build link with parameter
									String onclickURL = "";
									onclickURL += request.getRequestURI() + "?";
									onclickURL += "sortByField=" + m.getName().substring(3, m.getName().length());
									//if is already sort by them than use diffrent sort direction
									if (sortDirection != null) {
										if (sortDirection.equals("ASC") && m.getName().substring(3).equals(sortFielName)) {
											onclickURL += "&" + "sortDirection=" + "DESC";
										} else {
											onclickURL += "&" + "sortDirection=" + "ASC";
										}
									} else {
										onclickURL += "&" + "sortDirection=" + "ASC";
									}
									//create arrow indicaer of sort direction
									if (sortFielName != null && sortDirection != null) {
										if (m.getName().substring(3).equals(sortFielName)) {
											if (sortDirection.equals("DESC")) {
												out.println("<th onclick=\"window.location='" + onclickURL + "';\">"
														+ m.getName().substring(3) + "&#8595</th>");
											} else {
												out.println("<th onclick=\"window.location='" + onclickURL + "';\">"
														+ m.getName().substring(3) + "&#8593</th>");
											}
										} else {
											out.println("<th onclick=\"window.location='" + onclickURL + "';\">"
													+ m.getName().substring(3) + "</th>");
										}
									} else {
										out.println("<th onclick=\"window.location='" + onclickURL + "';\">"
												+ m.getName().substring(3) + "</th>");
									}

								}
							}
							
							out.println("</tr>");
						}
						
						out.println("<tr onclick=\"window.location='ClientDetail.jsp?clientId="+rc.getRscmclientId()+"'\">");
						//print content of db select
						
						for (Method m : methods) {
							if (m.getName().startsWith("get") && m.getParameterTypes().length == 0
									&& !m.getName().equals("getClass")) {
								if(m.getName().equals("getRSCMClientConnections")){
									out.println("<td>" + rc.getRSCMClientConnections().size() + "</td>");
								}else{
									if(m.getName().equals("getIsActive")){
										if(rc.getIsActive()){
											out.println("<td>" + openPorts.get(rc.getClientPort()) + "</td>");
										}else{
											out.println("<td>no</td>");
										}
									}else{
										//.scrollable { overflow: scroll; overflow-y:hidden; white-space: nowrap; text-overflow:ellipsis;}
										out.println("<td><div style='width:100%; max-width:200px; overflow:auto'>" + m.invoke(rc, null) + "</div></td>");
									}
								}
								
							}

						}
						
						
						out.println("</tr>");
					}
				%>

			</table>

		

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

