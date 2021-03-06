<%@page import="java.util.Optional"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="java.lang.reflect.Field"%>
<%@ page import="java.util.*,java.io.*"%>
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
<script src="https://code.jquery.com/jquery-latest.js"></script>


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
	<!-- Navbar (sit on top) --><div class="w3-top">
	<div class="w3-bar navbar w3-padding w3-card">
		<a href="ClientManagement.jsp" class="w3-bar-item w3-button w3-teal w3circle">Client-Management</a>
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
	<div class="w3-container w3-padding-64" id="maincontent">

			<textarea rows="9" cols="80" id="console" style="background-color: #000;
    border: 1px solid #000;
    color: #FFFFFF;
    padding: 8px;
    font-family: 'courier new';">
</textarea>
	
	</div>
</div>
<!-- Footer -->
<footer class="w3-center w3-content content w3-padding-16">
	<div w3-include-html="../WebressourcenImport/footer.html"></div>
	<script>
		includeHTML();
	</script>
</footer>

<script type="text/javascript">
	var shellContent = "";
	document
			.getElementById("console")
			.addEventListener(
					'keypress',
					function(e) {
						//event.preventDefault();
						if (e.key === 'Enter') {
							var shellComand = {}
							var beginComand = shellContent.length;
							
							shellComand["comand"] = document.getElementById("console").value.substring(beginComand,document.getElementById("console").value.length);
							shellComand["keyID"] = <% out.println(clientId); %>;
							//shellComand["firstName"] = $("#firstName").val();
							//shellComand["lastName"] = $("#lastName").val();

							jQuery
									.ajax({
										type : "POST",
										contentType : "application/json; charset=utf-8",
										url : "https://localhost:8443/ajax/postComand",
										data : JSON.stringify(shellComand),
										dataType : 'json',
										success : function(data) {
											for (var count = 0; count < data.length; count++) {
												document.getElementById("console").value += data[count] + "\n";
											}
											shellContent = document.getElementById("console").value;
											var textarea = document.getElementById('console');
											textarea.scrollTop = textarea.scrollHeight;
										}
									});
						}
					});
</script>
</body>
</html>

