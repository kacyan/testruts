<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
<h2>JavaScript Test</h2>
<hr>
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
<hr>
</body>
</html>
