<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isErrorPage="true" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<!-- inc/paging.jsp start -->
<%-- 汎用ページング処理用コントール
作成：2012/02/28 Kac
使用条件：
　jp.co.ksi.eip.commons.util.PagingArrayを使ってページングデータを用意する
　リクエスト属性名はpagingArray
　呼び出し元の検索フォームにpaging( index )を用意する
 --%>
<script type="text/javascript">
function jump()
{
	var index= document.PAGING_FORM.pagingCB.selectedIndex;
	paging( document.PAGING_FORM.pagingCB.options[index].value );
}
</script>
<jsp:useBean id="pagingArray" scope="request" type="jp.co.ksi.eip.commons.util.PagingArray"/>
<%
int	prev= pagingArray.getStart() - pagingArray.getMax();
String	prevStat= ( prev < 0 ) ? "disabled=\"disabled\"" : "";
int	next= pagingArray.getEnd() +1;
String	nextStat= ( next >= pagingArray.getTotal() ) ? "disabled=\"disabled\"" :"";
%>
<form method="post" name="PAGING_FORM" style="margin:0px;">
<bean:write name="pagingArray" property="total"/> / <%=pagingArray.getStart()+1 %> - <%=pagingArray.getEnd()+1 %>
<input type="hidden" name="start" value=""/>
<input type="button" name="submit.prev" value="Prev" onclick="paging(<%=prev %>)" <%=prevStat %>/>
<input type="button" name="submit.next" value="Next" onclick="paging(<%=next %>)" <%=nextStat %>/>
<select name="pagingCB" >
<%
int	startIndex= 0;
while( startIndex < pagingArray.getTotal() )
{
	String	selected= "";
	int	diff= pagingArray.getStart() - startIndex;
	if( ( 0 <= diff ) && ( diff < pagingArray.getMax() ) )
	{//	current
		selected= "selected";
	}
%> <option value="<%=startIndex %>" <%=selected %>><%=startIndex+1 %></option>
<%
	startIndex+= pagingArray.getMax();
}
%></select>
<input type="button" name="submit.jump" value="Jump" onclick="jump()"/>
</form>
<!-- inc/paging.jsp end -->