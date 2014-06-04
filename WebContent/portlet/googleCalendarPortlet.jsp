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
<!-- portlet/googleCalendarPortlet.jsp -->
<%-- 
GoogleCalendarポートレット
2012/07/11 Kac
 --%>
<jsp:useBean id="responseData" scope="request" type="java.lang.String"/>
<table width="100%" class="part-frame" cellspacing="1" cellpadding="0" border="1">
 <tr>
  <th>
   <table width="100%" cellspacing="0" cellpadding="0" border="0">
    <tr>
     <td class="part-header" align="left"></td>
     <td class="part-header" align="right"><a href="<%=request.getContextPath() %>/portlet/googleCalendarEventsList.LoadConfig.do?sid=googleCalendar"><bean:message key="link.config"/></a></td>
    </tr>
   </table>
  </th>
 </tr>
 <tr>
  <td class="part-body" >
   <bean:write name="oauth2" property="updateDate"/><logic:present parameter="refresh">(refresh)</logic:present>
<logic:equal name="result" value="APL_OK">
   <table border="1">
    <tr>
<%--
     <th>summary</th><th>start</th><th>end</th><th>description</th>
 --%>
     <th>start</th><th>summary</th><th>description</th>
    </tr>
<%
SimpleDateFormat	sdfLong= new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
SimpleDateFormat	sdfShort= new SimpleDateFormat( "yyyy-MM-dd" );
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
	String	description= props.getProperty( "items."+ i +".description" );
	String	location= props.getProperty( "items."+ i +".location" );
	
	try
	{
		Date	date= sdfLong.parse( start );
		start= outFormatter.format( date );
	}
	catch( Exception e )
	{
		try
		{
			Date	date= sdfShort.parse( start );
			start= outFormatter.format( date );
		}
		catch( Exception e2 )
		{
		}
	}
	try
	{
		Date	date= sdfLong.parse( end );
		end= outFormatter.format( date );
	}
	catch( Exception e )
	{
		try
		{
			Date	date= sdfShort.parse( end );
			end= outFormatter.format( date );
		}
		catch( Exception e2 )
		{
		}
	}
%>    <tr>
<%--
     <td><a href="<%=htmlLink %>"><%=HtmlFilter.parseTag(summary) %></a></td>
     <td><%=start %></td>
     <td><%=end %></td>
     <td><%=HtmlFilter.parseTag(description) %></td>
 --%>
     <td><%=start %></td>
     <td><a href="<%=htmlLink %>"><%=HtmlFilter.parseTag(summary) %></a></td>
     <td><%=HtmlFilter.parseTag(location) %>&nbsp;<%=HtmlFilter.parseTag(description) %></td>
    </tr>
<%
}
%>   </table>
</logic:equal>
<jsp:include page="/inc/error.jsp" flush="true"/>
  </td>
 </tr>
</table>
