<%@page import="javax.naming.directory.DirContext"%>
<%@page import="org.apache.struts.config.ModuleConfig"%>
<%@page import="org.apache.struts.Globals"%>
<%@page import="java.util.Enumeration"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page autoFlush="true"  isErrorPage="true" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<html>
<head>
</head>
<body>
<%
ClassLoader	cl= getClass().getClassLoader();
while( cl != null )
{
	out.println( cl +"<br/>");
	if( cl instanceof org.apache.catalina.loader.WebappClassLoader )
	{
		org.apache.catalina.loader.WebappClassLoader cl2= (org.apache.catalina.loader.WebappClassLoader)cl;
		
		DirContext	context= cl2.getResources();
		out.println( "->"+ context +"<br/>" );
		if( context instanceof org.apache.naming.resources.ProxyDirContext )
		{
			org.apache.naming.resources.ProxyDirContext proxyDirContext= (org.apache.naming.resources.ProxyDirContext)context;
			out.println( "->"+ proxyDirContext.getDirContext() +"<br/>" );
			org.apache.naming.resources.FileDirContext fileDirContext= (org.apache.naming.resources.FileDirContext)proxyDirContext.getDirContext();
//			fileDirContext.setDocBase( "/logs" );
		}
	}
	else if( cl instanceof org.apache.catalina.loader.StandardClassLoader )
	{
		org.apache.catalina.loader.StandardClassLoader cl2= (org.apache.catalina.loader.StandardClassLoader)cl;
	}
	cl= cl.getParent();
}
%>
<hr/>
<%--
<jsp:include page="check.jsp"/>
 --%>
</body>
</html>