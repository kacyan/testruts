<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="JP.co.ksi.util.HtmlFilter"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="javax.naming.Binding"%>
<%@ page import="javax.naming.Context"%>
<%@ page import="javax.naming.directory.InitialDirContext"%>
<%@ page import="javax.naming.directory.DirContext"%>
<%@ page import="javax.naming.directory.Attributes"%>
<%@ page import="javax.naming.directory.Attribute"%>
<%@ page import="javax.naming.NamingEnumeration"%>
<%@ page import="javax.naming.ldap.LdapContext"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- jndi.jsp -->
<%--
2014/02/04 XSS脆弱性対応
2011/01/24 jndi lookupの習作
 --%>
<%! void list( DirContext ctx, String jndiName, JspWriter out ) throws Exception
{
	NamingEnumeration	enumeration= ctx.listBindings( jndiName );
	while( enumeration.hasMore() )
	{
		try
		{
			Object	obj= enumeration.next();
			if( obj instanceof Binding )
			{
				Binding	b= (Binding)obj;
				String	name= "";
				if( jndiName.matches("^ldap:.*") )
				{//	LDAP
					int	start= jndiName.indexOf("ldap://")+7;
					int	end= jndiName.indexOf( "/", start )+1;
					String	prefix= jndiName.substring( 0, end );
					String	suffix= jndiName.substring( end );
					name= prefix + b.getName() +","+ suffix;
				}
				else if( jndiName.matches("^dns:.*") )
				{//	DNS
					int	start= jndiName.indexOf("dns://")+6;
					int	end= jndiName.indexOf( "/", start )+1;
					String	prefix= jndiName.substring( 0, end );
					String	suffix= jndiName.substring( end );
					name= prefix + b.getName() +"."+ suffix;
				}
				else if( jndiName.matches(".*[/:]") )
				{
					name= jndiName + b.getName();
				}
				else
				{
					name= jndiName +"/"+ b.getName();
				}
				Object	value= b.getObject();
				if( value instanceof Context )
				{//	Context　リンク表現にする
					Context	sub= (Context)value;
					out.println( "<tr><td><a href=\"jndi.jsp?jndiName="+ HtmlFilter.parseTag( name ) +"\">"+ HtmlFilter.parseTag( name ) +"</></td><td>"+ HtmlFilter.parseTag( value.toString() ) +"</td><td>"+"</td></tr>" );
				}
				else if( value instanceof javax.sql.DataSource )
				{//	データソースは太字表示
					out.println( "<tr><td><b>"+ HtmlFilter.parseTag( name ) +"</b></td><td>"+ HtmlFilter.parseTag( value.toString() ) +"</td></tr>" );
				}
				else
				{
					out.println( "<tr><td>"+ HtmlFilter.parseTag( name ) +"</td><td>"+ HtmlFilter.parseTag( value.toString() ) +"</td></tr>" );
				}
			}
			else
			{
				out.println( "<tr><td colspan=\"2\">"+ HtmlFilter.parseTag( obj.toString() ) +"</td></tr>" );
			}
		}
		catch( Exception e )
		{
			out.println( "<tr><td colspan=\"2\" style=\"color:red;\">"+ HtmlFilter.parseTag( e.toString() ) +"</td></tr>" );
		}
	}
}
%>
<%
String	jndiName= request.getParameter( "jndiName" );
if( jndiName == null )	jndiName= "java:comp/env";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
th{
	background: teal;
}
</style>
</head>

<body>
<h2 style="margin:0px;">jndi.jsp - InitialDirContext.listBinding()</h2>
<form style="margin:0px;">
<input type="text" name="jndiName" value="<%=HtmlFilter.parseTag(jndiName) %>" style="width:100%;">
<input type="submit" value="listBinding">
</form>
<hr>
<%
try
{

	InitialDirContext	ctx= new InitialDirContext();
	try
	{
		Attributes	attrs= ctx.getAttributes( jndiName );
%><table border="1">
 <tr>
  <th>AttrName</th><th>AttrValue</th>
 </tr>
<%
		NamingEnumeration	enumeration= attrs.getAll();
		while( enumeration.hasMore() )
		{
			Attribute	attr= (Attribute)enumeration.next();
			NamingEnumeration	enumValues= attr.getAll();
			while( enumValues.hasMore() )
			{
%> <tr>
  <td><%=HtmlFilter.parseTag( attr.getID() ) %></td>
  <td><%=HtmlFilter.parseTag( enumValues.next().toString() ) %></td>
 </tr>
<%
			}
		}
%></table>
<%
	}
	catch( Exception e )
	{
		out.println( HtmlFilter.parseTag( e.toString() ) );
	}
%><table border="1">
<tr><th>JNDI Name</th><th>Object</th></tr>
<%	//	サブエントリーを列挙する
	list( ctx, jndiName, out );
}
catch( Exception e )
{
	java.io.StringWriter sw= new java.io.StringWriter();
	e.printStackTrace( new java.io.PrintWriter( sw ) );
%><pre><%=HtmlFilter.parseTag( sw.toString() ) %></pre>
<%
}
%></table>
<hr>

</body>
</html>
