<%@page import="java.net.URLEncoder"%>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="JP.co.ksi.util.HtmlFilter"%>
<%@ page import="JP.co.ksi.ldap.LdapManager"%>
<%@ page import="JP.co.ksi.ldap.LdifProperties"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Enumeration"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- ldap.jsp -->
<!-- 2014/09/11 -->
<%
String	url= request.getParameter( "url" );
if( url == null )	url= "";
String	principal= request.getParameter( "principal" );
if( principal == null )	principal= "";
String	credentials= request.getParameter( "credentials" );

if( credentials == null )	credentials= "";
String	basedn= request.getParameter( "basedn" );
if( basedn == null )	basedn= "";
String	searchText= request.getParameter( "searchText" );
if( searchText == null )	searchText= "";
String	scope= request.getParameter( "scope" );
if( scope == null )	scope= String.valueOf(LdapManager.ONELEVEL_SCOPE);
String[]	attribute= request.getParameterValues( "attribute" );
String	submit= request.getParameter( "submit" );
if( submit == null )	submit= "";

%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
th{
	background: teal;
}
</style>
<script type="text/javascript">
function setBaseDN( dn )
{
	ldapForm.basedn.value= dn;
	location.hash= "ldapForm";
}
</script>
</head>

<body>
<h2 style="margin:0px;">ldap.jsp</h2>
<form action="ldap.jsp" name="ldapForm" id="ldapForm" method="post">
<table style="margin:0px;">
 <tr>
  <th>url</th>
  <td style="width:100%;"><input type="text" name="url" value="${param.url}" style="width:100%;"></td>
 </tr>
 <tr>
  <th>principal</th>
  <td style="width:100%;"><input type="text" name="principal" value="${param.principal}" style="width:100%;"></td>
 </tr>
 <tr>
  <th>credentials</th>
  <td style="width:100%;"><input type="password" name="credentials" value="${param.credentials}" style="width:100%;"></td>
 </tr>
 <tr>
  <th>basedn</th>
  <td style="width:100%;"><input type="text" name="basedn" value="${param.basedn}" style="width:100%;"></td>
 </tr>
 <tr>
  <th>searchText</th>
  <td><input type="text" name="searchText" value="${param.searchText}" style="width:100%;"></td>
 </tr>
 <tr>
  <th>scope</th>
  <td>
   <select name="scope">
<%
Properties	selectedScope= new Properties();
selectedScope.setProperty(scope, "selected");
%>   <option value="<%=LdapManager.OBJECT_SCOPE %>" <%=selectedScope.getProperty( String.valueOf(LdapManager.OBJECT_SCOPE), "" ) %>>OBJECT_SCOPE</option>
   <option value="<%=LdapManager.ONELEVEL_SCOPE %>" <%=selectedScope.getProperty( String.valueOf(LdapManager.ONELEVEL_SCOPE), "" ) %>>ONELEVEL_SCOPE</option>
   <option value="<%=LdapManager.SUBTREE_SCOPE %>" <%=selectedScope.getProperty( String.valueOf(LdapManager.SUBTREE_SCOPE), "" ) %>>SUBTREE_SCOPE</option>
   </select>
  </td>
 </tr>
 <tr>
  <th>attribute</th>
  <td><input type="text" name="attribute" value="" style="width:100%;"></td>
 </tr>
 <tr>
  <td><input type="submit" name="submit" value="searchEntry"></td>
  <td><input type="submit" name="submit" value="viewEntry"></td>
 </tr>
