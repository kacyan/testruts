<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<%@ page import="JP.co.ksi.eip.Auth"%>
<%@ page import="JP.co.ksi.util.HtmlFilter"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%--
facebook.jsp
2014/02/17 Kac
--%>
<bean:define id="oauth" name="oauth2" scope="session"/>
<jsp:useBean id="appConfig" scope="application" type="java.util.Properties"/>
<%
Auth	auth= Auth.getAuth( request, appConfig );
%>
<html>
<head>
<style type="text/css">
input
{
	width: 100%;
}
</style>
</head>
<body>
<hr/>

https://graph.facebook.com/me/home?
<form action="https://graph.facebook.com/me/home?" method="post">
<table border="1" width="100%">
 <tr>
  <th>access_token</th>
  <td><input type="text" name="access_token" value="<bean:write name="oauth" property="access_token"/>"/></td>
 </tr>
</table>
<input type="submit"/>
</form>

<hr/>

</body>
</html>
