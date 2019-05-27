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
			<div
				w3-include-html="../WebressourcenImport/NavBarGeneral.html"></div>

		</div>
	</div>

	<div w3-include-html="../WebressourcenImport/header.html"></div>

	<!-- Page content -->
	<div class="w3-content content">
		<div class="w3-container w3-padding-64" id="maincontent">
			<h1>Register User</h1>
			<br>
			
			<form id="registerUser" name="registerUser" method="POST" onsubmit="sendRegisterFormData();return false">
			<table border="0">
			
			<tr><td>Firstname:</td><td><input id="f1" type="text" name="userFirstname" required/></td></tr>
			<tr><td>Lastname:</td><td><input id="f2" type="text" name="userLastname" required/></td></tr>
			<tr><td>UserName:</td><td><input id="f3" type="text" name="userName" required/></td></tr>
			<tr><td>Password:</td><td><input id="f4" type="password" name="userPassword" required/></td></tr>
			<tr><td>Email:</td><td><input id="f5" type="email" name="userEmail" required /></td></tr>
			<tr><td>Submit:</td><td><input type='submit' value='submit'></td></tr>
			
			
			
			</table>
			
			</form>
			<div id="response"></div>
			

		</div>

	</div>

	<!-- Footer -->
	<footer class="w3-center w3-content content w3-padding-16">
		<div w3-include-html="../WebressourcenImport/footer.html"></div>
		<script>
			includeHTML();
			function sendRegisterFormData() {
				var formData = new FormData();
				//https://stackoverflow.com/questions/19978600/how-to-loop-through-elements-of-forms-with-javascript
				var elements = document.getElementById("registerUser").elements;
				for (var i = 0, element; element = elements[i++];) {
					if(element.type != "submit"){
						formData.append("formVars", element.name);
						formData.append("formVars", element.value);
					}
				}
				
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/users/pushUserRegistration");
				xhr.onload = function() {
					console.log(xhr.responseText);
					if (xhr.status == 200) {
						document.getElementById("response").innerHTML = xhr.responseText;
					} else {
						document.getElementById("response").innerHTML = "some error happend"
					}
				}
				
				
				xhr.send(formData);
				
			}
			
		</script>
	</footer>

</body>
</html>

