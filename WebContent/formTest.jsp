<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Locale"%>
<%@ page import="jp.co.ksi.eip.commons.struts.StrutsUtil"%>
<%@ page import="jp.co.ksi.eip.commons.taglib.Option"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="eip.tld" prefix="eip" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- formTest.jsp -->
<%--
2012/03/21 Kac
ComboBoxやRadioButton用タグリブを用意してみた。
あらかじめ、List<Option> を作っておき、このデータに基づいてHTMLを生成する
こんな感じになる
<select name="{paramName}">
<option value="{option.value}">{option.displayValue}</option>
</select>
多言語対応する場合、option.displayValueをメッセージリソースのキーとして扱いメッセージを取得する
この場合、List<Option>をコンテキストにセットしておけば良いかも
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>

<%
ArrayList<Option>	list= (ArrayList<Option>)application.getAttribute("comboTest");
if( list == null )
{
	list= new ArrayList<Option>();
	list.add( new Option( "uid", "combo.uid" ) );
	list.add( new Option( "cn", "combo.cn" ) );
	list.add( new Option( "sn", "combo.sn" ) );
	list.add( new Option( "wsoUserNameKana", "combo.wsoUserNameKana" ) );
	list.add( new Option( "officeNumber", "combo.officeNumber" ) );
	list.add( new Option( "officeName", "combo.officeName" ) );
	list.add( new Option( "departmentNumber", "combo.departmentNumber" ) );
	list.add( new Option( "departmentName", "combo.departmentName" ) );
	list.add( new Option( "companyNumber", "combo.companyNumber" ) );
	list.add( new Option( "employeeTypeNumber", "combo.employeeTypeNumber" ) );
	list.add( new Option( "employeeNumber", "combo.employeeNumber" ) );
	list.add( new Option( "mail", "combo.mail" ) );
	list.add( new Option( "wsoPwdChangedTime", "combo.wsoPwdChangedTime" ) );
	list.add( new Option( "wsoPwdExpireTime", "combo.wsoPwdExpireTime" ) );
	list.add( new Option( "wsoLastLogin", "combo.wsoLastLogin" ) );
	application.setAttribute("comboTest",list);
}
%>
<bean:parameter name="attrName" id="attrName" value=""/>
<span>eip:radio</span>
<form action="formTest.jsp">
<eip:radio name="comboTest" paramName="attrName" messageKey="true" checkedValue="<%=attrName %>"/><br/>
<input type="submit">
</form>
<hr/>

<span>eip:combobox</span>
<form action="formTest.jsp" method="post">
<eip:combobox name="comboTest" paramName="attrName" messageKey="true" selectedValue="<%=attrName %>"/>
<input type="submit"/>
</form>
<hr>
<%
ArrayList<Option>	list2= new ArrayList<Option>();
list2.add( new Option( String.valueOf(Calendar.SUNDAY), "日" ) );
//list2.add( new Option("1","日") );
list2.add( new Option("2","月") );
list2.add( new Option("3","火") );
list2.add( new Option("4","水") );
list2.add( new Option("5","木") );
list2.add( new Option("6","金") );
list2.add( new Option("7","土") );
request.setAttribute( "combo2", list2 );
String	dayOfWeek= request.getParameter("dayOfWeek");
Calendar	cal= Calendar.getInstance();
Locale	locale= request.getLocale();
if( ( dayOfWeek == null ) || dayOfWeek.equals("") )
{
	dayOfWeek= ""+ cal.get( Calendar.DAY_OF_WEEK );
}
%><ksi:filter><%=dayOfWeek %> - <%=cal.getDisplayName( Calendar.DAY_OF_WEEK, Calendar.LONG, locale ) %></ksi:filter>
<span>eip:combobox</span>
<form action="formTest.jsp" method="get">
<eip:combobox name="combo2" paramName="dayOfWeek" selectedValue="<%=dayOfWeek %>"/>
<input type="submit"/>
<input type="reset"/>
</form>
<hr/>

<span>logic:iterate</span>
<form>
<select multiple="multiple" >
<logic:iterate id="option" name="combo2">
<option value="<bean:write name="option" property="name"/>">
<bean:write name="option" property="displayValue"/>
</option>
</logic:iterate>
</select>
</form>
<hr/>
</body>
</html>
