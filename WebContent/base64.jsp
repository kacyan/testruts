<%@page import="sun.misc.BASE64Encoder"%>
<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="sun.misc.BASE64Decoder"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<bean:parameter name="src" id="src" value=""/>
<bean:parameter name="submit" id="submit" value=""/>
<%
String	dst= "";
if( submit.equals("encode") )
{
	BASE64Encoder	encoder= new BASE64Encoder();
	dst= encoder.encode( src.getBytes() );
}
else if( submit.equals("decode") )
{
	BASE64Decoder	decoder= new BASE64Decoder();
	dst= new String( decoder.decodeBuffer( src ) );
}
pageContext.setAttribute("dst",dst);
%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
<bean:write name="submit"/>
<hr>
<form action="base64.jsp" method="post">
<input type="text" name="src" value="<bean:write name="src"/>" style="width:100%"/><br/>
<input type="text" name="dst" value="<bean:write name="dst"/>" style="width:100%"/><br/>
<input type="submit" name="submit" value="encode"/>
<input type="submit" name="submit" value="decode"/>
</form>
<hr>
</body>
</html>
