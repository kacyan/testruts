<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="java.io.InputStreamReader"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Arrays"%>
<%--
propertiesファイルを編集する
2010/12/16 Kac
--%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<h1></h1>
<hr>
<%
String	file= "";
Properties	props= new Properties();
InputStream	in= null;
//in= new java.io.FileInputStream( file );
try
{
	in= application.getResourceAsStream( "/WEB-INF/testruts.properties" );
	props.load( in );
}
catch( Exception e )
{
	out.println( "<pre>" );
	e.printStackTrace( new PrintWriter(out) );
	out.println( "</pre>" );
%>
</body>
</html>
<%
	return;
}
finally
{
	in.close();
}
%>
<form action="check.jsp" method="post">
<table border="1">
<%
String[]	key= new String[props.keySet().size()];
key= props.keySet().toArray( key );
Arrays.sort( key );
for( int i= 0; i < key.length; i++ )
{
	String	value= props.getProperty( key[i] );
%> <tr>
  <td><%=key[i] %></td>
  <td style="width:100%;"><input type="text" name="<%=key[i] %>" value="<%=value %>" style="width:100%;"></td>
 </tr>
<%
}
%>
</table>
<input type="submit">
</form>
<hr>
</body>
</html>
