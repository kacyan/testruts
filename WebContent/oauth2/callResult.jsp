<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="JP.co.ksi.ldap.LdifProperties"%>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- callResult.jsp -->
<%-- 
oauthの習作
サービスを呼んだ後の結果画面
2012/07/23 Kac
 --%>
<bean:define id="oauth" name="oauth2" scope="session"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/default.css" />
</head>
<body>
<bean:parameter name="serviceURL" id="serviceURL" value=""/>
<span>サービス[<bean:write name="oauth" property="serviceURL"/>]をコールしました。</span>
<hr/>
<html:form action="/oauth2/callService" method="post">
<table class="formtable">
 <tr>
  <th>serviceURL</th>
  <td>
<logic:empty name="serviceURL">
   <input type="text" name="serviceURL" value="<bean:write name="oauth" property="serviceURL"/>" style="width:100%;"/>
</logic:empty>
<logic:notEmpty name="serviceURL">
   <input type="text" name="serviceURL" value="<bean:write name="serviceURL"/>" style="width:100%;"/>
</logic:notEmpty>
  </td>
 </tr>
 <tr>
  <td><input type="submit" style="width:100%;"/></td>
 </tr>
</table>
</html:form>
<a href="index.jsp">[OAuthコンシューマの習作]</a><br/>
<bean:write name="oauth"/>
<hr/>
<logic:present name="responseData">
<bean:write name="responseData"/>
</logic:present>
<logic:equal name="result" value="APL_ERR">
<div>
<bean:write name="oauth" property="refresh_token"/><br/>
<a href="refreshAccessToken.do">refreshAccessToken.do</a>
</div>
</logic:equal>
<hr/>
<jsp:include page="/inc/error.jsp" flush="true"/>
</body>
</html>
