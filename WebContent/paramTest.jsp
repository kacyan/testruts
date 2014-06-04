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
paramTest.jsp
2011/01/31 Kac@KSI
--%>

<%@page import="java.util.Map.Entry"%><html>
<bean:parameter name="actionPath" id="actionPath" value="check.jsp"/>
<bean:parameter name="method" id="method" value="POST"/>
<bean:parameter name="task" id="task" value="kac"/>
<bean:parameter name="param" id="param" value="P1"/>
<bean:parameter name="param1" id="param1" value=""/>
<bean:parameter name="param2" id="param2" value=""/>
<bean:parameter name="param3" id="param3" value=""/>
<bean:parameter name="param4" id="param4" value=""/>
<bean:parameter name="param5" id="param5" value=""/>
<%
String	servletPath= request.getServletPath();
servletPath= servletPath.replaceAll( "^/", "" );
%>
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
<h1 align="center"><a name="top"><%=servletPath %></a></h1>

<a href="javascript:history.back()">Back</a><br>

<table>
<%
java.util.Map	map= request.getParameterMap();
for( Iterator i= map.keySet().iterator(); i.hasNext(); )
{
	String	name= (String)i.next();
	String[]	value= (String[])map.get( name );
%> <tr>
  <td><%=name %></td>
  <td>
<%
	for( int j= 0; j < value.length; j++ )
	{
%>   <%=value[j] %><br/>
<%
	}
%>  </td>
  <td><%=value.length %></td>
 </tr>
<%
}

%>
</table>
<hr/>

<form action="" method="post">
<table style="width:100%;">
 <tr>
  <th>actionPath</th>
  <td><input type="text" name="actionPath" value="<%=actionPath %>"/></td>
 </tr>
 <tr>
  <th>method</th>
  <td>
   <input type="radio" name="method" value="GET" <%=method.equalsIgnoreCase("GET")?"checked":"" %>/>GET
   <input type="radio" name="method" value="POST" <%=method.equalsIgnoreCase("POST")?"checked":"" %>/>POST
  </td>
 </tr>
 <tr>
  <th>param1</th>
  <td><input type="text" name="param1" value="<bean:write name="param1"/>"/></td>
 </tr>
 <tr>
  <th>param2</th>
  <td><input type="text" name="param2" value="<bean:write name="param2"/>"/></td>
 </tr>
 <tr>
  <th>param3</th>
  <td><input type="text" name="param3" value="<bean:write name="param3"/>"/></td>
 </tr>
 <tr>
  <th>param4</th>
  <td><input type="text" name="param4" value="<bean:write name="param4"/>"/></td>
 </tr>
 <tr>
  <th>param5</th>
  <td><input type="text" name="param5" value="<bean:write name="param5"/>"/></td>
 </tr>
</table>
<input type="submit" name="submit" value="<%=method %>">
</form>

<hr class="line">
<bean:write name="actionPath"/>
<form action="<bean:write name="actionPath"/>" method="<bean:write name="method"/>">
<table>
<logic:notEmpty name="param1">
 <tr>
  <th><bean:write name="param1"/></th>
  <td><input type="text" name="<bean:write name="param1"/>"/></td>
 </tr>
</logic:notEmpty>
<logic:notEmpty name="param2">
 <tr>
  <th><bean:write name="param2"/></th>
  <td><input type="text" name="<bean:write name="param2"/>"/></td>
 </tr>
</logic:notEmpty>
<logic:notEmpty name="param3">
 <tr>
  <th><bean:write name="param3"/></th>
  <td><input type="text" name="<bean:write name="param3"/>"/></td>
 </tr>
</logic:notEmpty>
<logic:notEmpty name="param4">
 <tr>
  <th><bean:write name="param4"/></th>
  <td><input type="text" name="<bean:write name="param4"/>"/></td>
 </tr>
</logic:notEmpty>
<logic:notEmpty name="param5">
 <tr>
  <th><bean:write name="param5"/></th>
  <td><input type="text" name="<bean:write name="param5"/>"/></td>
 </tr>
</logic:notEmpty>
 <tr>
  <td colspan="2"><input type="submit" style="width:100%;"/></td>
</table>
</form>
<hr class="line">

taskのみ
<form action="<%=actionPath %>" method="<%=method %>" style="margin:0px;">
<table>
 <tr>
  <th>task</th>
  <td><input type="text" name="task" value="<%=task %>"></input></td>
 </tr>
</table>
<input type="submit" value="<%=method %>">
</form>
<br/>

taskとparam
<form action="<%=actionPath %>" method="<%=method %>" style="margin:0px;">
<table>
 <tr>
  <th>task</th>
  <td><input type="text" name="task" value="<%=task %>"></input></td>
 </tr>
 <tr>
  <th>param</th>
  <td><input type="text" name="param" value="<%=param %>"></input></td>
 </tr>
</table>
<input type="submit" value="<%=method %>">
</form>
<br/>

paramとtask
<form action="<%=actionPath %>" method="<%=method %>" style="margin:0px;">
<table>
 <tr>
  <th>param</th>
  <td><input type="text" name="param" value="<%=param %>"></input></td>
 </tr>
 <tr>
  <th>task</th>
  <td><input type="text" name="task" value="<%=task %>"></input></td>
 </tr>
</table>
<input type="submit" value="<%=method %>">
</form>
<br/>

taskが２つ
<form action="<%=actionPath %>" method="<%=method %>" style="margin:0px;">
<table>
 <tr>
  <th>task</th>
  <td><input type="text" name="task" value="<%=task %>"></input></td>
 </tr>
 <tr>
  <th>task</th>
  <td><input type="text" name="task" value=""></input></td>
 </tr>
</table>
<input type="submit" value="<%=method %>">
</form>
<br/>

paramが２つ
<form action="<%=actionPath %>" method="<%=method %>" style="margin:0px;">
<table>
 <tr>
  <th>param</th>
  <td><input type="text" name="param" value="<%=param %>"></input></td>
 </tr>
 <tr>
  <th>param</th>
  <td><input type="text" name="param" value=""></input></td>
 </tr>
</table>
<input type="submit" value="<%=method %>">
</form>
<br/>

<hr class="line">

<a href="javascript:history.back()">Back</a>

</body>
</html>
