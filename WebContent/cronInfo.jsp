<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="jp.co.ksi.eip.commons.cron.CronInfo"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
<h1></h1>

<hr>
<%
CronInfo	info= (CronInfo)application.getAttribute( CronInfo.CTX_CRON_INFO );
if( info != null )
{
%>servletURL= <%=info.getServletURL() %><br/>
count=<%=info.getCount() %><br/>
running=<%=info.isRunning() %><br/>
startTime=<%=info.getStartTime() %><br/>
<%
}
%>
<hr>
</body>
</html>
