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
			<script>document.write("<a href='"+returnURL+"' class='w3-bar-item marginRight w3-button w3-teal'>&#8592; Back to: "+backName+"</a>");</script>
			
			<div w3-include-html="../WebressourcenImport/NavBarGeneral.html"></div>
		</div>
	</div>





	<div w3-include-html="../WebressourcenImport/header.html"></div>

	
	

	<!-- Page content -->
	<div class="w3-content content " style="padding-top: 70px;">
	<div class="w3-row" id="menu">
		<div class="w3-col l6 w3-padding-large" id="leftPageContent">
			
		</div>
		<div class="w3-col l6 w3-padding-large" id="rightPageContent">
			
		</div>
	</div>
		<div class="w3-container " id="maincontent">
			<div id="response"></div>
			<table class='w3-table w3-striped w3-hoverable table'>
					<thead >
					<tr id="headLineContent" style="font-weight:bold" >
					<td style="border-bottom: 1px solid #000000;">SE Id</td>
					<td style="border-bottom: 1px solid #000000;">SE Assigneddate</td>
					<td style="border-bottom: 1px solid #000000;">SE Executiondate</td>
					<td style="border-bottom: 1px solid #000000;">Assignedby</td>
					<td style="border-bottom: 1px solid #000000;">A Id</td>
					<td style="border-bottom: 1px solid #000000;">A Name</td>
					<td style="border-bottom: 1px solid #000000;">A Firstname</td>
					<td style="border-bottom: 1px solid #000000;">A Lastname</td>
					<td style="border-bottom: 1px solid #000000;">A Email</td>
					<td style="border-bottom: 1px solid #000000;">A createdOn</td>
					<td style="border-bottom: 1px solid #000000;">Client ID</td>
					<td style="border-bottom: 1px solid #000000;">C Port</td>
					
					
						
					</tr>
					</thead>
					<tbody  id="clientList">
					
					</tbody>
			</table>	
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
				var json = JSON.parse(xhr.responseText); //parse Json
				var headline = "<h3>"
				var endHeadline ="</h3>"
				var beginText = "<p>"
				var endText = "</p>"
				var htmlContent ="";
				htmlContent += headline + "assigned by: (entityDetails)" +  endHeadline;
				htmlContent += beginText + json['entityDetails'] +  endText;
				
				document.getElementById("leftPageContent").innerHTML = htmlContent;
				htmlContent=""
				
				htmlContent += headline + "fileName" +  endHeadline;
				htmlContent += beginText + json['fileName'] +  endText;
				
				htmlContent += headline + "fileCreationDate" +  endHeadline;
				htmlContent += beginText + json['fileCreationDate'] +  endText;
				
				htmlContent += headline + "fileSize" +  endHeadline;
				htmlContent += beginText + json['fileSize'] + " Byte"+  endText;
				
				htmlContent += headline + "executionPercentageNumbers" +  endHeadline;
				htmlContent += beginText + json['executionPercentageNumbers'] +  endText;
				
				htmlContent += headline + "executionPercentage" +  endHeadline;
				htmlContent += beginText + json['executionPercentage'] +  endText;
				
				htmlContent += headline + "executionAssignDate" +  endHeadline;
				htmlContent += beginText + json['executionAssignDate'] +  endText;
				
				document.getElementById("rightPageContent").innerHTML = htmlContent;
				
				var tableContent ="";
				for ( var i in json['ClientArray']) {
					tableContent +="<tr>";
					tableContent += "<td>" + json['ClientArray'][i]['scriptexecutionId'] + "</td>";
					tableContent += "<td>" + json['ClientArray'][i]['scriptexecutionAssigneddate'] + "</td>";
					tableContent += "<td>" + json['ClientArray'][i]['scriptexecutionExecutiondate'] + "</td>";
					tableContent += "<td>" + json['ClientArray'][i]['Assignedby'] + "</td>";
					tableContent += "<td style=\"border-left: 1px solid #000000;\"  >" + json['ClientArray'][i]['userId'] + "</td>";
					tableContent += "<td>" + json['ClientArray'][i]['userName'] + "</td>";
					tableContent += "<td>" + json['ClientArray'][i]['userFirstname'] + "</td>";
					tableContent += "<td>" + json['ClientArray'][i]['userLastname'] + "</td>";
					tableContent += "<td>" + json['ClientArray'][i]['userEmail'] + "</td>";
					tableContent += "<td>" + json['ClientArray'][i]['createdOn'] + "</td>";
					tableContent += "<td style=\"border-left: 1px solid #000000;\" >" + json['ClientArray'][i]['rscmclientId'] + "</td>";
					tableContent += "<td>" + json['ClientArray'][i]['clientPort'] + "</td>";
					
					
					
					
					
					
					
					
					tableContent +="</tr>";
				}
				document.getElementById("clientList").innerHTML = tableContent;
				
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

