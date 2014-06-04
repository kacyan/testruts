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
2012/05/09 Kac
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/default.css" />
</head>
<body>
<logic:present name="serviceURL">
<span>サービス[<bean:write name="serviceURL"/>]をコールしました。</span>
<hr/>
<html:form action="/oauth/callService" method="post">
<table class="formtable">
 <tr>
  <th>serviceURL</th>
  <td>
   <input type="text" name="serviceURL" value="<bean:write name="serviceURL"/>" style="width:100%;"/>
  </td>
 </tr>
 <tr>
  <td><input type="submit" style="width:100%;"/></td>
  <td><input type="button" value="saveToken" onclick="onSaveBtn()"/></td>
 </tr>
</table>
</html:form>
<a href="index.jsp">[OAuthコンシューマの習作]</a>
<hr/>
<script type="text/javascript">
function onSaveBtn()
{
	form= document.forms['OAuthCallServiceForm'];
	form.action='saveToken.do';
	form.submit();
}
</script>
</logic:present>
<logic:present name="wsResponse">
<bean:write name="wsResponse"/>
</logic:present>
<logic:notPresent name="wsResponse">
<a href="loadToken.do">loadToken</a>
</logic:notPresent>
<hr/>
<jsp:include page="/inc/error.jsp" flush="true"/>
</body>
</html>
