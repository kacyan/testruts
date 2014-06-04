<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<%@ page isErrorPage="true" %>
<%@ page session="true" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.net.URL"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Iterator"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%--
ParamCharTest.jsp
2012/01/16 Kac
--%>

<%@page import="JP.co.ksi.util.CharCodeChecker"%><html>
<head>
<style type="text/css">
<!--
table
{
}
th
{
	background: #00a8a9;
    color: white;
	border-color: #a0a0a0;
	border-style: solid;
	border-width: 1px;
}
td
{
    background: white;
	border-color: #a0a0a0;
	border-style: solid;
	border-width: 1px;
}
 -->
</style>
</head>

<body>

<form action="" method="post">
<input type="text" name="text1" value=""/><br/>
<textarea rows="5" cols="5" name="area1">&yen;</textarea><br/>
<input type="submit"/>
</form>
<hr/>

<table>
 <tr>
  <th>Name</th>
  <th>Value</th>
  <th>unicode</th>
 </tr>
<%
Enumeration	enumeration= request.getParameterNames();
while( enumeration.hasMoreElements() )
{
	String	name= (String)enumeration.nextElement();
	String[]	values= request.getParameterValues( name );
	for( int i= 0; i < values.length; i++ )
	{
%> <tr>
<% if( i == 0 ){ %>
  <td rowspan="<%=values.length %>"><ksi:filter><%=name %></ksi:filter></td>
<% } %>
  <td><ksi:filter><%=values[i] %></ksi:filter></td>
  <td><%=CharCodeChecker.toUnicode(values[i]) %></td>
 </tr>
<%
	}
}
%>
</table>

<hr/>

<a href="javascript:history.back()">Back</a>

</body>
</html>
