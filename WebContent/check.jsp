<%@page import="JP.co.ksi.util.HtmlFilter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<%@ page isErrorPage="true" %>
<%@ page session="true" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.net.URL"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%--
Chekc JSP for Servlet API 2.4 / JSP 1.2
2014/02/04 XSS脆弱性対応
2013/03/07 Kac@KSI com.sun.BASE64Decoderを使うのを辞めた。
--%>
<html>
<head>
<style type="text/css">
<!--
h1
{
	color: orange;
	margin-bottom: 0px;
	margin-top: 0px;
}
h2
{
	color: teal;
	margin-bottom: 0px;
	margin-top: 0px;
}
table
{
}
th
{
	background: #00a8a9;
    color: white;
	border-color: #a0a0a0;
	border-style: solid;
	border-width: 1px;
}
td
{
    background: white;
	border-color: #a0a0a0;
	border-style: solid;
	border-width: 1px;
}
.line
{
	color:gray;
	height:5;
}
 -->
</style>
</head>

<body>
<h1 align="center"><a name="top">Check JSP</a></h1>

<a href="javascript:history.back()">Back</a><br>

<a href="#RequestParameters">RequestParameters</a>&nbsp;
<a href="#RequestAttributes">RequestAttributes</a>&nbsp;
<a href="#HttpServletRequest">HttpServletRequest</a>&nbsp;
<a href="#RequestHeader">RequestHeader</a>&nbsp;
<a href="#HttpSession">HttpSession</a>&nbsp;
<a href="#ServletContext">ServletContext</a>&nbsp;
<a href="#ServletConfig">ServletConfig</a>&nbsp;
<a href="#SystemProperties">SystemProperties</a>&nbsp;

<hr class="line">
<pre><%
if( exception != null )
{
	exception.printStackTrace( new PrintWriter(out) );
}
%></pre>
<h2><a name="RequestParameters">RequestParameters</a></h2>
<%=request.getParameterMap().size() %>
<%= request.getParameterMap().size() %>
<table width="100%">
<%
{
	Enumeration<?>	enumeration= request.getParameterNames();
	while( enumeration.hasMoreElements() )
	{
		String	name= (String)enumeration.nextElement();
%> <tr>
  <th><%=name %></th>
  <td>
<%
		String[]	value= request.getParameterValues( name );
		for( int i= 0 ; i < value.length; i++ )
		{
%>     <%=HtmlFilter.parseTag( value[i] ) %><br/>
<%
		}
%>  </td>
 </tr>
<%
	}
}
%>
</table>
<form action="check.jsp" method="get" style="margin:0px;">
GET:<input type="text" name="getParam" value="">
<input type="submit">
</form>
<form action="check.jsp" method="post" style="margin:0px;">
POST:<input type="text" name="postParam" value="">
<input type="submit">
</form>

<hr class="line">

<h2><a name="RequestAttributes">RequestAttributes</a></h2>
<table width="100%">
 <tr>
  <th>name</th>
  <th>value</th>
  <th>class</th>
  <th>file</th>
 </tr>
<%{
	Enumeration<?>	enumeration= request.getAttributeNames();
	while( enumeration.hasMoreElements() )
	{
		String	name= (String)enumeration.nextElement();
		Object	value= request.getAttribute( name );
		URL		url= null;
		ClassLoader	cl= value.getClass().getClassLoader();
		if( cl != null )
		{
			url= cl.getResource( value.getClass().getName() );
		}
%>
 <tr>
  <td><%=name %></td>
  <td><%=value %></td>
  <td><%=value.getClass().getName() %></td>
  <td><%=url %></td>
 </tr>
<%
	}
}%>
</table>

<hr class="line">

<h2><a name="HttpServletRequest">HttpServletRequest</a></h2>

<table width="100%">
 <tr>
  <th>AuthType</th>
  <td><%=request.getAuthType() %></td>
 </tr>
 <tr>
  <th>ContextPath</th>
  <td><%=request.getContextPath() %></td>
 </tr>
 <tr>
  <th>Cookies</th>
  <td><%
