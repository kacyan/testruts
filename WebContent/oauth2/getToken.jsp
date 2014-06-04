<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%--
getToken.jsp
2012/05/30 Kac
--%>
<bean:parameter name="code" id="code" value=""/>
<bean:parameter name="state" id="state" value=""/>
<bean:parameter name="error" id="error" value=""/>
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
<form action="https://accounts.google.com/o/oauth2/token" method="post">
<table border="1" width="100%">
 <tr>
  <th>state</th>
  <td><bean:write name="state"/></td>
 </tr>
 <logic:notEmpty name="error">
 <tr>
  <th>error</th>
  <td><bean:write name="error"/></td>
 </tr>
 </logic:notEmpty>
 <tr>
  <th>code</th>
  <td><input type="text" name="code" value="<bean:write name="code"/>"/></td>
 </tr>
 <tr>
  <th>client_id</th>
  <td><input type="text" name="client_id" value="494593688356-pllemu8erp8a82p9cn8qhfhjvplo90fd.apps.googleusercontent.com"/></td>
 </tr>
 <tr>
  <th>client_secret</th>
  <td><input type="text" name="client_secret" value="ubiPnDb_43KliRI7bFiZt-Uh"/></td>
 </tr>
 <tr>
  <th>redirect_uri</th>
  <td><input type="text" name="redirect_uri" value="http://ticket.os.ksi.co.jp/testruts/oauth2/getToken.jsp"/></td>
 </tr>
 <tr>
  <th>grant_type</th>
  <td><input type="text" name="grant_type" value="authorization_code"/></td>
 </tr>
</table>
<input type="submit"/>
</form>
<hr/>
<jsp:include page="/check.jsp"/>
</body>
</html>
