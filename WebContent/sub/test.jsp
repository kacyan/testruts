<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page autoFlush="true"  isErrorPage="true" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<!-- sub/test.jsp -->
<html>
<head>
<style type="text/css">
table.test{
	font-size: 80%;
}
table.test tr th{
	border: 1px solid;
}
table.test tr td{
	border: 1px solid;
}
.bar{
	background-color:transparent;
}
</style>
</head>
<body>
<h2>テストページ</h2>

<hr/>

<h3>test1</h3>
<html:form action="/test1">
<html:text property="result"/>
<html:submit/>
</html:form>
<%
String	switchActionURL= "switchAction.do?prefix=/&page=/check.jsp";
String	subURL= request.getContextPath() +"/check.jsp";
%>
<dl>
<dd><a href="<%=switchActionURL %>"><ksi:filter><%=switchActionURL %></ksi:filter></a>
<dd><a href="<%=subURL %>"><ksi:filter><%=subURL %></ksi:filter></a>
</dl>

<hr/>

<h3>test2</h3>
<html:form action="/test2">
<table>
 <tr>
  <th>result</th>
  <td><html:text property="result"/></td>
 </tr>
 <tr>
  <th><bean:message key="label.param1"/></th>
  <td><html:text property="param1"/></td>
 </tr>
 <tr>
  <th><bean:message key="label.param2"/></th>
  <td><html:text property="param2"/></td>
 </tr>
 <tr>
  <th><bean:message key="label.param3"/></th>
  <td><html:text property="param3"/></td>
 </tr>
</table>
<html:submit/>
</html:form>
<html:errors/>

<hr/>

</body>
</html>