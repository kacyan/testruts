<%@page import="JP.co.ksi.util.HtmlFilter"%>
<%@page import="org.apache.naming.java.javaURLContextFactory"%>
<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="java.io.InputStreamReader"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.net.HttpURLConnection"%>
<%@ page import="java.net.URLConnection"%>
<%@ page import="java.net.URL"%>
<%@ page import="java.net.Proxy"%>
<%@ page import="java.net.InetSocketAddress"%>
<%--
URLConnectionの習作
2014/02/04 Kac XSS脆弱性対応 printStackTraceの出力をHTMLエスケープする
2010/12/14 Kac SSLを使う時は以下の設定を行う事
(1)サーバ証明書をcacertsにインポートする
　keytool -import -keystore %JAVA_HOME%\jre\lib\securiry\cacerts -alias hoge -file hoge.cer
(2)tomcat起動時にkeystoreとしてcacertsを指定する
　CATALINA_OPTS="$CATALINA_OPTS -Djavax.net.ssl.trustStore=$JAVA_HOME/jre/lib/security/cacerts"
　CATALINA_OPTS="$CATALINA_OPTS -Djavax.net.ssl.trustStorePassword=changeit"

proxyについては、プログラム側でopenConnection()する時にも指定できるようになった(java-1.5以上)
--%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<bean:parameter name="url" id="url" value=""/>
<bean:parameter name="proxyHost" id="proxyHost" value=""/>
<bean:parameter name="proxyPort" id="proxyPort" value=""/>
<bean:parameter name="proxyUse" id="proxyUse" value=""/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<h1></h1>
<hr>
<form action="" method="post">
<input type="text" name="url" value="<bean:write name="url"/>" style="width:100%;"><br>

<input type="checkbox" name="proxyUse" value="true" <%=proxyUse.equals("true")?"checked":"" %>>proxy:
<input type="text" name="proxyHost" value="<bean:write name="proxyHost"/>" size="40">
<input type="text" name="proxyPort" value="<bean:write name="proxyPort"/>" size="10"><br>
<input type="submit">
</form>
<hr>
<%
try
{
	URL	u= new URL( url );
	URLConnection	con= null;
	if( proxyUse.equals("true") )
	{//	use proxy
		InetSocketAddress	sa= new InetSocketAddress( proxyHost, Integer.parseInt(proxyPort) );
		Proxy	proxy= new Proxy( Proxy.Type.HTTP, sa );
		con= u.openConnection( proxy );
	}
	else
	{//	no proxy
		con= u.openConnection();
	}
	if( con instanceof HttpURLConnection )
	{
		HttpURLConnection	http= (HttpURLConnection)con;
		int	responseCode= http.getResponseCode();
		String	contentType= http.getContentType();
		String	charset="utf-8";
		if( contentType.matches( ".*charset=.*" ) )
		{
			charset= contentType.replaceAll( ".*charset=", "" );
		}
		String	contentEncoding= http.getContentEncoding();
		int	contentLength= http.getContentLength();
%><table border="1">
 <tr>
  <th>responseCode</th><td><%=responseCode %></td>
 </tr>
 <tr>
  <th>responseMessage</th><td><%=http.getResponseMessage() %></td>
 </tr>
 <tr>
  <th>contentEncoding</th><td><%=contentEncoding %></td>
 </tr>
 <tr>
  <th>contentType</th><td><%=contentType %></td>
 </tr>
 <tr>
  <th>charset</th><td><%=charset %></td>
 </tr>
 <tr>
  <th>contentLength</th><td><%=contentLength %></td>
 </tr>
</table>
<%
		BufferedReader	in= new BufferedReader( new InputStreamReader( http.getInputStream(), charset ) );
		String	line= in.readLine();
		while( line != null )
		{
			out.println( line );
			line= in.readLine();
		}
		in.close();
	}//	if end
}//	try end
catch( Exception e )
{
	java.io.StringWriter sw= new java.io.StringWriter();
	e.printStackTrace( new PrintWriter( sw ) );
	out.print( "<pre>");
	out.print( HtmlFilter.parseTag( sw.toString() ) );
	out.print("</pre>");
}
%>
</body>
</html>
