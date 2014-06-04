<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page autoFlush="true"  isErrorPage="true" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<html>
<body>
<ksi:filter><%=getServletInfo() %></ksi:filter><br/>
<ksi:filter><%=getServletName() %></ksi:filter><br/>
<logic:present parameter="hoge" >
<bean:write name="hoge"/>
</logic:present>
<logic:notPresent parameter="hoge">
hoge not found.<br/>
</logic:notPresent>
</body>
</html>