<%@page import="java.text.DecimalFormat"%>
<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %><%@page import="java.util.Enumeration"%>
<html>
<body>
<form action="check.jsp" method="post">
<table>
<%
DecimalFormat	df= new DecimalFormat( "0000" );
for( int i= 0; i < 20000; i++ )
{
	int	amari= i % 100;
	if( amari == 0 )
	{
		%><tr><%
	}
	%><td><input type="text" name="P<%=df.format(i) %>" value="<%=i %>"  size="4"/></td><%
	if( amari == 0 )
	{
		%></tr><%
	}
}
%></table>
<input type="submit" />
</form>
</body>
</html>
