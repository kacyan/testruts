<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page isErrorPage="true" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Locale"%>
<%@ page import="org.apache.struts.action.ActionMessages"%>
<%@ page import="org.apache.struts.action.ActionMessage"%>
<%@ page import="org.apache.struts.util.MessageResources"%>
<%@ page import="org.apache.struts.Globals"%>
<%@ page import="jp.co.ksi.eip.commons.struts.StrutsUtil"%>
<%@ page import="JP.co.ksi.util.HtmlFilter"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%--
エラー画面
2011/12/13 多言語対応

html:errosタグはパラメータのHTMLエスケープを行わないのでXSS脆弱性がある
仕方ないので、独自処理を実装した。
Strutsのエラーオブジェクトのリクエスト属性名：org.apache.struts.action.ERROR
Strutsのリソースオブジェクトのセッション属性名：org.apache.struts.action.MESSAGE
--%>
<%
Locale	locale= (Locale)StrutsUtil.getLocale( request );
MessageResources	resources= (MessageResources)application.getAttribute( Globals.MESSAGES_KEY );
ActionMessages	messages= (ActionMessages)request.getAttribute( Globals.ERROR_KEY );
if( messages != null )
{
	Iterator<?>	it= messages.get();
	while( it.hasNext() )
	{
		ActionMessage	msg= (ActionMessage)it.next();
		Object[]	values= msg.getValues();
		if( values == null )	values= new Object[0];
		ArrayList<String>	list= new ArrayList<String>();
		for( int i= 0; i < values.length; i++ )
		{
			list.add( HtmlFilter.parseTag( values[i].toString() ) );
		}
		String	text= resources.getMessage( locale, msg.getKey(), list.toArray() );
%><%=text %><br>
<%
	}
}
%>