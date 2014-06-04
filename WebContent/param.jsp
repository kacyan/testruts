<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %><%@page import="java.util.Enumeration"%>
<%
Enumeration<?>	enumeration= request.getParameterNames();
while( enumeration.hasMoreElements() )
{
	String	name= (String)enumeration.nextElement();
	String[]	values= request.getParameterValues( name );
	for( int i= 0; i < values.length; i++ )
	{
%><%=name %>=[<%=values[i] %>]
<%
	}
}
%>