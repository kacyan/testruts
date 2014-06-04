<%@page import="jp.co.ksi.incubator.portlet.OAuthConfig"%>
<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- oauth2/oauth.jsp -->
<%-- 
facebook認証の習作
facebookでログイン ボタンを表示する
2014/02/19 Kac
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/default.css" />
<style type="text/css">
</style>
</head>
<body>
<hr/>
<logic:present name="auth">
ようこそ <bean:write name="auth"/> さん。
<a href="/logout">[Logout]</a>
</logic:present>
<logic:notPresent name="auth">
<logic:present name="oauthConfig">
<form action="<bean:write name="oauthConfig" property="authURL"/>">
<input type="hidden" name="response_type" value="<bean:write name="oauthConfig" property="response_type"/>"/>
<input type="hidden" name="client_id" value="<bean:write name="oauthConfig" property="client_id"/>"/>
<input type="hidden" name="redirect_uri" value="<bean:write name="oauthConfig" property="redirect_uri"/>"/>
<input type="hidden" name="scope" value="<bean:write name="oauthConfig" property="scope"/>"/>
<%
String	state= "facebook."+ String.valueOf( System.currentTimeMillis() );
session.setAttribute( "state", state );
%><input type="hidden" name="state" value="<bean:write name="state"/>"/>
<input type="hidden" name="access_type" value="<bean:write name="oauthConfig" property="access_type"/>"/>
<input type="hidden" name="approval_prompt" value="<bean:write name="oauthConfig" property="approval_prompt"/>"/>
<input type="image" name="submit" value="OK" src="active_404.png" alt="Log in with Facebook"/>
</form>
</logic:present>
</logic:notPresent>
<hr/>
<jsp:include page="/session.jsp"/>
</body>
</html>
