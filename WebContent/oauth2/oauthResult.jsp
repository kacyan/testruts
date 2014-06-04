<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="JP.co.ksi.ldap.LdifProperties"%>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- oauth2/oauthResult.jsp -->
<%-- 
oauth-2.0の習作
アクセストークン取得後の画面
2012/07/23 Kac
 --%>
<bean:define id="oauth" name="oauth2" scope="session"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/default.css" />
</head>
<body>
<span>アクセス・トークンの取得に成功しました。</span>
<hr/>
<form action="callService.do">
<table class="formtable">
 <tr>
  <th>access_token</th>
  <td>
   <bean:write name="oauth" property="access_token"/><br/>
   プロバイダに発行されたアクセストークン。サービス呼出時に付与する。
  </td>
 </tr>
 <tr>
  <th>expires_in</th>
  <td>
   <bean:write name="oauth" property="expires_in"/>
  </td>
 </tr>
 <tr>
  <th>token_type</th>
  <td><bean:write name="oauth" property="token_type"/></td>
 </tr>
 <tr>
  <th>refresh_token</th>
  <td><bean:write name="oauth" property="refresh_token"/></td>
 </tr>
 <tr>
  <td colspan="2"><input type="submit" value="call service" style="width:100%;"/></td>
 </tr>
</table>
</form>
<a href="index.jsp">[OAuth-2.0の習作]</a><br/>
<bean:write name="oauth"/>
<hr/>
<logic:present name="responseData">
<bean:write name="responseData"/>
<hr/>
</logic:present>
<jsp:include page="/check.jsp" flush="true"/>
</body>
</html>
