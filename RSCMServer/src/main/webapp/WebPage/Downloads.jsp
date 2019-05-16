<%@page import="java.util.Iterator"%>
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
		
		GlobalSettingsAndVariablesInterface gsav = GlobalSettingsAndVariables.getInstance();
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
		The Installation require: vcredist
		<a href="https://www.microsoft.com/en-us/download/confirmation.aspx?id=21254">download vcredist</a>
		<a href="Downloads.jsp"
				class="w3-bar-item w3-button <%if (thisPageName.equals("Downloads")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%> 
				w3circle">Downloads</a>
		
			
			<div w3-include-html="../WebressourcenImport/NavBarGeneral.html"></div>
		</div>
	</div>
	<div w3-include-html="../WebressourcenImport/header.html"></div>

	<!-- Page content -->
	<div class="w3-content content">
		<div class="w3-container w3-padding-64" id="maincontent">
		<table class='w3-table w3-striped w3-hoverable table'>
		<%
		//https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
		Iterator it = gsav.getProvidedDownloads().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        out.println("<tr onclick=\"window.location='Downloads/"+pair.getKey()+"'\">");
	        out.println("<td>"+pair.getKey()+"</td>");
	        out.println("<td>"+pair.getValue()+"</td>");
	        out.println("</tr>");
	        //it.remove(); // avoids a ConcurrentModificationException
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

