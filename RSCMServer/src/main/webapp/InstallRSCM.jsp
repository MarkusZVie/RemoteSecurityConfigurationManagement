<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.springframework.security.core.authority.SimpleGrantedAuthority"%>
<%@page import="java.util.Collection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html>
<html>
<body>
<table><tr><td>
 <security:authorize access="hasRole('ROLE_Administrator','ROLE_Technician')">
This zone will be visible to Supervisor only.<br/>
You have Supervisor role.<br/>



</security:authorize>
 </td></tr>
 <tr><td>
 <a href="j_spring_security_logout">logout </a>
 </td></tr>
 </table>
 
 
 </body>
 </html> 