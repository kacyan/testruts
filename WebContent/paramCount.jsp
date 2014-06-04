<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<%@ page isErrorPage="true" %>
<%@ page session="true" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.text.DecimalFormat"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%--
paramCount.jsp
2012/07/25 Kac@KSI
【経緯】
HashDoS攻撃への対策として、リクエスト・パラメータ数には制限がある。
(tomcat6の場合、100000。connectorのmaxParameterCountで変更可能)
実際にどうなるのかを調べる為の習作
--%>
<%
int	count= 10000;
try
{
	count= Integer.parseInt( request.getParameter("count") );
}
catch( Exception e )
{
	count= 10000;
}
%>
<html>
<head>
</head>
<body>

<a href="javascript:history.back()">Back</a><br>
<form action="paramCount.jsp" method="get">
<input type="text" name="count" value="<%=count %>" />
<input type="submit" value="param数変更" />
</form>
<form action="check.jsp" method="post">
<table>
<%
DecimalFormat	df= new DecimalFormat( "000" );
for( int i= 0; i < count; i++ )
{
	int amari= i % 100;
	if( amari == 0 ){%> <tr><%}
%>  <td><input type="text" name="P<%=i %>" value="<%=df.format(i) %>" maxlength="4" size="2"/></td>
<%
	if( amari == 99 ){%> </tr><%}
}
%></table>
<input type="submit"/>
</form>
<hr/>

<a href="javascript:history.back()">Back</a>

</body>
</html>
