<%@page import="java.util.Enumeration"%>
<%@page import="org.apache.log4j.Logger"%>
<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%
String	authorization= request.getHeader( "authorization" );
if( authorization == null )
{//	authorizationヘッダがない
	response.setHeader( "WWW-Authenticate", "BASIC realm=HOGE" );
	response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Authorization Required" );
	return;
}
else if( !authorization.matches( "^[Bb][Aa][Ss][Ii][Cc] .*" ) )
{//	BASIC認証じゃない
	response.setHeader( "WWW-Authenticate", "BASIC realm=HOGE" );
	response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Authorization Required" );
	return;
}
String	tmp= authorization.substring( 6 );
String	dec= new String( org.apache.commons.codec.binary.Base64.decodeBase64( tmp.getBytes() ) );
if( !dec.equals(":") )
{//	から
	response.setHeader( "WWW-Authenticate", "BASIC realm=hoge/huga" );
	response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Authorization Required" );
	return;
}
%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
<h1></h1>
<hr>
authorization: [<%=authorization %>]<br/>
Basic: [<%=authorization.matches( "^[Bb][Aa][Ss][Ii][Cc] .*" ) %>]<br/>
<%=dec %><br/>
<hr>
<%
Enumeration	enumeration= request.getHeaders( "authorization" );
while( enumeration.hasMoreElements() )
{
	Object	obj= enumeration.nextElement();
%><%=obj %> ---- <%=obj.getClass().getName() %><br/>
<%
}
%>
</body>
</html>
