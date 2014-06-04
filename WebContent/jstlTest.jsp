<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- jstlTest.jsp -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>

<hr>
log4j.rootLogger=<c:out value="${appConfig['log4j.rootLogger']}"/><br/>
<hr>
<%=application.getAttribute("appConfig") %><br/>
<hr>
<%
String	hoge= "ほげ";
request.setAttribute( "hoge", hoge );
%>
<c:out value="${hoge}"/><br/>

</body>
</html>
