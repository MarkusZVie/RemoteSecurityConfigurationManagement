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

			<div w3-include-html="../WebressourcenImport/NavBarGeneral.html"></div>
		</div>
	</div>





	<div w3-include-html="../WebressourcenImport/header.html"></div>

	<!-- Page content -->
	<div class="w3-content content " style="padding-top: 70px;">
		<form id="updateScriptAssignment" name="updateScriptAssignment" method="POST" onsubmit="updateAssignment();return false">
		<div class="w3-container " id="maincontent">

			<div id="response"></div>
			<div id="updateAssignmentButtonDiv">
				
			</div>
		</div>

		<!-- Menu Section -->
		<div class="w3-row" id="menu">
			<div class="w3-col l6 w3-padding-large">
				<h2 class="w3-center">Applicantgroups</h2>
				<table class='w3-table w3-striped w3-hoverable'>
					<thead>
						<td><button onclick="clearSelection();"  class="w3-bar-item w3-btn w3-teal" >Clear<br>Selection</button></td>
						<td><input type="text" id="searchApplicantgroup" size="15"name="searchApplicantgroup" placeholder="Search by Name" />
							<button onclick="loadApplicantgroupList();">Search</button></td>
						<td>Applicantgroup Description</td>
						<td>Assigned Applicants</td>
						<td>Assigned Clients</td>

					</thead>
					<tbody id="applicantgroupListContent">

					</tbody>
				</table>
			</div>
			<div class="w3-col l6 w3-padding-large">
				<h2 class="w3-center">Assign Scripts</h2>
				<table class='w3-table w3-striped w3-hoverable table'>
					<thead>
					<tr id="headLineContent">
						
						
					</tr>
					</thead>
					<tbody  id="scriptListContent">
					
					</tbody>
				
				</table>
				<br>
			</div>
		</div>
	</div>
	<!-- Footer -->
	<footer class="w3-center w3-content content w3-padding-16">
		<div w3-include-html="../WebressourcenImport/footer.html"></div>
		<script>
			
			function getFileList() {
				
				//check for Parameter
				var baseHeadLineContent = "<th><input type=\"text\" id=\"searchFile\" name=\"searchFile\" placeholder=\"Search by Name\" />";
				baseHeadLineContent += "<button onclick=\"getFileList();\">Search</button></th>";
				baseHeadLineContent += "<th>Creation Date</th>";
				baseHeadLineContent += "<th>Size</th>";
				
				
				
				var applicantgroupIdParameter ="";
				var url = new URL(window.location.href);
				if(window.location.href.indexOf('?applicantgroupId=') != -1){
					applicantgroupIdParameter=url.searchParams.get("applicantgroupId");
					document.getElementById("headLineContent").innerHTML = baseHeadLineContent + "<th>Assigned</th><th>Progress</th>";
				}else if(window.location.href.indexOf('&applicantgroupId=') != -1){
					applicantgroupIdParameter=url.searchParams.get("applicantgroupId");
					document.getElementById("headLineContent").innerHTML = baseHeadLineContent + "<th>Assigned</th><th>Progress</th>";
				}else{
					applicantgroupIdParameter=-1;
					document.getElementById("headLineContent").innerHTML = baseHeadLineContent;
				}

				var onclickURL = location.protocol + '//' + location.host + location.pathname; //GetURL
				var searchString = document.getElementById("searchFile").value;
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/ScriptExecution/getFileList");
				xhr.onload = function() {
					var responsArray = JSON.parse(xhr.responseText); //parse Json
					if (xhr.status == 200) {
						var contentString = ""; //build table content
						for (var i = 0; i < responsArray.length; i++) {
							contentString += "<tr>";
							contentString += "<td><div style='overflow-x: auto;'>" + responsArray[i]['fileName']+ "</div></td>";
							contentString += "<td>" + responsArray[i]['fileCreationDate']	+ "</td>";
							contentString += "<td>"	+ responsArray[i]['fileSize'] + "</td>";
							//File settings
							if(applicantgroupIdParameter>=0){
								contentString += "<td>"	+ responsArray[i]['executionAssignDate'] + "</td>";
								if(responsArray[i]['executionPercentageNumbers']!=""){
									contentString += "<td> ";
									var onclickString = ""
									onclickString += "window.location='ExecutionStatus.jsp";
									onclickString += "?id="+ applicantgroupIdParameter;
									onclickString += "&table=applicantgroup";
									onclickString += "&fileName=" + responsArray[i]['fileName'];
									onclickString += "&returnURL=" + onclickURL;
									onclickString += "'";
									contentString += "<button class=\"w3-bar-item marginRight w3-btn w3-teal\" onclick=\""+onclickString+"\" >";	
									contentString += responsArray[i]['executionPercentageNumbers'] + " " + responsArray[i]['executionPercentage'];
									contentString +="</button>";
									contentString +="</td>";
								}else{
									contentString += "<td></td>";
								}
							}else{
								contentString += "<td><input type='checkbox' name='fileAssignment' value='"+responsArray[i]['fileName']+"'></td>";
							}
							
							
							
							
							
							contentString += "</tr>";
						}
						document.getElementById("scriptListContent").innerHTML = contentString;
						
						
					} else {
						multipleFileUploadSuccess.style.display = "none";
						multipleFileUploadError.innerHTML = (response && response.message)	|| "Some Error Occurred";
					}
				}
				var data = new FormData();
				data.append("table", "applicantgroup");
				data.append("identifier", applicantgroupIdParameter);
				data.append("searchString", document.getElementById("searchFile").value);
				xhr.send(data);
				
			}

			function clearSelection(){
				window.location=location.protocol + '//' + location.host + location.pathname;
			}
			
			function loadApplicantgroupList() {
				
				//check for Parameter
				var applicantgroupIdParameter ="";
				var url = new URL(window.location.href);
				if(window.location.href.indexOf('?applicantgroupId=') != -1){
					applicantgroupIdParameter=url.searchParams.get("applicantgroupId");
					document.getElementById("updateAssignmentButtonDiv").innerHTML = "";
				}else if(window.location.href.indexOf('&applicantgroupId=') != -1){
					applicantgroupIdParameter=url.searchParams.get("applicantgroupId");
					document.getElementById("updateAssignmentButtonDiv").innerHTML = "";
				}else{
					applicantgroupIdParameter=-1;
					document.getElementById("updateAssignmentButtonDiv").innerHTML = "<input type='submit' value='Assign selected Applicantgroups to selected Scripts (checkboxes)' style=\"width: 100%;\" class=\"w3-bar-item marginRight w3-button w3-teal\" >";
				}
				
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/ScriptExecution/getApplicantgroupList");
				xhr.onload = function() {
					var responsArray = JSON.parse(xhr.responseText); //parse Json
					var contentString = ""; //build table content
					var onclickURL = location.protocol + '//' + location.host + location.pathname; //GetURL
					for ( var i in responsArray) {
						if(applicantgroupIdParameter == responsArray[i]['applicantgroupId']){
							contentString += "<tr style='background-color: #bde9ba;'>";
						}else{
							contentString += "<tr>";
						}
						contentString += "<td onclick=\"window.location='" + onclickURL + "?"+"applicantgroupId="+responsArray[i]['applicantgroupId']+"'\">" + responsArray[i]['applicantgroupId']+ "</td>";
						contentString += "<td onclick=\"window.location='" + onclickURL + "?"+"applicantgroupId="+responsArray[i]['applicantgroupId']+"'\">" + responsArray[i]['applicantgroupName']	+ "</td>";
						contentString += "<td onclick=\"window.location='" + onclickURL + "?"+"applicantgroupId="+responsArray[i]['applicantgroupId']+"'\">"	+ responsArray[i]['applicantgroupDescription'] + "</td>";
						contentString += "<td onclick=\"window.location='" + onclickURL + "?"+"applicantgroupId="+responsArray[i]['applicantgroupId']+"'\">"	+ responsArray[i]['applicantgroupMembersApplicant'] + "</td>";
						contentString += "<td onclick=\"window.location='" + onclickURL + "?"+"applicantgroupId="+responsArray[i]['applicantgroupId']+"'\">"	+ responsArray[i]['applicantgroupMembersRSCMClients'] + "</td>";
						if(applicantgroupIdParameter<0){
							contentString += "<td><input type='checkbox' name='entityAssignment' value='"+responsArray[i]['applicantgroupId']+"'></td>";
						}
						
						contentString += "</tr>";
					}
					document.getElementById("applicantgroupListContent").innerHTML = contentString; //set table content
					getFileList();
				}

				var data = new FormData();
				data.append("searchString", document.getElementById("searchApplicantgroup").value);
				xhr.send(data);

			}
			
			function updateAssignment() {
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/ScriptExecution/updateApplicantgroupAssignment");
				xhr.onload = function() {
					document.getElementById("response").innerHTML = xhr.responseText;
					getFileList();
					loadApplicantgroupList();
				}
				//https://www.aspsnippets.com/Articles/Get-multiple-selected-checked-CheckBox-values-in-Array-using-JavaScript.aspx
		        var entityAssignment = new Array();
		        var chks = document.getElementsByName("entityAssignment");
		        for (var i = 0; i < chks.length; i++) {
		            if (chks[i].checked) {
		            	entityAssignment.push(chks[i].value);
		            }
		        }
		        var fileAssignment = new Array();
		        var chks = document.getElementsByName("fileAssignment");
		        for (var i = 0; i < chks.length; i++) {
		            if (chks[i].checked) {
		            	fileAssignment.push(chks[i].value);
		            }
		        }
		        
		        var data = new FormData();
		        data.append("entityAssignment", entityAssignment);
		        data.append("fileAssignment", fileAssignment);

				xhr.send(data);

			}
			
			includeHTML();
			loadApplicantgroupList();
		</script>
	</footer>

</body>
</html>

