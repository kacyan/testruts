<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="net.arnx.jsonic.JSON"%>
<%@ page import="JP.co.ksi.util.HtmlFilter"%>
<%@ page import="jp.co.ksi.eip.commons.util.StringUtil"%>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- portlet/facebook.jsp -->
<%-- 
Facebookポートレット
2012/07/24 Kac
 --%>

<jsp:useBean id="responseData" scope="request" type="java.lang.String"/>
<style type="text/css">
.crop{
	float:left;
	overflow:hidden;  /* これが重要 */
	font-size: 80%;
	}
.crop img{
	margin:-33px -0px -32px -0px;　/* 位置コントロール */
	}
</style>
<table width="100%" class="part-frame" cellspacing="1" cellpadding="0" border="1">
 <tr>
  <th>
   <table width="100%" cellspacing="0" cellpadding="0" border="0">
    <tr>
     <td class="part-header" align="left"></td>
     <td class="part-header" align="right">
      <a href="<%=request.getContextPath() %>/portlet/googleCalendarEventsList.LoadConfig.do?sid=googleCalendar"><bean:message key="link.config"/></a>
     </td>
    </tr>
   </table>
  </th>
 </tr>
 <tr>
  <td class="part-body" >
   <bean:write name="oauth2" property="updateDate"/><logic:present parameter="refresh">(refresh)</logic:present>
<%
SimpleDateFormat	sdf1= new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'+0000'" );
SimpleDateFormat	sdf2= new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
Properties	props= JSON.decode( responseData, Properties.class );
%>
<table>
 <tr>
  <th>&nbsp;</th>
  <th colspan="2"></th>
 </tr>
<%
for( int i= 0; i < 25; i++ )
{
	String	type= props.getProperty( "data."+ i +".type", "" );
	String	message= props.getProperty( "data."+ i +".message", "" );
	message= StringUtil.substrByteLen( message, 100, "..." );
	String	caption= props.getProperty( "data."+ i +".caption", "" );
	caption= StringUtil.substrByteLen( caption, 100, "..." );
	String	fromName= props.getProperty( "data."+ i +".from.name", "" );
	String	picture= props.getProperty( "data."+ i +".picture", "" );
	String	link= props.getProperty( "data."+ i +".link", "" );
	String	likesCount= props.getProperty( "data."+ i +".likes.count", "" );
	String	createTime= props.getProperty( "data."+ i +".created_time", "" );
	String	updateTime= props.getProperty( "data."+ i +".updated_time", "" );
	updateTime= sdf2.format( sdf1.parse( updateTime ) );
	String	story= props.getProperty( "data."+ i +".story", "" );
	if( type.equals("photo") )
	{
%> <tr>
  <td><%=i %></td>
  <td>
<% if( link.length() > 0 ){ %>
<a href="<%=link %>"><% if( picture.length() > 0 ){ %><img src="<%=picture %>"/><% }else{ %>link<% } %></a>
<% } %>
  </td>
  <td><p class="crop">
  <img src="0SX5c2iiXAb.png" alt="いいね" /><%=HtmlFilter.parseTag( likesCount) %>
  </p>
  </td>
  <td>
   <%=HtmlFilter.parseTag( fromName ) %>&nbsp;-&nbsp;<%=HtmlFilter.parseTag( updateTime ) %><br/>
   <%=HtmlFilter.parseTag( caption ) %>
   <%=HtmlFilter.parseTag( message ) %><br/>
   <%=HtmlFilter.parseTag( story ) %>
  </td>
 </tr>
<%
	}//	if end
}
%></table>
<jsp:include page="/inc/error.jsp" flush="true"/>
  </td>
 </tr>
</table>
<%
String[]	keys= new String[props.size()];
keys= (String[])props.keySet().toArray( keys );
java.util.Arrays.sort( keys );
for( int i= 0; i < keys.length; i++ )
{
	String	value= props.getProperty( keys[i] );
%><%=HtmlFilter.parseTag(keys[i]) %>=<%=HtmlFilter.parseTag(value) %><br/>
<%
}
%>
