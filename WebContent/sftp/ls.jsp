<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="jp.co.ksi.testruts.Const"%>
<%@ page import="jp.co.ksi.incubator.jcraft.MyUserInfo"%>
<%@ page import="jp.co.ksi.incubator.jcraft.SftpPutBL"%>
<%@ page import="JP.co.ksi.util.HtmlFilter"%>
<%@ page import="com.jcraft.jsch.ChannelSftp.LsEntry"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.io.InputStreamReader"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.io.PrintWriter"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- ls.jsp -->
<%-- 
JSchを使ったsftpの習作
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<hr/>

<bean:parameter name="host" id="host" value=""/>
<bean:parameter name="port" id="port" value="22"/>
<bean:parameter name="uid" id="uid" value=""/>
<bean:parameter name="pwd" id="pwd" value=""/>
<bean:parameter name="path" id="path" value=""/>
<bean:parameter name="submit" id="submit" value=""/>
<form action="operate.do" method="post" enctype="multipart/form-data"  style="margin:0px;">
host : <input type="text" name="host" value="<bean:write name="host"/>"/>
<input type="text" name="port" maxlength="2" size="2"  value="<bean:write name="port"/>"/><br/>
uid : <input type="text" name="uid" value="<bean:write name="uid"/>"/><br/>
pwd  : <input type="password" name="pwd" value="<bean:write name="pwd"/>"/><br/>
path : <input type="text" name="path" size="50" value="<bean:write name="path"/>"/><br/>
<input type="submit" name="submit.ls" value="list"/>
<input type="submit" name="submit.get" value="get"/>
<br/><input type="file" name="<%=SftpPutBL.FORM_FILE_NAME %>"/>
<input type="submit" name="submit.put" value="put"/>
</form>
<%--
<form action="put.do" method="post" enctype="multipart/form-data" style="margin:0px;">
<input type="hidden" name="host" value="<bean:write name="host"/>"/>
<input type="hidden" name="port" maxlength="2" size="2"  value="<bean:write name="port"/>"/>
<input type="hidden" name="uid" value="<bean:write name="uid"/>"/>
<input type="hidden" name="pwd" value="<bean:write name="pwd"/>"/>
path : <input type="text" name="path" size="50" value="<bean:write name="path"/>"/><br/>
<input type="file" name="<%=SftpPutBL.FORM_FILE_NAME %>"/>
<input type="submit" name="submit.put" value="put"/>
</form>
 --%>
<hr/>

<jsp:useBean id="vecLsEntry" scope="request" class="java.util.Vector"/>
<%
for( int i= 0; i < vecLsEntry.size(); i++ )
{
	LsEntry	lsEntry= (LsEntry)vecLsEntry.get( i );
%><pre style="margin:1px;">
<%=lsEntry.getLongname() %>
</pre>
<%
}
%>
<jsp:include page="inc_error.jsp"/>
<hr/>
</body>
</html>
