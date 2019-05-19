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
		//Get parameter if some exists
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
			<div
				w3-include-html="../WebressourcenImport/NavBarGeneral.html"></div>

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
		<script>
			includeHTML();
			
			//Entire Script form : https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/

			'use strict';

			var multipleUploadForm = document.querySelector('#multipleUploadForm');
			var multipleFileUploadInput = document
					.querySelector('#multipleFileUploadInput');
			var multipleFileUploadError = document
					.querySelector('#multipleFileUploadError');
			var multipleFileUploadSuccess = document
					.querySelector('#multipleFileUploadSuccess');
			var searchName = document.querySelector('#searchName');

			function getFileList(searchString) {
				
				var searchString = document.getElementById("searchName").value;
				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/getFileList");
				xhr.onload = function() {
					console.log(xhr.responseText);
					var response = JSON.parse(xhr.responseText);
					if (xhr.status == 200) {
						var tableBuild = "";

						for (var i = 0; i < response.length; i++) {
							tableBuild += "<tr>"
							tableBuild += "<td id='Col1Cell"+i+"'>" + response[i].filename.replace(new RegExp(searchString, 'g'), "<span class='highlight'>"+searchString+"</span>"); + "</td>"
							tableBuild += "<td>" + response[i].size + " Byte</td>"
							tableBuild += "<td>" + response[i].creationDate + "</td>"
							tableBuild += "<td><input type='button' value='display' onclick=\"displayFile('"+response[i].filename+"')\"></td>";
							tableBuild += "<td id='Col2Cell"+i+"'><input type='button' value='delete' onclick=\"showConfirmationDelete('Col2Cell"+i+"','"+ response[i].filename +"')\"></td>"
							tableBuild += "</tr>"
						}

						document.getElementById("tableContent").innerHTML = tableBuild;
						
						
					} else {
						alter("asdasdklj");
						multipleFileUploadSuccess.style.display = "none";
						multipleFileUploadError.innerHTML = (response && response.message)
								|| "Some Error Occurred";
					}
				}
				var formData = new FormData();
				formData.append("search", searchString);
				xhr.send(formData);
				
			}

			function showConfirmationDelete(htmlID, filename) {
				var newCellContent = "should this file really be deleted? <br>";
				newCellContent += "<input type='button' value='Yes' onclick=\"deleteFile('"+filename+"');\"><input type='button' value='No' onclick=\"getFileList();\">";
				
				document.getElementById(htmlID).innerHTML = newCellContent;
			}

			function deleteFile(filename) {
				//https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/send
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '/deleteFile', true);
				
				xhr.onload = function () {
					getFileList();
					multipleFileUploadSuccess.innerHTML = "the file " + filename +" was deleted successfully";
					
				};
				var formData = new FormData();
				formData.append("fileName", filename);
				xhr.send(formData);
				// xhr.send('string');
				// xhr.send(new Blob());
				// xhr.send(new Int8Array());
				// xhr.send(document);
			}

			function displayFile(filename) {
				var onclickURL = location.protocol + '//' + location.host + location.pathname; //GetURL
				window.location="ScriptDisplay.jsp?filename=" +filename+"&returnURL="+onclickURL;
			}


			function uploadMultipleFiles(files) {
				var formData = new FormData();
				for (var index = 0; index < files.length; index++) {
					formData.append("files", files[index]);
				}

				var xhr = new XMLHttpRequest();
				xhr.open("POST", "/uploadMultipleFiles");

				xhr.onload = function() {
					console.log(xhr.responseText);
					var response = JSON.parse(xhr.responseText);
					if (xhr.status == 200) {
						
						multipleFileUploadError.style.display = "none";
						var content = "<p>All Files Uploaded Successfully</p>";
						content += "<ul>";
						for (var i = 0; i < response.length; i++) {
							if(response[i].size < 0){
								//when file already exists
								content += "<li>Upload " + i + " The File: "+response[i].fileName+" Already exists and the upload of this file is canceled</li>";
							}else{
								content += "<li>Upload " + i + " : <a href='"
								+ response[i].fileDownloadUri + "' target='_blank'>"
								+ response[i].fileName + "</a></li>";
							}
							
							
						}
						content += "</ul>";
						multipleFileUploadSuccess.innerHTML = content;
						multipleFileUploadSuccess.style.display = "block";
					} else {
						multipleFileUploadSuccess.style.display = "none";
						multipleFileUploadError.innerHTML = (response && response.message)
								|| "Some Error Occurred";
					}
				}

				xhr.send(formData);
				getFileList("");
			}

			multipleUploadForm.addEventListener('submit', function(event) {
				var files = multipleFileUploadInput.files;
				if (files.length === 0) {
					multipleFileUploadError.innerHTML = "Please select at least one file";
					multipleFileUploadError.style.display = "block";
				}
				uploadMultipleFiles(files);
				event.preventDefault();
			}, true);



			var down = false;
			document.getElementById("searchName").addEventListener('keydown', function (){ // https://stackoverflow.com/questions/11991062/keyup-event-listener-fires-when-enter-is-pressed-on-chromes-ominbox
			    down = true;
			}, false);

			document.getElementById("searchName").addEventListener('keyup', function (){ // https://stackoverflow.com/questions/11991062/keyup-event-listener-fires-when-enter-is-pressed-on-chromes-ominbox
			    if(down === true){
			    	getFileList(document.getElementById("searchName").value);
			    }
			    else{
			        //ignor it there was no key down before
			    }
			    down = false;
			}, false);
			
			getFileList();
		</script>
	</footer>

</body>
</html>

