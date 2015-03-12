<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- update:2015/02/27 -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
<h2>JavaScript Test</h2>
<hr/>
<script type="text/javascript">
function openWnd( url )
{
	open( url, "", "menubar=1,resizable=1,toolbar=0,location=1,scrollbars=1,status=0,screenX=0,screenY=0,left=0,top=0,width=320=,height=240" );
}
function openWndNoMenu( url )
{
	open( url, "", "menubar=0,resizable=1,toolbar=0,location=1,scrollbars=1,status=0,screenX=0,screenY=0,left=0,top=0,width=320=,height=240" );
}
</script>
<ul>
<li><a href="javascript:openWnd('about.jsp')">about.jsp with menu</a></li>
<li><a href="javascript:openWndNoMenu('about.jsp')">about.jsp no menu</a></li>
</ul>

<hr/>
<script type="text/javascript">
var date = new Date();
document.write( "date()" +"<br/>" );
document.write( "toGMTString()="+ date.toGMTString() +"<br/>" );
document.write( "toLocaleString()="+ date.toLocaleString() +"<br/>" );
document.write( "toUTCString()="+ date.toUTCString() +"<br/>" );
document.write( "<br/>" );

date.setTime(0);
document.write( "date(0)" +"<br/>" );
document.write( "toGMTString()="+ date.toGMTString() +"<br/>" );
document.write( "toLocaleString()="+ date.toLocaleString() +"<br/>" );
document.write( "toUTCString()="+ date.toUTCString() +"<br/>" );
document.write( "<br/>" );

date.setTime(-1);
document.write( "date(-1)" +"<br/>" );
document.write( "toGMTString()="+ date.toGMTString() +"<br/>" );
document.write( "toLocaleString()="+ date.toLocaleString() +"<br/>" );
document.write( "toUTCString()="+ date.toUTCString() +"<br/>" );
document.write( "<br/>" );

date.setTime(-62167219200000);
document.write( "date(-62167219200000)" +"<br/>" );
document.write( "toGMTString()="+ date.toGMTString() +"<br/>" );
document.write( "toLocaleString()="+ date.toLocaleString() +"<br/>" );
document.write( "toUTCString()="+ date.toUTCString() +"<br/>" );
document.write( "<br/>" );

</script>
<hr/>
</body>
</html>
