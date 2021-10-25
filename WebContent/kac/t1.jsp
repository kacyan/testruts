<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%@page import="java.io.File"%>
<%@page import="jp.co.ksi.eip.commons.util.StringUtil"%>
<%--
画像ファイルを img src タグで列挙表示するJSP
ListFileServletからforwardされる。
作成；2021/09/09
 --%>
<jsp:useBean id="requestURI" class="java.lang.String" scope="request"/>
<jsp:useBean id="baseLink" class="java.lang.String" scope="request"/>
<jsp:useBean id="pathInfo" class="java.lang.String" scope="request"/>
<jsp:useBean id="width" class="java.lang.String" scope="session"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
requestURI=<%=requestURI %><br/>
<hr/>
<%
File[]	children= (File[])request.getAttribute("children");
if( children == null )
{
	children= new File[0];
}
for( int i= 0; i < children.length; i++ )
{
	if( children[i].isDirectory() )
	{
%>
<%=children[i].getName() %><br/>
<%
	}
	else
	{
		String	link= StringUtil.concatPath(baseLink, pathInfo);
		link= StringUtil.concatPath(link, children[i].getName() );
%>
<img src="<%=link %>" vspace="2" /><br/>
<%
	}
}
%>
<hr>
</body>
</html>