javax.servlet.http.Cookie[]    cookies= request.getCookies();
if( cookies != null )
{
	for( int i= 0; i < cookies.length; i++ )
	{
		out.println( cookies[i].getName() + "=" + cookies[i].getValue() + "<br>" );
	}
}
%>
  </td>
 </tr>
 <tr>
  <th>Method</th>
  <td><%=HtmlFilter.parseTag( request.getMethod() ) %></td>
 </tr>
 <tr>
  <th>PathInfo</th>
  <td><%=HtmlFilter.parseTag( request.getPathInfo() ) %></td>
 </tr>
 <tr>
  <th>PathTranslated</th>
  <td><%=HtmlFilter.parseTag( request.getPathTranslated() ) %></td>
 </tr>
 <tr>
  <th>QueryString</th>
  <td><%=HtmlFilter.parseTag( request.getQueryString() ) %></td>
 </tr>
 <tr>
  <th>RemoteUser</th>
  <td><%=HtmlFilter.parseTag( request.getRemoteUser() ) %></td>
 </tr>
 <tr>
  <th>RequestedSessionId</th>
  <td><%=HtmlFilter.parseTag( request.getRequestedSessionId() ) %></td>
 </tr>
 <tr>
  <th>RequestURI</th>
  <td><%=HtmlFilter.parseTag( request.getRequestURI() ) %></td>
 </tr>
 <tr>
  <th>RequestURL</th>
  <td><%=HtmlFilter.parseTag( request.getRequestURL().toString() ) %></td>
 </tr>
 <tr>
  <th>ServletPath</th>
  <td><%=HtmlFilter.parseTag( request.getServletPath() ) %></td>
 </tr>
 <tr>
  <th>isRequestedSessionIdFromCookie</th>
  <td><%=request.isRequestedSessionIdFromCookie() %></td>
 </tr>
 <tr>
  <th>isRequestedSessionIdFromURL</th>
  <td><%=request.isRequestedSessionIdFromURL() %></td>
 </tr>
 <tr>
  <th>isRequestedSessionIdValid</th>
  <td><%=request.isRequestedSessionIdValid() %></td>
 </tr>
 <tr>
  <th>UserPrincipal</th>
  <td>
  <%
  java.security.Principal	principal= request.getUserPrincipal();
  if( principal != null )
  {
  %>  <%=request.getUserPrincipal().getClass() %><br/>
  <%=HtmlFilter.parseTag( request.getUserPrincipal().toString() ) %><br/>
  <%
  }
  %>  </td>
 </tr>
 <tr><td></td></tr>
 <tr>
  <th>Attributes</th>
  <td><%{
	Enumeration<?> enumeration= request.getAttributeNames();
	while( enumeration.hasMoreElements() )
	{
		String  name= (String)enumeration.nextElement();
		out.println( HtmlFilter.parseTag( name ) + "=" + HtmlFilter.parseTag( request.getAttribute( name ).toString() ) + "<br>" );
	}
}%>
  </td>
 </tr>
 <tr>
  <th>CharacterEncoding</th>
  <td><%=HtmlFilter.parseTag( request.getCharacterEncoding() ) %></td>
 </tr>
 <tr>
  <th>ContentLength</th>
  <td><%=request.getContentLength() %></td>
 </tr>
 <tr>
  <th>ContentType</th>
  <td><%=HtmlFilter.parseTag( request.getContentType() ) %></td>
 </tr>
 <tr>
  <th>Locale</th>
  <td><%=HtmlFilter.parseTag( request.getLocale().toString() ) %></td>
 </tr>
 <tr>
  <th>Protocol</th>
  <td><%=HtmlFilter.parseTag( request.getProtocol() ) %></td>
 </tr>
 <tr>
  <th>RemoteAddr</th>
  <td><%=HtmlFilter.parseTag( request.getRemoteAddr() ) %></td>
 </tr>
 <tr>
  <th>RemoteHost</th>
  <td><%=HtmlFilter.parseTag( request.getRemoteHost() ) %></td>
 </tr>
 <tr>
  <th>Scheme</th>
  <td><%=HtmlFilter.parseTag( request.getScheme() ) %></td>
 </tr>
 <tr>
  <th>ServerName</th>
  <td><%=HtmlFilter.parseTag( request.getServerName() ) %></td>
 </tr>
 <tr>
  <th>ServerPort</th>
  <td><%=request.getServerPort() %></td>
 </tr>
 <tr>
  <th>isSecure</th>
  <td><%=request.isSecure() %></td>
 </tr>
</table>

<hr class="line">

<h2><a name="RequestHeader">HttpServletRequest.getHeader()</a></h2>
<table width="100%">
<%{
    Enumeration<?> enumeration= request.getHeaderNames();
    while( enumeration.hasMoreElements() )
    {
        out.println( "<tr>" );
        String      name= (String)enumeration.nextElement();
        out.println( "<th>" + HtmlFilter.parseTag( name ) + "</th>" );
        out.println( "<td>" );
        Enumeration<?> enumValue= request.getHeaders( name );
        while( enumValue.hasMoreElements() )
        {
        	String	value= (String)enumValue.nextElement();
            out.println( HtmlFilter.parseTag( value ) + "<br>" );
        }
        out.println( "</td>" );
        out.println( "</tr>" );
    }
}%>
</table>

<hr class="line">

<h2><a name="HttpSession">HttpSession</a></h2>
<%
SimpleDateFormat	sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
%>
<table width="100%">
 <tr>
  <th>createTime</th>
  <td><%=sdf.format( new Date(session.getCreationTime()) ) %></td>
 </tr>
 <tr>
  <th>id</th>
  <td><%=HtmlFilter.parseTag( session.getId() ) %></td>
 </tr>
 <tr>
  <th>lastAccessedTime</th>
  <td><%=sdf.format( new Date(session.getLastAccessedTime()) ) %></td>
 </tr>
 <tr>
  <th>maxInactiveInterval</th>
  <td><%=session.getMaxInactiveInterval() %></td>
 </tr>
 <tr>
  <th>isNew</th>
  <td><%=session.isNew() %></td>
 </tr>
