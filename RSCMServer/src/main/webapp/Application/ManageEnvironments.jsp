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
			<a href="ManageRoles.jsp" class="w3-bar-item marginRight w3-button 
			<%if (thisPageName.equals("ManageRoles")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%>	w3circle">Manage-Roles</a>
			
			<a href="ManageGroups.jsp" class="w3-bar-item marginRight w3-button 
			<%if (thisPageName.equals("ManageGroups")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%>	w3circle">Manage-Groups</a>
			
			<a href="ManageJobs.jsp" class="w3-bar-item marginRight w3-button 
			<%if (thisPageName.equals("ManageJobs")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%>	w3circle">Manage-Jobs</a>
			
			<a href="ManageTasks.jsp" class="w3-bar-item marginRight w3-button 
			<%if (thisPageName.equals("ManageTasks")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%>	w3circle">Manage-Tasks</a>
			
			<a href="ManageEnvironments.jsp" class="w3-bar-item marginRight w3-button 
			<%if (thisPageName.equals("ManageEnvironments")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%>	w3circle">Manage-Environments</a>	
			
			<a href="ManageScripts.jsp" class="w3-bar-item marginRight w3-button 
			<%if (thisPageName.equals("ManageScripts")) {
				out.println("w3-orange");
			} else {
				out.println("w3-teal");
			}%>	w3circle">Manage-Scripts</a>	
			
			<div w3-include-html="../WebressourcenImport/NavBarGeneral.html"></div>
		</div>
	</div>




	
	<div w3-include-html="../WebressourcenImport/header.html"></div>

	
	<!-- Page content -->
	<div class="w3-content content">
	
				
	
		<div class="w3-container w3-padding-64" id="maincontent">
			<!-- Menu Section -->
		<div class="w3-row" id="menu">
			<div class="w3-col l6 w3-padding-large">
				<button class="collapsible w3-button w3-teal" id="collapsibleButtonEnv">add Environment</button>
			<div class="collapsiblecontent">
				<form id="addEnvironmentform" name="addEnvironmentform" method="POST"
					onsubmit="sendAddEnvironmentData();return false">
					<table border="0">
						<tr>
							<td>ip-address (single or begin range)</td>
							<td><input id="f1" type="text" name="ipRangeBegin" required /></td>
						</tr>
						<tr>
							<td>ip-address range-end (optional)</td>
							<td><input id="f2" type="text" name="ipEangeEnd" required /></td>
						</tr>
						<tr>
							<td>Environment Description:</td>
							<td><input id="f3" type="text" name="environmentDescription"
								required /></td>
						</tr>
						<tr>
							<td>Submit:</td>
							<td><input type='submit' value='submit'></td>
						</tr>
					</table>

				</form>

			</div>
			</div>
			<div class="w3-col l6 w3-padding-large">
				<button class="collapsible w3-button w3-teal" id="collapsibleButton">add Environmentthreat</button>
			<div class="collapsiblecontent">
				<form id="addEnvironmentthreatform" name="addEnvironmentthreatform" method="POST"
					onsubmit="sendAddEnvironmentthreatData();return false">
					<table border="0">
						<tr>
							<td>Threat Title</td>
							<td><input id="f1" type="text" name="threatTitle" required /></td>
						</tr>
						<tr>
							<td>Threat Description</td>
							<td><input id="f2" type="text" name="threatDescription" required /></td>
						</tr>
						<tr>
							<td>Expected Problem</td>
							<td><input id="f3" type="text" name="expectedProblem" /></td>
						</tr>
						<tr>
							<td>Threat Level</td>
							<td><input id="f4" type="number" name="threatLevel" /></td>
						</tr>
						<tr>
							<td>Submit:</td>
							<td><input type='submit' value='submit'></td>
						</tr>
					</table>

				</form>

			</div>
			</div>
		</div>
		
			
			<div id="response"></div>
		</div>

		<!-- Menu Section -->
		<div class="w3-row" id="menu">
			<div class="w3-col l6 w3-padding-large">
				<h2 class="w3-center">Environments</h2>
				
				<form id="updateEnvironment" name="updateEnvironment" method="POST"	onsubmit="return false">
				<table class='w3-table w3-striped w3-hoverable'>
					<thead>
					<td>Env.ID</td>
					<td><input type="text" id="searchEnvironment" name="searchEnvironment" placeholder="Search by IP"/><button onclick="loadEnvironmentList();">Search</button></td>
					
					<td>Threat Description</td>
					<td>Expected dProblem</td>
					<td>Threat Level</td>
					
					</thead>
					<tbody id="environmentListContent">
					
					</tbody>
				</table>
				</form>
				<br>
			</div>
			<div class="w3-col l6 w3-padding-large">
				<h2 class="w3-center">Assign Environmentthreats</h2>
				
				<form id="updateEnvironmentthreat" name="updateEnvironmentthreat" method="POST"	onsubmit="updateApplicantEnvironmentthreat();return false">
				<table class='w3-table w3-striped w3-hoverable'>
					<thead>
					<td>ThreatID</td>
					<td><input type="text" id="searchEnvironmentthreat" name="searchEnvironmentthreat" placeholder="Search by Title"/><button onclick="loadEnvironmentthreatList();">Search</button></td>
					<td>Threat Description</td>
					<td>Expected Problem</td>
					<td>Threat Level</td>
					
					</thead>
					<tbody id="environmentthreatListContent">
					
					</tbody>
				</table>
				</form>
				<br>
			</div>
		</div>
	</div>
	<!-- Footer -->
	<footer class="w3-center w3-content content w3-padding-16">
		<div w3-include-html="../WebressourcenImport/footer.html"></div>
		<script>
			includeHTML();
			
			function loadEnvironmentList() {
				
				var environmentIdParameter ="";
				var url = new URL(window.location.href);
				if(window.location.href.indexOf('?environmentId=') != -1){
					environmentIdParameter=url.searchParams.get("environmentId");
				}else if(window.location.href.indexOf('&environmentId=') != -1){
					environmentIdParameter=url.searchParams.get("environmentId");
				}else{
					environmentIdParameter=-1;
				}
				
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/Application/getEnvironmentList");
				xhr.onload = function() {
					var responsArray = JSON.parse(xhr.responseText);
					var contentString = "";
					var onclickURL = location.protocol + '//' + location.host + location.pathname;
					for ( var i in responsArray) {
						if(environmentIdParameter == responsArray[i]['environmentId']){
							contentString += "<tr style='background-color: #bde9ba;' >";
						}else{
							contentString += "<tr >";
						}
						contentString += "<td onclick=\"window.location='" + onclickURL + "?"+"environmentId="+responsArray[i]['environmentId']+"'\">" + responsArray[i]['environmentId']	+ "</td>";
						contentString += "<td onclick=\"window.location='" + onclickURL + "?"+"environmentId="+responsArray[i]['environmentId']+"'\">" + responsArray[i]['ipRangeBegin']+ "</td>";
						contentString += "<td onclick=\"window.location='" + onclickURL + "?"+"environmentId="+responsArray[i]['environmentId']+"'\">" + responsArray[i]['ipEangeEnd']+ "</td>";
						contentString += "<td onclick=\"window.location='" + onclickURL + "?"+"environmentId="+responsArray[i]['environmentId']+"'\">" + responsArray[i]['environmentDescription'] + "</td>";
						contentString += "<td id='collEnvDelete"+i+"'><button onclick=\"showConfirmationEnvDelete('collEnvDelete"+i+"','"+responsArray[i]['environmentId']+"')\">delete</button></td>";
						contentString += "</tr>";
					}
					
					 document.getElementById("environmentListContent").innerHTML =contentString;
				}

				var data = new FormData();
				data.append("searchString", document.getElementById("searchEnvironment").value);
				xhr.send(data);

			}
			function loadEnvironmentthreatList() {
				var environmentIdParameter ="";
				var url = new URL(window.location.href);
				if(window.location.href.indexOf('?environmentId=') != -1){
					environmentIdParameter=url.searchParams.get("environmentId");
				}else if(window.location.href.indexOf('&environmentId=') != -1){
					environmentIdParameter=url.searchParams.get("environmentId");
				}else{
					environmentIdParameter=-1;
				}
				
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/Application/getEnvironmentthreatList");
				xhr.onload = function() {
					var responsArray = JSON.parse(xhr.responseText);
					var contentString = "";
					for ( var i in responsArray) {
						contentString += "<tr>";
						contentString += "<td>" + responsArray[i]['environmentthreatId']+ "</td>";
						contentString += "<td>" + responsArray[i]['threatTitle']	+ "</td>";
						contentString += "<td>" + responsArray[i]['threatDescription'] + "</td>";
						contentString += "<td>" + responsArray[i]['expectedProblem'] + "</td>";
						contentString += "<td>" + responsArray[i]['threatLevel'] + "</td>";
						if(environmentIdParameter>=0){
							if(responsArray[i]['isAssignetTo']==='yes'){
								contentString += "<td><input type='checkbox' name='assignToEnvironmentthreat' value='"+responsArray[i]['environmentthreatId']+"' checked></td>";
							}else{
								contentString += "<td><input type='checkbox' name='assignToEnvironmentthreat' value='"+responsArray[i]['environmentthreatId']+"'></td>";
							}
							
						}
						contentString += "<td id='collDelete"+i+"'><button onclick=\"showConfirmationThrDelete('collDelete"+i+"','"+responsArray[i]['environmentthreatId']+"')\">delete</button></td>";
						contentString += "</tr>";
					}
					if(environmentIdParameter>=0){
						contentString += "<tr><td/><td/><td/><td><input type='submit' value='submit'></td></tr>";
					}
					 document.getElementById("environmentthreatListContent").innerHTML =contentString;
				}

				var data = new FormData();
				data.append("searchString", document.getElementById("searchEnvironmentthreat").value);
				data.append("environmentIdParameter", environmentIdParameter);
				xhr.send(data);

			}
			loadEnvironmentthreatList();
			loadEnvironmentList();
			
			function showConfirmationThrDelete(htmlID, environmentthreatId) {
				var newCellContent = "should this environmentthreat really be deleted? <br>";
				newCellContent += "<input type='button' value='Yes' onclick=\"deleteEnvironmentthreat('"+environmentthreatId+"');\"><input type='button' value='No' onclick=\"loadEnvironmentthreatList();\">";
				
				document.getElementById(htmlID).innerHTML = newCellContent;
			}
			

			function deleteEnvironmentthreat(environmentthreatId) {
				var formData = new FormData();
				formData.append("environmentthreatId", environmentthreatId);
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/Application/deleteEnvironmentthreat");
				xhr.onload = function() {
					if (xhr.status == 200) {
						document.getElementById("response").innerHTML = xhr.responseText;
						loadEnvironmentthreatList()
					} else {
						document.getElementById("response").innerHTML = "some error happend"
					}
				}

				xhr.send(formData);
			}
			
			function forceDeleteEnvironmentthreat(environmentthreatId) {
				var formData = new FormData();
				formData.append("environmentthreatId", environmentthreatId);
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/Application/forceDeleteEnvironmentthreat");
				xhr.onload = function() {
					if (xhr.status == 200) {
						document.getElementById("response").innerHTML = xhr.responseText;
						loadEnvironmentthreatList()
					} else {
						console.log(xhr.responseText);
						document.getElementById("response").innerHTML = "some error happend "
					}
				}

				xhr.send(formData);
			}
			
			function updateApplicantEnvironmentthreat(){
				//https://www.aspsnippets.com/Articles/Get-multiple-selected-checked-CheckBox-values-in-Array-using-JavaScript.aspx
		        var selected = new Array();
		        var updateEnvironmentthreatForm = document.getElementById("updateEnvironmentthreat");
		        var chks = updateEnvironmentthreatForm.getElementsByTagName("INPUT");
		        for (var i = 0; i < chks.length; i++) {
		            if (chks[i].checked) {
		                selected.push(chks[i].value);
		            }
		        }
		        var formData = new FormData();
				formData.append("environmentthreatIds", selected);
				
				
				var environmentIdParameter ="";
				var url = new URL(window.location.href);
				if(window.location.href.indexOf('?environmentId=') != -1){
					environmentIdParameter=url.searchParams.get("environmentId");
				}else if(window.location.href.indexOf('&environmentId=') != -1){
					environmentIdParameter=url.searchParams.get("environmentId");
				}else{
					environmentIdParameter=-1;
				}
				
				
				
				formData.append("environmentId", environmentIdParameter);
				
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/Application/updateAssignedToEnvironmentthreat");
				xhr.onload = function() {
					if (xhr.status == 200) {
						document.getElementById("response").innerHTML = xhr.responseText;
						loadEnvironmentthreatList()
					} else {
						document.getElementById("response").innerHTML = "some error happend"
					}
				}

				xhr.send(formData);

			}
			
			function sendAddEnvironmentthreatData() {
				var formData = new FormData();
				//https://stackoverflow.com/questions/19978600/how-to-loop-through-elements-of-forms-with-javascript
				var elements = document.getElementById("addEnvironmentthreatform").elements;
				for (var i = 0, element; element = elements[i++];) {
					if (element.type != "submit") {
						formData.append("formVars", element.name);
						formData.append("formVars", element.value);
					}
				}

				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/Application/pushEnvironmentthreat");
				xhr.onload = function() {
					if (xhr.status == 200) {
						document.getElementById("response").innerHTML = xhr.responseText;
						loadEnvironmentthreatList()
						document.getElementById("collapsibleButton").click();
						document.getElementById("addEnvironmentthreatform").reset();
					} else {
						document.getElementById("response").innerHTML = "some error happend"
					}
				}

				xhr.send(formData);

			}

			
			function showConfirmationEnvDelete(htmlID, environmentId) {
				var newCellContent = "should this environment really be deleted? <br>";
				newCellContent += "<input type='button' value='Yes' onclick=\"deleteEnvironment('"+environmentId+"');\"><input type='button' value='No' onclick=\"loadEnvironmentList();\">";
				
				document.getElementById(htmlID).innerHTML = newCellContent;
			}
			
			function deleteEnvironment(environmentId) {
				var formData = new FormData();
				formData.append("environmentId", environmentId);
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/Application/deleteEnvironment");
				xhr.onload = function() {
					if (xhr.status == 200) {
						document.getElementById("response").innerHTML = xhr.responseText;
						loadEnvironmentList()
					} else {
						document.getElementById("response").innerHTML = "some error happend"
					}
				}

				xhr.send(formData);
			}
			

			function forceDeleteEnvironment(environmentId) {
				var formData = new FormData();
				formData.append("environmentId", environmentId);
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/Application/forceDeleteEnvironment");
				xhr.onload = function() {
					if (xhr.status == 200) {
						document.getElementById("response").innerHTML = xhr.responseText;
						loadEnvironmentList()
					} else {
						console.log(xhr.responseText);
						document.getElementById("response").innerHTML = "some error happend "
					}
				}

				xhr.send(formData);
			}
			
			function sendAddEnvironmentData() {
				var formData = new FormData();
				//https://stackoverflow.com/questions/19978600/how-to-loop-through-elements-of-forms-with-javascript
				var elements = document.getElementById("addEnvironmentform").elements;
				for (var i = 0, element; element = elements[i++];) {
					if (element.type != "submit") {
						formData.append("formVars", element.name);
						formData.append("formVars", element.value);
					}
				}

				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/Application/pushEnvironment");
				xhr.onload = function() {
					if (xhr.status == 200) {
						document.getElementById("response").innerHTML = xhr.responseText;
						loadEnvironmentList()
						document.getElementById("collapsibleButtonEnv").click();
						document.getElementById("addEnvironmentform").reset();
					} else {
						document.getElementById("response").innerHTML = "some error happend"
					}
				}

				xhr.send(formData);

			}

			//https://www.w3schools.com/howto/howto_js_collapsible.asp
			var coll = document.getElementsByClassName("collapsible");
			var i;

			for (i = 0; i < coll.length; i++) {
				coll[i].addEventListener("click", function() {
					this.classList.toggle("active");
					var content = this.nextElementSibling;
					if (content.style.maxHeight) {
						content.style.maxHeight = null;
					} else {
						content.style.maxHeight = content.scrollHeight + "px";
					}
				});
			}
		</script>
	</footer>

</body>
</html>

