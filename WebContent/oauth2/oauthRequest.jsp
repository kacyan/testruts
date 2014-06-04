<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="JP.co.ksi.ldap.LdifProperties"%>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- oauth.jsp -->
<%-- 
OAuth認可要求を送るFORM画面
2012/06/26 Kac

URL:https://accounts.google.com/o/oauth2/auth <- oauthConfigから
引数：
　response_type: oauthConfigから
　client_id: oauthConfigから
　redirect_uri: oauthConfigから
　scope: oauthConfigから
　state: form画面で生成する <-- oauthConfigをセッションに保持する時の属性名がよいのでは？
　access_type: oauthConfigから(default=offline)
　approval_prompt: oauthConfigから(default=force)
戻値：
　googleの認可画面に遷移し、認可完了後に、２へリダイレクトされる
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/default.css" />
</head>
<body>
Step1.OAuth-2.0のトークンを要求します。
<hr/>
<form action="<bean:write name="oauthConfig" property="authURL"/>">
<input type="hidden" name="response_type" value="<bean:write name="oauthConfig" property="response_type"/>"/>
<input type="hidden" name="client_id" value="<bean:write name="oauthConfig" property="client_id"/>"/>
<input type="hidden" name="redirect_uri" value="<bean:write name="oauthConfig" property="redirect_uri"/>"/>
<input type="hidden" name="scope" value="<bean:write name="oauthConfig" property="scope"/>"/>
<input type="hidden" name="state" value="<bean:write name="oauthConfig" property="state"/>"/>
<input type="hidden" name="access_type" value="<bean:write name="oauthConfig" property="access_type"/>"/>
<input type="hidden" name="approval_prompt" value="<bean:write name="oauthConfig" property="approval_prompt"/>"/>
<input type="submit" value="認可を要求する" />
</form>
<hr/>
</body>
</html>
