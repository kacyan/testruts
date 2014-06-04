<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="Shift_JIS" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.Locale"%>
<%
String	lang= request.getParameter("lang");
String	country= request.getParameter("country");
String	remove= request.getParameter("remove");
if( ( remove != null ) && !remove.equals("") )
{//	locale‚ðœ‹Ž‚·‚é
	session.removeAttribute( Globals.LOCALE_KEY );
}
else if( ( lang != null ) && !lang.equals("") )
{
	if( ( country != null ) && !country.equals("") )
	{
		session.setAttribute( Globals.LOCALE_KEY, new Locale( lang, country ) );
	}
	else
	{
		session.setAttribute( Globals.LOCALE_KEY, new Locale( lang, "" ) );
	}
}
%>
<html>
<body>
&nbsp;<a href="javascript:history.back();">–ß‚é</a>
&nbsp;<a href="check.jsp">check.jsp</a>
&nbsp;<a href="about.jsp">about.jsp</a>
&nbsp;<a href="debugMsg.jsp">debugMsg.jsp</a>
<hr>
<h4 style="margin: 0px;">Session</h4>
<%
Locale locale= (Locale)session.getAttribute( Globals.LOCALE_KEY );
%>
<%=Globals.LOCALE_KEY %>=<%=locale %><br>
<hr>
<form action="" method="post">
lang: <input type="text" name="lang" value=""><br>
country:<input type="text" name="country" value=""><br>
<input type="submit">
<input type="submit" name="remove" value="íœ">
</form>
</body>
</html>