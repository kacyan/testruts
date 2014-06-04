<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%
Cookie	cookie= new Cookie("KacDebug","hoge");
cookie.setDomain( "localhost" );
response.addCookie( cookie );
Cookie	cookie2= new Cookie("KacDebug2","hoge2");
cookie2.setDomain( "http://localhost/" );
response.addCookie( cookie2 );
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
<h1></h1>

<hr>
<%
Cookie[]	cookies= request.getCookies();
if( cookies == null )	cookies= new Cookie[0];
for( int i= 0; i < cookies.length; i++ )
{
%> <%=cookies[i].getName() %>
<%=cookies[i].getValue() %>
<%=cookies[i].getDomain() %>
<%=cookies[i].getPath() %>
<%=cookies[i].getVersion() %>
<br/>
<%
}
%>
<hr>
<script type="text/javascript">
alert( document.cookie );
</script>
</body>
</html>