</table>
</form>
<hr>
<% if( url.equals( "" ) ){ %>
<pre>
[ad-local]
 ldap://fscs1.ap.ksi.co.jp/DC=oa,DC=ksi,DC=co,DC=jp
 s000000@oa.ksi.co.jp
 password
 OU=User,OU=Account,DC=oa,DC=ksi,DC=co,DC=jp / OU=Group,DC=oa,DC=ksi,DC=co,DC=jp
 (displayName=名前*)
 
[beta-ldap]
 ldap://beta-ldap.dev.ksi.co.jp/o=WebSignOn,c=JP
 ou=People,ou=Master,o=WebSignOn,c=JP
 ou=Groups,ou=Master,o=WebSignOn,c=JP
 
[kpwso]
 ldap://172.16.98.111/
 ou=People,ou=Master,o=WebSignOn,c=JP
 ou=Groups,ou=Master,o=WebSignOn,c=JP

[nkhub]
 ldap://nkhub.ap.ksi.co.jp/
 
</pre>
<% } %>
<%
Properties	props= new Properties();
props.setProperty( "java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory" );
props.setProperty( "java.naming.provider.url", url );
props.setProperty( "java.naming.security.authentication", "simple" );
props.setProperty( "java.naming.security.principal", principal );
props.setProperty( "java.naming.security.credentials", credentials );
LdapManager	ldap= null;
ArrayList	ldifs= new ArrayList();	//	LDIF一覧
ArrayList	attrs= new ArrayList();	//	属性一覧
try
{
	ldap= new LdapManager( props );
	if( ldap.getContext() instanceof javax.naming.ldap.InitialLdapContext )
	{
		javax.naming.ldap.InitialLdapContext ldapCtx= (javax.naming.ldap.InitialLdapContext )ldap.getContext();
	}
	
	if( basedn.equals( "" ) )
	{
		basedn= ldap.getBaseDN();
	}
	if( submit.equals( "searchEntry" ) )
	{
		String[]	dn= ldap.searchEntry( basedn, searchText, Integer.parseInt(scope) );
		Arrays.sort( dn );
		for( int i= 0; i < dn.length; i++ )
		{
			LdifProperties	ldif= ldap.getEntry( dn[i] );
			ldifs.add( ldif );
			Enumeration	enumeration= ldif.keys();
			while( enumeration.hasMoreElements() )
			{
				String	attrName= (String)enumeration.nextElement();
				if( !attrs.contains( attrName ) )
				{
					attrs.add( attrName );
				}
			}
		}
	}
	else if( submit.equals( "viewEntry" ) )
	{
		LdifProperties	ldif= ldap.getEntry( basedn );
		ldifs.add( ldif );
		Enumeration	enumeration= ldif.keys();
		while( enumeration.hasMoreElements() )
		{
			String	attrName= (String)enumeration.nextElement();
			if( !attrs.contains( attrName ) )
			{
				attrs.add( attrName );
			}
		}
	}
	else
	{
		
	}
}
catch( Exception e )
{
	java.io.StringWriter sw= new java.io.StringWriter();
	e.printStackTrace( new java.io.PrintWriter( sw ) );
%><pre><%=HtmlFilter.parseTag( sw.toString() ) %></pre>
<%
}
finally
{//	後始末
	if( ldap != null )	ldap.close();

	attribute= new String[attrs.size()];
	attribute= (String[])attrs.toArray( attribute );
	Arrays.sort( attribute );
}
%>
<%=ldifs.size() %>
<table border="1">
 <tr>
  <th></th>
  <th>dn</th>
<%
for( int i= 0; i < attribute.length; i++ )
{
%>  <th><%=HtmlFilter.parseTag( attribute[i] ) %></th>
<%
}
%>
 </tr>
<%
for( int i= 0; i < ldifs.size(); i++ )
{
	LdifProperties	ldif= (LdifProperties)ldifs.get( i );
%> <tr>
  <td><%=i %></td>
  <td><a href="javascript:setBaseDN( '<%=ldif.getDN() %>');"><%=ldif.getDN() %></a></td>
<%
	for( int j= 0; j < attribute.length; j++ )
	{
%>  <td>
<%
		String[]	attrValue= ldif.get( attribute[j] );
		for( int k= 0; k < attrValue.length; k++ )
		{
%>   <%=HtmlFilter.parseTag( attrValue[k] ) %>
<%
		}
%>  </td>
<%
	}
%>
 </tr>
<%
}
%>
</table>
<hr>

</body>
</html>