</table>
<h2><a name="SessionAttributes">SessionAttributes</a></h2>
<table width="100%">
 <tr>
  <th>name</th>
  <th>value</th>
  <th>class</th>
  <th>file</th>
 </tr>
<%{
	Enumeration<?>	enumeration= session.getAttributeNames();
	while( enumeration.hasMoreElements() )
	{
		String	name= (String)enumeration.nextElement();
		Object	value= session.getAttribute( name );
		URL		url= null;
		ClassLoader	cl= value.getClass().getClassLoader();
		if( cl != null )
		{
			url= cl.getResource( value.getClass().getName() );
		}
%>
 <tr>
  <td><%=HtmlFilter.parseTag( name )%></td>
  <td><%=HtmlFilter.parseTag( value.toString() ) %></td>
  <td><%=value.getClass().getName() %></td>
  <td><%=url %></td>
 </tr>
<%
	}
}%>
</table>

<hr class="line">

<h2><a name="ServletContext">ServletContext</a></h2>
<table width="100%">
 <tr>
  <th>InitParameterNames</th>
  <td><%{
        Enumeration<?> enumeration= application.getInitParameterNames();
        while( enumeration.hasMoreElements() )
        {
            String  name= (String)enumeration.nextElement();
            out.println( HtmlFilter.parseTag( name ) + "=" + HtmlFilter.parseTag( application.getInitParameter( name ) ) + "<br>" );
        }
    }%></td>
 </tr>
 <tr>
  <th>MajorVarsion</th>
  <td><%=application.getMajorVersion() %></td>
 </tr>
 <tr>
  <th>MinorVarsion</th>
  <td><%=application.getMinorVersion() %></td>
 </tr>
 <tr>
  <th>RealPath(/)</th>
  <td><%=HtmlFilter.parseTag( application.getRealPath( "/" ) ) %></td>
 </tr>
 <tr>
  <th>Resource(/)</th>
  <td><%=application.getResource( "/" ) %></td>
 </tr>
 <tr>
  <th>ServerInfo</th>
  <td><%=HtmlFilter.parseTag( application.getServerInfo() ) %></td>
 </tr>
 <tr>
  <th>ServletContextName</th>
  <td><%=HtmlFilter.parseTag( application.getServletContextName() ) %></td>
 </tr>
</table>

<hr class="line">

<h2><a name="ContextAttributes">ContextAttributes</a></h2>
<table width="100%">
 <tr>
  <th>name</th>
  <th>value</th>
  <th>class</th>
  <th>file</th>
 </tr>
<%{
	Enumeration<?>	enumeration= application.getAttributeNames();
	while( enumeration.hasMoreElements() )
	{
		String	name= (String)enumeration.nextElement();
		Object	value= application.getAttribute( name );
		URL		url= null;
		ClassLoader	cl= value.getClass().getClassLoader();
		if( cl != null )
		{
			url= cl.getResource( value.getClass().getName() );
		}
%>
 <tr>
  <td><%=HtmlFilter.parseTag( name ) %></td>
  <td><%=HtmlFilter.parseTag( value.toString() ) %></td>
  <td><%=value.getClass().getName() %></td>
  <td><%=url %></td>
 </tr>
<%
	}
}%>
</table>

<hr class="line">

<h2><a name="ServletConfig">ServletConfig</a></h2>
<table width="100%">
 <tr>
  <th>InitParameterNames</th>
  <td>
<%{
	Enumeration<?> enumeration= config.getInitParameterNames();
	while( enumeration.hasMoreElements() )
	{
		String  name= (String)enumeration.nextElement();
		out.println( HtmlFilter.parseTag( name ) + "=" + HtmlFilter.parseTag( config.getInitParameter( name ) ) + "<br>" );
	}
}%>
  </td>
 </tr>
 <tr>
  <th>ServletName</th>
  <td><%=HtmlFilter.parseTag( config.getServletName() ) %></td>
 </tr>
</table>

<hr class="line">

<h2><a name="SystemProperties">System Properties</a></h2>
<table width="100%">
<%{
	Properties	props= System.getProperties();
	Object[]	keys= props.keySet().toArray();
	Arrays.sort( keys );
	for( int i= 0; i < keys.length; i++ )
	{
		String	name= (String)keys[i];
		String	value= props.getProperty( name );
%>
 <tr>
  <th><%=HtmlFilter.parseTag( name ) %></th>
  <td><%=HtmlFilter.parseTag( value ) %></td>
 </tr>
<%
	}
}%>
</table>

<hr class="line">

<a href="javascript:history.back()">Back</a>

<div align="right"><i>check.jsp / KSI</i></div>

</body>
</html>
