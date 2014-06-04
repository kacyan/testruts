<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="java.io.File"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="org.apache.struts.config.ModuleConfig"%>
<%@ page import="org.apache.struts.config.ControllerConfig"%>
<%@ page import="org.apache.struts.config.ActionConfig"%>
<%@ page import="org.apache.struts.config.ExceptionConfig"%>
<%@ page import="org.apache.struts.config.ForwardConfig"%>
<%@ page import="org.apache.struts.config.FormBeanConfig"%>
<%@ page import="org.apache.struts.config.MessageResourcesConfig"%>
<%@ page import="org.apache.struts.config.PlugInConfig"%>
<%@ page import="org.apache.struts.config.FormPropertyConfig"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- ModuleConfig.jsp -->
<%-- 
2012/02/13 Kac
 --%>
<%
ModuleConfig moduleConfig= (ModuleConfig)application.getAttribute("org.apache.struts.action.MODULE");
%>
<%! FormBeanConfig getFormBeanConfig( ModuleConfig moduleConfig, String name )
{
	FormBeanConfig[]	formBeanConfigs= moduleConfig.findFormBeanConfigs();
	for( int i= 0; i < formBeanConfigs.length; i++ )
	{
		if( name.equals( formBeanConfigs[i].getName() ) )
		{
			return formBeanConfigs[i];
		}
	}
	return null;
}
%>

<%@page import="java.net.URLEncoder"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
h2
{
	margin-bottom: 0px;
}
</style>
</head>

<body>
<h1>ModuleConfig.jsp</h1>
<%=moduleConfig.getPrefix() %>
<hr>

<h2>controllerConfig</h2>
<%
ControllerConfig	controllerConfig= moduleConfig.getControllerConfig();
pageContext.setAttribute( "controllerConfig", controllerConfig );
%>
<table border="1">
 <tr>
  <th>bufferSize</th><td><bean:write name="controllerConfig" property="bufferSize"/></td>
 </tr>
 <tr>
  <th>catalog</th><td><bean:write name="controllerConfig" property="catalog"/></td>
 </tr>
 <tr>
  <th>command</th><td><bean:write name="controllerConfig" property="command"/></td>
 </tr>
 <tr>
  <th>contentType</th><td><bean:write name="controllerConfig" property="contentType"/></td>
 </tr>
 <tr>
  <th>forwardPattern</th><td><bean:write name="controllerConfig" property="forwardPattern"/></td>
 </tr>
 <tr>
  <th>inputForward</th><td><bean:write name="controllerConfig" property="inputForward"/></td>
 </tr>
 <tr>
  <th>locale</th><td><bean:write name="controllerConfig" property="locale"/></td>
 </tr>
 <tr>
  <th>maxFileSize</th><td><bean:write name="controllerConfig" property="maxFileSize"/></td>
 </tr>
 <tr>
  <th>memFileSize</th><td><bean:write name="controllerConfig" property="memFileSize"/></td>
 </tr>
 <tr>
  <th>multipartClass</th><td><bean:write name="controllerConfig" property="multipartClass"/></td>
 </tr>
 <tr>
  <th>nocache</th><td><bean:write name="controllerConfig" property="nocache"/></td>
 </tr>
 <tr>
  <th>pagePattern</th><td><bean:write name="controllerConfig" property="pagePattern"/></td>
 </tr>
 <tr>
  <th>processorClass</th><td><bean:write name="controllerConfig" property="processorClass"/></td>
 </tr>
 <tr>
  <th>tempDir</th><td><bean:write name="controllerConfig" property="tempDir"/></td>
 </tr>
</table>

<h2>actionConfigs</h2>
<table border="1">
 <tr>
  <th>&nbsp;</th>
  <th>actionId</th>
  <th>path</th>
  <th>attribute</th>
  <th>cancellable</th>
  <th>catalog</th>
  <th>command</th>
  <th>extends</th>
  <th>forward</th>
  <th>include</th>
  <th>input</th>
  <th>multipartClass</th>
  <th>name</th>
  <th>parameter</th>
  <th>prefix</th>
  <th>roles</th>
  <th>scope</th>
  <th>suffix</th>
  <th>type</th>
  <th>unknown</th>
  <th>validate</th>
 </tr>
<%
ActionConfig[]	actionConfigs= moduleConfig.findActionConfigs();
for( int i= 0; i < actionConfigs.length; i++ )
{
	pageContext.setAttribute( "actionConfig", actionConfigs[i] );
%> <tr>
  <td><%=i %></td>
  <td><bean:write name="actionConfig" property="actionId"/></td>
  <td><a href="testruts.jsp?path=<%=URLEncoder.encode(actionConfigs[i].getPath(),"utf-8") %>"><bean:write name="actionConfig" property="path"/></a>
  </td>
  <td><bean:write name="actionConfig" property="attribute"/></td>
  <td><bean:write name="actionConfig" property="cancellable"/></td>
  <td><bean:write name="actionConfig" property="catalog"/></td>
  <td><bean:write name="actionConfig" property="command"/></td>
  <td><bean:write name="actionConfig" property="extends"/></td>
  <td><bean:write name="actionConfig" property="forward"/></td>
  <td><bean:write name="actionConfig" property="include"/></td>
  <td><bean:write name="actionConfig" property="input"/></td>
  <td><bean:write name="actionConfig" property="multipartClass"/></td>
  <td><bean:write name="actionConfig" property="name"/></td>
  <td><bean:write name="actionConfig" property="parameter"/></td>
  <td><bean:write name="actionConfig" property="prefix"/></td>
  <td><bean:write name="actionConfig" property="roles"/></td>
  <td><bean:write name="actionConfig" property="scope"/></td>
  <td><bean:write name="actionConfig" property="suffix"/></td>
  <td><bean:write name="actionConfig" property="type"/></td>
  <td><bean:write name="actionConfig" property="unknown"/></td>
  <td><bean:write name="actionConfig" property="validate"/></td>
 </tr>
<%
}
%>
</table>

