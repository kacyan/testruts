<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<%@ page import="JP.co.ksi.eip.Auth"%>
<%@ page import="JP.co.ksi.util.HtmlFilter"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%--
oauth2/index.jsp
2012/08/10 Kac
--%>
<jsp:useBean id="appConfig" scope="application" type="java.util.Properties"/>
<%
Auth	auth= Auth.getAuth( request, appConfig );
String	requestURL= request.getRequestURL().toString();
String	requestURI= request.getRequestURI();
String	callbackURL= requestURL.replaceAll( requestURI, "" ) + request.getContextPath();
//	callbackURL += "/oauth2/getAccessToken.do";
callbackURL += "/oauth2/facebookLogin";
%><html>
<head>
<style type="text/css">
input
{
	width: 100%;
}
</style>
</head>
<body>
<logic:present name="auth" scope="session">
<bean:write name="auth" property="uid"/> でログイン中
<a href="googleCalendar.do">Googleカレンダー</a>
</logic:present>
<logic:notPresent name="auth" scope="session">
<a href="<%=application.getContextPath() %>/basic.jsp">basic.jsp</a>
</logic:notPresent>
<a href="facebook.jsp">facebook.jsp</a>
<hr/>
<form action="init.do" method="post">
<table border="1" width="100%">
 <tr>
  <th>serviceURL</th>
  <td><input type="text" name="serviceURL" value="https://graph.facebook.com/me"/></td>
 </tr>
 <tr>
  <th>authURL</th>
  <td><input type="text" name="authURL" value="https://www.facebook.com/dialog/oauth"/></td>
 </tr>
 <tr>
  <th>tokenURL</th>
  <td><input type="text" name="tokenURL" value="https://graph.facebook.com/oauth/access_token"/></td>
 </tr>
 <tr>
  <th>response_type</th>
  <td><input type="text" name="response_type" value="code"/></td>
 </tr>
 <tr>
  <th>client_id</th>
  <td><input type="text" name="client_id" value="346343205441904"/></td>
 </tr>
 <tr>
  <th>client_secret</th>
  <td><input type="text" name="client_secret" value="e24f361c59ee4ffa476dae6085222e3f""/></td>
 </tr>
 <tr>
  <th>redirect_uri</th>
  <td><input type="text" name="redirect_uri" value="<%=callbackURL %>"/></td>
 </tr>
 <tr>
  <th>scope</th>
  <td><input type="text" name="scope" value="email"/></td>
 </tr>
 <tr>
  <th>state</th>
  <td><input type="text" name="state" value="profile"/></td>
 </tr>
 <tr>
  <th>approval_prompt</th>
  <td><input type="text" name="approval_prompt" value="force"/></td>
 </tr>
 <tr>
  <th>access_type</th>
  <td><input type="text" name="access_type" value="offline"/></td>
 </tr>
 <tr>
  <th>grant_type</th>
  <td><input type="text" name="grant_type" value="authorization_code"/></td>
 </tr>
</table>
<input type="submit"/>
</form>
<hr/>
<!-- Facebook OAuth2 -->
<form action="init.do" method="post">
<input type="hidden" name="serviceURL" value="https://graph.facebook.com/"/>
<input type="hidden" name="authURL" value="https://www.facebook.com/dialog/oauth"/>
<input type="hidden" name="tokenURL" value="https://graph.facebook.com/oauth/access_token"/>
<input type="hidden" name="response_type" value="code"/>
<input type="hidden" name="client_id" value="346343205441904"/>
<input type="hidden" name="client_secret" value="e24f361c59ee4ffa476dae6085222e3f""/>
<input type="hidden" name="redirect_uri" value="http://ticket.os.ksi.co.jp/testruts/oauth2/getAccessToken.do"/>
<input type="hidden" name="scope" value="read_stream"/>
<input type="hidden" name="state" value=""/>
<input type="hidden" name="access_type" value="offline"/>
<input type="hidden" name="approval_prompt" value="force"/>
<input type="hidden" name="grant_type" value="authorization_code"/>
<input type="submit" value="Facebook read_stream"/>
</form>

<!-- Google Calendar API -->
<form action="init.do" method="post">
<logic:present name="auth" scope="session">
 <logic:equal name="auth" property="uid" value="kac">
<input type="hidden" name="serviceURL" value="https://www.googleapis.com/calendar/v3/calendars/kacyan@gmail.com/events"/>
 </logic:equal>
 <logic:notEqual name="auth" property="uid" value="kac">
<input type="hidden" name="serviceURL" value="https://www.googleapis.com/calendar/v3/users/me/calendarList"/>
 </logic:notEqual>
</logic:present>
<logic:notPresent name="auth" scope="session">
<input type="hidden" name="serviceURL" value="https://www.googleapis.com/calendar/v3/users/me/calendarList"/>
</logic:notPresent>
<input type="hidden" name="authURL" value="https://accounts.google.com/o/oauth2/auth"/>
<input type="hidden" name="tokenURL" value="https://accounts.google.com/o/oauth2/token"/>
<input type="hidden" name="response_type" value="code"/>
<input type="hidden" name="client_id" value="494593688356-pllemu8erp8a82p9cn8qhfhjvplo90fd.apps.googleusercontent.com"/>
<input type="hidden" name="client_secret" value="ubiPnDb_43KliRI7bFiZt-Uh""/>
<input type="hidden" name="redirect_uri" value="<%=callbackURL %>"/>
<input type="hidden" name="scope" value="https://www.googleapis.com/auth/calendar https://www.googleapis.com/auth/calendar.readonly"/>
<input type="hidden" name="state" value="profile"/>
<input type="hidden" name="access_type" value="offline"/>
<input type="hidden" name="approval_prompt" value="force"/>
<input type="hidden" name="grant_type" value="authorization_code"/>
<input type="submit" value="Google Calendar API"/>
</form>
<hr/>
<ul>
<li>ログイン</li>
<li></li>
</ul>
<hr/>
<a href="https://www.facebook.com/dialog/oauth?client_id=346343205441904&redirect_uri=<%=HtmlFilter.parseTag("http://ticket.os.ksi.co.jp/testruts/oauth2/getAccessToken.do") %>">facebook</a>
<%--
<iframe src="https://www.facebook.com/plugins/like.php?href=http://www.ksi.co.jp/"
        scrolling="no" frameborder="0"
        style="border:none; width:450px; height:80px">
</iframe>
 --%>
</body>
</html>
