<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<!-- RequestDispatcherTest.jsp -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
<h1>RequestDispatcherTest.jsp</h1>

<hr>
<jsp:include page="/check.jsp" flush="true"/>
<%--
request.getRequestDispatcher("/check.jsp").include( request, response );
--%>
<hr>

</body>
</html>
