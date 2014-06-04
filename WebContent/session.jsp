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
<%--
session.jsp
2014/02/19 Kac@KSI
--%>
<%
String	submit= request.getParameter( "submit" );
if( submit == null )	submit= "";
if( submit.equals( "invalidate" ) )
{
	session.invalidate();
	session= request.getSession();
}
%>
<html>
<head>
<style type="text/css">
<!--
h1
{
	color: orange;
	margin-bottom: 0px;
	margin-top: 0px;
}
h2
{
	color: teal;
	margin-bottom: 0px;
	margin-top: 0px;
}
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
.line
{
	color:gray;
	height:5;
}
 -->
</style>
</head>

<body>
<h2><a name="HttpSession">HttpSession</a></h2>
<%
SimpleDateFormat	sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
%>
<table width="100%">
 <tr>
  <th>createTime</th>
  <td><%=sdf.format( new Date(session.getCreationTime()) ) %></td>
 </tr>
 <tr>
  <th>id</th>
  <td><%=session.getId() %></td>
 </tr>
 <tr>
  <th>lastAccessedTime</th>
  <td><%=sdf.format( new Date(session.getLastAccessedTime()) ) %></td>
 </tr>
 <tr>
  <th>maxInactiveInterval</th>
  <td><%=session.getMaxInactiveInterval() %></td>
 </tr>
 <tr>
  <th>isNew</th>
  <td><%=session.isNew() %></td>
 </tr>
</table>
<h2><a name="SessionAttributes">SessionAttributes</a></h2>
<table width="100%">
 <tr>
  <th>name</th>
  <th>value</th>
  <th>class</th>
  <th>file</th>
 </tr>
<%{
	Enumeration<?>	enumeration= session.getAttributeNames();
	while( enumeration.hasMoreElements() )
	{
		String	name= (String)enumeration.nextElement();
		Object	value= session.getAttribute( name );
		URL		url= null;
		ClassLoader	cl= value.getClass().getClassLoader();
		if( cl != null )
		{
			url= cl.getResource( value.getClass().getName() );
		}
%> <tr>
  <td><%=name %></td>
  <td><%=value %></td>
  <td><%=value.getClass().getName() %></td>
  <td><%=url %></td>
 </tr>
<%
	}
}%></table>
<%
String	requestURI= (String)request.getAttribute( "javax.servlet.include.request_uri" );
if( requestURI == null )
{
	requestURI= request.getRequestURI();
}
%><form action="<%=requestURI %>" method="post" style="margin:0px;">
<input type="submit" name="submit" value="invalidate">
</form>

<hr>

</body>
</html>
