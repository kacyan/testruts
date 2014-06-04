<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="JP.co.ksi.ldap.LdifProperties"%>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- oauthResult.jsp -->
<%-- 
oauthの習作
アクセストークン取得後の画面
2012/05/09 Kac
 --%>
<logic:notEqual parameter="serviceURL" value="">
<bean:parameter name="serviceURL" id="serviceURL" />
</logic:notEqual>
<logic:notPresent name="serviceURL">
<bean:define id="serviceURL" value="" />
</logic:notPresent>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/default.css" />
</head>
<body>
<span>アクセス・トークンの取得に成功しました。</span>
<hr/>
<html:form action="/oauth/callService">
<table class="formtable">
 <tr>
  <th>consumerKey</th>
  <td>
   <bean:write name="oauth" property="consumer_key"/><br/>
   プロバイダにコンシューマ登録した際に発行されたコンシューマ・キー。
  </td>
 </tr>
 <tr>
  <th>oauthToken</th>
  <td>
   <bean:write name="oauth" property="oauth_token"/><br/>
   プロバイダに発行されたアクセストークン。サービス呼出時に付与する。
  </td>
 </tr>
 <tr>
  <th>oauthTokenSecret</th>
  <td>
   <bean:write name="oauth" property="oauth_token_secret"/><br/>
   プロバイダに発行されたトークンシークレット。サービス呼出時の署名に使う。
  </td>
 </tr>
 <tr>
  <th>serviceURL</th>
  <td>
   <input type="text" name="serviceURL" value="<bean:write name="serviceURL"/>" style="width:100%;"/>
   サービスのURL。
  </td>
 </tr>
 <tr>
  <th>oauthSessionHandle</th>
  <td>
   <bean:write name="oauthSessionHandle"/><br/>
   使わない？
  </td>
 </tr>
 <tr>
  <th>oauthExpiresIn</th>
  <td>
   <bean:write name="oauthExpiresIn"/><br/>
   有効期限？
  </td>
 </tr>
 <tr>
  <th>oauthAuthorizationExpiresIn</th>
  <td>
   <bean:write name="oauthAuthorizationExpiresIn"/><br/>
   有効期限？どうちがう？
  </td>
 </tr>
 <tr>
  <th>xoauthYahooGuid</th>
  <td>
   <bean:write name="xoauthYahooGuid"/><br/>
   Yahooの独自拡張。
  </td>
 </tr>
 <tr>
  <td colspan="2"><input type="submit" value="call service" style="width:100%;"/></td>
 </tr>
</table>
</html:form>
<a href="index.jsp">[OAuthコンシューマの習作]</a>
<hr/>
<jsp:include page="/check.jsp" flush="true"/>
</body>
</html>
