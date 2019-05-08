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
				w3-include-html="../WebressourcenImport/NavBarAdministrator.html"></div>

		</div>
	</div>

	<div w3-include-html="../WebressourcenImport/header.html"></div>

	<!-- Page content -->
	<div class="w3-content content">
		<div class="w3-container w3-padding-64" id="maincontent">
			<div class="multiple-upload">
				<h3>Upload Script(s)</h3>
				<form id="multipleUploadForm" name="multipleUploadForm">
					<input id="multipleFileUploadInput" type="file" name="files"
						class="file-input" multiple required />
					<button type="submit" class="primary submit-btn">Submit</button>
				</form>
				<div class="upload-response">
					<div id="multipleFileUploadError"></div>
					<div id="multipleFileUploadSuccess"></div>
				</div>
			</div>
			<script src="../WebressourcenImport/fileUploadHelper.js"></script>


			<h1>Script Management</h1>
			<br>
			<form id="searchNameForm" name="searchNameForm">
				<input id="searchName" type="text" name="searchName" />
			</form>

			<table class='w3-table w3-striped w3-hoverable table'>
				<thead>
				<tr>
					<th>filename</th>
					<th>size</th>
					<th>creation date</th>
					
					<th>inspect</th>
					<th>delete</th>
					</tr>
				</thead>
				<tbody  id="tableContent">
				
				</tbody>
				
			</table>

		</div>

	</div>

	<!-- Footer -->
	<footer class="w3-center w3-content content w3-padding-16">
		<div w3-include-html="../WebressourcenImport/footer.html"></div>
		<script src="../WebressourcenImport/fileUploadHelper.js"></script>
		<script>
			includeHTML();
			getFileList();
		</script>
	</footer>

</body>
</html>

