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
	var coll = document.getElementsByClassName("collapsible");
	var i;

	for (i = 0; i < coll.length; i++) {
		coll[i].addEventListener("click", function() {
			this.classList.toggle("active");
			var content = this.nextElementSibling;
			if (content.style.display === "block") {
				content.style.display = "none";
			} else {
				content.style.display = "block";
			}
		});
	}
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
				
			<div w3-include-html="../WebressourcenImport/NavBarGeneral.html"></div>
		</div>
	</div>





	<div w3-include-html="../WebressourcenImport/header.html"></div>

	<!-- Page content -->
	<div class="w3-content content">
		<div class="w3-container w3-padding-64" id="maincontent">
			<button class="collapsible w3-button w3-teal" id="collapsibleButton">add Group</button>
			<div class="collapsiblecontent">
				<form id="addGroupform" name="addGroupform" method="POST"
					onsubmit="sendAddGroupData();return false">
					<table border="0">
						<tr>
							<td>Group Name:</td>
							<td><input id="f1" type="text" name="groupName" required /></td>
						</tr>
						<tr>
							<td>Group Description:</td>
							<td><input id="f2" type="text" name="groupDescription"
								required /></td>
						</tr>
						<tr>
							<td>Submit:</td>
							<td><input type='submit' value='submit'></td>
						</tr>
					</table>

				</form>

			</div>
			<div id="response"></div>
		</div>

		<!-- Menu Section -->
		<div class="w3-row" id="menu">
			<div class="w3-col l6 w3-padding-large">
				<h2 class="w3-center">
					ClientInformation
					</h2>
				<table class='w3-table w3-striped w3-hoverable'>
					<thead>
					<td>ApplicantID</td>
					<td><input type="text" id="searchApplicant" name="searchApplicant" placeholder="Search by Name"/><button onclick="loadApplicantList();">Search</button></td>
					<td>LastName</td>
					<td>FirstName</td>
					<td>E-Mail</td>
					
					</thead>
					<tbody id="applicantListContent">

					</tbody>
				</table>	
			</div>
			<div class="w3-col l6 w3-padding-large">
				<h2 class="w3-center">Assign Groups</h2>
				
				<form id="updateGroup" name="updateGroup" method="POST"	onsubmit="updateApplicantGroup();return false">
				<table class='w3-table w3-striped w3-hoverable'>
					<thead>
					<td>GroupID</td>
					<td><input type="text" id="searchGroup" name="searchGroup" placeholder="Search by Name"/><button onclick="loadGroupList();">Search</button></td>
					<td>GroupDescription</td>
					
					</thead>
					<tbody id="groupListContent">
					
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

			function loadApplicantList() {
				var applicantIdParameter ="";
				var url = new URL(window.location.href);
				if(window.location.href.indexOf('?applicantId=') != -1){
					applicantIdParameter=url.searchParams.get("applicantId");
				}else if(window.location.href.indexOf('&applicantId=') != -1){
					applicantIdParameter=url.searchParams.get("applicantId");
				}else{
					applicantIdParameter=-1;
				}
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/Application/getApplicantList");
				xhr.onload = function() {
					var responsArray = JSON.parse(xhr.responseText);
					var contentString = "";
					var onclickURL = location.protocol + '//' + location.host + location.pathname;
					for ( var i in responsArray) {
						if(applicantIdParameter == responsArray[i]['applicantId']){
							contentString += "<tr style='background-color: #bde9ba;' onclick=\"window.location='" + onclickURL + "?"+"applicantId="+responsArray[i]['applicantId']+"'\">";
						}else{
							contentString += "<tr onclick=\"window.location='" + onclickURL + "?"+"applicantId="+responsArray[i]['applicantId']+"'\">";
						}
						
						contentString += "<td>" + responsArray[i]['applicantId']	+ "</td>";
						contentString += "<td>" + responsArray[i]['applicantName']	+ "</td>";
						contentString += "<td>" + responsArray[i]['applicantFirstname']	+ "</td>";
						contentString += "<td>" + responsArray[i]['applicantLastname']	+ "</td>";
						contentString += "<td>" + responsArray[i]['applicantEmail']	+ "</td>";
						contentString += "</tr>";
						
					}
					 document.getElementById("applicantListContent").innerHTML =contentString;
					 loadGroupList();
				}

				var data = new FormData();
				data.append("searchString", document.getElementById("searchApplicant").value);
				xhr.send(data);

			}
			
			function loadGroupList() {
				var applicantIdParameter ="";
				var url = new URL(window.location.href);
				if(window.location.href.indexOf('?applicantId=') != -1){
					applicantIdParameter=url.searchParams.get("applicantId");
				}else if(window.location.href.indexOf('&applicantId=') != -1){
					applicantIdParameter=url.searchParams.get("applicantId");
				}else{
					applicantIdParameter=-1;
				}
				
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/Application/getGroupList");
				xhr.onload = function() {
					var responsArray = JSON.parse(xhr.responseText);
					var contentString = "";
					for ( var i in responsArray) {
						contentString += "<tr>";
						contentString += "<td>" + responsArray[i]['groupId']
								+ "</td>";
						contentString += "<td>" + responsArray[i]['groupName']
								+ "</td>";
						contentString += "<td>" + responsArray[i]['groupDescription'] + "</td>";
						if(applicantIdParameter>=0){
							if(responsArray[i]['isAssignetTo']==='yes'){
								contentString += "<td><input type='checkbox' name='assignToGroup' value='"+responsArray[i]['groupId']+"' checked></td>";
							}else{
								contentString += "<td><input type='checkbox' name='assignToGroup' value='"+responsArray[i]['groupId']+"'></td>";
							}
							
						}
						contentString += "<td id='collDelete"+i+"'><button onclick=\"showConfirmationDelete('collDelete"+i+"','"+responsArray[i]['groupId']+"')\">delete</button></td>";
						contentString += "</tr>";
					}
					if(applicantIdParameter>=0){
						contentString += "<tr><td/><td/><td/><td><input type='submit' value='submit'></td></tr>";
					}
					 document.getElementById("groupListContent").innerHTML =contentString;
				}

				var data = new FormData();
				data.append("searchString", document.getElementById("searchGroup").value);
				data.append("applicantIdParameter", applicantIdParameter);
				xhr.send(data);

			}
			loadApplicantList();
			loadGroupList();
			
			function showConfirmationDelete(htmlID, groupId) {
				var newCellContent = "should this group really be deleted? <br>";
				newCellContent += "<input type='button' value='Yes' onclick=\"deleteGroup('"+groupId+"');\"><input type='button' value='No' onclick=\"loadGroupList();\">";
				
				document.getElementById(htmlID).innerHTML = newCellContent;
			}
			
			function updateApplicantGroup(){
				//https://www.aspsnippets.com/Articles/Get-multiple-selected-checked-CheckBox-values-in-Array-using-JavaScript.aspx
		        var selected = new Array();
		        var updateGroupForm = document.getElementById("updateGroup");
		        var chks = updateGroupForm.getElementsByTagName("INPUT");
		        for (var i = 0; i < chks.length; i++) {
		            if (chks[i].checked) {
		                selected.push(chks[i].value);
		            }
		        }
		        var formData = new FormData();
				formData.append("groupIds", selected);
				
				
				var applicantIdParameter ="";
				var url = new URL(window.location.href);
				if(window.location.href.indexOf('?applicantId=') != -1){
					applicantIdParameter=url.searchParams.get("applicantId");
				}else if(window.location.href.indexOf('&applicantId=') != -1){
					applicantIdParameter=url.searchParams.get("applicantId");
				}else{
					applicantIdParameter=-1;
				}
				
				
				formData.append("applicantId", applicantIdParameter);
				
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/Application/updateAssignedToGroup");
				xhr.onload = function() {
					if (xhr.status == 200) {
						document.getElementById("response").innerHTML = xhr.responseText;
						loadGroupList()
					} else {
						document.getElementById("response").innerHTML = "some error happend"
					}
				}

				xhr.send(formData);

			}
			
			
			function deleteGroup(groupId) {
				var formData = new FormData();
				formData.append("groupId", groupId);
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/Application/deleteGroup");
				xhr.onload = function() {
					if (xhr.status == 200) {
						document.getElementById("response").innerHTML = xhr.responseText;
						loadGroupList()
					} else {
						document.getElementById("response").innerHTML = "some error happend"
					}
				}

				xhr.send(formData);
			}
			
			function sendAddGroupData() {
				var formData = new FormData();
				//https://stackoverflow.com/questions/19978600/how-to-loop-through-elements-of-forms-with-javascript
				var elements = document.getElementById("addGroupform").elements;
				for (var i = 0, element; element = elements[i++];) {
					if (element.type != "submit") {
						formData.append("formVars", element.name);
						formData.append("formVars", element.value);
					}
				}

				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/Application/pushGroup");
				xhr.onload = function() {
					if (xhr.status == 200) {
						document.getElementById("response").innerHTML = xhr.responseText;
						loadGroupList()
						document.getElementById("collapsibleButton").click();
						document.getElementById("addGroupform").reset();
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

