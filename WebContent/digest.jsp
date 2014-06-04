<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%
String	authorization= request.getHeader( "authorization" );
if( authorization == null )
{//	authorizationヘッダがない
//	WWW-Authenticate=Digest realm="hoge", qop="auth", nonce="682ca2565e754afe682b21d133772914", opaque="9775072c7c8c07508937069c590a0eb5"
	response.setHeader( "WWW-Authenticate", "Digest realm=HOGE" );
	response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Authorization Required" );
	return;
}
else if( !authorization.matches( "^[Dd][Ii][Gg][Ee][Ss][Tt] .*" ) )
{//	DIGEST認証じゃない
	response.setHeader( "WWW-Authenticate", "Digest realm=HOGE" );
	response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Authorization Required" );
	return;
}
String	tmp= authorization.substring( 6 );
String	dec= new String( org.apache.commons.codec.binary.Base64.decodeBase64( tmp.getBytes() ) );
if( dec.equals(":") )
{//	からだ
	response.setHeader( "WWW-Authenticate", "Digest realm=HOGE" );
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
Digest: [<%=authorization.matches( "^[Dd][Ii][Gg][Ee][Ss][Tt] .*" ) %>]<br/>
<%=dec %><br/>
<hr>
<jsp:include page="check.jsp"/>
</body>
</html>
