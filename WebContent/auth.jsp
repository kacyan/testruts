<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="JP.co.ksi.eip.Auth"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<jsp:useBean id="appConfig" scope="application" type="java.util.Properties"/>
<%
Auth	auth= Auth.getAuth( request, appConfig );
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
<%=auth %>
<hr>
remoteUser=<%=request.getRemoteUser() %><br/>
authorization=<%=request.getHeader( "authorization" ) %><br/>
<hr>
</body>
</html>
