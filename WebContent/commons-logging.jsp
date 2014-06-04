<%@page import="org.apache.commons.beanutils.BeanUtils"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
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
<h4>commons-loggingの調査</h4>
<%
Class	cls= BeanUtils.class;
Log	log= LogFactory.getLog( cls );
%>
実装：[<%=log %>]<br/>
<%=cls.getName() %>のログレベル<br/>
debug=<%=log.isDebugEnabled() %><br/>
error=<%=log.isErrorEnabled() %><br/>
fatal=<%=log.isFatalEnabled() %><br/>
info=<%=log.isInfoEnabled() %><br/>
trace=<%=log.isTraceEnabled() %><br/>
warn=<%=log.isWarnEnabled() %><br/>
<hr/>
<%=getClass().getClassLoader().getResource( "commons-logging.properties" ) %>
</body>
</html>