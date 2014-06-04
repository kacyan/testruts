<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="JP.co.ksi.ldap.LdifProperties"%>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- oauthRedirect.jsp -->
<%-- 
oauthの習作
トークンべリファイを受け取ってアクセストークンを取得する画面
2012/05/09 Kac
 --%>
<bean:define id="oauth" name="oauth" scope="session" type="jp.co.ksi.incubator.oauth.OAuthBean" />
<bean:parameter name="serviceURL" id="serviceURL" value=""/>
<%
if( !oauth.getOauth_callback().equals( "oob" ) )
{//	リダイレクトする
	session.setAttribute( "serviceURL", serviceURL );
	response.sendRedirect( oauth.getXoauth_request_auth_url() );
	return;
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/default.css" />
</head>
<body>
<a href="<bean:write name="oauth" property="xoauth_request_auth_url"/>"><bean:write scope="session" name="oauth" property="xoauth_request_auth_url"/></a>
<div>
上記URLで認証後に得られるoauthVerifierの値を、下記のoauthVerifierフィールドに入力します。
</div>
<hr/>
<html:form action="/oauth/getAccessToken">
<input type="hidden" name="serviceURL" value="<bean:write name="serviceURL"/>"/>
<table class="formtable" style="width:100%;">
 <tr>
  <th style="width:10em;">consumerKey</th>
  <td style="">
   <bean:write name="oauth" property="consumer_key" /><br/>
   プロバイダにコンシューマ登録した際に発行されたコンシューマ・キー
  </td>
 </tr>
 <tr>
  <th>consumerSecret</th>
  <td>
   <bean:write name="oauth" property="consumer_secret" /><br/>
   プロバイダにコンシューマ登録した際に発行されたコンシューマシークレット。通常は秘密にしておく。
  </td>
 </tr>
 <tr>
  <th>accessTokenURL</th>
  <td>
   <bean:write name="oauth" property="accessTokenURL" /><br/>
   プロバイダが提供しているアクセストークン発行用URL
  </td>
 </tr>
 <tr>
  <th>scope</th>
  <td>
   <bean:write name="oauth" property="scope" /><br/>
   プロバイダに認可を求めるサービスのURL。
  </td>
 </tr>
 <tr>
  <th>oauthToken</th>
  <td>
   <bean:write name="oauth" property="oauth_token" /><br/>
   プロバイダに発行されたリクエストトークン。
  </td>
 </tr>
 <tr>
  <th>oauthTokenSecret</th>
  <td>
   <bean:write name="oauth" property="oauth_token_secret" /><br/>
   プロバイダに発行されたトークンシークレット。
  </td>
 </tr>
 <tr>
  <th>oauthVerifier</th>
  <td>
   <html:text property="oauth_verifier" style="width:100%;"/>
   上記のリンクでプロバイダの認証後に得られた値を入力する。
  </td>
 </tr>
 <tr>
  <td colspan="2"><input type="submit" value="get access token" style="width:100%;"/></td>
 </tr>
</table>
</html:form>
<a href="index.jsp">[OAuthコンシューマの習作]</a>
<hr/>
<jsp:include page="/check.jsp"/>
</body>
</html>
