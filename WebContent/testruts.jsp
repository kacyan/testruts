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
<!-- testruts.jsp -->
<%-- 
2012/02/13 Kac
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
table
{
	margin-bottom: 0px;
}
</style>
</head>
<body>
<h1>testruts.jsp</h1>
<hr>
<bean:parameter name="path" id="path" value=""/>
<logic:empty name="path">
<%
ModuleConfig moduleConfig= (ModuleConfig)application.getAttribute("org.apache.struts.action.MODULE");
ActionConfig[]	actionConfigs= moduleConfig.findActionConfigs();
pageContext.setAttribute( "actionConfigs", actionConfigs );
%>
<form action="testruts.jsp" method="post">
<table border="1">
 <tr>
  <th>path</th>
  <th>name</th>
  <th>type</th>
  <th>parameter</th>
 </tr>
 <logic:iterate id="actionConfig" name="actionConfigs">
 <tr>
  <td><a href="testruts.jsp?path=<bean:write name="actionConfig" property="path"/>"><bean:write name="actionConfig" property="path"/>.do</a></td>
  <td><bean:write name="actionConfig" property="name"/></td>
  <td><bean:write name="actionConfig" property="type"/></td>
  <td><bean:write name="actionConfig" property="parameter"/></td>
 </tr>
 </logic:iterate>
</table>
</form>
</logic:empty>
<logic:notEmpty name="path">
<%
//	アクション
ModuleConfig moduleConfig= (ModuleConfig)application.getAttribute("org.apache.struts.action.MODULE");
ActionConfig	actionConfig= moduleConfig.findActionConfig( path );
pageContext.setAttribute( "actionConfig", actionConfig );
//	フォームビーン
FormBeanConfig	formBeanConfig= moduleConfig.findFormBeanConfig( actionConfig.getName() );
pageContext.setAttribute( "formBeanConfig", formBeanConfig );
pageContext.setAttribute( "formPropertyConfigs", formBeanConfig.findFormPropertyConfigs() );
%>
<table border="1">
 <tr>
  <th>path</th>
  <td><bean:write name="actionConfig" property="path"/></td>
 </tr>
 <tr>
  <th>type</th>
  <td><bean:write name="actionConfig" property="type"/></td>
 </tr>
 <tr>
  <th>parameter</th>
  <td><bean:write name="actionConfig" property="parameter"/></td>
 </tr>
</table>
<form action=".<bean:write name="actionConfig" property="path"/>.do" method="post">
<table border="1">
 <tr>
  <th><bean:write name="formBeanConfig" property="name"/></th>
  <th><bean:write name="formBeanConfig" property="type"/></th>
  <th><bean:write name="formBeanConfig" property="dynamic"/></th>
  <th><bean:write name="formBeanConfig" property="extends"/></th>
 </tr>
 <logic:iterate id="prop" name="formPropertyConfigs">
 <tr>
  <td><bean:write name="prop" property="name"/><!-- name --></td>
  <td>
  <logic:equal name="prop" property="type" value="org.apache.struts.upload.FormFile">
  <input type="file" name="<bean:write name="prop" property="name"/>" value=""/>
  </logic:equal>
  <logic:notEqual name="prop" property="type" value="org.apache.struts.upload.FormFile">
  <input type="text" name="<bean:write name="prop" property="name"/>" value="<bean:write name="prop" property="initial"/>" style="width:100%;"/>
  </logic:notEqual>
  </td>
  <td><bean:write name="prop" property="type"/><!-- type --></td>
  <td><bean:write name="prop" property="typeClass"/><!-- typeClass --></td>
  <td><bean:write name="prop" property="size"/><!-- size --></td>
  <td><bean:write name="prop" property="reset"/><!-- reset --></td>
 </tr>
 </logic:iterate>
 <tr>
  <td><input type="submit" value="<bean:write name="actionConfig" property="path"/>.do"/></td>
 </tr>
</table>
</form>
</logic:notEmpty>
<hr>
</body>
</html>
