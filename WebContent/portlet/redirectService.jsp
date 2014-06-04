<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="JP.co.ksi.ldap.LdifProperties"%>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- portlet/redirectService.jsp -->
<%-- 
所定のサービス呼出にリダイレクトする
oauthConfigのserviceURL
2012/07/11 Kac
 --%>
<jsp:useBean id="oauthConfig" scope="request" type="jp.co.ksi.incubator.portlet.OAuthConfig" />
<%
request.getRequestDispatcher( oauthConfig.getServiceURL() ).include( request, response );
%>