<h2>exceptionConfigs</h2>
<table border="1">
 <tr>
  <th>&nbsp;</th>
  <th>bundle</th>
  <th>class</th>
  <th>extends</th>
  <th>handler</th>
  <th>key</th>
  <th>path</th>
  <th>scope</th>
  <th>type</th>
 </tr>
<%
ExceptionConfig[]	exceptionConfigs= moduleConfig.findExceptionConfigs();
for( int i= 0; i < exceptionConfigs.length; i++ )
{
	pageContext.setAttribute( "exceptionConfig", exceptionConfigs[i] );
%> <tr>
  <td><bean:write name="exceptionConfig" property="bundle"/></td>
  <td><bean:write name="exceptionConfig" property="class"/></td>
  <td><bean:write name="exceptionConfig" property="extends"/></td>
  <td><bean:write name="exceptionConfig" property="handler"/></td>
  <td><bean:write name="exceptionConfig" property="key"/></td>
  <td><bean:write name="exceptionConfig" property="path"/></td>
  <td><bean:write name="exceptionConfig" property="scope"/></td>
  <td><bean:write name="exceptionConfig" property="type"/></td>
 </tr>
<%
}
%>
</table>

<h2>formBeanConfigs</h2>
<table border="1">
 <tr>
  <th>&nbsp;</th>
  <th>dynamic</th>
  <th>extends</th>
  <th>name</th>
  <th>type</th>
 </tr>
<%
FormBeanConfig[]	formBeanConfigs= moduleConfig.findFormBeanConfigs();
for( int i= 0; i < formBeanConfigs.length; i++ )
{
	pageContext.setAttribute( "formBeanConfig", formBeanConfigs[i] );
	pageContext.setAttribute( "formBeanProperty", formBeanConfigs[i].findFormPropertyConfigs() );
%> <tr>
  <td><%=i %></td>
  <td><bean:write name="formBeanConfig" property="dynamic"/></td>
  <td><bean:write name="formBeanConfig" property="extends"/></td>
  <td><bean:write name="formBeanConfig" property="name"/></td>
  <td><bean:write name="formBeanConfig" property="type"/></td>
  <td><logic:iterate id="prop" name="formBeanProperty">
  <bean:write name="prop" property="name"/>:
  <input type="text" name="<bean:write name="prop" property="name"/>" value=""/><br/>
  </logic:iterate>
  </td>
 </tr>
<%
}
%></table>

<h2>forwardConfigs</h2>
<table border="1">
 <tr>
  <th>&nbsp;</th>
  <th>catalog</th>
  <th>command</th>
  <th>extends</th>
  <th>module</th>
  <th>name</th>
  <th>path</th>
  <th>redirect</th>
 </tr>
<%
ForwardConfig[]	forwardConfigs= moduleConfig.findForwardConfigs();
for( int i= 0; i < forwardConfigs.length; i++ )
{
	pageContext.setAttribute( "forwardConfig", forwardConfigs[i] );
%> <tr>
  <td><%=i %></td>
  <td><bean:write name="forwardConfig" property="catalog"/></td>
  <td><bean:write name="forwardConfig" property="command"/></td>
  <td><bean:write name="forwardConfig" property="extends"/></td>
  <td><bean:write name="forwardConfig" property="module"/></td>
  <td><bean:write name="forwardConfig" property="name"/></td>
  <td><bean:write name="forwardConfig" property="path"/></td>
  <td><bean:write name="forwardConfig" property="redirect"/></td>
 </tr>
<%
}
%></table>

<h2>messageResourcesConfigs</h2>
<table border="1">
 <tr>
  <th>&nbsp;</th>
  <th>class</th>
  <th>factory</th>
  <th>key</th>
  <th>null</th>
  <th>parameter</th>
 </tr>
<%
MessageResourcesConfig[]	messageResourcesConfigs= moduleConfig.findMessageResourcesConfigs();
for( int i= 0; i < messageResourcesConfigs.length; i++ )
{
	pageContext.setAttribute( "messageResourcesConfig", messageResourcesConfigs[i] );
%> <tr>
  <td><%=i %></td>
  <td><bean:write name="messageResourcesConfig" property="class"/></td>
  <td><bean:write name="messageResourcesConfig" property="factory"/></td>
  <td><bean:write name="messageResourcesConfig" property="key"/></td>
  <td><bean:write name="messageResourcesConfig" property="null"/></td>
  <td><bean:write name="messageResourcesConfig" property="parameter"/></td>
 </tr>
<%
}
%>
</table>

<h2>plugInConfigs</h2>
<table border="1">
 <tr>
  <th>&nbsp;</th>
  <th>className</th>
 </tr>
<%
PlugInConfig[]	plugInConfigs= moduleConfig.findPlugInConfigs();
for( int i= 0; i < plugInConfigs.length; i++ )
{
	pageContext.setAttribute( "plugInConfig", plugInConfigs[i] );
%> <tr>
  <td><%=i %></td>
  <td><bean:write name="plugInConfig" property="className"/></td>
 </tr>
<%
}
%></table>

<hr>
</body>
</html>
