<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="jp.co.ksi.testruts.Const"%>
<%@ page import="jp.co.ksi.incubator.JCSTest"%>
<%@ page import="jp.co.ksi.incubator.JcsIElementEventHandler"%>
<%@ page import="org.apache.jcs.JCS"%>
<%@ page import="org.apache.jcs.engine.behavior.IElementAttributes"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- JcsTest.jsp -->
<%-- 
JCSの習作：キャッシュのイベントハンドラを調査してみる
 --%>
<bean:parameter name="name" id="name" value="default"/>
<bean:parameter name="value" id="value" value=""/>
<bean:parameter name="cmd" id="cmd" value=""/>
<%
//	キャッシュ取得
JCS	jcs= JCSTest.getJCS();

if( cmd.equals("put") )
{//	キャッシュにput
	jcs.put( name, value );
}
else if( cmd.equals("get") )
{//	キャッシュからget
	value= (String)jcs.get( name );
	if( value == null )	value= "";
	pageContext.setAttribute("value",value);
}
else
{//	初期状態？
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<div><bean:write name="cmd"/> <%=value %></div>
<hr>
<form action="JcsTest.jsp" method="post">
<input type="text" name="name" value="<bean:write name="name"/>"/>
<input type="text" name="value" value="<bean:write name="value"/>"/>
<input type="submit" name="cmd" value="put"/>
<br/>
<input type="submit" name="cmd" value="get"/>
</form>
<hr>
<%=jcs.getStats() %>
</body>
</html>
