<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page autoFlush="true"  isErrorPage="true" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%--
Desknetsのスケジュール登録画面を呼び出す習作
2011/12/28
--%>
<%
SimpleDateFormat	sdf1= new SimpleDateFormat( "yyyy/MM/dd" );
SimpleDateFormat	sdf2= new SimpleDateFormat( "yyyyMMdd" );
Date	date= new Date();
%>
<html>
<body>

https://wso.ap.ksi.co.jp/intra/dnet/dnet.exe?page=schadd&gid=&id=499&bpage=schmonth&bdate=20120101&caldate=20120101&date=20120106
<br/>
<a href="https://wso.ap.ksi.co.jp/intra/dnet/dnet.exe?page=schadd&gid=&bpage=schmonth&date=<%=sdf2.format( date ) %>"><%=sdf1.format( date ) %></a>
<hr/>
http://ardbeg.dev.ksi.co.jp/dnet/mdnet/mdnet.exe?id=@C1Pr|jBLB0HHgH0J70XR&mn=1&sn=3
<br/>
<a href="http://ardbeg.dev.ksi.co.jp/dnet/mdnet/mdnet.exe?mn=1&sn=3&startdate=20111230">mobile</a>
</body>
</html>
