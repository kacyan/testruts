<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<%@ page import="JP.co.ksi.eip.Auth"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%--
index.jsp
2012/07/24 Kac
--%>
<jsp:useBean id="appConfig" scope="application" type="java.util.Properties"/>
<%
Auth	auth= Auth.getAuth( request, appConfig );
String	requestURL= request.getRequestURL().toString();
String	requestURI= request.getRequestURI();
String	callbackURL= requestURL.replaceAll( requestURI, "" ) + request.getContextPath();
callbackURL += "/oauth2/getAccessToken.do";
%><html>
<head>
<style type="text/css">
input
{
	width: 100%;
}
.crop{
	float:left;
	overflow:hidden;  /* これが重要 */
	font-size: 80%;
	}
.crop img{
	margin:-33px -0px -32px -0px;　/* 位置コントロール */
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
<div>
<p class="crop"><img src="http://static.ak.fbcdn.net/rsrc.php/v2/yY/r/0SX5c2iiXAb.png" alt="いいね" /><%=(int)(Math.random()*1000) %></p>
</div><br/>
<hr/>

<!-- Google Calendar API -->
<form action="googleCalendarEventsList.LoadConfig.do" method="post">
<input type="hidden" name="sid" value="googleCalendar"/>
<input type="submit" value="googleCalendarEventsList.LoadConfig.do"/>
</form>
<form action="oauthRequest.do" method="post">
<input type="hidden" name="sid" value="googleCalendar"/>
<input type="submit" value="oauthRequest.do"/>
</form>
<form action="googleCalendarEventsList.do" method="post">
<input type="hidden" name="sid" value="googleCalendar"/>
<input type="submit" value="googleCalendarEventsList.do"/>
</form>

<hr/>

<!-- Facebook API -->
<form action="oauthRequest.do" method="post">
<input type="hidden" name="sid" value="facebook"/>
<input type="submit" value="oauthRequest.do"/>
</form>
<form action="facebook.do" method="get">
<input type="hidden" name="sid" value="facebook"/>
<input type="submit" value="facebook.do"/>
</form>
<form action="facebookPhoto.do" method="get">
<input type="submit" value="facebookPhoto.do"/>
</form>

<hr/>

</body>
</html>
