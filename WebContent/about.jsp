<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="Shift_JIS" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.jar.*" %>
<%--
変更：2012/05/23
 --%>
<%	//	WARのバージョン情報をManifestから取得する
	String		version= "unknown";
	String		build= "unknown";
	InputStream	in= application.getResourceAsStream( "/META-INF/MANIFEST.MF" );
	if( in != null )
	{
		Manifest		mf= new java.util.jar.Manifest( in );
		try
		{
			Attributes	attr= mf.getMainAttributes();
			version= attr.getValue( java.util.jar.Attributes.Name.SPECIFICATION_VERSION );
			build= attr.getValue( java.util.jar.Attributes.Name.IMPLEMENTATION_VERSION );
		}
		catch( Exception e )
		{
			version= "Exception";
			build= e.toString();
		}
		in.close();
	}
%>
<%	//	EARのバージョン情報をManifestから取得する
	String	earVersion= "unknown";
	String	earBuild= "unknown";
	in= application.getResourceAsStream( "../META-INF/MANIFEST.MF" );
	if( in != null )
	{
		Manifest		mf= new java.util.jar.Manifest( in );
		try
		{
			Attributes	attr= mf.getMainAttributes();
			earVersion= attr.getValue( java.util.jar.Attributes.Name.SPECIFICATION_VERSION );
			earBuild= attr.getValue( java.util.jar.Attributes.Name.IMPLEMENTATION_VERSION );
		}
		catch( Exception e )
		{
			version= "Exception";
			build= e.toString();
		}
		in.close();
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
.header
{
background:#00a8a9;
	color:white;
	font-weight:bold;
}
.title
{
	font-family:Courier New;
	color:orange;
	font-size:24pt;
	font-weight:bold;
}
</style>
</head>

<body bgcolor="white" text="black">
<h2 align="center" class="title">about</h2>
<a href="javascript:history.back()">Back</a>

<hr>

<center>
<table border="1" cellpadding="5" cellspacing="1" bgcolor="white">
<tr>
	<td class="header">EAR version</td>
	<td><%= earVersion %> - <%= earBuild %></td>
</tr>
<tr>
	<td class="header">WAR version</td>
	<td><%= version %> - <%= build %></td>
</tr>
<tr>
	<td class="header">servlet container</td>
	<td><%= application.getServerInfo() %></td>
</tr>
<tr>
	<td class="header">servlet version</td>
	<td>
		Servlet API <%= application.getMajorVersion() %>.<%= application.getMinorVersion() %>
		&nbsp;,&nbsp;
		JSP <%= JspFactory.getDefaultFactory().getEngineInfo().getSpecificationVersion() %>
	</td>
</tr>
<tr>
	<td class="header">java vendor</td>
	<td>
		<%= System.getProperty( "java.vendor" ) %>
	</td>
</tr>
<tr>
	<td class="header">java version</td>
	<td>
		<%= System.getProperty( "java.version" ) %><br>
	</td>
</tr>
<tr>
	<td class="header">java vm info</td>
	<td>
		<small><%= System.getProperty( "java.vm.info" ) %></small>
	</td>
</tr>
<tr>
	<td class="header">context root</td>
	<td><%= application.getRealPath( "/" ) %></td>
</tr>
</table>
</center>

<hr>

<center>
<table border="1" cellpadding="5" cellspacing="1" bgcolor="white">
<caption><b>WEB-INF/lib</b></caption>
<%	//	WEB-INF/libのjarファイルを表示する
	File	f= new File( application.getRealPath( "/WEB-INF/lib" ) );
	String[]	files= f.list();
	for( int i= 0; i < files.length; i++ )
	{
		String	jarVersion= "none";
		String	jarBuild= "none";
		try
		{
			Manifest	mf = null;
			String	file= "/WEB-INF/lib/" + files[i];
			JarInputStream	jar = new JarInputStream( application.getResourceAsStream( file ) );
			mf = jar.getManifest();
			jar.close();

			Attributes	attr= mf.getMainAttributes();
			jarVersion= attr.getValue( java.util.jar.Attributes.Name.SPECIFICATION_VERSION );
			jarBuild= attr.getValue( java.util.jar.Attributes.Name.IMPLEMENTATION_VERSION );

		}
		catch( Exception e )
		{
			jarVersion= "Exception";
			jarBuild= e.toString();
		}

%><tr>
	<td class="header"><%= files[i] %></td>
	<td><%= jarVersion %> - <%= jarBuild %></td>
</tr><%
	}
%>
</table>
</center>

<hr>

<a href="javascript:history.back()">Back</a>
<div align="right"><i>about.jsp / KSI</i></div>

</body>
</html>
