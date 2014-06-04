<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="JP.co.ksi.ldap.LdifProperties"%>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- oauth2/oauth.jsp -->
<%-- 
oauth-2.0の習作
2014/02/17 Kac
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/default.css" />
<style type="text/css">
table.oauth2
{
	border: none;
}
table.oauth2 tr th
{
	text-align: right;
}
table.oauth2 tr td
{
	width: 100%;
}
table.oauth2 tr td input
{
	width: 100%;
}
</style>
</head>
<body>
Step1.OAuth-2.0のトークンを要求します。
<hr/>
<form action="<bean:write name="oauthConfig" property="authURL"/>">
<table class="oauth2">
<logic:notEmpty name="oauthConfig" property="response_type">
 <tr>
  <th>response_type</th>
  <td><input type="text" name="response_type" value="<bean:write name="oauthConfig" property="response_type"/>"/></td>
 </tr>
</logic:notEmpty>
 <tr>
  <th>client_id</th>
  <td><input type="text" name="client_id" value="<bean:write name="oauthConfig" property="client_id"/>"/></td>
 </tr>
 <tr>
  <th>redirect_uri</th>
  <td><input type="text" name="redirect_uri" value="<bean:write name="oauthConfig" property="redirect_uri"/>"/></td>
 </tr>
 <tr>
  <th>scope</th>
  <td><input type="text" name="scope" value="<bean:write name="oauthConfig" property="scope"/>"/></td>
 </tr>
 <tr>
  <th>state</th>
  <td><input type="text" name="state" value="<bean:write name="oauthConfig" property="state"/>"/></td>
 </tr>
<logic:notEmpty name="oauthConfig" property="access_type">
 <tr>
  <th>access_type</th>
  <td><input type="text" name="access_type" value="offline"/></td>
 </tr>
</logic:notEmpty>
<logic:notEmpty name="oauthConfig" property="approval_prompt">
 <tr>
  <th>approval_prompt</th>
  <td><input type="text" name="approval_prompt" value="<bean:write name="oauthConfig" property="approval_prompt"/>"/></td>
 </tr>
</logic:notEmpty>
</table>
<input type="submit"/>
</form>
<hr/>
<jsp:include page="/session.jsp"/>
</body>
</html>
