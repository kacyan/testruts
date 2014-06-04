<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="Shift_JIS" %>
<%@ page import="JP.co.ksi.eip.Auth"%>
<!-- portaletest.jsp -->
<%--
ポートレットのテスト用
 --%>
<jsp:useBean id="appConfig" scope="application" type="java.util.Properties"/>
<%
Auth	auth= Auth.getAuth( request, appConfig );
%>
<html>
<head>
<style type="text/css">
.part-frame
{
	background: #666699;
}
.part-header
{
	background: #666699;
	color:white;
	font-weight:bold;
}
.part-header a
{
	background: #666699;
	color:white;
	font-weight:bold;
}
.part-body
{
	background:white;
	color:black;
}
.new
{
	color: red;
	font-weight: bold;
}
.caution
{
	color: red;
	font-weight: bold;
}
</style>
</head>
<body>[<%=auth %>]
<% String portletURL= "/oauth2/calendarPortlet.do?calendarId=shamail.ksi@gmail.com"; %>
<jsp:include page="<%=portletURL %>" flush="true"/>
<%--
request.getRequestDispatcher("summaryBBSRest.do").include(request,response);
--%>
</body>
</html>