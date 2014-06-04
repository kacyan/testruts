<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">
function test( id )
{
	var item= document.getElementById( id );
	if( item.style.display == "none" )
	{
		item.style.display= "block";
	}
	else
	{
		item.style.display= "none";
	}
}
function detail( id )
{
	var items= document.getElementsByName( id );
	for( i= 0; i < items.length; i++ )
	{
		if( items[i].style.display == "none" )
		{
			items[i].style.display= "block";
		}
		else
		{
			items[i].style.display= "none";
		}
	}
}
</script>
</head>

<body>
<h1></h1>
<form>
<input type="button" value="div" onclick="test('div')"/>
<input type="button" value="ol" onclick="test('ol')"/>
<input type="button" value="ul" onclick="test('ul')"/>
<input type="button" value="table" onclick="test('table')"/>
<input type="button" value="tr" onclick="detail('tr')"/>
<hr>
<div id="div">DIV</div>
<ol id="ol" >
 <li>ol li 1</li>
 <li>ol li 2</li>
</ol>
<ul id="ul" >
 <li>ul li 1</li>
 <li>ul li 2</li>
</ul>
<table id="table" border="1">
 <tr>
  <th>row1</th><td>あああ</td>
 </tr>
 <tr>
  <th>row2</th><td>いいい</td>
 </tr>
 <tr id="tr">
  <th>row3</th><td>ううう</td>
 </tr>
 <tr id="tr">
  <th>row4</th><td>えええ</td>
 </tr>
</table>
<hr>
</form>

</body>
</html>
