<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="JP.co.ksi.ldap.LdifProperties"%>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- callResult.jsp -->
<%-- 
oauthの習作
RSSサービスを呼んだ後の結果画面
RSSを表示する
2012/05/10 Kac
 --%>

<%@page import="com.sun.syndication.feed.synd.SyndEntry"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/default.css" />
</head>
<body>
<bean:parameter name="serviceURL" id="serviceURL" value=""/>
<span>サービス[<bean:write name="serviceURL"/>]をコールしました。</span>
<hr/>
<html:form action="/rss" method="post">
<table class="formtable">
 <tr>
  <th>serviceURL</th>
  <td>
   <input type="text" name="serviceURL" value="<bean:write name="serviceURL"/>" style="width:100%;"/>
  </td>
 </tr>
 <tr>
  <td><input type="submit" style="width:100%;"/></td>
 </tr>
</table>
</html:form>
<a href="index.jsp">[Top]</a>
<hr/>

<logic:present name="syndFeed">
<div><a href="<bean:write name="syndFeed" property="link"/>"><bean:write name="syndFeed" property="title"/></a></div>
<div style="font-size:80%;"><bean:write name="syndFeed" property="publishedDate" format="yyyy/MM/dd HH:mm:ss"/> - <bean:write name="syndFeed" property="author"/></div>
<ul>
 <logic:iterate id="entry" name="syndFeed" property="entries" type="com.sun.syndication.feed.synd.SyndEntry">
 <li><a href="<bean:write name="entry" property="link"/>"><bean:write name="entry" property="title"/></a><br/>
 <div style="font-size:80%;"><bean:write name="entry" property="publishedDate" format="yyyy/MM/dd HH:mm:ss"/> - <bean:write name="entry" property="author"/></div>
 <logic:present name="entry" property="description">
  <bean:define id="description" name="entry" property="description"/>
  <div><bean:write name="description" property="value" filter="false"/></div>
 </logic:present>
 <logic:iterate id="content" name="entry" property="contents" type="com.sun.syndication.feed.synd.SyndContent">
  <div><bean:write name="content" property="value" filter="false"/></div>
 </logic:iterate>
 <br/>
 </li>
 </logic:iterate>
</ul>
<hr/></logic:present>
<logic:equal name="result" value="APL_ERR">
<bean:write name="result"/>
<a href="">oauth認証を再度行う</a>
<hr/></logic:equal>
<jsp:include page="/inc/error.jsp" flush="true"/>
</body>
</html>
