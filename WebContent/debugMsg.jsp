<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="Shift_JIS" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="JP.co.ksi.util.HtmlFilter"%>
<%@ page import="jp.co.ksi.eip.commons.struts.KsiMessageResources"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="org.apache.struts.util.MessageResources" %>
<%@ page import="java.net.URL"%>
<html>
<body>
&nbsp;<a href="javascript:history.back();">–ß‚é</a>
&nbsp;<a href="check.jsp">check.jsp</a>
&nbsp;<a href="about.jsp">about.jsp</a>
&nbsp;<a href="setLocale.jsp">setLocale.jsp</a>
<hr>
<%
URL	logClsUrl= null;
Log	log= LogFactory.getLog( getClass() );
ClassLoader	cl= log.getClass().getClassLoader();
if( cl != null )
{
	logClsUrl= cl.getResource( log.getClass().getName() );
}
%>
log class url=<%=logClsUrl %><br>
servlet=<%=application.getServerInfo() %><br>
<h1 style="margin: 0px;">Debug MessageResources(<%=Globals.MESSAGES_KEY %>)</h1>
<%
MessageResources	resources= (MessageResources)application.getAttribute( Globals.MESSAGES_KEY );
if( resources instanceof KsiMessageResources )
{
	KsiMessageResources propResource= (KsiMessageResources)resources;
	HashMap<?,?>	msgs= propResource.getMessages();
	String[]	key= {};
	key= (String[])msgs.keySet().toArray( key );
	Arrays.sort( key );
	for( int i= 0; i < key.length; i++ )
	{
		%><%=HtmlFilter.parseTag(key[i]) %>=<%=HtmlFilter.parseTag(msgs.get(key[i]).toString()) %><br><%
	}
}
%>
</body>
</html>