<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="jp.co.ksi.incubator.MailStore"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%
String	msg= "connected.";
MailStore	mailStore= new MailStore();
try
{
	mailStore.connect( "bowmore.osk.ksi.co.jp", "hoge", "huga" );
	
	session.setAttribute( "mailStore", mailStore );
}
catch( Exception e )
{
	msg= e.toString();
}
finally
{
	if( mailStore != null )	mailStore.disconnect();
}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
<h1></h1>

<hr>
<%=msg %>
<hr>

</body>
</html>
