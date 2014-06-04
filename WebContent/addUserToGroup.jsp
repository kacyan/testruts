<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="JP.co.ksi.ldap.LdifProperties"%>
<%@ taglib uri="eip.tld" prefix="eip" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- addUserToGroup.jsp -->
<%-- 
グループへのユーザ追加画面の習作
2012/02/28 Kac
 --%>
<jsp:useBean id="groupLdif" scope="request" type="JP.co.ksi.ldap.LdifProperties"/>
<jsp:useBean id="userArray" scope="request" type="jp.co.ksi.eip.commons.util.PagingArray"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="default.css" />
</head>
<body>
<%-- グループ情報 --%>
<table class="formtable">
 <tr>
  <th>Group ID</th>
  <td><eip:ldif name="groupLdif" property="cn"/></td>
 </tr>
 <tr>
  <th>Group名</th>
  <td><eip:ldif name="groupLdif" property="description" escape=""/></td>
 </tr>
</table>
<%-- グループ操作メニュー --%>
<hr>
<%-- ユーザ検索フォーム --%>
<html:form action="/searchUserGroup" method="post" style="margin:0px;">
検索文字列：<html:text property="searchText"/>
<html:hidden property="start"/>
<html:hidden property="max"/>
<html:hidden property="groupdn"/>
<html:submit value="Search"/>
</html:form>
<hr/>
<%-- ページング用スクリプト --%>
<script type="text/javascript">
function paging( index )
{
	document.AddUserToGroupForm.start.value= index;
	document.AddUserToGroupForm.submit();
}
</script>
<%	//	ページング用データ
request.setAttribute( "pagingArray", userArray );
%>
<%-- ページング --%>
<jsp:include page="/inc/paging.jsp" />
<%-- ユーザ一覧 --%>
<table class="listtable">
 <tr>
  <th></th>
  <th>uid</th>
  <th>employeeNumber</th>
  <th>cn</th>
  <th>sn</th>
  <th>officeName</th>
  <th>depertmentName</th>
 </tr>
<%
for( int i= 0; i < userArray.getMax(); i++ )
{
	LdifProperties	userLdif= (LdifProperties)userArray.get( i );
	pageContext.setAttribute( "userLdif", userLdif );
	pageContext.setAttribute( "added", userLdif.getString( "added" ) );
%> <tr>
  <td><logic:notEqual name="added" value="true">
   <form action="addUserToGroup.do" method="post" style="margin:0px;">
   <input type="hidden" name="groupdn" value="<eip:ldif name="groupLdif"/>"/>
   <input type="hidden" name="userdn" value="<eip:ldif name="userLdif"/>"/>
   <input type="submit" name="submit.add" value="Add"/>
   </form>
  </logic:notEqual></td>
  <td><eip:ldif name="userLdif" property="uid"/></td>
  <td><eip:ldif name="userLdif" property="employeeNumber"/></td>
  <td><eip:ldif name="userLdif" property="cn"/></td>
  <td><eip:ldif name="userLdif" property="sn"/></td>
  <td><eip:ldif name="userLdif" property="officeName"/></td>
  <td><eip:ldif name="userLdif" property="depertmentName"/></td>
 </tr>
<%
}
%></table>
<hr/>
</body>
</html>
