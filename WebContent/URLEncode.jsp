<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.net.URLDecoder"%>
<%--
URLエンコード・デコード
2012/05/10 Kac
--%>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<bean:parameter name="srcText" id="srcText" value=""/>
<bean:parameter name="mode" id="mode" value=""/>
<bean:define id="charset" value="utf-8"/>
<bean:define id="dstText" value=""/>
<logic:equal name="mode" value="URLエンコード">
<bean:define id="dstText" value="<%=URLEncoder.encode(srcText,charset) %>"/>
</logic:equal>
<logic:equal name="mode" value="URLデコード">
<bean:define id="dstText" value="<%=URLDecoder.decode(srcText,charset) %>"/>
</logic:equal>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<h1></h1>
<hr>
<%
int	width= Math.max( srcText.length(), dstText.length() );
width= Math.max( 10, width );
%>
<form action="" method="post" style="margin-bottom:0px;">
<table>
 <tr>
  <th style="width:8em;">元の文字列</th>
  <td style="width:<%=String.valueOf(width) %>em;"><input type="text" name="srcText" value="<bean:write name="srcText"/>" style="width:100%;"></td>
 </tr>
 <tr>
  <td colspan="2">
   <input type="submit" name="mode" value="URLエンコード">
   <input type="submit" name="mode" value="URLデコード">
  </td>  
 </tr>
 <tr>
  <th><bean:write name="mode"/></th>
  <td><input type="text" name="dstText" value="<bean:write name="dstText"/>" style="width:100%;"></td>
 </tr>
</table>
</form>
<hr>
</body>
</html>
