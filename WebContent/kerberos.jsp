<%@page import="java.io.PrintWriter"%>
<%@page import="javax.naming.directory.Attribute"%>
<%@page import="javax.naming.NamingEnumeration"%>
<%@page import="javax.naming.directory.Attributes"%>
<%@page import="jp.co.ksi.incubator.kerberos.LdapAction"%>
<%@page import="javax.naming.ldap.LdapContext"%>
<%@page import="javax.security.auth.Subject"%>
<%@page import="jp.co.ksi.incubator.kerberos.MyCallbackHandler"%>
<%@page import="javax.security.auth.login.LoginContext"%>
<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- kerberos.jsp -->
<%-- 2015/01/20 ケルベロス認証の習作--%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
<%
String	uid= request.getParameter( "uid" );
if( uid == null )	uid= "s911013";
String	pwd= request.getParameter( "pwd" );
if( pwd == null )	pwd= "";
%>
<form action="kerberos.jsp" method="post">
<input type="text" name="uid" value="<%=uid %>">
<input type="password" name="pwd" value="<%=pwd %>">
<input type="submit">
</form>
<hr>
<%
if( uid.equals( "" ) || pwd.equals( "" ) )
{
%></body>
</html>
<%
	return;
}
LoginContext	lc= null;
try
{
	//	ケルベロス認証
	lc= new LoginContext( "SampleClient", new MyCallbackHandler( uid, pwd ) );
	lc.login();
	Subject	subject= lc.getSubject();
%><pre>
<%=subject %>
</pre>
<hr/>
<%	
	//	LdapActionを呼出して、ldapに接続する
	LdapContext	ldap= Subject.doAs( subject, new LdapAction() );
	out.println( ldap +"<br/>" );

	Attributes	attrs= ldap.getAttributes( "CN=s911013,OU=User,OU=Account" );
	NamingEnumeration<?> enumeration= attrs.getAll();
	while( enumeration.hasMore() )
	{
		Object	obj= enumeration.next();
		if( obj instanceof Attribute )
		{
			Attribute attr= (Attribute)obj;
			out.println( attr +"<br/>" );
		}
	}
	
	//	後始末
	ldap.close();
}
catch( Exception e )
{
	out.print( "<pre>" );
	e.printStackTrace( new PrintWriter(out) );
	out.print( "</pre>" );
}
%>
<hr>

</body>
</html>
