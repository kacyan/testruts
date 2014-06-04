<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="JP.co.ksi.ldap.LdifProperties"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="net.arnx.jsonic.JSON"%>
<%@ page import="java.util.Properties"%>
<%@ page import="JP.co.ksi.util.HtmlFilter"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- oauth2/googleCalendar.jsp -->
<%-- 
oauth-2.0を使ってgoogle Calendarにアクセスする習作(Events: list)
https://www.googleapis.com/calendar/v3/calendars/{calendarId}/events
2012/07/23 Kac
 --%>
<bean:define id="oauth" name="oauth2" scope="session"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/default.css" />
<style type="text/css">
table.oauth2
{
	border: none;
}
table.oauth2 tr th
{
	text-align: right;
}
table.oauth2 tr td
{
	width: 100%;
}
table.oauth2 tr td input
{
	width: 100%;
}
</style>
</head>
<bean:parameter name="calendarId" id="calendarId" value=""/>
<bean:parameter name="maxResults" id="maxResults" value=""/>
<bean:parameter name="orderBy" id="orderBy" value=""/>
<bean:parameter name="singleEvents" id="singleEvents" value="true"/>
<bean:parameter name="timeMin" id="timeMin" value=""/>
<bean:parameter name="timeMax" id="timeMax" value=""/>
<bean:parameter name="refresh" id="refresh" value=""/>
<body>
Google CalendarのEvents: list
<bean:write name="refresh" />
<hr/>
https://www.googleapis.com/calendar/v3/calendars/<bean:write name="calendarId"/>/events
<form action="googleCalendar.do" method="post">
<table class="oauth2">
 <tr>
  <th>calendarId</th>
  <td><input type="text" name="calendarId" value="<bean:write name="calendarId"/>"/></td>
 </tr>
 <tr>
  <th>maxResults</th>
  <td><input type="text" name="maxResults" value="<bean:write name="maxResults" />"/></td>
 </tr>
 <tr>
  <th>orderBy</th>
  <td><input type="text" name="orderBy" value="<bean:write name="orderBy" />"/></td>
 </tr>
 <tr>
  <th>singleEvents</th>
  <td><input type="text" name="singleEvents" value="<bean:write name="singleEvents" />"/></td>
 </tr>
 <tr>
  <th>timeMin</th><!-- 指定した日時以前のイベントはフィルタする -->
  <td><input type="text" name="timeMin" value="<bean:write name="timeMin" />"/></td>
 </tr>
 <tr>
  <th>timeMax</th><!-- 指定した日時以降のイベントはフィルタする 指定例 2012-06-01T00:00:00.000Z -->
  <td><input type="text" name="timeMax" value="<bean:write name="timeMax" />"/></td>
 </tr>
</table>
<input type="submit"/>
</form>
<hr/>
<logic:equal name="result" value="APL_OK">
<jsp:useBean id="responseData" scope="request" type="java.lang.String"/>
<table border="1">
 <tr>
  <th>summary</th><th>start</th><th>end</th><th>description</th>
 </tr>
<%
SimpleDateFormat	sdf= new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
SimpleDateFormat	outFormatter= new SimpleDateFormat( "yyyy/MM/dd HH:mm" );
Properties	props= JSON.decode( responseData, Properties.class );
for( int i= 0; true; i++ )
{
	String	start= props.getProperty( "items."+ i +".start.dateTime" );
	if( start == null )
	{
		start= props.getProperty( "items."+ i +".start.date" );
	}
	String	end= props.getProperty( "items."+ i +".end.dateTime" );
	if( end == null )
	{
		end= props.getProperty( "items."+ i +".end.date" );
	}
	String	summary= props.getProperty( "items."+ i +".summary" );
	if( summary == null )	break;
	String	id= props.getProperty( "items."+ i +".id" );
	String	htmlLink= props.getProperty( "items."+ i +".htmlLink" );
	
	try
	{
		Date	date= sdf.parse( start );
		start= outFormatter.format( date );
	}
	catch( Exception e )
	{
	}
	try
	{
		Date	date= sdf.parse( end );
		end= outFormatter.format( date );
	}
	catch( Exception e )
	{
	}
	
	String	description= props.getProperty( "items."+ i +".description" );
%> <tr>
  <td><a href="<%=htmlLink %>"><%=HtmlFilter.parseTag(summary) %></a></td>
  <td><%=start %></td><td><%=end %></td>
  <td><%=HtmlFilter.parseTag(description) %></td>
 </tr>
<%
}
%></table>
<hr/>
</logic:equal>
<logic:present name="responseData">
<bean:write name="responseData"/>
<hr/>
</logic:present>
<logic:equal name="result" value="APL_ERR">
<div>
<bean:write name="oauth2" property="refresh_token"/><br/>
<a href="refreshAccessToken.do">refreshAccessToken.do</a>
</div>
</logic:equal>
<jsp:include page="/inc/error.jsp" flush="true"/>
<bean:write name="oauth2" /><br/>
<%--
<jsp:include page="/check.jsp"/>
 --%>
</body>
</html>
