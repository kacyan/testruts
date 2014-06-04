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
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- ldap.jsp -->
<!-- 2011/01/24 -->
<jsp:useBean id="appConfig" scope="application" type="java.util.Properties" />
<bean:parameter name="basedn" id="basedn" value=""/>
<bean:parameter name="searchText" id="searchText" value=""/>
<bean:parameter name="scope" id="scope" value="<%=String.valueOf(LdapManager.SUBTREE_SCOPE) %>"/>
<bean:parameter name="attribute" id="attribute" multiple="true" value=""/>
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
<h2 style="margin:0px;">ldap.jsp</h2>
<form action="ldap.jsp" method="post">
<table style="margin:0px;">
 <tr>
  <th>basedn</th>
  <td style="width:100%;"><input type="text" name="basedn" value="<bean:write name="basedn"/>" style="width:100%;"></td>
 </tr>
 <tr>
  <th>searchText</th>
  <td><input type="text" name="searchText" value="<bean:write name="searchText"/>" style="width:100%;"></td>
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
  <td><input type="submit" value="searchEntry"></td>
 </tr>
</table>
</form>
<hr>
<%
LdapManager	ldap= null;
ArrayList	ldifs= new ArrayList();	//	LDIF一覧
ArrayList	attrs= new ArrayList();	//	属性一覧
try
{
	ldap= new LdapManager( appConfig );
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
%><table border="1">
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
  <td><%=ldif.getDN() %></td>
<%
	for( int j= 0; j < attribute.length; j++ )
	{
%>  <td><%=HtmlFilter.parseTag( ldif.getString( attribute[j] ) ) %></td>
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
