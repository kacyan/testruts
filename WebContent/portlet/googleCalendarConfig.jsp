<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="JP.co.ksi.ldap.LdifProperties"%>
<%@ page import="JP.co.ksi.util.HtmlFilter"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="net.arnx.jsonic.JSON"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="jp.co.ksi.eip.commons.taglib.Option"%>
<%@ taglib uri="eip.tld" prefix="eip" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- portlet/googleCalendarConfig.jsp -->
<%-- 
GoogleCalendarポートレットの設定
2012/07/11 Kac
 --%>
<jsp:useBean id="serviceParam" scope="request" type="java.util.Properties"/>
<%! String getProperty( Properties props, String name )
{
	return HtmlFilter.parseTag( props.getProperty( name, "" ) );
}
%>
<bean:parameter name="sid" id="sid" value=""/>
<%
ArrayList<Option>	periodList= new ArrayList<Option>();
periodList.add( new Option( "today", "today" ) );
periodList.add( new Option( "this week", "this week" ) );
periodList.add( new Option( "this month", "this month" ) );
periodList.add( new Option( "1 week", "after 1 week" ) );
periodList.add( new Option( "1 month", "after 1 month" ) );
pageContext.setAttribute( "periodList", periodList );
String	period= getProperty(serviceParam,"period");
%>
<html>
<body>
<h2>Google Calendar - Events.List のパラメータ設定</h2>
<form action="googleCalendarEventsList.SaveConfig.do" method="post">
<input type="hidden" name="sid" value="<bean:write name="sid"/>">
<table>
 <tr>
  <th>calendarId</th>
  <td><input type="text" name="calendarId" value="<%=getProperty(serviceParam,"calendarId") %>"/></td>
 </tr>
 <tr>
  <th>maxResults</th>
  <td><input type="text" name="maxResults" value="<%=getProperty(serviceParam,"maxResults") %>"/></td>
 </tr>
 <tr>
  <th>orderBy</th>
  <td><input type="text" name="orderBy" value="<%=getProperty(serviceParam,"orderBy") %>"/></td>
 </tr>
 <tr>
  <th>period</th>
  <td>
   <eip:combobox name="periodList" paramName="period" selectedValue="<%=period %>" />
  </td>
 </tr>
 <tr>
  <th>singleEvents</th>
  <td><input type="text" name="singleEvents" value="<%=getProperty(serviceParam,"singleEvents") %>"/></td>
 </tr>
 <tr>
  <td colspan="2" align="center"><input type="submit" name="submit.save" value="<bean:message key="button.ok"/>"/></td>
 </tr>
</table>
</form>
パラメータの意味については、こちらも参照して下さい。
<a href="https://developers.google.com/google-apps/calendar/v3/reference/events/list" target="_blank">Google Calendar API</a>
<hr/>
<logic:notEqual name="result" value="APL_OK">
<jsp:include page="/inc/error.jsp"/>
</logic:notEqual>
</body>
</html> 