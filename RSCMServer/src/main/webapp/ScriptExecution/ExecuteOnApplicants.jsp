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

			<a href="ExecuteOnApplicants.jsp" class="w3-bar-item marginRight w3-button 
			<%if (thisPageName.equals("ExecuteOnApplicants")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%>	w3circle">E. Applicants</a>
		
			<a href="ExecuteOnApplicantgroups.jsp" class="w3-bar-item marginRight w3-button 
			<%if (thisPageName.equals("ExecuteOnApplicantgroups")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%>	w3circle">E. Applicantgroups</a>
			
			<a href="ExecuteOnRoles.jsp" class="w3-bar-item marginRight w3-button 
			<%if (thisPageName.equals("ExecuteOnRoles")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%>	w3circle">E. Roles</a>	
			
			<a href="ExecuteOnTasks.jsp" class="w3-bar-item marginRight w3-button 
			<%if (thisPageName.equals("ExecuteOnTasks")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%>	w3circle">E. Tasks</a>	
			
			<a href="ExecuteOnJobs.jsp" class="w3-bar-item marginRight w3-button 
			<%if (thisPageName.equals("ExecuteOnJobs")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%>	w3circle">E. Jobs</a>
			
			<a href="ExecuteOnEnvironments.jsp" class="w3-bar-item marginRight w3-button 
			<%if (thisPageName.equals("ExecuteOnEnvironments")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%>	w3circle">E. Environments</a>
			
			<a href="ExecuteOnEnvironmentthreats.jsp" class="w3-bar-item marginRight w3-button 
			<%if (thisPageName.equals("ExecuteOnEnvironmentthreats")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%>	w3circle">E. Environmentthreats</a>

			<div w3-include-html="../WebressourcenImport/NavBarGeneral.html"></div>
		</div>
	</div>





	<div w3-include-html="../WebressourcenImport/header.html"></div>

	<!-- Page content -->
	<div class="w3-content content " style="padding-top: 70px;">
		<form id="updateScriptAssignment" name="updateScriptAssignment" method="POST" onsubmit="updateAssignment();return false">
		<div class="w3-container " id="maincontent">

			<div id="response"></div>
			<div id="updateAssignmentButtonDiv1">
				
			</div>
		</div>

		<!-- Menu Section -->
		<div class="w3-row" id="menu">
			<div class="w3-col l6 w3-padding-large">
				<h2 class="w3-center">Applicants</h2>
				<table class='w3-table w3-striped w3-hoverable' style="width:775px; overflow: scroll;">
					<thead>
						<td><button style="width: 100%" onclick="clearSelection(); return false;"  class="w3-bar-item w3-btn w3-teal" >Clear<br> Selection</button></td>
						<td>
						<input type="text" id="searchApplicant" style="width: 100%" name="searchApplicant" placeholder="Search by Name" width="100%"/>
							<button onclick="loadApplicantList(); return false;">Search</button></td>
							
						<td style="font-size: 12px;">Applicant Firstname</td>
						<td style="font-size: 12px;">Applicant Lastname</td>
						<td style="font-size: 12px;">Applicant Email</td>
						<td style="font-size: 12px;">Assigned Clients</td>

					</thead>
					<tbody id="applicantListContent">

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
		<div class="w3-container " id="maincontent">
			<div id="updateAssignmentButtonDiv2">
				
			</div>
		</div>
		</form>
	</div>
	<!-- Footer -->
	<footer class="w3-center w3-content content w3-padding-16">
		<div w3-include-html="../WebressourcenImport/footer.html"></div>
		<script>
			
			function getFileList() {
				
				//check for Parameter
				var baseHeadLineContent = "<th><input type=\"text\" id=\"searchFile\" name=\"searchFile\" placeholder=\"Search by Name\" />";
				baseHeadLineContent += "<button onclick=\"getFileList(); return false;\">Search</button></th>";
				baseHeadLineContent += "<th>Creation Date</th>";
				baseHeadLineContent += "<th>Size</th>";
				
				
				
				var applicantIdParameter ="";
				var url = new URL(window.location.href);
				if(window.location.href.indexOf('?applicantId=') != -1){
					applicantIdParameter=url.searchParams.get("applicantId");
					document.getElementById("headLineContent").innerHTML = baseHeadLineContent + "<th>Assigned</th><th>Progress</th>";
				}else if(window.location.href.indexOf('&applicantId=') != -1){
					applicantIdParameter=url.searchParams.get("applicantId");
					document.getElementById("headLineContent").innerHTML = baseHeadLineContent + "<th>Assigned</th><th>Progress</th>";
				}else{
					applicantIdParameter=-1;
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
							contentString += "<td><div style='overflow-x: auto;'> <a href='ScriptDisplay.jsp?fileName="+responsArray[i]['fileName']+"&returnURL="+onclickURL+"'>" + responsArray[i]['fileName']+ "</div></td>";
							contentString += "<td>" + responsArray[i]['fileCreationDate']	+ "</td>";
							contentString += "<td>"	+ responsArray[i]['fileSize'] + "</td>";
							//File settings
							if(applicantIdParameter>=0){
								contentString += "<td>"	+ responsArray[i]['executionAssignDate'] + "</td>";
								if(responsArray[i]['executionPercentageNumbers']!=""){
									contentString += "<td> ";
									var onclickString = ""
									onclickString += "window.location='ExecutionStatus.jsp";
									onclickString += "?id="+ applicantIdParameter;
									onclickString += "&table=applicant";
									onclickString += "&fileName=" + responsArray[i]['fileName'];
									onclickString += "&returnURL=" + onclickURL;
									onclickString += "'";
									contentString += "<button class=\"w3-bar-item marginRight w3-btn w3-teal\" onclick=\""+onclickString+"\" >";	
									contentString += responsArray[i]['executionPercentageNumbers'] + " " + responsArray[i]['executionPercentage'];
									contentString +="</button>";
									contentString +="</td>";
									contentString += "<td><input type='checkbox' name='assignToByEntity' value='"+responsArray[i]['fileName']+"' checked></td>";
								}else{
									contentString += "<td></td>";
									contentString += "<td></td>";
									
								}

								
								
							}else{
								contentString += "<td><input type='checkbox' name='fileAssignment' value='"+responsArray[i]['fileName']+"'></td>";
							}
							
							
							
							
							
							contentString += "</tr>";
						}
						if(applicantIdParameter>=0){
							contentString += "<tr>";
							contentString += "<td></td>";
							contentString += "<td></td>";
							contentString += "<td></td>";
							contentString += "<td></td>";
							contentString += "<td align='right' colspan=\"2\"><button class=\"w3-bar-item marginRight w3-btn w3-teal\" onclick=\"removeAssignment(); return false;\" >update assign &#8593;</button></td>";
							contentString += "</tr>";
						}
						
						
						document.getElementById("scriptListContent").innerHTML = contentString;
						
						
					} else {
						multipleFileUploadSuccess.style.display = "none";
						multipleFileUploadError.innerHTML = (response && response.message)	|| "Some Error Occurred";
					}
				}
				var data = new FormData();
				data.append("table", "applicant");
				data.append("identifier", applicantIdParameter);
				data.append("searchString", document.getElementById("searchFile").value);
				xhr.send(data);
				
			}

			function clearSelection(){
				window.location=location.protocol + '//' + location.host + location.pathname;
			}
			
			function removeAssignment(){
				var applicantIdParameter ="";
				var url = new URL(window.location.href);
				if(window.location.href.indexOf('?applicantId=') != -1){
					applicantIdParameter=url.searchParams.get("applicantId");
				}else if(window.location.href.indexOf('&applicantId=') != -1){
					applicantIdParameter=url.searchParams.get("applicantId");
				}else{
					applicantIdParameter=-1;
					document.getElementById("updateAssignmentButtonDiv1").innerHTML = "<input type='submit' value='Assign selected Applicants to selected Scripts (checkboxes)' style=\"width: 100%;\" class=\"w3-bar-item marginRight w3-button w3-teal\" >";
					document.getElementById("updateAssignmentButtonDiv2").innerHTML = "<input type='submit' value='Assign selected Applicants to selected Scripts (checkboxes)' style=\"width: 100%;\" class=\"w3-bar-item marginRight w3-button w3-teal\" >";
					
				}
				
				var uncheckedAssignments = new Array();
		        var chks = document.getElementsByName("assignToByEntity");
		        for (var i = 0; i < chks.length; i++) {
		            if (!chks[i].checked) {
		            	uncheckedAssignments.push(chks[i].value);
		            }
		        }
			        
			    console.log(uncheckedAssignments);    
			    console.log(applicantIdParameter);  
			    console.log("applicant");  
		        var formData = new FormData();
				formData.append("uncheckedFileNameAssignments", uncheckedAssignments);
				formData.append("entityID", applicantIdParameter);
				formData.append("table", "applicant");
				
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/ScriptExecution/removeAssignment");
				xhr.onload = function() {
					if (xhr.status == 200) {
						document.getElementById("response").innerHTML = xhr.responseText;
						getFileList();
					} else {
						console.log(xhr.responseText);
						document.getElementById("response").innerHTML = "some error happend"
					}
				}
				xhr.send(formData);
			}
			
			function loadApplicantList() {
				
				//check for Parameter
				var applicantIdParameter ="";
				var url = new URL(window.location.href);
				if(window.location.href.indexOf('?applicantId=') != -1){
					applicantIdParameter=url.searchParams.get("applicantId");
					document.getElementById("updateAssignmentButtonDiv1").innerHTML = "";
					document.getElementById("updateAssignmentButtonDiv2").innerHTML = "";
				}else if(window.location.href.indexOf('&applicantId=') != -1){
					applicantIdParameter=url.searchParams.get("applicantId");
					document.getElementById("updateAssignmentButtonDiv1").innerHTML = "";
					document.getElementById("updateAssignmentButtonDiv2").innerHTML = "";
				}else{
					applicantIdParameter=-1;
					document.getElementById("updateAssignmentButtonDiv1").innerHTML = "<input type='submit' value='Assign selected Applicants to selected Scripts (checkboxes)' style=\"width: 100%;\" class=\"w3-bar-item marginRight w3-button w3-teal\" >";
					document.getElementById("updateAssignmentButtonDiv2").innerHTML = "<input type='submit' value='Assign selected Applicants to selected Scripts (checkboxes)' style=\"width: 100%;\" class=\"w3-bar-item marginRight w3-button w3-teal\" >";

					
				}
				
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/ScriptExecution/getApplicantList");
				xhr.onload = function() {
					var responsArray = JSON.parse(xhr.responseText); //parse Json
					var contentString = ""; //build table content
					var onclickURL = location.protocol + '//' + location.host + location.pathname; //GetURL
					for ( var i in responsArray) {
						if(applicantIdParameter == responsArray[i]['applicantId']){
							contentString += "<tr style='background-color: #bde9ba;'>";
						}else{
							contentString += "<tr>";
						}						

						contentString += "<td onclick=\"window.location='" + onclickURL + "?"+"applicantId="+responsArray[i]['applicantId']+"'\">" + responsArray[i]['applicantId']+ "</td>";
						contentString += "<td onclick=\"window.location='" + onclickURL + "?"+"applicantId="+responsArray[i]['applicantId']+"'\">" + responsArray[i]['applicantName']	+ "</td>";
						contentString += "<td onclick=\"window.location='" + onclickURL + "?"+"applicantId="+responsArray[i]['applicantId']+"'\">"	+ responsArray[i]['applicantFirstname'] + "</td>";
						contentString += "<td onclick=\"window.location='" + onclickURL + "?"+"applicantId="+responsArray[i]['applicantId']+"'\">"	+ responsArray[i]['applicantLastname'] + "</td>";
						contentString += "<td onclick=\"window.location='" + onclickURL + "?"+"applicantId="+responsArray[i]['applicantId']+"'\">"	+ responsArray[i]['applicantEmail'] + "</td>";
						contentString += "<td onclick=\"window.location='" + onclickURL + "?"+"applicantId="+responsArray[i]['applicantId']+"'\">"	+ responsArray[i]['applicantMembersRSCMClients'] + "</td>";
						if(applicantIdParameter<0){
							contentString += "<td><input type='checkbox' name='entityAssignment' value='"+responsArray[i]['applicantId']+"'></td>";
						}
						
						contentString += "</tr>";
					}
					document.getElementById("applicantListContent").innerHTML = contentString; //set table content
					getFileList();
				}

				var data = new FormData();
				data.append("searchString", document.getElementById("searchApplicant").value);
				xhr.send(data);

			}
			
			function updateAssignment() {
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/ScriptExecution/updateApplicantAssignment");
				xhr.onload = function() {
					document.getElementById("response").innerHTML = xhr.responseText;
					getFileList();
					loadApplicantList();
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
			loadApplicantList();
		</script>
	</footer>

</body>
</html>

