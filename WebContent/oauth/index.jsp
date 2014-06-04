<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- index.jsp -->
<%-- 
oauthの習作
2012/05/08 Kac
 --%>
<%
String	requestURL= request.getRequestURL().toString();
String	requestURI= request.getRequestURI();
String	callbackURL= requestURL.replaceAll( requestURI, "" ) + request.getContextPath();
callbackURL += "/oauth/getAccessToken.do";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="shortcut icon" href="<%=request.getContextPath() %>/oauth/favicon.png">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/default.css" />
</head>
<body>
<img src="oauth_logo.png" style="float:left;">

<div>OAuthコンシューマの習作</div>
<hr/>

<form action="init.do" method="post">
 <input type="hidden" name="consumer_key" value="dj0yJmk9ODhNZm9hOGtsZVZPJmQ9WVdrOU5YQXhWbXhVTjJrbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD01Nw--" />
 <input type="hidden" name="consumer_secret" value="2f04a4a0505fed0f1ac5f9222339d631cb658240" />
 <input type="hidden" name="oauth_callback" value="oob" />
 <input type="hidden" name="scope" value="" />
 <input type="hidden" name="requestTokenURL" value="https://auth.login.yahoo.co.jp/oauth/v2/get_request_token" />
 <input type="hidden" name="authorizeTokenURL" value="" />
 <input type="hidden" name="accessTokenURL" value="https://auth.login.yahoo.co.jp/oauth/v2/get_token" />
 <input type="hidden" name="serviceURL" value=""/>
 <input type="submit" value="Yahoo OAuth"/>
</form>

<form action="init.do" method="post">
 <input type="hidden" name="consumer_key" value="test-st.ksi.co.jp" />
 <input type="hidden" name="consumer_secret" value="Y4hF4rRyDtIy-YJuVyn9Ia2S" />
 <input type="hidden" name="oauth_callback" value="<%=callbackURL %>" />
 <input type="hidden" name="scope" value="https://mail.google.com/mail/feed/atom" />
 <input type="hidden" name="requestTokenURL" value="https://www.google.com/accounts/OAuthGetRequestToken" />
 <input type="hidden" name="authorizeTokenURL" value="https://www.google.com/accounts/OAuthAuthorizeToken" />
 <input type="hidden" name="accessTokenURL" value="https://www.google.com/accounts/OAuthGetAccessToken" />
 <input type="hidden" name="serviceURL" value="https://mail.google.com/mail/feed/atom/anywhere""/>
 <input type="submit" value="Google OAuth"/>
</form>

<form action="init.do" method="post">
 <input type="hidden" name="consumer_key" value="wtLWm9pDTjTaeQ==" />
 <input type="hidden" name="consumer_secret" value="VHtXrZ4ZVQNCJNzsv9aOjGHIEYU=" />
 <input type="hidden" name="oauth_callback" value="oob" />
 <input type="hidden" name="scope" value="read_public" />
 <input type="hidden" name="requestTokenURL" value="https://www.hatena.com/oauth/initiate" />
 <input type="hidden" name="authorizeTokenURL" value="https://www.hatena.ne.jp/oauth/authorize" />
 <input type="hidden" name="accessTokenURL" value="https://www.hatena.com/oauth/token" />
 <input type="hidden" name="serviceURL" value="http://b.hatena.ne.jp/atom/feed"/>
 <input type="submit" value="はてな OAuth"/>
</form>

<form action="init.do" method="post">
 <input type="hidden" name="consumer_key" value="VAXk8lBtsUPgCVOA9vENA" />
 <input type="hidden" name="consumer_secret" value="PRElIMWOBYPduICFW9FZY7ERM4y7kbrzNh2GZsFDKo" />
 <input type="hidden" name="oauth_callback" value="<%=callbackURL %>" />
 <input type="hidden" name="scope" value="" />
 <input type="hidden" name="requestTokenURL" value="https://api.twitter.com/oauth/request_token" />
 <input type="hidden" name="authorizeTokenURL" value="https://api.twitter.com/oauth/authorize" />
 <input type="hidden" name="accessTokenURL" value="https://api.twitter.com/oauth/access_token" />
 <input type="hidden" name="serviceURL" value="http://search.twitter.com/search.rss?q=nhk"/>
 <input type="submit" value="Twitter OAuth"/>
</form>

<logic:present name="oauth" scope="session" >
<bean:write name="oauth" scope="session"/><br/>
<form action="rss.do" method="post">
<input type="text" name="serviceURL" value="<bean:write name="serviceURL" ignore="true"/>" style="width:100%;"/>
<input type="submit" value="RSSサービスを呼び出す"/>
</form>
</logic:present>
<a href="loadToken.do">loadToken</a>

</body>
</html>
