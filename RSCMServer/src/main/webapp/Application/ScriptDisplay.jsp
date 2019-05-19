<%@page import="java.nio.file.Paths"%>
<%@page import="java.nio.file.Files"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.File"%>
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
<!-- //https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/ -->
<title>
	<%
		out.println(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1,
				request.getRequestURI().lastIndexOf(".jsp")));
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
	
	var fileName ="";
	var url = new URL(window.location.href);
	if(window.location.href.indexOf('?filename=') != -1){
		fileName=url.searchParams.get("filename");
	}else if(window.location.href.indexOf('&filename=') != -1){
		fileName=url.searchParams.get("filename");
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
		//Get parameter if some exists
	%>
	<!-- Navbar (sit on top) -->
	<div class="w3-top">
		<div class="w3-bar navbar w3-padding w3-card">
		<script>document.write("<a href='"+returnURL+"' class='w3-bar-item marginRight w3-button w3-teal'>&#8592; Back to: "+backName+"</a>");</script>
			<div
				w3-include-html="../WebressourcenImport/NavBarGeneral.html"></div>

		</div>
	</div>

	<div w3-include-html="../WebressourcenImport/header.html"></div>

	<!-- Page content -->
	<div class="w3-content content">
		<div class="w3-container w3-padding-64" id="maincontent">
			
			<h1>File: <%out.println(request.getParameter("filename")); %></h1>
			
			<div id="content" style="font-family: 'Courier New'; width:auto;">
			<%
			 try (BufferedReader br = Files.newBufferedReader(Paths.get(gsav.getFileDownloadDirectory()+"/"+request.getParameter("filename")))) {

		            // read line by line
		            String line;
		            while ((line = br.readLine()) != null) {
		                out.println(line + "<br>");
		            }

		        } catch (Exception e) {
		            System.err.format("IOException: %s%n", e);
		        }
			
			%>
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

