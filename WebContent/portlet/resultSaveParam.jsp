<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="JP.co.ksi.ldap.LdifProperties"%>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- portlet/resultSaveParam.jsp -->
<%-- 
serviceParam保存後の画面
2012/07/11 Kac
 --%>
<html>
<body>
<logic:equal name="result" value="APL_OK">
<bean:write name="result" /><br/>
<a href="googleCalendarEventsList.do">googleCalendarEventsList.do</a>
</logic:equal>
<logic:notEqual name="result" value="APL_OK">
<a href="history:back();"><bean:message key="link.back"/></a>
<jsp:include page="/inc/error.jsp"/>
</logic:notEqual>
</body>
</html>