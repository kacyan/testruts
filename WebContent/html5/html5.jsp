<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- update:2015/03/12 -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
<h2>HTML5 Test</h2>
<%=request.getRequestURI() %><br/>
<%=request.getHeader( "user-agent" ) %>
<hr/>

<form action="../check.jsp" method="post">
<table>
 <tr>
  <th>&lt;input type="search"/&gt;</th>
  <td><input type="search" name="search"/></td>
 </tr>
 <tr>
  <th>&lt;input type="url"/&gt;</th>
  <td><input type="url" name="url"/></td>
 </tr>
 <tr>
  <th>&lt;input type="email"/&gt;</th>
  <td><input type="email" name="email"/></td>
 </tr>
 <tr>
  <th>&lt;input type="date"/&gt;</th>
  <td><input type="date" name="date"/></td>
 </tr>
 <tr>
  <th>&lt;input type="number"/&gt;</th>
  <td><input type="number" name="number"/></td>
 </tr>
 <tr>
  <th>&lt;input type="range"/&gt;</th>
  <td><input type="range" name="range"/></td>
 </tr>
 <tr>
  <th>&lt;input type="color"/&gt;</th>
  <td><input type="color" name="color"/></td>
 </tr>
</table>
<input type="submit">
</form>

<hr/>
</body>
</html>
