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
<h1></h1>

<hr>
<%
String	contextPath= request.getContextPath();
String	path= request.getRequestURI();
int	index= path.lastIndexOf( "/" );
if( index >= 0 )
{
	path= path.substring( 0, index+1 );
}
path= path.replaceAll( contextPath, "" );
%><%=contextPath %> - <%=path %>
<hr/>
<%
File	rootFol= new File( application.getRealPath( path ) );
File[]	files= rootFol.listFiles();
Arrays.sort( files );
for( int i= 0; i < files.length; i++ )
{
	%><a href="<%=files[i].getName() %>"><%=files[i].getName() %></a><br><%
}
%>
<hr>

</body>
</html>
