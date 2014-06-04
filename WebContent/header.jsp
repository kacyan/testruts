<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="java.util.Enumeration"%>
<%@ page autoFlush="true"  isErrorPage="true" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%=request.getMethod() %>
<%
Enumeration	enumeration= request.getHeaderNames();
while( enumeration.hasMoreElements() )
{
	String	name= (String)enumeration.nextElement();
	String	value= request.getHeader( name );
%><%=name %>: <%=value %>
<%
}
%>
