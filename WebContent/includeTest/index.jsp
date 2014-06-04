<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%@page import="java.io.File"%>
<%@page import="java.util.Arrays"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
<h1>jsp:include TEST</h1>
<hr/>
redirectをincludeする
<jsp:include page="redirect.jsp" flush="true"/>
<hr/>
includeをincludeする
<jsp:include page="include.jsp" flush="true"/>
<hr/>

</body>
</html>
