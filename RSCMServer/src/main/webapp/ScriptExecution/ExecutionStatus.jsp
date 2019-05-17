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
<link rel="stylesheet" type="text/css"
	href="../WebressourcenImport/style.css">
<script type="text/javascript"
	src="../WebressourcenImport/w3ContentLoader.js"></script>
	<script type="text/javascript">
	
	var returnURL ="";
	var url = new URL(window.location.href);
	if(window.location.href.indexOf('?returnURL=') != -1){
		returnURL=url.searchParams.get("returnURL");
	}else if(window.location.href.indexOf('&returnURL=') != -1){
		returnURL=url.searchParams.get("returnURL");
	}else{
		window.location='../index.jsp';
	}
	
	var entityID ="";
	var url = new URL(window.location.href);
	if(window.location.href.indexOf('?id=') != -1){
		entityID=url.searchParams.get("id");
	}else if(window.location.href.indexOf('&id=') != -1){
		entityID=url.searchParams.get("id");
	}else{
		window.location=returnURL;
	}
	
	var table ="";
	var url = new URL(window.location.href);
	if(window.location.href.indexOf('?table=') != -1){
		table=url.searchParams.get("table");
	}else if(window.location.href.indexOf('&table=') != -1){
		table=url.searchParams.get("table");
	}else{
		window.location=returnURL;
	}
	
	var fileName ="";
	var url = new URL(window.location.href);
	if(window.location.href.indexOf('?fileName=') != -1){
		fileName=url.searchParams.get("fileName");
	}else if(window.location.href.indexOf('&fileName=') != -1){
		fileName=url.searchParams.get("fileName");
	}else{
		window.location=returnURL;
	}
	
	var backName= returnURL.substring(returnURL.lastIndexOf("/")+1, returnURL.length-4);
	
	</script>
</head>

<body>
	<%
		GlobalSettingsAndVariablesInterface gsav = GlobalSettingsAndVariables.getInstance();
		ApplicationContext ac = RequestContextUtils.findWebApplicationContext(request);
		JSPSupporterBean jsb = (JSPSupporterBean) ac.getBean("jspSupporterBean");
		RSCMClientRepository rcr = jsb.getRSCMClientRepository();
	%>
	<!-- Navbar (sit on top) -->
	<div class="w3-top">
		<div class="w3-bar navbar w3-padding w3-card">
			<script>document.write("<a href='"+returnURL+"' class='w3-bar-item marginRight w3-button w3-teal'>Back to: "+backName+"</a>");</script>
			
			<div w3-include-html="../WebressourcenImport/NavBarGeneral.html"></div>
		</div>
	</div>





	<div w3-include-html="../WebressourcenImport/header.html"></div>

	<!-- Page content -->
	<div class="w3-content content " style="padding-top: 70px;">
		<div class="w3-container " id="maincontent">
			asdasd
			<div id="response"></div>
				
		</div>
	</div>

	<!-- Footer -->
	<footer class="w3-center w3-content content w3-padding-16">
		<div w3-include-html="../WebressourcenImport/footer.html"></div>
		<script>
			
		function showExecutionDetails() {
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "/ScriptExecution/showExecutionDetails");
			xhr.onload = function() {
				console.log(xhr.responseText);
				var responsArray = JSON.parse(xhr.responseText); //parse Json
				document.getElementById("response").innerHTML = xhr.responseText;
			}
			
	        var data = new FormData();
	        data.append("entityID", entityID);
	        data.append("table", table);
	        data.append("fileName", fileName);

			xhr.send(data);

		}

		showExecutionDetails();		
		</script>
	</footer>

</body>
</html>

