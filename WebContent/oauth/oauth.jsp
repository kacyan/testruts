<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="JP.co.ksi.ldap.LdifProperties"%>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- oauth.jsp -->
<%-- 
oauthの習作
2012/03/13 Kac
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/default.css" />
</head>
<body>
Step1.OAuthのリクエスト・トークンを取得します。
<hr/>
<html:form action="/oauth/getRequestToken">
<table class="formtable" style="width:100%;">
 <tr>
  <th style="width:10em;">consumerKey</th>
  <td style="">
   <html:text property="consumer_key" style="width:100%;"/>
   プロバイダにコンシューマ登録した際に発行されたコンシューマキー
  </td>
 </tr>
 <tr>
  <th>consumerSecret</th>
  <td>
   <html:text property="consumer_secret" style="width:100%;"/>
   プロバイダにコンシューマ登録した際に発行されたコンシューマシークレット。通常は秘密にしておく。
  </td>
 </tr>
 <tr>
  <th>requestTokenURL</th>
  <td>
   <html:text property="requestTokenURL" style="width:100%;"/>
   プロバイダが提供しているリクエストトークン発行用URL
  </td>
 </tr>
 <tr>
  <th>oauthCallback</th>
  <td>
   <html:text property="oauth_callback" style="width:100%;"/>
   プロバイダで認証した後に遷移するコンシューマのURL。無ければoob
  </td>
 </tr>
 <tr>
  <th>authorizeTokenURL</th>
  <td>
   <html:text property="authorizeTokenURL" style="width:100%;"/>
   プロバイダが提供している認証用URL。
   Yahooの場合、リクエストトークン発行用URLのレスポンスで得られるので不要。
   Googleの場合、必要。
  </td>
 </tr>
 <tr>
  <th>scope</th>
  <td>
   <html:text property="scope" style="width:100%;"/>
   プロバイダのどのサービスに対して認可を得るかを示す。URLを指定するみたい。
   Yahooの場合、scopeには対応していない。
   Googleの場合、必須。
  </td>
 </tr>
 <tr>
  <th>accessTokenURL</th>
  <td>
   <html:text property="accessTokenURL" style="width:100%;"/>
   プロバイダが提供しているリクエストトークン発行用URL
  </td>
 </tr>
 <tr>
  <th>serviceURL</th>
  <td><bean:parameter name="serviceURL" id="serviceURL" value=""/>
   <input type="text" name="serviceURL" value="<bean:write name="serviceURL"/>" style="width:100%;"/>
   サービスのURL。
  </td>
 </tr>
 <tr>
  <td colspan="2"><input type="submit" value="get request token" style="width:100%;"/></td>
 </tr>
</table>
</html:form>
<hr/>
<jsp:include page="/check.jsp"/>
</body>
</html>
